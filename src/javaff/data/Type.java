//
//  Type.java
//  JavaFF
//
//  Created by Keith Halsey on Tue Apr 06 2004.
//

package javaff.data;

import javaff.data.strips.RootType;
import java.io.PrintStream;

public abstract class Type implements PDDLPrintable
{
	public static Type rootType = RootType.getInstance();
	
	public abstract boolean isOfType(Type t); // is this of type t (i.e. is t it parent or higher up the hierarchy)

	public abstract String toStringTyped();

	public void PDDLPrint(PrintStream p, int indent)
	{
		PDDLPrinter.printToString(this, p, true, false, indent);
	}
}
