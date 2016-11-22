//
//  HelpfulFilter.java
//  JavaFF
//
//  Created by Keith Halsey on Thu May 13 2004.
//

package javaff.planning;

import javaff.data.Action;

import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

public class HelpfulFilter implements Filter
{
	private static HelpfulFilter hf = null;

	private HelpfulFilter()
	{
	}

	public static HelpfulFilter getInstance()
	{
		if (hf == null) hf = new HelpfulFilter(); // Singleton, as in NullFilter
		return hf;
	}

	public Set getActions(State S)
	{
		STRIPSState SS = (STRIPSState) S;
		SS.calculateRP(); // get the relaxed plan to the goal, to make sure helpful actions exist for S
		Set ns = new HashSet();
		Iterator ait = SS.helpfulActions.iterator(); // iterate over helpful actions
		while (ait.hasNext())
		{
			Action a = (Action) ait.next();
			if (a.isApplicable(S)) ns.add(a); // and add them to the set to return if they're applicable
		}
		return ns;
	}
} 