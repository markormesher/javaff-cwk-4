//
//  UngroundCondition.java
//  JavaFF
//
//  Created by Keith Halsey on Mon Apr 26 2004.
//

package javaff.data;

import java.util.Map;
import java.util.Set;

public interface UngroundCondition extends Condition
{
	public GroundCondition groundCondition(Map varMap);
	public Set getStaticPredicates();
	public UngroundCondition minus(UngroundEffect effect);
}
