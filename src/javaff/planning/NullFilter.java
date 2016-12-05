//
//  NullFilter.java
//  JavaFF
//
//  Created by Andrew Coles on Mon Jan 28 2007.
//

package javaff.planning;

import javaff.data.Action;

import java.util.HashSet;
import java.util.Set;

public class NullFilter implements Filter {

	public Set getActions(State state) {
		Set actionsFromS = state.getActions();
		Set<Action> ns = new HashSet<>();
		for (Object action : actionsFromS) {
			if (((Action) action).isApplicable(state)) ns.add((Action) action);
		}
		return ns;
	}

}
