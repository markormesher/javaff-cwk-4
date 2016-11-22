//
//  PredicateSymbol.java
//  JavaFF
//
//  Created by Keith Halsey on Tue Apr 06 2004.
//

package javaff.data.strips;

import javaff.data.PDDLPrinter;
import javaff.data.PDDLPrintable;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.io.PrintStream;

public class PredicateSymbol implements PDDLPrintable
{
	protected String name;
	protected boolean staticValue;

	protected List params = new ArrayList(); //The parameters (types) that this predicate symbol takes

	protected PredicateSymbol()
	{
		
	}

	public PredicateSymbol(String pName)
	{
		name = pName;
	}

	public String toString()
	{
		return name;
	}

	public String toStringTyped()
	{
		String str = name;
		Iterator it = params.iterator();
		while (it.hasNext())
		{
			Variable v = (Variable) it.next();
			str += " " + v.toStringTyped();
		}
		return str;
	}

	public boolean isStatic()
	{
		return staticValue;
	}

	public void setStatic(boolean stat)
	{
		staticValue = stat;
	}

	public void addVar(Variable v)
	{
		params.add(v);
	}

	public boolean equals(Object obj)
	{
		if (obj instanceof PredicateSymbol)
		{
			PredicateSymbol ps = (PredicateSymbol) obj;
			return (name.equals(ps.name) && params.equals(ps.params));
		}
		else return false;
	}

	public int hashCode()
	{
		int hash = 8;
		hash = 31 * hash ^ name.hashCode();
		hash = 31 * hash ^ params.hashCode();
		return hash;
	}

	public void PDDLPrint(PrintStream p, int indent)
	{
		PDDLPrinter.printToString(this, p, true, true, indent);
	}
}
