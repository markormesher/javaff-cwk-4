//
//  Filter.java
//  JavaFF
//
//  Created by Keith Halsey on Thu May 13 2004.
//

package javaff.planning;

import java.util.Set;

public interface Filter
{
	public Set getActions(State S); // simple method: takes a state S, returns a Set of states in its neighbourhood
} 