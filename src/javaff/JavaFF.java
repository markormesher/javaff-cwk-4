//
//  JavaFF.java
//  JavaFF
//
//  Created by Keith Halsey on Oct 2003.
//
//  The main file for the JavaFF Planner
//

package javaff;

import javaff.data.*;
import javaff.parser.PDDL21parser;
import javaff.planning.TemporalMetricState;
import javaff.scheduling.JavaFFScheduler;
import javaff.scheduling.Scheduler;

import java.io.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JavaFF {

	/* START OPTIONS */
	private static final boolean SINGLE_SOLUTION = false;
	private static final SearchType[] ALGORITHMS_TO_USE = new SearchType[]{
			SearchType.RANDOM_NULL_FILTER,
			SearchType.EHC_HELPFUL_FILTER,
			SearchType.HC_HELPFUL_FILTER,
			SearchType.BEST_FIRST_NULL_FILTER_WITH_RANDOM
	};
	/* END OPTIONS */

	public static final BigDecimal EPSILON = new BigDecimal(0.01).setScale(2, BigDecimal.ROUND_HALF_EVEN);
	public static final BigDecimal MAX_DURATION = new BigDecimal("100000").setScale(2, BigDecimal.ROUND_HALF_EVEN);

	public static Random generator = null;

	private static final PrintStream planOutput = System.out;
	public static final PrintStream parsingOutput = System.out;
	public static final PrintStream infoOutput = System.out;
	public static final PrintStream errorOutput = System.err;

	private static final int logicalThreads = Runtime.getRuntime().availableProcessors();
	private static final ExecutorService executorService = Executors.newFixedThreadPool(logicalThreads);
	private static long startTime;
	private static HashSet<TotalOrderPlan> solutions = new HashSet<>();
	private static int bestPlanLength = -1;

	private static File solutionFile;
	private static GroundProblem groundProblem;

	public static void main(String args[]) {
		startTime = System.currentTimeMillis();

		if (args.length < 2) {
			System.out.println("Parameters needed: domainFile.pddl problemFile.pddl [random seed] [outputfile.sol]");
			return;
		}

		// read inputs
		File domainFile = new File(args[0]);
		File problemFile = new File(args[1]);
		if (args.length > 2) {
			generator = new Random(Integer.parseInt(args[2]));
		} else {
			generator = new Random();
		}
		if (args.length > 3) {
			solutionFile = new File(args[3]);
		} else {
			solutionFile = null;
		}

		// avoid repeated work by parsing and grounding here
		UngroundProblem ungroundProblem = PDDL21parser.parseFiles(domainFile, problemFile);
		if (ungroundProblem == null) {
			System.out.println("Parsing error - see console for details");
			return;
		}
		groundProblem = ungroundProblem.ground();
		if (groundProblem == null) {
			errorOutput.println("Ground problem was null!");
			return;
		}

		// spawn searches
		for (SearchType st : ALGORITHMS_TO_USE) spawnSearch(st);

		infoOutput.println("Setup finished - planners now running in background on " + logicalThreads + " logical threads");
		infoOutput.println();
	}

	private static void spawnSearch(SearchType type) {
		GroundProblem localGroundProblem;
		try {
			localGroundProblem = (GroundProblem) groundProblem.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return;
		}

		TemporalMetricState initialState = localGroundProblem.getTemporalMetricInitialState();
		if (initialState == null) {
			errorOutput.println("Initial state was null!");
			return;
		}

		switch (type) {
			case RANDOM_NULL_FILTER:
				// bound the depth to the best plan found so far - no point in finding worse plans!
				executorService.submit(new ParallelRandomForwardsSearch(initialState, bestPlanLength));
				break;

			case EHC_HELPFUL_FILTER:
				// bound the depth to the best plan found so far - no point in finding worse plans!
				executorService.submit(new ParallelEnforcedHillClimbingHelpfulActionSearch(initialState));
				break;

			case HC_HELPFUL_FILTER:
				// bound the depth to the best plan found so far - no point in finding worse plans!
				executorService.submit(new ParallelHillClimbingHelpfulActionSearch(initialState, bestPlanLength));
				break;

			case BEST_FIRST_NULL_FILTER_WITH_RANDOM:
				executorService.submit(new ParallelBestFirstSearch(initialState));
				break;
		}
	}

	static synchronized void onPlanFound(TotalOrderPlan totalOrderPlan, boolean reRun, SearchType type, double planningTime) {

		// quit if we're in single-solution mode and we already found something
		if (SINGLE_SOLUTION && solutions.size() > 0) {
			System.exit(0);
			return;
		}

		// cheapest solution checks first:

		boolean planWasFound = totalOrderPlan != null;
		if (!planWasFound) {
			if (reRun) spawnSearch(type);
			return;
		}

		boolean planIsBetter = bestPlanLength < 0 || totalOrderPlan.getPlanLength() < bestPlanLength;
		if (!planIsBetter) {
			if (reRun) spawnSearch(type);
			return;
		}

		boolean planIsUnique = !solutions.contains(totalOrderPlan);
		solutions.add(totalOrderPlan);
		if (!planIsUnique) {
			if (reRun) spawnSearch(type);
			return;
		}

		// by this point, we know that we have a unique plan with the best length seen so far

		// **************
		// Print a header
		// **************

		infoOutput.println();
		infoOutput.println("-----------------------------");
		infoOutput.println();
		infoOutput.println(((System.currentTimeMillis() - startTime) / 1000.0) + "s: Better plan found by " + type);
		infoOutput.println("This is plan #" + solutions.size());
		infoOutput.println("Best length was " + (bestPlanLength < 0 ? "inf" : bestPlanLength));
		infoOutput.println("Best length now " + totalOrderPlan.getPlanLength());
		infoOutput.println();
		bestPlanLength = totalOrderPlan.getPlanLength();

		// ***************
		// Schedule a plan
		// ***************

		long beforeScheduling = System.currentTimeMillis();
		Scheduler scheduler = new JavaFFScheduler(groundProblem);
		TimeStampedPlan timeStampedPlan = scheduler.schedule(totalOrderPlan);
		long afterScheduling = System.currentTimeMillis();
		double schedulingTime = (afterScheduling - beforeScheduling) / 1000.00;

		// **************
		// Print the plan
		// **************

		totalOrderPlan.print(planOutput);
		timeStampedPlan.print(planOutput);
		if (solutionFile != null) writePlanToFile(timeStampedPlan, solutionFile);

		infoOutput.println();
		infoOutput.println("Plan Length     =   " + bestPlanLength);
		infoOutput.println("Planning Time   =   " + planningTime + "sec");
		infoOutput.println("Scheduling Time =   " + schedulingTime + "sec");

		// **************
		// Next iteration
		// **************

		// quit now if we're in single solution mode
		if (SINGLE_SOLUTION) System.exit(0);

		// otherwise, keep going
		if (reRun) spawnSearch(type);
	}

	private static void writePlanToFile(Plan plan, File fileOut) {
		try {
			FileOutputStream outputStream = new FileOutputStream(fileOut);
			PrintWriter printWriter = new PrintWriter(outputStream);
			plan.print(printWriter);
			printWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			errorOutput.println(e);
		}
	}

	public enum SearchType {
		BEST_FIRST_NULL_FILTER_WITH_RANDOM,
		EHC_HELPFUL_FILTER,
		HC_HELPFUL_FILTER,
		RANDOM_NULL_FILTER
	}

}
