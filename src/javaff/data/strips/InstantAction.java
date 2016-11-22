//
//  InstantAction.java
//  JavaFF
//
//  Created by Keith Halsey on Mon Oct 20 2003.
//

package javaff.data.strips;

import javaff.data.GroundCondition;
import javaff.data.GroundEffect;
import javaff.data.Action;
import javaff.planning.State;
import java.util.Set;
import java.util.Map;

public abstract class InstantAction extends Action
{
    public GroundCondition condition;
	public GroundEffect effect;

	public boolean isApplicable(State s)
	{
		return condition.isTrue(s) && s.checkAvailability(this);
	}

	public void apply(State s)
	{
		effect.applyDels(s);
		effect.applyAdds(s);
	}

	public Set getConditionalPropositions()
	{
		return condition.getConditionalPropositions();
	}

	public Set getAddPropositions()
	{
		return effect.getAddPropositions();
	}

	public Set getDeletePropositions()
	{
		return effect.getDeletePropositions();
	}

	public Set getComparators()
	{
		return condition.getComparators();
	}

	public Set getOperators()
	{
		return effect.getOperators();
	}

	public void staticify(Map fValues)
	{
		condition = condition.staticifyCondition(fValues);
		effect = effect.staticifyEffect(fValues);
	}

}
