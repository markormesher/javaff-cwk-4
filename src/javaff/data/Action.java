//
//  Action.java
//  JavaFF
//
//  Created by Keith Halsey on Mon Dec 01 2003.
//

package javaff.data;

import javaff.data.strips.OperatorName;
import javaff.planning.State;

import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.math.BigDecimal;

public abstract class Action
{
    public OperatorName name;
    public List params = new ArrayList(); // List of PDDLObjects

	public BigDecimal cost = new BigDecimal(0);

    public String toString()
    {
		String stringrep = name.toString();
		Iterator i = params.iterator();
		while (i.hasNext())
		{
			stringrep = stringrep + " " +  i.next();
		}
		return stringrep;
    }

	public abstract boolean isApplicable(State s);
	public abstract void apply(State s);
	public abstract Set getConditionalPropositions();
	public abstract Set getAddPropositions();
	public abstract Set getDeletePropositions();
	public abstract Set getComparators();
	public abstract Set getOperators();
	public abstract void staticify(Map fValues);

	public boolean equals(Object obj)
    {
		if (obj instanceof Action)
		{
			Action a = (Action) obj;
			return (name.equals(a.name) && params.equals(a.params));
		}
		else return false;
    }

    public int hashCode()
    {
        return name.hashCode() ^ params.hashCode();
    }
}
