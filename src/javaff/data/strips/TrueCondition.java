//
//  TrueCondition.java
//  JavaFF
//
//  Created by Keith Halsey on Wed Apr 21 2004.
//

package javaff.data.strips;

import javaff.data.PDDLPrinter;
import javaff.data.GroundCondition;
import javaff.data.UngroundCondition;
import javaff.data.UngroundEffect;
import javaff.planning.State;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.io.PrintStream;

public class TrueCondition implements GroundCondition, UngroundCondition
{
	private static TrueCondition t;
	
	private TrueCondition()
    {
    }

	public GroundCondition staticifyCondition(Map fValues)
	{
		return this;
	}
	

	public static TrueCondition getInstance()
	{
		if (t == null) t = new TrueCondition();
		return t;
	}

	public UngroundCondition minus(UngroundEffect effect)
	{
		return this;
	}
	
	public boolean isTrue(State s)
    {
		return true;
    }
	
	public boolean isStatic()
	{
		return true;
	}

	public Set getStaticPredicates()
	{
		return new HashSet();
	}

	public Set getConditionalPropositions()
	{
		return new HashSet();
	}

  public Set getComparators()
  {
  	return new HashSet();
  }

	public GroundCondition groundCondition(Map varMap)
	{
		return this;
	}

	public String toString()
	{
		return "()";
	}

	public String toStringTyped()
	{
		return toString();
	}

	public void PDDLPrint(java.io.PrintStream p, int indent)
	{
		PDDLPrinter.printToString(this, p, false, false, indent);
	}

}
