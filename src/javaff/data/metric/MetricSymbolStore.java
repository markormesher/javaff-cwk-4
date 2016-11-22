//
//  MetricSymbolStore.java
//  JavaFF
//
//  Created by Keith Halsey on Thu Jul 15 2004.
//

package javaff.data.metric;

import java.math.BigDecimal;

public abstract class MetricSymbolStore
{
	public static int GREATER_THAN = 0;
    public static int GREATER_THAN_EQUAL = 1;
    public static int LESS_THAN = 2;
    public static int LESS_THAN_EQUAL = 3;
    public static int EQUAL = 4;
	
	public static int PLUS = 5;
    public static int MINUS = 6;
    public static int MULTIPLY = 7;
    public static int DIVIDE = 8;

	public static int ASSIGN = 9;
    public static int INCREASE = 10;
    public static int DECREASE = 11;
    public static int SCALE_UP = 12;
    public static int SCALE_DOWN = 13;

	public static int SCALE = 2;
    public static int ROUND = BigDecimal.ROUND_HALF_EVEN;

	public static int getType(String s)
	{
		if (s.equals(">")) return GREATER_THAN;
		else if (s.equals(">=")) return GREATER_THAN_EQUAL;
		else if (s.equals("<")) return LESS_THAN;
		else if (s.equals("<=")) return LESS_THAN_EQUAL;
		else if (s.equals("=")) return EQUAL;
		else if (s.equals("+")) return PLUS;
		else if (s.equals("-")) return MINUS;
		else if (s.equals("*")) return MULTIPLY;
		else if (s.equals("/")) return DIVIDE;
		else if (s.equals("assign") || s.equals(":=")) return ASSIGN;
		else if (s.equals("increase") || s.equals("+=")) return INCREASE;
		else if (s.equals("decrease") || s.equals("-=")) return DECREASE;
		else if (s.equals("scale-up") || s.equals("*=")) return SCALE_UP;
        else if (s.equals("scale-down") || s.equals("/=")) return SCALE_DOWN;
		else return -1;
	}

	public static String getSymbol(int t)
	{
		switch (t)
		{
			case 0: return ">";
			case 1: return ">=";
			case 2: return "<";
			case 3: return "<=";
			case 4: return "=";
			case 5: return "+";
			case 6: return "-";
			case 7: return "*";
			case 8: return "/";
			case 9: return "assign";
			case 10: return "increase";
			case 11: return "decrease";
			case 12: return "scale-up";
			case 13: return "scale-down";
		}
		return "";
	}
}
