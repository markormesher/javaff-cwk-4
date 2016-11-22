//
//  Metric.java
//  JavaFF
//
//  Created by Keith Halsey on Fri Jul 16 2004.
//

package javaff.data;

import javaff.data.metric.Function;

import java.io.PrintStream;

public class Metric implements PDDLPrintable
{
	public static int MAXIMIZE = 0;
	public static int MINIMIZE = 1;

	public int type;
	public Function func;
	
	public Metric(int t, Function f)
	{
		type = t;
		func = f;
	}

	public void PDDLPrint(PrintStream p, int indent)
	{
		p.print("(:metric ");
		p.print(toString());
		p.print(")");
	}

	public String toString()
	{
		String str = "";
		if (type == MAXIMIZE) str += "maximize ";
		else if (type == MINIMIZE) str += "minimize ";
		str += func.toString();
		return str;
	}

	public String toStringTyped()
	{
		String str = "";
		if (type == MAXIMIZE) str += "maximize ";
		else if (type == MINIMIZE) str += "minimize ";
		str += func.toStringTyped();
		return str;
	}
}
