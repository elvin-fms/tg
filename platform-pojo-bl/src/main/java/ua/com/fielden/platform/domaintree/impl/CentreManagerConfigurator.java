package ua.com.fielden.platform.domaintree.impl;

import ua.com.fielden.platform.domaintree.ICalculatedProperty.CalculatedPropertyAttribute;
import ua.com.fielden.platform.domaintree.centre.ICentreDomainTreeManager.ICentreDomainTreeManagerAndEnhancer;
import ua.com.fielden.platform.domaintree.centre.ICentreDomainTreeRepresentation.IAddToResultTickRepresentation;
import ua.com.fielden.platform.entity.AbstractEntity;

/**
 *
 * @author TG Team
 *
 */
public class CentreManagerConfigurator {
    private final Class<?> root;

    public CentreManagerConfigurator(final Class<? extends AbstractEntity<?>> root) {
	this.root = root;
    }

    public ICentreDomainTreeManagerAndEnhancer configCentre(final ICentreDomainTreeManagerAndEnhancer cdtme) {
	return cdtme;
    }

    public CentreManagerConfigurator addCriteria(final ICentreDomainTreeManagerAndEnhancer cdtme, final String prop) {
	cdtme.getFirstTick().check(root, prop, true);
	return this;
    }

    public CentreManagerConfigurator addColumn(final ICentreDomainTreeManagerAndEnhancer cdtme, final String prop) {
	final IAddToResultTickRepresentation rtr = cdtme.getRepresentation().getSecondTick();
	cdtme.getSecondTick().check(root, prop, true);
	cdtme.getSecondTick().setWidth(root, prop, rtr.getWidthByDefault(root, prop));
	return this;
    }

    public CentreManagerConfigurator addColumn(final ICentreDomainTreeManagerAndEnhancer cdtme, final String prop, final int width) {
	cdtme.getSecondTick().check(root, prop, true);
	cdtme.getSecondTick().setWidth(root, prop, width);
	return this;
    }

    public CentreManagerConfigurator addTotal(final ICentreDomainTreeManagerAndEnhancer cdtme, final String title, final String description, final String formula, final String relatedProperty) {
	cdtme.getEnhancer().addCalculatedProperty(root, "", formula, title, description, CalculatedPropertyAttribute.NO_ATTR, relatedProperty);
	cdtme.getEnhancer().apply();
	cdtme.getSecondTick().check(root, CalculatedProperty.generateNameFrom(title), true);
	return this;
    }
}
