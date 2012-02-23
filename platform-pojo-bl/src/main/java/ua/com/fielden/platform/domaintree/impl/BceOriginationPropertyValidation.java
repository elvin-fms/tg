package ua.com.fielden.platform.domaintree.impl;

import java.lang.annotation.Annotation;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import ua.com.fielden.platform.domaintree.IDomainTreeEnhancer.CalcPropertyKeyWarning;
import ua.com.fielden.platform.domaintree.IDomainTreeEnhancer.IncorrectCalcPropertyKeyException;
import ua.com.fielden.platform.entity.meta.MetaProperty;
import ua.com.fielden.platform.entity.validation.IBeforeChangeEventHandler;
import ua.com.fielden.platform.error.Result;

/**
 * {@link CalculatedProperty} validation for its expression in a provided context.
 *
 * @author TG Team
 *
 */
public class BceOriginationPropertyValidation implements IBeforeChangeEventHandler<String> {
    @Override
    public Result handle(final MetaProperty property, final String newOriginationProperty, final String oldValue, final Set<Annotation> mutatorAnnotations) {
	final CalculatedProperty cp = (CalculatedProperty) property.getEntity();
	// check if the "originationProperty" is correct in context of "contextType":
	if (property.isRequired() && StringUtils.isEmpty(newOriginationProperty)) {
	    return new IncorrectCalcPropertyKeyException("The origination property cannot be empty for Aggregated Expressions. It is required to ");
	}
	try {
	    DomainTreeEnhancer.validatePath(cp.contextType(), newOriginationProperty, "The origination property [" + newOriginationProperty + "] does not exist in type [" + cp.contextType() + "].");
	} catch (final IncorrectCalcPropertyKeyException e) {
	    return e;
	}

	if (!cp.getContextualExpression().contains(newOriginationProperty)) {
	    return new CalcPropertyKeyWarning("The origination property does not take a part in the expression. Is that correct?");
	}
	return Result.successful(newOriginationProperty);
    }

}