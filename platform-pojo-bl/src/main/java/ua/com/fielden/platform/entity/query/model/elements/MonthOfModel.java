package ua.com.fielden.platform.entity.query.model.elements;


public class MonthOfModel extends SingleOperandFunctionModel {

    public MonthOfModel(final ISingleOperand operand) {
	super(operand);
    }

    @Override
    public String sql() {
	return "MONTH(" + getOperand().sql() + ")";
    }
}