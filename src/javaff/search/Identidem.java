package javaff.search;

import javaff.planning.State;
import javaff.planning.Filter;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.LinkedList;
import java.util.Comparator;
import java.math.BigDecimal;
import java.util.Hashtable;
import java.util.Iterator;

/* Identidem is built on the Enforced Hill Climbing framework, I will be using the EHC skeleton code to 
	implement the Identidem algorithm, possibly with lookaheads

	Sedar Olmez 01/12/2016*/

public class Identidem extends Search
{
	protected BigDecimal bestHValue;

	protected Hashtable closed;
	protected LinkedList open;
	protected Filter filter = null;
	protected int probeDepthBound;
	
	public Identidem(State s)
	{
		this(s, new HValueComparator());
	}

	public Identidem(State s, Comparator c)
	{
		super(s);
		setComparator(c);
		
		closed = new Hashtable();
		open = new LinkedList();
	}

	public void setFilter(Filter f)
	{
		filter = f;
	}

	public State removeNext()
	{
			
		return (State) ((LinkedList) open).removeFirst();
	}
	
	public boolean needToVisit(State s) {
		Integer Shash = new Integer(s.hashCode()); // compute hash for state
		State D = (State) closed.get(Shash); // see if its on the closed list
		
		if (closed.containsKey(Shash) && D.equals(s)) return false;  // if it is return false
		
		closed.put(Shash, s); // otherwise put it on
		return true; // and return true
	}

	public State localSearchMinima(State min){
		
	} 
	
	public State search() {
		
		/*Intially we set our lookahead, probeDepthBound to 2 and increment by 1 if no better 
			successor found, less than the current one.*/
		probeDepthBound = 2;

		if (start.goalReached()) { 
			return start;
		}

		needToVisit(start); 
		open.add(start); 
		bestHValue = start.getHValue();
		javaff.JavaFF.infoOutput.println(bestHValue);
		
		while (!open.isEmpty()) 
		{	
			 State s = removeNext(); //Initial node
			
			 Set neighbour = s.getNextStates(filter.getActions(s)); // and find its neighbourhood
			
			 Iterator succItr = neighbour.iterator();			
		
			 while (succItr.hasNext()) {

			 	State succ = (State) succItr.next(); // next successor

				if (needToVisit(succ)) {

			 		if (succ.goalReached()) { // if we've found a goal state - return it as the solution
			 			return succ;
			 		} else if (succ.getHValue() <= s.getHValue) {	
						// if we've found a state with a better heuristic value than the best seen so far
						bestHValue = succ.getHValue(); // note the new best avlue
						javaff.JavaFF.infoOutput.println(bestHValue);
						open = new LinkedList(); // clear the open list
						open.add(succ); // put this on it
						break; // and skip looking at the other successors
					} else {

						//If no better heuristic value found, then we must look ahead using the probeDepthBound

						open.add(succ); // otherwise, add to the open list
					}
			 	}
			}

		}
		return null;
	}
}