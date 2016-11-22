//
//  NullFilter.java
//  JavaFF
//
//  Created by Andrew Coles on Mon Jan 28 2007.
//

package javaff.planning;

import javaff.data.Action;
import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;

public class NullFilter implements Filter
{
	private static NullFilter nf = null;

	private NullFilter()
	{
	}

	public static NullFilter getInstance()
	{
		if (nf == null) nf = new NullFilter(); // Singleton design pattern - return one central instance
		return nf;
	}

	public Set getActions(State S)
	{
		Set actionsFromS = S.getActions(); // get the logically appicable actions in S
		Set ns = new HashSet();
		Iterator ait = actionsFromS.iterator(); // Get an iterator over these actions
		while (ait.hasNext())
		{
			Action a = (Action) ait.next();
			if (a.isApplicable(S)) ns.add(a); // Check they are applicable (will check numeric/temporal constraints)
		}
		return ns;
	}

} 