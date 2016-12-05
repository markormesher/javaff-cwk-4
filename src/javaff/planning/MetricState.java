//
//  MetricState.java
//  JavaFF
//
//  Created by Keith Halsey on Thu Jul 15 2004.
//

package javaff.planning;

import javaff.data.GroundCondition;
import javaff.data.TotalOrderPlan;
import javaff.data.Metric;
import javaff.data.metric.NamedFunction;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.Hashtable;
import java.math.BigDecimal;

public class MetricState extends STRIPSState
{
	public Map funcValues; //maps Named Functions onto BigDecimals
	public Metric metric;

	protected MetricState()
	{

	}

	public MetricState(Set a, Set f, GroundCondition g, Map funcs, Metric m)
	{
		super(a,f,g);
		funcValues = funcs;
		metric = m;
	}

	protected MetricState(Set a, Set f, GroundCondition g, Map funcs, TotalOrderPlan p, Metric m)
	{
		super(a,f,g,p);
		funcValues = funcs;
		metric = m;
	}

	public Object clone()
	{
		Set nf = (Set) ((HashSet) facts).clone();
		TotalOrderPlan p = (TotalOrderPlan) plan.clone();
		Map nfuncs = (Map) ((Hashtable) funcValues).clone();
		MetricState ms = new MetricState(actions, nf, goal, nfuncs, p, metric);
		ms.setRPG(relaxedPlanningGraph);
//		ms.setFilter(filter);
		return ms;
	}

	public BigDecimal getValue(NamedFunction nf)
	{
		return (BigDecimal) funcValues.get(nf);
	}

	public void setValue(NamedFunction nf, BigDecimal d)
	{
		funcValues.put(nf, d);
	}

	// WARNING - not yet implemented  - must be overridden and take account of the metric
	public BigDecimal getHValue()
	{
		return super.getHValue();
	}

	public BigDecimal getGValue()
	{
		return super.getGValue();
	}

	public boolean equals(Object obj)
	{
		if (obj instanceof MetricState)
		{
			MetricState s = (MetricState) obj;
			return (s.facts.equals(facts) && s.funcValues.equals(funcValues));
		}
		else return false;
	}

	public int hashCode()
	{
		int hash = 8;
		hash = 31 * hash ^ facts.hashCode();
		hash = 31 * hash ^ funcValues.hashCode();
		return hash;
	}

}
