//
//  Predicate.java
//  JavaFF
//
//  Created by Keith Halsey on Wed Oct 15 2003.
//

package javaff.data.strips;

import javaff.data.Literal;
import javaff.data.UngroundCondition;
import javaff.data.UngroundEffect;
import javaff.data.GroundCondition;
import javaff.data.GroundEffect;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

public class Predicate extends Literal implements UngroundCondition, UngroundEffect
{
	public Predicate(PredicateSymbol p)
    {
		name = p;
	}

	public boolean effects(PredicateSymbol ps)
    {
		return name.equals(ps);
	}

	public UngroundCondition minus(UngroundEffect effect)
    {
		return effect.effectsAdd(this);
    }

	public UngroundCondition effectsAdd(UngroundCondition cond)
    {
		if (this.equals(cond)) return TrueCondition.getInstance();
		else return cond;
    }

	public Set getStaticPredicates()
    {
		Set rSet = new HashSet();
		if (name.isStatic()) rSet.add(this);
		return rSet;
    }

	public Proposition ground(Map varMap)
    {
		Proposition p = new Proposition(name);
		Iterator pit = parameters.iterator();
		while (pit.hasNext())
		{
                        Object o = pit.next();
                        PDDLObject po;
                        if (o instanceof PDDLObject) po = (PDDLObject) o;
                        else 
                        {
                           Variable v = (Variable) o;                          
                           po = (PDDLObject) varMap.get(v);
                        }
                        
			p.addParameter(po);
		}
		return p;
    }

	public GroundCondition groundCondition(Map varMap)
    {
		return ground(varMap);
    }

	public GroundEffect groundEffect(Map varMap)
    {
		return ground(varMap);
	}

	public int hashCode()
    {
		int hash = 5;
		hash = 31 * hash ^ name.hashCode();
		hash = 31 * hash ^ parameters.hashCode();
		return hash;
	}

}
