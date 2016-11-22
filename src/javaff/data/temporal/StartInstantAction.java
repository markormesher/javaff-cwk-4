//
//  StartInstantAction.java
//  JavaFF
//
//  Created by Keith Halsey on Mon Oct 20 2003.
//

package javaff.data.temporal;

import javaff.data.strips.Proposition;
import javaff.planning.TemporalMetricState;

public class StartInstantAction extends SplitInstantAction
{

	public SplitInstantAction getSibling()
	{
		return parent.endAction;
	}

	public void applySplit(TemporalMetricState ts)
    {
		ts.invariants.addAll(parent.invariant.getConditionalPropositions());
		ts.openActions.add(parent);
		ts.actions.remove(this);
		ts.actions.add(getSibling());
	}

	public boolean exclusivelyInvariant(Proposition p)
    {
		return !parent.startCondition.getConditionalPropositions().contains(p) || !parent.startEffect.getAddPropositions().contains(p) || !parent.startEffect.getDeletePropositions().contains(p);
    }
}
