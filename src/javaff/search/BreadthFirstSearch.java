//
//  BreadthFirstSearch.java
//  JavaFF
//
//  Created by Keith Halsey on Wed Oct 22 2003.
//

package javaff.search;

import javaff.planning.State;
import java.util.LinkedList;
import java.util.Hashtable;

public class BreadthFirstSearch extends Search
{
	
	protected LinkedList open;
	protected Hashtable closed;
	
	public BreadthFirstSearch(State s)
	{
		super(s);
		open = new LinkedList();
		closed = new Hashtable();
	}

	public void updateOpen(State S)
    {
		open.addAll(S.getNextStates());
	}

	public State removeNext()
    {
		return (State) ((LinkedList) open).removeFirst();
	}
	
	public boolean needToVisit(State s) {
		Integer Shash = new Integer(s.hashCode());
		State D = (State) closed.get(Shash);
		
		if (closed.containsKey(Shash) && D.equals(s)) return false;
		
		closed.put(Shash, s);
		return true;
	}

	public State search() {
		
		open.add(start);

		while (!open.isEmpty())
		{
			State s = removeNext();
			if (needToVisit(s)) {
				++nodeCount;
				if (s.goalReached()) {
					return s;
				} else {
					updateOpen(s);
				}
			}
			
		}
		return null;
	}
}
