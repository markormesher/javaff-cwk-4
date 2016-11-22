//
//  PartialOrderPlan.java
//  JavaFF
//
//  Created by Keith Halsey on Tue Aug 03 2004.
//

package javaff.data;

import javaff.data.strips.Proposition;
import javaff.data.strips.InstantAction;
import javaff.data.temporal.SplitInstantAction;
import javaff.scheduling.TemporalConstraint;

import java.util.Map;
import java.util.Hashtable;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.io.PrintStream;
import java.io.PrintWriter;


public class PartialOrderPlan implements Plan
{
	public Map strictOrderings = new Hashtable();
	public Map equalOrderings = new Hashtable();
	public Set actions = new HashSet();

	public PartialOrderPlan()
	{
		
	}

	public void addStrictOrdering(Action first, Action second)
	{
		Set ord = null;
		Object o = strictOrderings.get(first);
		if (o == null)
		{
			ord = new HashSet();
			strictOrderings.put(first, ord);
		}
		else ord = (HashSet) o;
		ord.add(second);
		actions.add(first);
		actions.add(second);
	}

	public void addEqualOrdering(Action first, Action second)
	{
		Set ord = null;
		Object o = equalOrderings.get(first);
		if (o == null)
		{
			ord = new HashSet();
			equalOrderings.put(first, ord);
		}
		else ord = (HashSet) o;
		ord.add(second);
		actions.add(first);
		actions.add(second);
	}

	public void addOrder(Action first, Action second, Proposition p)
	{
		if (first instanceof SplitInstantAction)
		{
			SplitInstantAction sa = (SplitInstantAction) first;
			if (!sa.exclusivelyInvariant(p))
			{
				addEqualOrdering(first, second);
				return;
			}
			
		}

		if (second instanceof SplitInstantAction)
		{
			SplitInstantAction sa = (SplitInstantAction) second;
			if (!sa.exclusivelyInvariant(p))
			{
				addEqualOrdering(first, second);
				return;
			}
		}
		
		addStrictOrdering(first, second);
		
		
	}

	public void addAction(Action a)
	{
		actions.add(a);
		strictOrderings.put(a, new HashSet());
		equalOrderings.put(a, new HashSet());
	}

	public void addActions(Set s)
	{
		Iterator sit = s.iterator();
		while (sit.hasNext()) addAction((Action) sit.next());
	}

	public Set getActions()
	{
		return actions;
	}

	public Set getTemporalConstraints()
	{
		Set rSet = new HashSet();
		Iterator ait = actions.iterator();
		while (ait.hasNext())
		{
			Action a = (Action) ait.next();
			
			Set ss = (HashSet) strictOrderings.get(a);
			Iterator sit = ss.iterator();
			while (sit.hasNext())
			{
				Action b = (Action) sit.next();
				rSet.add(TemporalConstraint.getConstraint((InstantAction)a,(InstantAction)b));
			}

			Set es = (HashSet) equalOrderings.get(a);
			Iterator eit = es.iterator();
			while (eit.hasNext())
			{
				Action b = (Action) eit.next();
				rSet.add(TemporalConstraint.getConstraintEqual((InstantAction)a,(InstantAction)b));
			}
		}
		return rSet;
			
	}

	public void print(PrintStream p)
	{
		Iterator sit = actions.iterator();
		while (sit.hasNext())
		{
			Action a = (Action) sit.next();
			p.println(a);
			p.println("\tStrict Orderings: "+strictOrderings.get(a));
			p.println("\tLess than or equal Orderings: "+equalOrderings.get(a));
		}
	}

	public void print(PrintWriter p)
	{
		Iterator sit = actions.iterator();
		while (sit.hasNext())
		{
			Action a = (Action) sit.next();
			p.println(a);
			p.println("\tStrict Orderings: "+strictOrderings.get(a));
			p.println("\tLess than or equal Orderings: "+equalOrderings.get(a));
		}
	}
}
