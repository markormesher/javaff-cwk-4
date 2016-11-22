//
//  Function.java
//  JavaFF
//
//  Created by Keith Halsey on Mon Jan 12 2004.
//

package javaff.data.metric;

import javaff.planning.MetricState;
import java.math.BigDecimal;
import java.util.Map;
import javaff.scheduling.MatrixSTN;

public interface Function 
{
    public BigDecimal getValue(MetricState s);
	public boolean isStatic();
	public String toStringTyped();
	public Function ground(Map varMap);
	public Function staticify(Map fValues);
	public boolean effectedBy(ResourceOperator ro);
	public Function replace(ResourceOperator ro); //replaces the resource for the change
	public Function makeOnlyDurationDependent(MetricState s);
	public BigDecimal getMaxValue(MatrixSTN stn);
	public BigDecimal getMinValue(MatrixSTN stn);
}
