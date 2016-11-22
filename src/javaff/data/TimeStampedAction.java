//
//  TimeStampedAction.java
//  JavaFF
//
//  Created by Keith Halsey on Tue Aug 03 2004.
//

package javaff.data;

import java.math.BigDecimal;

public class TimeStampedAction implements Comparable
{
	public Action action;
	public BigDecimal time;
	public BigDecimal duration;

	public TimeStampedAction(Action a, BigDecimal t, BigDecimal d)
	{
		action = a;
		time = t;
		duration = d;
	}

	public String toString()
	{
		String str = time +": ("+action+")";
		if (duration != null) str += " ["+duration+"]";
		return str;
	}

	public int compareTo(Object o)
	{
		TimeStampedAction that = (TimeStampedAction) o;
		if (this.time.compareTo(that.time) != 0) return this.time.compareTo(that.time);
		return ((new Integer(this.action.hashCode())).compareTo(new Integer(that.action.hashCode())));
	}
}
