//
//  EitherType.java
//  JavaFF
//
//  Created by Keith Halsey on Tue Apr 06 2004.
//

package javaff.data.strips;

import javaff.data.Type;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

public class EitherType extends Type
{
	protected Set types = new HashSet();

	public void addType(SimpleType t)
	{
		types.add(t);
	}

	public String toString()
	{
		String str = "(either";
		Iterator tit = types.iterator();
		while (tit.hasNext())
		{
			str+=" " +tit.next();
		}
		str += ")";
		return str;
	}

	public String toStringTyped()
	{
		return toString();
	}

	public boolean equals(Object obj)
	{
		if (obj instanceof EitherType)
		{
			EitherType et = (EitherType) obj;
			return (types.equals(et.types));
		}
		else return false;
	}

	public boolean isOfType(Type t) // is this of type t (i.e. is type further up the hierarchy)
	{
		Iterator tit = types.iterator();
		while (tit.hasNext())
		{
			SimpleType st = (SimpleType) tit.next();
			if (st.isOfType(t)) return true;
		}
		return false;
	}
	
}
