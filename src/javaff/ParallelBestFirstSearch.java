package javaff;

import javaff.data.GroundProblem;
import javaff.planning.NullFilter;
import javaff.planning.TemporalMetricState;
import javaff.search.BestFirstSearch;

public class ParallelBestFirstSearch extends ParallelSearch {

	public ParallelBestFirstSearch(GroundProblem groundProblem, TemporalMetricState initialState) {
		super(groundProblem, initialState);
	}

	@Override
	JavaFF.SearchType getType() {
		return JavaFF.SearchType.BEST_FIRST_NULL_FILTER;
	}

	@Override
	boolean reRun() {
		return true; // TODO: should be false
	}

	@Override
	public javaff.planning.State doSearch(TemporalMetricState initialState) {
		JavaFF.infoOutput.println("BFS running");
		BestFirstSearch bfs = new BestFirstSearch(initialState);
		bfs.setFilter(new NullFilter());
		return bfs.search();
	}

}
