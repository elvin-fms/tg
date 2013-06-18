package ua.com.fielden.platform.eql.s2.elements;

import java.util.Collections;
import java.util.List;

import ua.com.fielden.platform.eql.meta.AbstractPropInfo;

public class EntProp2 implements ISingleOperand2 {
    private final String name;
    private final boolean aliased;
    private final ISource2 source;
    private final AbstractPropInfo resolution;
    private final Expression2 expression;


    public EntProp2(final String name, final ISource2 source, final boolean aliased, final AbstractPropInfo resolution, final Expression2 expression) {
        this.name = name;
        this.source = source;
        this.aliased = aliased;
        this.resolution = resolution;
        this.expression = expression;
        source.addProp(this);
        System.out.println(toString());
    }

    @Override
    public String toString() {
        return " name = " + name + "; source = " + source + "; aliased = " + aliased + "; resolution = " + resolution;
    }

    @Override
    public List<EntValue2> getAllValues() {
        //return isExpression() ? expression.getAllValues() : Collections.<EntValue> emptyList();
        return Collections.<EntValue2> emptyList();
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        if (!(obj instanceof EntProp2)) {
            return false;
        }
        final EntProp2 other = (EntProp2) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public Class type() {
	return resolution.javaType();
    }
}