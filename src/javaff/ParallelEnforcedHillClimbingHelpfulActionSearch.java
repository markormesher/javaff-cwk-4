package javaff;

import javaff.planning.HelpfulFilter;
import javaff.planning.TemporalMetricState;
import javaff.search.EnforcedHillClimbingSearch;

public class ParallelEnforcedHillClimbingHelpfulActionSearch extends ParallelSearch {

	private int depthBound;

	ParallelEnforcedHillClimbingHelpfulActionSearch(TemporalMetricState initialState, int depthBound) {
		super(initialState);
		this.depthBound = depthBound;
	}

	@Override
	JavaFF.SearchType getType() {
		return JavaFF.SearchType.EHC_HELPFUL_FILTER;
	}

	@Override
	boolean reRun() {
		return false;
	}

	@Override
	public javaff.planning.State doSearch(TemporalMetricState initialState) {
		EnforcedHillClimbingSearch ehcs = new EnforcedHillClimbingSearch(initialState, depthBound);
		ehcs.setFilter(new HelpfulFilter());
		return ehcs.search();
	}

}
