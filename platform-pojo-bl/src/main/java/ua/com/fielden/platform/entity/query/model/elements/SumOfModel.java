package ua.com.fielden.platform.entity.query.model.elements;

public class SumOfModel extends SingleOperandFunctionModel {

    public SumOfModel(final ISingleOperand operand) {
	super(operand);
    }

    @Override
    public String sql() {
	return "SUM(" + getOperand().sql() + ")";
    }
}