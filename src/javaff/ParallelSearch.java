package javaff;

import javaff.data.GroundProblem;
import javaff.data.TotalOrderPlan;
import javaff.planning.TemporalMetricState;

import java.util.ConcurrentModificationException;

public abstract class ParallelSearch extends Thread {

	private GroundProblem groundProblem;
	private TemporalMetricState initialState;

	public ParallelSearch(GroundProblem groundProblem, TemporalMetricState initialState) {
		this.groundProblem = groundProblem;
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
			JavaFF.errorOutput.println("Killed");
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
