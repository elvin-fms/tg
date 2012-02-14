package ua.com.fielden.platform.domaintree.centre.impl;

import java.util.Set;

import org.junit.BeforeClass;

import ua.com.fielden.platform.domaintree.centre.ILocatorDomainTreeManager.ILocatorDomainTreeManagerAndEnhancer;
import ua.com.fielden.platform.domaintree.testing.MasterEntity;

/**
 * A test for entity locators tree representation.
 *
 * @author TG Team
 *
 */
public class LocatorDomainTreeRepresentationTest extends CentreDomainTreeRepresentationTest {
    @Override
    protected ILocatorDomainTreeManagerAndEnhancer dtm() {
	return (ILocatorDomainTreeManagerAndEnhancer) super.dtm();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////// Test initialisation ///////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Creates root types.
     *
     * @return
     */
    protected static Set<Class<?>> createRootTypes_for_LocatorDomainTreeRepresentationTest() {
	final Set<Class<?>> rootTypes = createRootTypes_for_CentreDomainTreeRepresentationTest();
	return rootTypes;
    }

    /**
     * Provides a testing configuration for the manager.
     *
     * @param dtm
     */
    protected static void manageTestingDTM_for_LocatorDomainTreeRepresentationTest(final ILocatorDomainTreeManagerAndEnhancer dtm) {
	manageTestingDTM_for_CentreDomainTreeRepresentationTest(dtm);

	dtm.getFirstTick().checkedProperties(MasterEntity.class);
	dtm.getSecondTick().checkedProperties(MasterEntity.class);
    }

    @BeforeClass
    public static void initDomainTreeTest() {
	final ILocatorDomainTreeManagerAndEnhancer dtm = new LocatorDomainTreeManagerAndEnhancer(serialiser(), createRootTypes_for_LocatorDomainTreeRepresentationTest());
	manageTestingDTM_for_LocatorDomainTreeRepresentationTest(dtm);
	setDtmArray(serialiser().serialise(dtm));
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////// End of Test initialisation ////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void test_that_serialisation_works() throws Exception {
        super.test_that_serialisation_works();
    }

    @Override
    public void test_that_equality_and_copying_works() {
	super.test_that_equality_and_copying_works();
    }
}
