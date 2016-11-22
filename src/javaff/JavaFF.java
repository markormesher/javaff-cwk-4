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
import javaff.planning.HelpfulFilter;
import javaff.planning.NullFilter;
import javaff.planning.State;
import javaff.planning.TemporalMetricState;
import javaff.scheduling.JavaFFScheduler;
import javaff.scheduling.Scheduler;
import javaff.search.BestFirstSearch;
import javaff.search.EnforcedHillClimbingSearch;
import javaff.search.HillClimbingSearch;

import java.io.*;
import java.math.BigDecimal;
import java.util.Random;

public class JavaFF {

	public static BigDecimal EPSILON = new BigDecimal(0.01);
	public static BigDecimal MAX_DURATION = new BigDecimal("100000"); //maximum duration in a duration constraint

	public static Random generator = null;

	public static PrintStream planOutput = System.out;
	public static PrintStream parsingOutput = System.out;
	public static PrintStream infoOutput = System.out;
	public static PrintStream errorOutput = System.err;

	public static void main(String args[]) {
		EPSILON = EPSILON.setScale(2, BigDecimal.ROUND_HALF_EVEN);
		MAX_DURATION = MAX_DURATION.setScale(2, BigDecimal.ROUND_HALF_EVEN);

		generator = new Random();

		if (args.length < 2) {
			System.out.println("Parameters needed: domainFile.pddl problemFile.pddl [random seed] [outputfile.sol]");
		} else {
			File domainFile = new File(args[0]);
			File problemFile = new File(args[1]);
			File solutionFile = null;
			if (args.length > 2) generator = new Random(Integer.parseInt(args[2]));

			if (args.length > 3) solutionFile = new File(args[3]);

			Plan plan = plan(domainFile, problemFile);

			if (solutionFile != null && plan != null) writePlanToFile(plan, solutionFile);
		}
	}

	public static Plan plan(File dFile, File pFile) {

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

		// *****************
		// Search for a plan
		// *****************

		infoOutput.println("Searching...");
		TemporalMetricState initialState = ground.getTemporalMetricInitialState();
		State goalState = performFFSearch(initialState);
		long afterPlanning = System.currentTimeMillis();

		TotalOrderPlan top = null;
		if (goalState != null) top = (TotalOrderPlan) goalState.getSolution();
		top.print(planOutput);

		/*javaff.planning.PlanningGraph pg = initialState.getRPG();
		Plan plan  = pg.getPlan(initialState);
		plan.print(planOutput);
		return null;*/

		// ***************
		// Schedule a plan
		// ***************

		TimeStampedPlan tsp = null;
		if (goalState != null) {
			infoOutput.println("Scheduling...");
			Scheduler scheduler = new JavaFFScheduler(ground);
			tsp = scheduler.schedule(top);
		}

		long afterScheduling = System.currentTimeMillis();

		if (tsp != null) tsp.print(planOutput);

		double groundingTime = (afterGrounding - startTime) / 1000.00;
		double planningTime = (afterPlanning - afterGrounding) / 1000.00;
		double schedulingTime = (afterScheduling - afterPlanning) / 1000.00;

		infoOutput.println("Instantiation Time =\t\t" + groundingTime + "sec");
		infoOutput.println("Planning Time =\t" + planningTime + "sec");
		infoOutput.println("Scheduling Time =\t" + schedulingTime + "sec");

		return tsp;
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

	public static State performFFSearch(TemporalMetricState initialState) {

		// version 1 = unmodified original (remainder of this method)
		// version 2 = modified for coursework 1, exercise 2
		// version 3 = modified for coursework 1, exercise 3
		// version 4 = modified for coursework 2
		final int VERSION = 4;
		if (VERSION == 2) return performModifiedFFSearch_cw1_ex2(initialState);
		if (VERSION == 3) return performModifiedFFSearch_cw1_ex3(initialState);
		if (VERSION == 4) return performModifiedFFSearch_cw2(initialState);

		// Enforced Hill Climbing, with the HelpfulFilter
		infoOutput.println("Performing search as in FF - first considering EHC with helpful actions");
		EnforcedHillClimbingSearch ehcs = new EnforcedHillClimbingSearch(initialState);
		ehcs.setFilter(HelpfulFilter.getInstance());
		State goalState = ehcs.search();
		if (goalState != null) return goalState;

		// Best-first search, with the NullFilter
		infoOutput.println("EHC failed; considering best-first search with all actions");
		BestFirstSearch bfs = new BestFirstSearch(initialState);
		bfs.setFilter(NullFilter.getInstance());
		goalState = bfs.search();
		if (goalState != null) return goalState;

		return goalState;
	}

	private static State performModifiedFFSearch_cw1_ex2(TemporalMetricState initialState) {
		// Enforced Hill Climbing, with the NullFilter
		infoOutput.println("Performing search as in FF - first considering EHC with all actions");
		EnforcedHillClimbingSearch ehcs = new EnforcedHillClimbingSearch(initialState);
		ehcs.setFilter(NullFilter.getInstance());
		State goalState = ehcs.search();
		if (goalState != null) return goalState;

		// Best-first search, with the NullFilter
		infoOutput.println("EHC failed; considering best-first search with all actions");
		BestFirstSearch bfs = new BestFirstSearch(initialState);
		bfs.setFilter(NullFilter.getInstance());
		goalState = bfs.search();
		if (goalState != null) return goalState;

		return null;
	}

	private static State performModifiedFFSearch_cw1_ex3(TemporalMetricState initialState) {
		// Enforced Hill Climbing, with the HelpfulFilter
		infoOutput.println("Performing search as in FF - first considering EHC with only helpful actions");
		EnforcedHillClimbingSearch ehcs1 = new EnforcedHillClimbingSearch(initialState);
		ehcs1.setFilter(HelpfulFilter.getInstance());
		State goalState = ehcs1.search();
		if (goalState != null) return goalState;

		// Enforced Hill Climbing, with the NullFilter
		infoOutput.println("First EHC failed; considering EHC with all actions");
		EnforcedHillClimbingSearch ehcs2 = new EnforcedHillClimbingSearch(initialState);
		ehcs2.setFilter(NullFilter.getInstance());
		goalState = ehcs2.search();
		if (goalState != null) return goalState;

		// Best-first search, with the NullFilter
		infoOutput.println("Second EHC failed; considering best-first search with all actions");
		BestFirstSearch bfs = new BestFirstSearch(initialState);
		bfs.setFilter(NullFilter.getInstance());
		goalState = bfs.search();
		if (goalState != null) return goalState;

		return null;
	}

	private static State performModifiedFFSearch_cw2(TemporalMetricState initialState) {
		State goalState;

		// Hill Climbing, with the HelpfulFilter
		infoOutput.println("Performing search as in FF - first considering HC with helpful actions");
		for (int depthBound = 5; depthBound < 100; ++depthBound) {
			infoOutput.println("Trying HC with depth bound " + depthBound);
			HillClimbingSearch hcs = new HillClimbingSearch(initialState);
			hcs.setFilter(HelpfulFilter.getInstance());
			hcs.setMaxDepth(depthBound);
			goalState = hcs.search();
			if (goalState != null) return goalState;
		}

		// Best-first search, with the NullFilter
		infoOutput.println("HC failed; considering best-first search with all actions");
		BestFirstSearch bfs = new BestFirstSearch(initialState);
		bfs.setFilter(NullFilter.getInstance());
		goalState = bfs.search();
		if (goalState != null) return goalState;

		return null;
	}

}
