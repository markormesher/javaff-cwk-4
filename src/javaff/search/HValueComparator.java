//
//  HValueComparator.java
//  JavaFF
//
//  Created by Keith Halsey on Tue Oct 21 2003.
//

package javaff.search;

import javaff.planning.State;

import java.util.Comparator;
import java.math.BigDecimal;

public class HValueComparator implements Comparator<State> {
	public int compare(State s1, State s2) {
		BigDecimal d1 = s1.getHValue();
		BigDecimal d2 = s2.getHValue();
		int r = d1.compareTo(d2);
		if (r == 0) {
			d1 = s1.getGValue();
			d2 = s2.getGValue();
			r = d1.compareTo(d2);
			if (r == 0) {
				int s1hash = s1.hashCode();
				int s2hash = s2.hashCode();
				return s1hash - s2hash;
			} else {
				return r;
			}
		}
		return 0;
	}
}
