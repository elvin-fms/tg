package ua.com.fielden.platform.entity.query.generation.elements;


public class Yield {
    private final ISingleOperand operand;
    private final String alias;
    private ResultQueryYieldDetails info;

    public Yield(final ISingleOperand operand, final String alias) {
	this.operand = operand;
	this.alias = alias;
    }

    @Override
    public String toString() {
	return alias;//sql();
    }

    public String sql() {
	return operand.sql() + " AS " + info.getColumn() + "/*" + alias + "*/";
    }

    public ISingleOperand getOperand() {
	return operand;
    }

    public String getAlias() {
	return alias;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((alias == null) ? 0 : alias.hashCode());
	result = prime * result + ((operand == null) ? 0 : operand.hashCode());
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
	if (!(obj instanceof Yield)) {
	    return false;
	}
	final Yield other = (Yield) obj;
	if (alias == null) {
	    if (other.alias != null) {
		return false;
	    }
	} else if (!alias.equals(other.alias)) {
	    return false;
	}
	if (operand == null) {
	    if (other.operand != null) {
		return false;
	    }
	} else if (!operand.equals(other.operand)) {
	    return false;
	}
	return true;
    }

    public ResultQueryYieldDetails getInfo() {
	return info;
    }

    public void setInfo(final ResultQueryYieldDetails info) {
	this.info = info;
    }
}