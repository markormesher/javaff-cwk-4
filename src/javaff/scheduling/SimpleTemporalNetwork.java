//
//  SimpleTemporalNetwork.java
//  JavaFF
//
//  Created by Keith Halsey on Fri Jul 30 2004.
//

package javaff.scheduling;

import javaff.data.strips.InstantAction;

import java.util.Set;

public interface SimpleTemporalNetwork
{
	public void addConstraints(Set constraints);
	public void addConstraint(TemporalConstraint c);
	public boolean consistentSource(InstantAction s);
	public boolean consistent();
}
