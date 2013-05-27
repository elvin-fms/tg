package ua.com.fielden.platform.eql.s2.elements;

import java.util.ArrayList;
import java.util.List;


public class OperandsBasedSet implements ISetOperand2{
    private final List<ISingleOperand2> operands;

    public OperandsBasedSet(final List<ISingleOperand2> operands) {
	super();
	this.operands = operands;
    }

    @Override
    public List<EntProp> getLocalProps() {
	final List<EntProp> result = new ArrayList<EntProp>();
	for (final ISingleOperand2 operand : operands) {
	    result.addAll(operand.getLocalProps());
	}
	return result;
    }

    @Override
    public List<EntQuery> getLocalSubQueries() {
	final List<EntQuery> result = new ArrayList<EntQuery>();
	for (final ISingleOperand2 operand : operands) {
	    result.addAll(operand.getLocalSubQueries());
	}
	return result;
    }

    @Override
    public List<EntValue> getAllValues() {
	final List<EntValue> result = new ArrayList<EntValue>();
	for (final ISingleOperand2 operand : operands) {
	    result.addAll(operand.getAllValues());
	}
	return result;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((operands == null) ? 0 : operands.hashCode());
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
	if (!(obj instanceof OperandsBasedSet)) {
	    return false;
	}
	final OperandsBasedSet other = (OperandsBasedSet) obj;
	if (operands == null) {
	    if (other.operands != null) {
		return false;
	    }
	} else if (!operands.equals(other.operands)) {
	    return false;
	}
	return true;
    }

    @Override
    public boolean ignore() {
	// TODO Auto-generated method stub
	return false;
    }
}