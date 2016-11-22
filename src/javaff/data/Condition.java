//
//  Condition.java
//  JavaFF
//
//  Created by Keith Halsey on Thu Apr 15 2004.
//

package javaff.data;

import javaff.planning.State;

public interface Condition extends PDDLPrintable
{
	public boolean isStatic();      // returns whether this condition is static
}
