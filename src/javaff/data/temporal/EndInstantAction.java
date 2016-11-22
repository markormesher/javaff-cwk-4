//
//  EndInstantAction.java
//  JavaFF
//
//  Created by Keith Halsey on Mon Oct 20 2003.
//

package javaff.data.temporal;

import javaff.data.strips.Proposition;
import javaff.planning.TemporalMetricState;

import java.util.Set;
import java.util.Iterator;

public class EndInstantAction extends SplitInstantAction 
{

	public SplitInstantAction getSibling()
    {
		return parent.startAction;
	}

	public void applySplit(TemporalMetricState ts)
    {
		Set is = parent.invariant.getConditionalPropositions();
		
		Iterator iit = is.iterator();
		while (iit.hasNext())
		{
			ts.invariants.remove(iit.next());
		}
		ts.openActions.remove(parent);
		ts.actions.remove(this);
		ts.actions.add(getSibling());
	}

	public boolean exclusivelyInvariant(Proposition p)
    {
		return !parent.endCondition.getConditionalPropositions().contains(p) || !parent.endEffect.getAddPropositions().contains(p) || !parent.endEffect.getDeletePropositions().contains(p);
	}
}
