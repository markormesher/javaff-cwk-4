//
//  GroundProblem.java
//  JavaFF
//
//  Created by Keith Halsey on Tue Jan 20 2004.
//

package javaff.data;

import javaff.planning.STRIPSState;
import javaff.planning.MetricState;
import javaff.planning.TemporalMetricState;
import javaff.planning.RelaxedPlanningGraph;
import javaff.planning.RelaxedMetricPlanningGraph;
import javaff.planning.RelaxedTemporalMetricPlanningGraph;
import javaff.data.strips.InstantAction;
import javaff.data.temporal.DurativeAction;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.Hashtable;
import java.util.Iterator;

public class GroundProblem implements Cloneable
{
    //public Set facts = new HashSet();                  // (Proposition)
    public Set actions = new HashSet();                // (GroundAction)
    public Map functionValues = new Hashtable();     // (NamedFunction => BigDecimal)
	public Metric metric;

    public GroundCondition goal;
    public Set initial;                                // (Proposition)

	public TemporalMetricState state = null;

	public GroundProblem(Set a, Set i, GroundCondition g, Map f, Metric m)
	{
		actions = a;
		initial = i;
		goal = g;
		functionValues = f;
		metric = m;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		Set actionsCopy = new HashSet();
		actionsCopy.addAll(actions);

		Set initialCopy = new HashSet();
		initialCopy.addAll(initial);

		Map functionValuesCopy = new Hashtable();
		functionValuesCopy.putAll(functionValues);

		return new GroundProblem(actionsCopy, initialCopy, goal, functionValuesCopy, metric);
	}

	public STRIPSState getSTRIPSInitialState()
    {
    	STRIPSState s = new STRIPSState(actions, initial, goal);
		s.setRPG(new RelaxedPlanningGraph(this));
		return s;
	}

	public MetricState getMetricInitialState()
    {
		MetricState ms = new MetricState(actions, initial, goal, functionValues, metric);
		ms.setRPG(new RelaxedMetricPlanningGraph(this));
		return ms;
	}

	public TemporalMetricState getTemporalMetricInitialState()
    {
		if (state == null)
		{
			Set na = new HashSet();
			Set ni = new HashSet();
			Iterator ait = actions.iterator();
			while (ait.hasNext())
			{
				Action act = (Action) ait.next();
				if (act instanceof InstantAction)
				{
					na.add(act);
					ni.add(act);
				}
				else if (act instanceof DurativeAction)
				{
					DurativeAction dact = (DurativeAction) act;
					na.add(dact.startAction);
					na.add(dact.endAction);
					ni.add(dact.startAction);
				}
			}
			TemporalMetricState ts = new TemporalMetricState(ni, initial, goal, functionValues, metric);
			GroundProblem gp = new GroundProblem(na, initial, goal, functionValues, metric);
			ts.setRPG(new RelaxedTemporalMetricPlanningGraph(gp));
			state = ts;
		}
		return state;
	}


}
