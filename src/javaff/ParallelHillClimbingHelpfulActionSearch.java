package javaff;

import javaff.planning.HelpfulFilter;
import javaff.planning.TemporalMetricState;
import javaff.search.HillClimbingSearch;

public class ParallelHillClimbingHelpfulActionSearch extends ParallelSearch {

	ParallelHillClimbingHelpfulActionSearch(TemporalMetricState initialState) {
		super(initialState);
	}

	@Override
	JavaFF.SearchType getType() {
		return JavaFF.SearchType.HC_HELPFUL_FILTER;
	}

	@Override
	boolean reRun() {
		return true;
	}

	@Override
	public javaff.planning.State doSearch(TemporalMetricState initialState) {
		HillClimbingSearch hcs = new HillClimbingSearch(initialState);
		hcs.setFilter(new HelpfulFilter());
		return hcs.search();
	}

}
