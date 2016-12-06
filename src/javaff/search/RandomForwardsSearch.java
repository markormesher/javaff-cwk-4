//
//  BestFirstSearch.java
//  JavaFF
//
//  Created by Keith Halsey on Wed Oct 22 2003.
//

package javaff.search;

import javaff.JavaFF;
import javaff.planning.Filter;
import javaff.planning.State;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;


public class RandomForwardsSearch extends Search {

	protected Hashtable closed;
	protected List<State> open;
	protected Filter filter = null;
	private int depthBound;

	public RandomForwardsSearch(State s, int depthBound) {
		super(s);

		this.depthBound = depthBound;

		closed = new Hashtable();
		open = new ArrayList<>();
	}

	public void setFilter(Filter f) {
		filter = f;
	}

	private boolean needToVisit(State s) {
		Integer Shash = s.hashCode();
		State D = (State) closed.get(Shash);

		if (closed.containsKey(Shash) && D.equals(s)) return false;

		closed.put(Shash, s);
		return true;
	}

	public State search() {

		open.add(start);
		int depth = 0;

		while (!open.isEmpty()) {
			State s = open.get(JavaFF.generator.nextInt(open.size()));
			open.remove(s);
			if (needToVisit(s)) {
				++nodeCount;
				if (s.goalReached()) {
					return s;
				} else {
					open.clear();
					open.addAll(s.getNextStates(filter.getActions(s)));
				}
			}

			// quit if we hit the depth bound
			++depth;
			if (depthBound > 0 && depth >= depthBound) break;
		}
		return null;
	}

}
