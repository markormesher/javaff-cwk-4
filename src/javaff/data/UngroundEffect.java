//
//  UngroundEffect.java
//  JavaFF
//
//  Created by Keith Halsey on Mon Apr 26 2004.
//

package javaff.data;

import java.util.Map;
import java.util.Set;
import javaff.data.strips.PredicateSymbol;

public interface UngroundEffect extends Effect
{
	public boolean effects(PredicateSymbol ps);
	public UngroundCondition effectsAdd(UngroundCondition cond);
	public GroundEffect groundEffect(Map varMap);
}
