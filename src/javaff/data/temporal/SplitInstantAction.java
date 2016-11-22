//
//  SplitInstantAction.java
//  JavaFF
//
//  Created by Keith Halsey on Tue Dec 02 2003.
//

package javaff.data.temporal;

import javaff.data.strips.Proposition;
import javaff.data.strips.InstantAction;
import javaff.planning.TemporalMetricState;

public abstract class SplitInstantAction extends InstantAction
{
    public DurativeAction parent;
	public abstract SplitInstantAction getSibling();

	public boolean equals(Object obj)
    {
		if (obj instanceof SplitInstantAction)
		{
			SplitInstantAction a = (SplitInstantAction) obj;
			return (name.equals(a.name) && params.equals(a.params) && this.getClass().equals(a.getClass()));
		}
		else return false;
    }

    public int hashCode()
    {
        return name.hashCode() ^ params.hashCode() ^ this.getClass().hashCode();
    }

	public abstract void applySplit(TemporalMetricState ts);
	public abstract boolean exclusivelyInvariant(Proposition p);
	
}
