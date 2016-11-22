//
//  GraphSTN.java
//  JavaFF
//
//  Created by Keith Halsey on Mon Oct 20 2003.
//

package javaff.scheduling;

import javaff.data.strips.InstantAction;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.math.BigDecimal;

public class GraphSTN implements Cloneable, SimpleTemporalNetwork
{    
    Set nodes = new HashSet();
    Set edges = new HashSet();
   
    public Object clone()
    {
		GraphSTN newSTN = new GraphSTN();
		newSTN.nodes.addAll(this.nodes);
		newSTN.edges.addAll(this.edges);
		return newSTN;
    }
   
    public boolean consistent()
    {
		boolean consistency = true;
		Iterator sit = nodes.iterator();
		while (sit.hasNext() && consistency)
		{
			InstantAction source = (InstantAction) sit.next();
			consistency = consistentSource(source);
		}
		return consistency;
    }

    //Implementation of Bellman-Ford Algorithm for Single Source Shortest Path with negative edges
    public boolean consistentSource(InstantAction source)
    {
		List nodeIndex = new ArrayList(nodes);
		InstantAction p[] = new InstantAction[nodeIndex.size()];
		BigDecimal d[] = new BigDecimal[nodeIndex.size()];
		for (int count = 0; count < nodeIndex.size(); ++count)
		{
			p[count] = (InstantAction) nodeIndex.get(count);
			d[count] = javaff.JavaFF.MAX_DURATION;
		}
		d[nodeIndex.indexOf(source)] = new BigDecimal(0);

		for (int count = 0; count < nodeIndex.size(); ++count)
		{
			Iterator eit = edges.iterator();
			while (eit.hasNext())
			{
				TemporalConstraint e = (TemporalConstraint) eit.next();
				//relax
				int sourceIndex = nodeIndex.indexOf(e.y);
				int sinkIndex = nodeIndex.indexOf(e.x);
				if (e.b.add(d[sourceIndex]).compareTo(d[sinkIndex])<0)
				{
					d[sinkIndex] = e.b.add(d[sourceIndex]);
					p[sinkIndex] = e.y;
				}
			}
		}

		Iterator eit = edges.iterator();
		while (eit.hasNext())
		{
			TemporalConstraint e = (TemporalConstraint) eit.next();
			if (e.b.add(d[nodeIndex.indexOf(e.y)]).compareTo(d[nodeIndex.indexOf(e.x)]) < 0) return false;
		}
		
		return true;
    }

    public void addConstraints(Set constraints)
    {
		Iterator oit = constraints.iterator();
		while (oit.hasNext())
		{
			TemporalConstraint c = (TemporalConstraint) oit.next();
			addConstraint(c);
		}
    }

	public void addConstraint(TemporalConstraint c)
    {
		nodes.add(c.y);
		nodes.add(c.x);
		edges.add(c);
    }


	private class Node
    {
		InstantAction a;
		BigDecimal d;
		Node p;

		public Node(InstantAction act)
		{
			act = a;
		}

		public int hashCode()
		{
			return a.hashCode();
		}
    }
   
}







