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

public class JavaFF {

	public static final BigDecimal EPSILON = new BigDecimal(0.01).setScale(2, BigDecimal.ROUND_HALF_EVEN);
	public static final BigDecimal MAX_DURATION = new BigDecimal("100000").setScale(2, BigDecimal.ROUND_HALF_EVEN); //maximum duration in a duration constraint

	public static Random generator = null;

	private static final PrintStream planOutput = System.out;
	public static final PrintStream parsingOutput = System.out;
	public static final PrintStream infoOutput = System.out;
	public static final PrintStream errorOutput = System.err;

	private static long startTime;
	private static HashSet<TotalOrderPlan> solutions = new HashSet<>();
	private static int bestPlanLength = -1;

	private static File domainFile;
	private static File problemFile;
	private static File solutionFile;

	private static UngroundProblem ungroundProblem;
	private static GroundProblem groundProblem;

	public static void main(String args[]) {
		startTime = System.currentTimeMillis();

		if (args.length < 2) {
			System.out.println("Parameters needed: domainFile.pddl problemFile.pddl [random seed] [outputfile.sol]");
			return;
		}

		// read inputs
		domainFile = new File(args[0]);
		problemFile = new File(args[1]);
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

		// avoid repeated work
		ungroundProblem = PDDL21parser.parseFiles(domainFile, problemFile);
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
		spawnSearch(SearchType.BEST_FIRST_NULL_FILTER);
		spawnSearch(SearchType.HC_HELPFUL_FILTER);
		spawnSearch(SearchType.EHC_HELPFUL_FILTER);

		infoOutput.println("Setup finished - planners now running in background");
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
			case BEST_FIRST_NULL_FILTER:
				new ParallelBestFirstSearch(localGroundProblem, initialState).start();
				break;

			case HC_HELPFUL_FILTER:
				new ParallelHillClimbingHelpfulActionSearch(localGroundProblem, initialState).start();
				break;

			case EHC_HELPFUL_FILTER:
				new ParallelEnforcedHillClimbingHelpfulActionSearch(localGroundProblem, initialState).start();
				break;
		}
	}

	static synchronized void onPlanFound(TotalOrderPlan totalOrderPlan, boolean reRun, SearchType type, double planningTime) {

		boolean planWasFound = totalOrderPlan != null;

		boolean planIsUnique = planWasFound && !solutions.contains(totalOrderPlan);
		solutions.add(totalOrderPlan);

		boolean planIsBetter = planIsUnique && (bestPlanLength < 0 || totalOrderPlan.getPlanLength() < bestPlanLength);

		if (planWasFound && planIsUnique && planIsBetter) {

			// **************
			// Print a header
			// **************

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

			infoOutput.println("Planning Time   =   " + planningTime + "sec");
			infoOutput.println("Scheduling Time =   " + schedulingTime + "sec");
		}

		if (reRun) spawnSearch(type);
	}

	private static void writePlanToFile(Plan plan, File fileOut) {
		try {
			FileOutputStream outputStream = new FileOutputStream(fileOut);
			PrintWriter printWriter = new PrintWriter(outputStream);
			plan.print(printWriter);
			printWriter.close();
		} catch (FileNotFoundException e) {
			errorOutput.println(e);
			e.printStackTrace();
		}
	}

	public enum SearchType {
		BEST_FIRST_NULL_FILTER,
		EHC_HELPFUL_FILTER,
		HC_HELPFUL_FILTER
	}

}