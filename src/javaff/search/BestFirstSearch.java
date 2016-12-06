//
//  BestFirstSearch.java
//  JavaFF
//
//  Created by Keith Halsey on Wed Oct 22 2003.
//

package javaff.search;

import javaff.planning.Filter;
import javaff.planning.State;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class BestFirstSearch extends Search {

	protected HashSet<State> open;
	private HashMap<Integer, State> closed;

	protected Filter filter;
	private SuccessorSelector successorSelector;

	public BestFirstSearch(State state) {
		super(state);
		closed = new HashMap<>();
		open = new HashSet<>();
	}

	public void setFilter(Filter filter) {
		this.filter = filter;
	}

	public void setSuccessorSelector(SuccessorSelector successorSelector) {
		this.successorSelector = successorSelector;
	}

	private State removeNext() {
		State next = successorSelector.choose(open);
		open.remove(next);
		return next;
	}

	private boolean visited(State s) {
		Integer stateHash = s.hashCode();
		State retrievedState = closed.get(stateHash);

		return closed.containsKey(stateHash) && retrievedState.equals(s);
	}

	private void visit(State s) {
		Integer stateHash = s.hashCode();
		closed.put(stateHash, s);
	}

	public State search() {
		open.add(start);

		while (!open.isEmpty()) {
			State s = removeNext();
			if (!visited(s)) {
				visit(s);
				if (s.goalReached()) {
					return s;
				} else {
					Set<State> nextStates = s.getNextStates(filter.getActions(s));
					for (State ns : nextStates) {
						if (!visited(ns)) open.add(ns);
					}
				}
			}
		}

		return null;
	}

}
