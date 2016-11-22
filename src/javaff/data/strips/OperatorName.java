//
//  OperatorName.java
//  JavaFF
//
//  Created by Keith Halsey on Thu Apr 15 2004.
//

package javaff.data.strips;

public class OperatorName
{
	protected String name;

	public OperatorName(String n)
	{
		name = n;
	}

	public String toString()
	{
		return name;
	}

	public boolean equals(Object obj)
	{
		if (obj instanceof OperatorName)
		{
			OperatorName os = (OperatorName) obj;
			return (name.equals(os.name));
		}
		else return false;
	}

	public int hashCode()
	{
		return name.hashCode();
	}

	
}
