//
//  TimeStampedPlan.java
//  JavaFF
//
//  Created by Keith Halsey on Tue Aug 03 2004.
//

package javaff.data;

import java.util.Set;
import java.util.HashSet;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Iterator;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.math.BigDecimal;

public class TimeStampedPlan implements Plan
{
	public SortedSet actions = new TreeSet();

	public void addAction(Action a, BigDecimal t)
	{
		addAction(a, t, null);
	}

	public void addAction(Action a, BigDecimal t, BigDecimal d)
	{
		actions.add(new TimeStampedAction(a,t,d));
	}

		
	public void print(PrintStream p)
	{
		Iterator ait = actions.iterator();
		while (ait.hasNext())
		{
			TimeStampedAction a = (TimeStampedAction) ait.next();
			p.println(a);
		}
	}

	public void print(PrintWriter p)
	{
		Iterator ait = actions.iterator();
		while (ait.hasNext())
		{
			TimeStampedAction a = (TimeStampedAction) ait.next();
			p.println(a);
		}
	}
	
	public Set getActions()
	{
		Set s = new HashSet();
		Iterator ait = actions.iterator();
		while (ait.hasNext())
		{
			TimeStampedAction a = (TimeStampedAction) ait.next();
			s.add(a.action);
		}
		return s;
	}	
}
