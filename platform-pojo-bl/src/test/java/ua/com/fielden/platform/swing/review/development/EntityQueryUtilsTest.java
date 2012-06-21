package ua.com.fielden.platform.swing.review.development;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import ua.com.fielden.platform.domaintree.ICalculatedProperty;
import ua.com.fielden.platform.domaintree.ICalculatedProperty.CalculatedPropertyAttribute;
import ua.com.fielden.platform.domaintree.IDomainTreeEnhancer;
import ua.com.fielden.platform.domaintree.centre.ICentreDomainTreeManager.ICentreDomainTreeManagerAndEnhancer;
import ua.com.fielden.platform.domaintree.centre.IOrderingRepresentation.Ordering;
import ua.com.fielden.platform.domaintree.centre.impl.CentreDomainTreeManagerAndEnhancer;
import ua.com.fielden.platform.domaintree.testing.ClassProviderForTestingPurposes;
import ua.com.fielden.platform.domaintree.testing.MasterEntity;
import ua.com.fielden.platform.domaintree.testing.TgKryoForDomainTreesTestingPurposes;
import ua.com.fielden.platform.entity.AbstractEntity;
import ua.com.fielden.platform.entity.factory.EntityFactory;
import ua.com.fielden.platform.ioc.ApplicationInjectorFactory;
import ua.com.fielden.platform.serialisation.api.ISerialiser;
import ua.com.fielden.platform.test.CommonTestEntityModuleWithPropertyFactory;
import ua.com.fielden.platform.test.EntityModuleWithPropertyFactory;
import ua.com.fielden.platform.utils.Pair;

import com.google.inject.Injector;

@SuppressWarnings("unchecked")
public class EntityQueryUtilsTest {

    @SuppressWarnings("serial")
    private final static ICentreDomainTreeManagerAndEnhancer cdtme = new CentreDomainTreeManagerAndEnhancer(createSerialiser(createFactory()),  new HashSet<Class<?>>() {{ add(MasterEntity.class); }});

    private static EntityFactory createFactory() {
	final EntityModuleWithPropertyFactory module = new CommonTestEntityModuleWithPropertyFactory();
	final Injector injector = new ApplicationInjectorFactory().add(module).getInjector();
	return injector.getInstance(EntityFactory.class);
    }

    private static ISerialiser createSerialiser(final EntityFactory factory) {
	return new TgKryoForDomainTreesTestingPurposes(factory, new ClassProviderForTestingPurposes());
    }

    private static final Class<? extends AbstractEntity<?>> masterKlass;
    private static final ICalculatedProperty firstCalc, secondCalc, thirdCalc;


    static {
	final IDomainTreeEnhancer dte = cdtme.getEnhancer();
 	dte.addCalculatedProperty(MasterEntity.class, "", "3 + integerProp", "firstCalc", "firstCalc", CalculatedPropertyAttribute.NO_ATTR, "integerProp");
 	dte.addCalculatedProperty(MasterEntity.class, "", "SUM(integerProp)", "sumInt", "Int Summary", CalculatedPropertyAttribute.NO_ATTR, "integerProp");
 	dte.addCalculatedProperty(MasterEntity.class, "", "AVG(integerProp)", "avgInt", "Int Average", CalculatedPropertyAttribute.NO_ATTR, "integerProp");
 	dte.addCalculatedProperty(MasterEntity.class, "entityProp.mutablyCheckedProp", "3 + integerProp", "secondCalc", "secondCalc", CalculatedPropertyAttribute.NO_ATTR, "integerProp");
 	dte.addCalculatedProperty(MasterEntity.class, "entityProp.mutablyCheckedProp", "SUM(integerProp)", "mutIntSum", "Integer another summary", CalculatedPropertyAttribute.NO_ATTR, "integerProp");
 	dte.addCalculatedProperty(MasterEntity.class, "entityProp.entityProp.simpleEntityProp", "3 + integerProp", "thirdCalc", "thirdCalc", CalculatedPropertyAttribute.NO_ATTR, "integerProp");
 	dte.addCalculatedProperty(MasterEntity.class, "entityProp.entityProp.simpleEntityProp", "SUM(integerProp)", "propIntSum", "Property int summary", CalculatedPropertyAttribute.NO_ATTR, "integerProp");
 	dte.addCalculatedProperty(MasterEntity.class, "entityProp.entityProp.simpleEntityProp", "AVG(integerProp)", "propIntAvg", "Property Int average", CalculatedPropertyAttribute.NO_ATTR, "integerProp");
 	dte.addCalculatedProperty(MasterEntity.class, "entityProp.entityProp.simpleEntityProp", "MIN(integerProp)", "propIntMin", "Property Int minimum", CalculatedPropertyAttribute.NO_ATTR, "integerProp");
 	dte.apply();

 	masterKlass = (Class<? extends AbstractEntity<?>>) dte.getManagedType(MasterEntity.class);

 	firstCalc = dte.getCalculatedProperty(MasterEntity.class, "firstCalc");
 	secondCalc = dte.getCalculatedProperty(MasterEntity.class, "entityProp.mutablyCheckedProp.secondCalc");
 	thirdCalc = dte.getCalculatedProperty(MasterEntity.class, "entityProp.entityProp.simpleEntityProp.thirdCalc");

 	cdtme.getSecondTick().check(MasterEntity.class, "", true);
 	cdtme.getSecondTick().check(MasterEntity.class, "stringProp", true);
 	cdtme.getSecondTick().check(MasterEntity.class, "firstCalc", true);
 	cdtme.getSecondTick().check(MasterEntity.class, "sumInt", true);
 	cdtme.getSecondTick().check(MasterEntity.class, "avgInt", true);
 	cdtme.getSecondTick().check(MasterEntity.class, "entityProp.mutablyCheckedProp", true);
 	cdtme.getSecondTick().check(MasterEntity.class, "entityProp.mutablyCheckedProp.integerProp", true);
 	cdtme.getSecondTick().check(MasterEntity.class, "entityProp.mutablyCheckedProp.secondCalc", true);
 	cdtme.getSecondTick().check(MasterEntity.class, "mutIntSum", true);
 	cdtme.getSecondTick().check(MasterEntity.class, "entityProp.entityProp.simpleEntityProp", true);
 	cdtme.getSecondTick().check(MasterEntity.class, "entityProp.entityProp.simpleEntityProp.integerProp", true);
 	cdtme.getSecondTick().check(MasterEntity.class, "entityProp.entityProp.simpleEntityProp.thirdCalc", true);
 	cdtme.getSecondTick().check(MasterEntity.class, "propIntSum", true);
 	cdtme.getSecondTick().check(MasterEntity.class, "propIntAvg", true);
 	cdtme.getSecondTick().check(MasterEntity.class, "propIntMin", true);

 	cdtme.getSecondTick().toggleOrdering(MasterEntity.class, "");
 	cdtme.getSecondTick().toggleOrdering(MasterEntity.class, "stringProp");
 	cdtme.getSecondTick().toggleOrdering(MasterEntity.class, "stringProp");
 	cdtme.getSecondTick().toggleOrdering(MasterEntity.class, "firstCalc");
 	cdtme.getSecondTick().toggleOrdering(MasterEntity.class, "entityProp.mutablyCheckedProp");
 	cdtme.getSecondTick().toggleOrdering(MasterEntity.class, "entityProp.mutablyCheckedProp.integerProp");
 	cdtme.getSecondTick().toggleOrdering(MasterEntity.class, "entityProp.mutablyCheckedProp.integerProp");
 	cdtme.getSecondTick().toggleOrdering(MasterEntity.class, "entityProp.mutablyCheckedProp.secondCalc");
 	cdtme.getSecondTick().toggleOrdering(MasterEntity.class, "entityProp.mutablyCheckedProp.secondCalc");
 	cdtme.getSecondTick().toggleOrdering(MasterEntity.class, "entityProp.entityProp.simpleEntityProp");
 	cdtme.getSecondTick().toggleOrdering(MasterEntity.class, "entityProp.entityProp.simpleEntityProp");
 	cdtme.getSecondTick().toggleOrdering(MasterEntity.class, "entityProp.entityProp.simpleEntityProp.integerProp");
 	cdtme.getSecondTick().toggleOrdering(MasterEntity.class, "entityProp.entityProp.simpleEntityProp.thirdCalc");
    }

