//
//  GroundEffect.java
//  JavaFF
//
//  Created by Keith Halsey on Mon Apr 26 2004.
//

package javaff.data;

import javaff.planning.State;
import java.util.Set;
import java.util.Map;

public interface GroundEffect extends Effect
{
	public void apply(State s); // carry out the effects of this on State s
	public void applyAdds(State s);
	public void applyDels(State s);
	public Set getAddPropositions();
	public Set getDeletePropositions();
    public Set getOperators();
	public GroundEffect staticifyEffect(Map fValues);
}
