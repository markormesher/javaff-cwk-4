//
//  EnforcedHillClimbingSearch.java
//  JavaFF
//
//  Created by Keith Halsey on Wed Oct 22 2003.
//

package javaff.search;

import javaff.planning.State;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.LinkedList;
import java.util.Comparator;
import java.util.Iterator;
import java.math.BigDecimal;

public class LimitedEnforcedHillClimbingSearch extends EnforcedHillClimbingSearch
{
        private BigDecimal planSizeLimit;

	public LimitedEnforcedHillClimbingSearch(State s, BigDecimal l)
	{
		this(s, new HValueComparator(), l);
	}

	public LimitedEnforcedHillClimbingSearch(State s, Comparator c, BigDecimal l)
	{
		super(s);
		setComparator(c);
                planSizeLimit = l;
	}

	public void updateOpen(State S)
	{
           
		if (S.getHValue().compareTo(currentHValue) < 0)
		{
			open = new TreeSet(comp);
			currentHValue = S.getHValue();
			javaff.JavaFF.infoOutput.println(currentHValue);
		}
                Iterator it = S.getNextStates().iterator();
                while (it.hasNext())
                {
                   State S2 = (State) it.next();
                   if (S2.getGValue().compareTo(planSizeLimit) <= 0)
                   {
                      open.add(S2);
                   }
                }
	}
}