    @Test
    public void test_that_getOrderingList_works(){
	final List<Pair<Object, Ordering>> expectedList = new ArrayList<Pair<Object,Ordering>>();
	expectedList.add(new Pair<Object, Ordering>("", Ordering.DESCENDING));
	expectedList.add(new Pair<Object, Ordering>("stringProp", Ordering.DESCENDING));
	expectedList.add(new Pair<Object, Ordering>(firstCalc.getExpressionModel(), Ordering.ASCENDING));
	expectedList.add(new Pair<Object, Ordering>("entityProp.mutablyCheckedProp", Ordering.ASCENDING));
	expectedList.add(new Pair<Object, Ordering>("entityProp.mutablyCheckedProp.integerProp", Ordering.DESCENDING));
	expectedList.add(new Pair<Object, Ordering>(secondCalc.getExpressionModel(), Ordering.DESCENDING));
	expectedList.add(new Pair<Object, Ordering>("entityProp.entityProp.simpleEntityProp", Ordering.DESCENDING));
	expectedList.add(new Pair<Object, Ordering>("entityProp.entityProp.simpleEntityProp.integerProp", Ordering.ASCENDING));
	expectedList.add(new Pair<Object, Ordering>(thirdCalc.getExpressionModel(), Ordering.ASCENDING));

	assertEquals("The getOrderingList() method works incorrect", expectedList, EntityQueryCriteriaUtils.getOrderingList(MasterEntity.class, cdtme.getSecondTick(), cdtme.getEnhancer()));
    }

    @Test
    public void test_that_separateFetchFromTotalsProperties_works(){
	final Set<String> expectedFetchProperties = new HashSet<String>();
	expectedFetchProperties.add("");
	expectedFetchProperties.add("stringProp");
	expectedFetchProperties.add("firstCalc");
	expectedFetchProperties.add("entityProp.mutablyCheckedProp");
	expectedFetchProperties.add("entityProp.mutablyCheckedProp.integerProp");
	expectedFetchProperties.add("entityProp.mutablyCheckedProp.secondCalc");
	expectedFetchProperties.add("entityProp.entityProp.simpleEntityProp");
	expectedFetchProperties.add("entityProp.entityProp.simpleEntityProp.integerProp");
	expectedFetchProperties.add("entityProp.entityProp.simpleEntityProp.thirdCalc");
	final Set<String> expectedTotalProperties = new HashSet<String>();
	expectedTotalProperties.add("mutIntSum");
	expectedTotalProperties.add("propIntSum");
	expectedTotalProperties.add("propIntAvg");
	expectedTotalProperties.add("propIntMin");

	assertEquals("Separate properties doesn't work correctly", new Pair<Set<String>, Set<String>>(expectedFetchProperties, expectedTotalProperties), EntityQueryCriteriaUtils.separateFetchAndTotalProperties(MasterEntity.class, cdtme.getSecondTick(), cdtme.getEnhancer()));
    }

}
