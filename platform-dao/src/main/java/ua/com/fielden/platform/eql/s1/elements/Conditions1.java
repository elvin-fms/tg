package ua.com.fielden.platform.eql.s1.elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ua.com.fielden.platform.entity.query.fluent.LogicalOperator;
import ua.com.fielden.platform.eql.meta.TransformatorToS2;
import ua.com.fielden.platform.eql.s2.elements.Conditions2;
import ua.com.fielden.platform.eql.s2.elements.ICondition2;

public class Conditions1 extends AbstractCondition1<Conditions2> {
    private final boolean negated;
    private final ICondition1<? extends ICondition2> firstCondition;
    private final List<CompoundCondition1> otherConditions = new ArrayList<>();

    //private final List<List<ICondition1<? extends ICondition2>>> allConditions = new ArrayList<>();

    public Conditions1(final boolean negated, final ICondition1<? extends ICondition2> firstCondition, final List<CompoundCondition1> otherConditions) {
        //this.allConditions.addAll(formConditionIntoLogicalGroups(firstCondition, otherConditions));
        this.firstCondition = firstCondition;
        this.otherConditions.addAll(otherConditions);
        this.negated = negated;
    }

    public Conditions1(final boolean negated, final ICondition1<? extends ICondition2> firstCondition) {
        this(negated, firstCondition, Collections.<CompoundCondition1> emptyList());
    }

    public Conditions1() {
        negated = false;
        firstCondition = null;
    }

    public boolean isEmpty() {
        return firstCondition == null;
    }

    private List<List<ICondition1<? extends ICondition2>>> formConditionIntoLogicalGroups() {
        final List<List<ICondition1<? extends ICondition2>>> result = new ArrayList<>();
        List<ICondition1<? extends ICondition2>> currGroup = new ArrayList<ICondition1<? extends ICondition2>>();

        if (firstCondition != null) {
            currGroup.add(firstCondition);
        }

        for (final CompoundCondition1 compoundCondition : otherConditions) {
            if (compoundCondition.getLogicalOperator() == LogicalOperator.AND) {
                currGroup.add(compoundCondition.getCondition());
            } else {
                if (currGroup.size() > 0) {
                    result.add(currGroup);
                }

                currGroup = new ArrayList<ICondition1<? extends ICondition2>>();
                currGroup.add(compoundCondition.getCondition());
            }
        }

        if (currGroup.size() > 0) {
            result.add(currGroup);
        }

        return result;
    }

    @Override
    public Conditions2 transform(final TransformatorToS2 resolver) {
        final List<List<ICondition2>> transformed = new ArrayList<>();
        for (final List<ICondition1<? extends ICondition2>> conditionGroup : formConditionIntoLogicalGroups()) {
            final List<ICondition2> transformedGroup = new ArrayList<>();
            for (final ICondition1<? extends ICondition2> condition : conditionGroup) {
                final ICondition2 transformedCondition = condition.transform(resolver);
                if (!transformedCondition.ignore()) {
                    transformedGroup.add(transformedCondition);
                }
            }
            if (transformedGroup.size() > 0) {
                transformed.add(transformedGroup);
            }
        }
        return new Conditions2(negated, transformed);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((firstCondition == null) ? 0 : firstCondition.hashCode());
        result = prime * result + (negated ? 1231 : 1237);
        result = prime * result + ((otherConditions == null) ? 0 : otherConditions.hashCode());
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
        if (!(obj instanceof Conditions1)) {
            return false;
        }
        final Conditions1 other = (Conditions1) obj;
        if (firstCondition == null) {
            if (other.firstCondition != null) {
                return false;
            }
        } else if (!firstCondition.equals(other.firstCondition)) {
            return false;
        }
        if (negated != other.negated) {
            return false;
        }
        if (otherConditions == null) {
            if (other.otherConditions != null) {
                return false;
            }
        } else if (!otherConditions.equals(other.otherConditions)) {
            return false;
        }
        return true;
    }

    //    @Override
    //    public String toString() {
    //	final StringBuffer sb = new StringBuffer();
    //
    //	for (final Iterator<List<ICondition1<? extends ICondition2>>> iterator = allConditions.iterator(); iterator.hasNext();) {
    //	    final List<ICondition1<? extends ICondition2>> list = iterator.next();
    //
    //	    for (final Iterator<ICondition1<? extends ICondition2>> iterator2 = list.iterator(); iterator2.hasNext();) {
    //		final ICondition1<? extends ICondition2> cond = iterator2.next();
    //		sb.append(cond);
    //		sb.append(iterator2.hasNext() ? " AND " : "");
    //	    }
    //	    sb.append(iterator.hasNext() ? " OR " : "");
    //
    //	}
    //	return (negated ? "NOT (" : "(") + sb.toString() + ")";
    //    }

    //    @Override
    //    public int hashCode() {
    //	final int prime = 31;
    //	int result = 1;
    //	result = prime * result + ((allConditions == null) ? 0 : allConditions.hashCode());
    //	result = prime * result + (negated ? 1231 : 1237);
    //	return result;
    //    }
    //
    //    @Override
    //    public boolean equals(final Object obj) {
    //	if (this == obj) {
    //	    return true;
    //	}
    //	if (obj == null) {
    //	    return false;
    //	}
    //	if (!(obj instanceof Conditions1)) {
    //	    return false;
    //	}
    //	final Conditions1 other = (Conditions1) obj;
    //	if (allConditions == null) {
    //	    if (other.allConditions != null) {
    //		return false;
    //	    }
    //	} else if (!allConditions.equals(other.allConditions)) {
    //	    return false;
    //	}
    //	if (negated != other.negated) {
    //	    return false;
    //	}
    //	return true;
    //    }
}