//
//  Scheduler.java
//  JavaFF
//
//  Created by Keith Halsey on Tue Aug 03 2004.
//

package javaff.scheduling;

import javaff.data.TotalOrderPlan;
import javaff.data.TimeStampedPlan;

public interface Scheduler
{
	public TimeStampedPlan schedule(TotalOrderPlan top);
}
