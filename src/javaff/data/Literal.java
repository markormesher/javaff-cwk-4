//
//  Literal.java
//  JavaFF
//
//  Created by Keith Halsey on Sat Apr 24 2004.
//

package javaff.data;

import javaff.data.strips.PredicateSymbol;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.PrintStream;

public abstract class Literal implements Condition, Effect
{
	protected PredicateSymbol name;
    protected List parameters = new ArrayList(); // list of Parameters

	public void setPredicateSymbol(PredicateSymbol n)
	{
		name = n;
	}

	public PredicateSymbol getPredicateSymbol()
	{
		return name;
	}
	
	public List getParameters()
	{
		return parameters;
	}

	public void addParameter(Parameter p)
	{
		parameters.add(p);
	}

	public void addParameters(List l)
	{
		parameters.addAll(l);
	}

	public String toString()
    {
		String stringrep = name.toString();
		Iterator i = parameters.iterator();
		while(i.hasNext())
		{
			stringrep = stringrep + " " + i.next();
		}
		return stringrep;
    }

	public String toStringTyped()
    {
		String stringrep = name.toString();
		Iterator i = parameters.iterator();
		while(i.hasNext())
		{
			Parameter o = (Parameter) i.next();
			stringrep += " " + o + " - " + o.type.toString();
		}
		return stringrep;
    }

    public boolean equals(Object obj)
    {
        if (obj instanceof Literal)
		{
			Literal p = (Literal) obj;
			return (name.equals(p.name) && parameters.equals(p.parameters) && this.getClass() == p.getClass());
		}
		else return false;
    }
  

	public boolean isStatic()
	{
		return name.isStatic();
	}

	public void PDDLPrint(PrintStream p, int indent)
	{
		PDDLPrinter.printToString(this, p, false, true, indent);
	}
}
