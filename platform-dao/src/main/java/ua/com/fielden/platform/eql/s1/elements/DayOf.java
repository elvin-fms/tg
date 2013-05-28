package ua.com.fielden.platform.eql.s1.elements;

import ua.com.fielden.platform.eql.meta.TransformatorToS2;
import ua.com.fielden.platform.eql.s2.elements.ISingleOperand2;



public class DayOf extends SingleOperandFunction<ua.com.fielden.platform.eql.s2.elements.DayOf> {
    public DayOf(final ISingleOperand<? extends ISingleOperand2> operand) {
	super(operand);
    }

    @Override
    public ua.com.fielden.platform.eql.s2.elements.DayOf transform(final TransformatorToS2 resolver) {
	return new ua.com.fielden.platform.eql.s2.elements.DayOf(getOperand().transform(resolver));
    }
}