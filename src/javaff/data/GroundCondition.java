//
//  GroundCondition.java
//  JavaFF
//
//  Created by Keith Halsey on Mon Apr 26 2004.
//

package javaff.data;

import javaff.planning.State;
import java.util.Set;
import java.util.Map;

public interface GroundCondition extends Condition
{
	public boolean isTrue(State s); // returns whether this conditions is true is State S
	public Set getConditionalPropositions();
	public Set getComparators();
	public GroundCondition staticifyCondition(Map fValues);
}
