package javaff;

import javaff.planning.NullFilter;
import javaff.planning.TemporalMetricState;
import javaff.search.RandomForwardsSearch;

public class ParallelRandomForwardsSearch extends ParallelSearch {

	public ParallelRandomForwardsSearch(TemporalMetricState initialState) {
		super(initialState);
	}

	@Override
	JavaFF.SearchType getType() {
		return JavaFF.SearchType.RANDOM_NULL_FILTER;
	}

	@Override
	boolean reRun() {
		return true;
	}

	@Override
	public javaff.planning.State doSearch(TemporalMetricState initialState) {
		RandomForwardsSearch rfs = new RandomForwardsSearch(initialState);
		rfs.setFilter(new NullFilter());
		return rfs.search();
	}

}
