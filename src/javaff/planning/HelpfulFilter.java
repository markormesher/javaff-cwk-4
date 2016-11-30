//
//  HelpfulFilter.java
//  JavaFF
//
//  Created by Keith Halsey on Thu May 13 2004.
//

package javaff.planning;

import javaff.data.Action;

import java.util.HashSet;
import java.util.Set;

public class HelpfulFilter implements Filter {

	public Set getActions(State state) {
		STRIPSState stripState = (STRIPSState) state;
		stripState.calculateRelaxedPlan();
		Set<Action> ns = new HashSet<>();
		for (Object helpfulAction : stripState.helpfulActions) {
			if (((Action) helpfulAction).isApplicable(state))
				ns.add((Action) helpfulAction); // and add them to the set to return if they're applicable
		}
		return ns;
	}
}
