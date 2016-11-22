//
//  PDDLObject.java
//  JavaFF
//
//  Created by Keith Halsey on Tue Apr 06 2004.
//

package javaff.data.strips;

import javaff.data.Type;

public class PDDLObject extends javaff.data.Parameter 
{
	public PDDLObject(String n)
    {
		super(n);
	}

	public PDDLObject(String n, Type t)
    {
		super(n,t);
	}

	public int hashCode()
    {
		int hash = 10;
		hash = 31 * hash ^ name.hashCode();
		hash = 31 * hash ^ type.hashCode();
		return hash;
	}
}
