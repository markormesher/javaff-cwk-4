//
//  Search.java
//  JavaFF
//
//  Created by Keith Halsey on Tue Oct 21 2003.
//


package javaff.search;

import javaff.planning.State;

import java.util.Comparator;
import java.util.Collection;
import java.util.Map;
import java.util.Hashtable;

public abstract class Search
{
	protected State start;
	protected int nodeCount = 0;
	protected Comparator comp;

	public Search(State s)
	{
		start = s;
	}

	public Comparator getComparator()
	{
		return comp;
	}

	public void setComparator(Comparator c)
	{
		comp = c;
	}

	public abstract State search();
	

}

