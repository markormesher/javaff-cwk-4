package javaff;

import javaff.data.TotalOrderPlan;
import javaff.planning.TemporalMetricState;

public abstract class ParallelSearch extends Thread {

	private TemporalMetricState initialState;

	ParallelSearch(TemporalMetricState initialState) {
		this.initialState = initialState;
	}

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
			goalState = doSearch(initialState);
		} catch (Exception e) {
			e.printStackTrace();
			JavaFF.onPlanFound(null, true, getType(), 0);
			return;
		}

		long afterPlanning = System.currentTimeMillis();

		TotalOrderPlan totalOrderPlan = null;
		if (goalState != null) totalOrderPlan = (TotalOrderPlan) goalState.getSolution();

		double planningTime = (afterPlanning - startTime) / 1000.00;

		JavaFF.onPlanFound(totalOrderPlan, reRun(), getType(), planningTime);
	}

}
