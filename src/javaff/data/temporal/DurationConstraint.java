//
//  DurationConstraint.java
//  JavaFF
//
//  Created by Keith Halsey on Tue Jul 27 2004.
//

package javaff.data.temporal;

import javaff.data.PDDLPrintable;
import javaff.data.PDDLPrinter;
import javaff.planning.MetricState;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.io.PrintStream;

public class DurationConstraint implements PDDLPrintable
{
	Set constraints = new HashSet();

	public boolean staticDuration()
	{
		boolean rTest = true;
		Iterator cit = constraints.iterator();
		while (cit.hasNext() && rTest)
		{
			SimpleDurationConstraint c = (SimpleDurationConstraint) cit.next();
			rTest = c.staticDuration();
		}
		return rTest;
	}

	public void add(SimpleDurationConstraint sdc)
	{
		constraints.add(sdc);
	}

	public DurationConstraint ground(Map varMap)
	{
		DurationConstraint dc = new DurationConstraint();
		Iterator cit = constraints.iterator();
		while (cit.hasNext())
		{
			SimpleDurationConstraint c = (SimpleDurationConstraint) cit.next();
			dc.add((SimpleDurationConstraint) c.ground(varMap));
		}
		return dc;	
	}

	public BigDecimal getDuration(MetricState ms)
	{
		return getMaxDuration(ms);
	}

	public BigDecimal getMaxDuration(MetricState ms)
	{
		BigDecimal sofar = javaff.JavaFF.MAX_DURATION;
		Iterator cit = constraints.iterator();
		while (cit.hasNext())
		{
			SimpleDurationConstraint c = (SimpleDurationConstraint) cit.next();
			BigDecimal ndec = c.getMaxDuration(ms);
			sofar = sofar.min(ndec);
		}
		return sofar;
	}

	public BigDecimal getMinDuration(MetricState ms)
	{
		BigDecimal sofar = new BigDecimal(0);
		Iterator cit = constraints.iterator();
		while (cit.hasNext())
		{
			SimpleDurationConstraint c = (SimpleDurationConstraint) cit.next();
			BigDecimal ndec = c.getMinDuration(ms);
			sofar = sofar.max(ndec);
		}
		return sofar;
	}

	public void PDDLPrint(PrintStream p, int indent)
	{
		p.println("(and ");
		Iterator cit = constraints.iterator();
		while (cit.hasNext())
		{
			SimpleDurationConstraint c = (SimpleDurationConstraint) cit.next();
			PDDLPrinter.printToString(c, p, false, false, indent);
		}
		p.print(")");
	}

	public String toString()
	{
		String str = "(and ";
		Iterator cit = constraints.iterator();
		while (cit.hasNext())
		{
			SimpleDurationConstraint c = (SimpleDurationConstraint) cit.next();
			str += c.toString();
		}
		str += ")";
		return str;
	}
	
	public String toStringTyped()
	{
		String str = "(and ";
		Iterator cit = constraints.iterator();
		while (cit.hasNext())
		{
			SimpleDurationConstraint c = (SimpleDurationConstraint) cit.next();
			str += c.toStringTyped();
		}
		str += ")";
		return str;
	}

	public int hashCode()
	{
		int hash = 7;
		hash = 31 * hash ^ constraints.hashCode();
		return hash;
	}

	public boolean equals(Object obj)
	{
		if (obj instanceof DurationConstraint)
		{
			DurationConstraint c = (DurationConstraint) obj;
			return c.constraints.equals(constraints);
		}
		else return false;
	}
}
