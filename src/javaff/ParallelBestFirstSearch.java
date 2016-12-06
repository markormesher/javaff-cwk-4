package javaff;

import javaff.planning.NullFilter;
import javaff.planning.TemporalMetricState;
import javaff.search.BestFirstSearch;
import javaff.search.BestSuccessorSelectorWithRandom;

public class ParallelBestFirstSearch extends ParallelSearch {

	ParallelBestFirstSearch(TemporalMetricState initialState) {
		super(initialState);
	}

	@Override
	JavaFF.SearchType getType() {
		return JavaFF.SearchType.BEST_FIRST_NULL_FILTER_WITH_RANDOM;
	}

	@Override
	boolean reRun() {
		return true;
	}

	@Override
	public javaff.planning.State doSearch(TemporalMetricState initialState) {
		BestFirstSearch bfs = new BestFirstSearch(initialState);
		bfs.setFilter(new NullFilter());
		bfs.setSuccessorSelector(BestSuccessorSelectorWithRandom.getInstance());
		return bfs.search();
	}

}
