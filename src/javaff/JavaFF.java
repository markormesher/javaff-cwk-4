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

	static GroundProblem groundProblem;
	static TemporalMetricState initialState;
	private static File solutionFile;

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

		// avoid repeated work
		groundProblem = parseAndGround(domainFile, problemFile);
		if (groundProblem == null) return;
		initialState = groundProblem.getTemporalMetricInitialState();
		if (initialState == null) return;

		// spawn searches
		spawnSearch(SearchType.BEST_FIRST_NULL_FILTER);
		spawnSearch(SearchType.HC_HELPFUL_FILTER);
		spawnSearch(SearchType.EHC_HELPFUL_FILTER);

		infoOutput.println("Setup finished - planners now running in background");
		infoOutput.println();
	}

	private static GroundProblem parseAndGround(File dFile, File pFile) {

		// ****************************
		// Parse and ground the problem
		// ****************************

		long startTime = System.currentTimeMillis();

		UngroundProblem unground = PDDL21parser.parseFiles(dFile, pFile);
		if (unground == null) {
			System.out.println("Parsing error - see console for details");
			return null;
		}

		GroundProblem ground = unground.ground();
		long afterGrounding = System.currentTimeMillis();
		double groundingTime = (afterGrounding - startTime) / 1000.00;
		infoOutput.println("Instantiation   =   " + groundingTime + "sec");

		return ground;
	}

	private static void spawnSearch(SearchType type) {
		switch (type) {
			case BEST_FIRST_NULL_FILTER:
				new ParallelBestFirstSearch().start();
				break;

			case HC_HELPFUL_FILTER:
				new ParallelHillClimbingHelpfulActionSearch().start();
				break;

			case EHC_HELPFUL_FILTER:
				new ParallelEnforcedHillClimbingHelpfulActionSearch().start();
				break;
		}
	}

	static synchronized void onPlanFound(TotalOrderPlan totalOrderPlan, TimeStampedPlan timeStampedPlan, boolean reRun, SearchType type, double planningTime, double schedulingTime) {
		// fail if no plan was found
		if (totalOrderPlan == null || timeStampedPlan == null) {
			if (reRun) spawnSearch(type);
			return;
		}

		// fail if we've seen this plan before
		if (solutions.contains(totalOrderPlan)) {
			if (reRun) spawnSearch(type);
			return;
		}
		solutions.add(totalOrderPlan);

		// fail if this plan is no better
		int planLength = totalOrderPlan.getPlanLength();
		if (bestPlanLength > 0 && planLength >= bestPlanLength) {
			if (reRun) spawnSearch(type);
			return;
		}

		// this plan is unique and the best so far, so print it
		infoOutput.println();
		infoOutput.println(((System.currentTimeMillis() - startTime) / 1000.0) + "s: Better plan found by " + type);
		infoOutput.println("This is plan #" + solutions.size());
		infoOutput.println("Best length was " + (bestPlanLength < 0 ? "inf" : bestPlanLength));
		infoOutput.println("Best length now " + planLength);
		infoOutput.println();
		bestPlanLength = planLength;

		totalOrderPlan.print(planOutput);
		timeStampedPlan.print(planOutput);

		infoOutput.println("Planning Time   =   " + planningTime + "sec");
		infoOutput.println("Scheduling Time =   " + schedulingTime + "sec");

		if (solutionFile != null) writePlanToFile(timeStampedPlan, solutionFile);

		// always maybe re-run
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
