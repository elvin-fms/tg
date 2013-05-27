package ua.com.fielden.platform.eql.s2.elements;

import java.util.ArrayList;
import java.util.List;


abstract class TwoOperandsFunction extends AbstractFunction {
    private final ISingleOperand2 operand1;
    private final ISingleOperand2 operand2;

    public TwoOperandsFunction(final ISingleOperand2 operand1, final ISingleOperand2 operand2) {
	this.operand1 = operand1;
	this.operand2 = operand2;
    }

    @Override
    public List<EntQuery> getLocalSubQueries() {
	final List<EntQuery> result = new ArrayList<EntQuery>();
	result.addAll(operand1.getLocalSubQueries());
	result.addAll(operand2.getLocalSubQueries());
	return result;
    }

    @Override
    public List<EntProp> getLocalProps() {
	final List<EntProp> result = new ArrayList<EntProp>();
	result.addAll(operand1.getLocalProps());
	result.addAll(operand2.getLocalProps());
	return result;
    }

    @Override
    public List<EntValue> getAllValues() {
	final List<EntValue> result = new ArrayList<EntValue>();
	result.addAll(operand1.getAllValues());
	result.addAll(operand2.getAllValues());
	return result;
    }

    @Override
    public boolean ignore() {
	return false;
    }

    public ISingleOperand2 getOperand1() {
        return operand1;
    }

    public ISingleOperand2 getOperand2() {
        return operand2;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((operand1 == null) ? 0 : operand1.hashCode());
	result = prime * result + ((operand2 == null) ? 0 : operand2.hashCode());
	return result;
    }

    @Override
    public boolean equals(final Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj == null) {
	    return false;
	}
	if (!(obj instanceof TwoOperandsFunction)) {
	    return false;
	}
	final TwoOperandsFunction other = (TwoOperandsFunction) obj;
	if (operand1 == null) {
	    if (other.operand1 != null) {
		return false;
	    }
	} else if (!operand1.equals(other.operand1)) {
	    return false;
	}
	if (operand2 == null) {
	    if (other.operand2 != null) {
		return false;
	    }
	} else if (!operand2.equals(other.operand2)) {
	    return false;
	}
	return true;
    }
}