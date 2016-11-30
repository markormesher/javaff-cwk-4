package javaff;

import javaff.data.TimeStampedPlan;
import javaff.data.TotalOrderPlan;
import javaff.planning.TemporalMetricState;
import javaff.scheduling.JavaFFScheduler;
import javaff.scheduling.Scheduler;

public abstract class ParallelSearch extends Thread {

	abstract javaff.planning.State doSearch(TemporalMetricState initialState);

	abstract JavaFF.SearchType getType();

	boolean reRun() {
		return false;
	}

	@Override
	public void run() {

		long startTime = System.currentTimeMillis();

		// *****************
		// Search for a plan
		// *****************

		javaff.planning.State goalState;
		try {
			goalState = doSearch(JavaFF.initialState);
		} catch (Exception e) { // TODO: what the hell is going on here?
			// always re-run on failure
			JavaFF.onPlanFound(null, null, true, getType(), 0, 0);
			return;
		}

		long afterPlanning = System.currentTimeMillis();

		TotalOrderPlan totalOrderPlan = null;
		if (goalState != null) totalOrderPlan = (TotalOrderPlan) goalState.getSolution();

		// ***************
		// Schedule a plan
		// ***************

		TimeStampedPlan timeStampedPlan = null;
		if (goalState != null) {
			Scheduler scheduler = new JavaFFScheduler(JavaFF.groundProblem);
			timeStampedPlan = scheduler.schedule(totalOrderPlan);
		}

		long afterScheduling = System.currentTimeMillis();

		double planningTime = (afterPlanning - startTime) / 1000.00;
		double schedulingTime = (afterScheduling - afterPlanning) / 1000.00;

		// *********
		// Job done!
		// *********

		JavaFF.onPlanFound(totalOrderPlan, timeStampedPlan, reRun(), getType(), planningTime, schedulingTime);
	}

}
