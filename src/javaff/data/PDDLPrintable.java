//
//  PDDLPrintable.java
//  JavaFF
//
//  Created by Keith Halsey on Mon Apr 19 2004.
//

package javaff.data;

import java.io.PrintStream;

public interface PDDLPrintable
{
	public void PDDLPrint(PrintStream p, int indent);
	public String toStringTyped();
}
