//
//  SchedulabilityChecker.java
//  JavaFF
//
//  Created by Keith Halsey on Fri Jul 30 2004.
//

// checks the schedulability of a forward produced plan

package javaff.scheduling;

import javaff.data.strips.InstantAction;
import javaff.planning.TemporalMetricState;

public interface SchedulabilityChecker
{
	public Object clone();
	public boolean addAction(InstantAction a, TemporalMetricState s);
}
