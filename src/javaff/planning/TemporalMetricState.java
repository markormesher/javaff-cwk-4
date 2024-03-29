//
//  TemporalMetricState.java
//  JavaFF
//
//  Created by Keith Halsey on Thu Jul 29 2004.
//

// WARNING - This State does deal with Metric Invariants

package javaff.planning;

import javaff.data.GroundCondition;
import javaff.data.TotalOrderPlan;
import javaff.data.Metric;
import javaff.data.Action;
import javaff.data.strips.InstantAction;
import javaff.data.temporal.StartInstantAction;
import javaff.data.temporal.SplitInstantAction;
import javaff.scheduling.SchedulabilityChecker;
import javaff.scheduling.VelosoSchedulabilityChecker;

import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Hashtable;
//import java.math.BigDecimal;

public class TemporalMetricState extends MetricState
{
	public Set openActions;         //Set of (DurativeActions)
	public List invariants;	        //Set of Propositions (Propositions)
	public SchedulabilityChecker checker;

	protected TemporalMetricState(Set a, Set f, GroundCondition g, Map funcs, TotalOrderPlan p, Metric m, Set oAc, List i)
	{
		super(a,f,g,funcs,p,m);
		openActions = oAc;
		invariants = i;
	}

	public TemporalMetricState(Set a, Set f, GroundCondition g, Map funcs, Metric m)
	{
		super(a,f,g, funcs, m);
		openActions = new HashSet();
		invariants = new ArrayList();
		checker = new VelosoSchedulabilityChecker();

	}

	public boolean goalReached()
	{
		return (openActions.isEmpty() && super.goalReached());
	}

	public boolean checkAvailability(Action a)
	{
		List rList = new ArrayList(invariants);
		if (a instanceof SplitInstantAction)
		{
			SplitInstantAction da = (SplitInstantAction) a;
			Iterator iit = da.parent.invariant.getConditionalPropositions().iterator();
			while (iit.hasNext())
			{
				rList.remove(iit.next());
			}
		}
		rList.retainAll(a.getDeletePropositions());
		if (!rList.isEmpty()) return false;
		SchedulabilityChecker c = (VelosoSchedulabilityChecker) checker.clone(); //This should have to be cloned here and below
		boolean result = c.addAction((InstantAction)a, this);
		if (result && a instanceof StartInstantAction)
		{
			TemporalMetricState dupli = (TemporalMetricState) this.clone();
			dupli.apply(a);
			result = c.addAction(((StartInstantAction)a).getSibling(), dupli);

		}
		return result;
	}

	public Object clone()
	{
		Set nf = (Set) ((HashSet) facts).clone();
		TotalOrderPlan p = (TotalOrderPlan) plan.clone();
		Map nfuncs = (Map) ((Hashtable) funcValues).clone();
		Set oA = (Set) ((HashSet)openActions).clone();
		List i = (List) ((ArrayList)invariants).clone();
		Set na = (Set) ((HashSet) actions).clone();
		TemporalMetricState ts = new TemporalMetricState(na, nf, goal, nfuncs, p, metric, oA, i);
		ts.setRPG(relaxedPlanningGraph);
//		ts.setFilter(filter);
		ts.checker = (VelosoSchedulabilityChecker) checker.clone();
		return ts;
	}

	public State apply(Action a)    // return a cloned copy
	{
		TemporalMetricState s = (TemporalMetricState) super.apply(a);
		if (a instanceof SplitInstantAction)
		{
			SplitInstantAction sia = (SplitInstantAction) a;
			sia.applySplit(s);
		}
		s.checker.addAction((InstantAction)a, s);
		return s;
	}

	//public BigDecimal getGValue()
	//{
		//return super.getGValue().subtract(new BigDecimal(openActions.size()));
	//}
}
