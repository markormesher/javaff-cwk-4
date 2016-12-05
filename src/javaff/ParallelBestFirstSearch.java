package javaff;

import javaff.planning.NullFilter;
import javaff.planning.TemporalMetricState;
import javaff.search.BestFirstSearch;

public class ParallelBestFirstSearch extends ParallelSearch {

	ParallelBestFirstSearch(TemporalMetricState initialState) {
		super(initialState);
	}

	@Override
	JavaFF.SearchType getType() {
		return JavaFF.SearchType.BEST_FIRST_NULL_FILTER;
	}

	@Override
	boolean reRun() {
		return false;
	}

	@Override
	public javaff.planning.State doSearch(TemporalMetricState initialState) {
		BestFirstSearch bfs = new BestFirstSearch(initialState);
		bfs.setFilter(new NullFilter());
		return bfs.search();
	}

}
