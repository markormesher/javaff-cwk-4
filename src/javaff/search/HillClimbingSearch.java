package javaff.search;

import javaff.JavaFF;
import javaff.planning.Filter;
import javaff.planning.State;

import java.math.BigDecimal;
import java.util.*;

public class HillClimbingSearch extends Search {
	private Hashtable<Integer, State> closed = new Hashtable<>();
	private int maxDepth = 0;
	protected Filter filter = null;

	public HillClimbingSearch(State s) {
		this(s, new HValueComparator());
	}

	private HillClimbingSearch(State s, Comparator c) {
		super(s);
		setComparator(c);
	}

	public void setFilter(Filter f) {
		filter = f;
	}

	public void setMaxDepth(int maxDepth) {
		this.maxDepth = maxDepth;
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
			// check we're not exceeding the max depth
			if (depth >= maxDepth) {
				JavaFF.errorOutput.println("Hill climbing max depth exceeded!");
				break;
			}
			++depth;

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
		}

		// failed!
		return null;
	}
}
