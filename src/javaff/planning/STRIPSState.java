//
//  STRIPSState.java
//  JavaFF
//
//  Created by Keith Halsey on Thu May 13 2004.
//

package javaff.planning;

import javaff.data.Action;
import javaff.data.TotalOrderPlan;
import javaff.data.GroundCondition;
import javaff.data.strips.Proposition;
import javaff.data.Plan;

import java.math.BigDecimal;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

public class STRIPSState extends State implements Cloneable {

	public Set facts;
	public Set actions;

	protected TotalOrderPlan plan = new TotalOrderPlan();

	protected RelaxedPlanningGraph relaxedPlanningGraph;
	protected boolean rpCalculated = false;
	protected BigDecimal hValue = null;
	public TotalOrderPlan relaxedPlan = null;
	public Set helpfulActions = null;

	protected STRIPSState() {

	}

	public STRIPSState(Set a, Set f, GroundCondition g) {
		facts = f;
		goal = g;
		actions = a;
	}

	protected STRIPSState(Set a, Set f, GroundCondition g, TotalOrderPlan p) {
		this(a, f, g);
		plan = p;
	}

	public Object clone() {
		Set nf = (Set) ((HashSet) facts).clone();
		TotalOrderPlan p = (TotalOrderPlan) plan.clone();
		STRIPSState SS = new STRIPSState(actions, nf, goal, p);
		SS.setRPG(relaxedPlanningGraph);
		return SS;
	}

	public void setRPG(RelaxedPlanningGraph rpg) {
		relaxedPlanningGraph = rpg;
	}

	public RelaxedPlanningGraph getRPG() {
		return relaxedPlanningGraph;
	}

	public State apply(Action a) {
		STRIPSState s = (STRIPSState) super.apply(a);
		s.plan.addAction(a);
		return s;
	}

	public void addProposition(Proposition p) {
		facts.add(p);
	}

	public void removeProposition(Proposition p) {
		facts.remove(p);
	}

	public boolean isTrue(Proposition p) {
		return facts.contains(p);
	}

	public Set getActions() {
		return actions;
	}

	public void calculateRelaxedPlan() {
		if (!rpCalculated) {
			relaxedPlan = (TotalOrderPlan) relaxedPlanningGraph.getPlan(this);
			helpfulActions = new HashSet();
			if (!(relaxedPlan == null)) {
				hValue = new BigDecimal(relaxedPlan.getPlanLength());

				Iterator it = relaxedPlan.iterator();
				while (it.hasNext()) {
					Action a = (Action) it.next();
					if (relaxedPlanningGraph.getLayer(a) == 0) helpfulActions.add(a);
				}
			} else {
				hValue = javaff.JavaFF.MAX_DURATION;
			}
			rpCalculated = true;
		}
	}

	public BigDecimal getHValue() {
		calculateRelaxedPlan();
		return hValue;
	}

	public BigDecimal getGValue() {
		return new BigDecimal(plan.getPlanLength());
	}

	public Plan getSolution() {
		return plan;
	}

	public boolean equals(Object obj) {
		if (obj instanceof STRIPSState) {
			STRIPSState s = (STRIPSState) obj;
			return s.facts.equals(facts);
		} else return false;
	}

	public int hashCode() {
		int hash = 7;
		hash = 31 * hash ^ facts.hashCode();
		return hash;
	}


}
