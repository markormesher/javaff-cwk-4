package javaff.search;

import javaff.planning.Filter;
import javaff.planning.State;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Set;

public class HillClimbingSearch extends Search {
	private Hashtable<Integer, State> closed = new Hashtable<>();
	protected Filter filter = null;
	private int depthBound;

	public HillClimbingSearch(State s, int depthBound) {
		this(s, new HValueComparator(), depthBound);
	}

	private HillClimbingSearch(State s, Comparator c, int depthBound) {
		super(s);
		setComparator(c);
		this.depthBound = depthBound;
	}

	public void setFilter(Filter f) {
		filter = f;
	}

	private boolean needToVisit(State state) {
		Integer stateHash = state.hashCode();
		State closedListStateForHash = closed.get(stateHash);

		if (closed.containsKey(stateHash) && closedListStateForHash.equals(state)) return false;

		closed.put(stateHash, state);
		return true;
	}

	public State search() {
		if (start.goalReached()) return start;

		// dummy call (adds start to the list of 'closed' states so we don't visit it again
		needToVisit(start);

		// start from the initial state
		State open = start;

		int depth = 0;

		while (open != null) { // whilst there is still a state to consider
			// expand the current open state
			Set<State> successors = open.getNextStates(filter.getActions(open));

			// prepare to store the best H-value that's encountered and the best successor(s)
			BigDecimal bestHValue = null; // null ~= infinity
			LinkedList<State> bestSuccessors = new LinkedList<>();

			// loop through the open state's neighbourhood
			for (State successor : successors) {
				if (needToVisit(successor)) {
					if (successor.goalReached()) {
						// if we've found a goal state, return it as the solution
						return successor;

					} else if (bestHValue == null || successor.getHValue().compareTo(bestHValue) < 0) {
						// if we've found a state with a better H-value than the best seen so far,
						// update the best-so-far H-value and remove previous best successors
						bestHValue = successor.getHValue();
						bestSuccessors = new LinkedList<>();
						bestSuccessors.add(successor);

					} else if (successor.getHValue().compareTo(bestHValue) == 0) {
						// if we've found a state with a H-value equal to the best seen so far,
						// add this successor to the list of potential best successors
						bestSuccessors.add(successor);
					}
				}
			}

			// select a random state from the candidate list
			if (bestSuccessors.isEmpty()) {
				open = null;
			} else {
				open = bestSuccessors.get(javaff.JavaFF.generator.nextInt(bestSuccessors.size()));
			}

			// quit if we hit the depth bound
			++depth;
			if (depthBound > 0 && depth >= depthBound) break;
		}

		// failed!
		return null;
	}
}
