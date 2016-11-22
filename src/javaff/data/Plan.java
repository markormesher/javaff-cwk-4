//
//  Plan.java
//  JavaFF
//
//  Created by Keith Halsey on Fri Oct 24 2003.
//

package javaff.data;

import java.util.Set;
import java.io.PrintStream;
import java.io.PrintWriter;

public interface Plan extends Cloneable
{
	public abstract void print(PrintStream p);
	public abstract void print(PrintWriter p);
	public abstract Set getActions();
}
