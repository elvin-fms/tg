package ua.com.fielden.platform.test;

import java.net.URL;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.cfg.Configuration;
import org.hibernate.type.YesNoType;

import ua.com.fielden.platform.dao.MappingExtractor;
import ua.com.fielden.platform.dao.MappingsGenerator;
import ua.com.fielden.platform.domain.PlatformDomainTypes;
import ua.com.fielden.platform.entity.AbstractEntity;
import ua.com.fielden.platform.entity.factory.EntityFactory;
import ua.com.fielden.platform.entity.meta.DomainMetaPropertyConfig;
import ua.com.fielden.platform.entity.validation.DomainValidationConfig;
import ua.com.fielden.platform.equery.Rdbms;
import ua.com.fielden.platform.ioc.ApplicationInjectorFactory;
import ua.com.fielden.platform.ioc.HibernateUserTypesModule;
import ua.com.fielden.platform.migration.LegacyConnectionModule;
import ua.com.fielden.platform.persistence.HibernateUtil;
import ua.com.fielden.platform.persistence.ProxyInterceptor;
import ua.com.fielden.platform.persistence.composite.EntityWithDynamicCompositeKey;
import ua.com.fielden.platform.persistence.types.DateTimeType;
import ua.com.fielden.platform.persistence.types.EntityWithExTaxAndTaxMoney;
import ua.com.fielden.platform.persistence.types.EntityWithMoney;
import ua.com.fielden.platform.persistence.types.EntityWithSimpleMoney;
import ua.com.fielden.platform.persistence.types.EntityWithSimpleTaxMoney;
import ua.com.fielden.platform.persistence.types.EntityWithTaxMoney;
import ua.com.fielden.platform.sample.domain.TgTimesheet;
import ua.com.fielden.platform.sample.domain.TgVehicleMake;
import ua.com.fielden.platform.sample.domain.TgVehicleModel;
import ua.com.fielden.platform.test.domain.entities.Advice;
import ua.com.fielden.platform.test.domain.entities.AdvicePosition;
import ua.com.fielden.platform.test.entities.ComplexKeyEntity;
import ua.com.fielden.platform.test.entities.CompositeEntity;
import ua.com.fielden.platform.test.entities.CompositeEntityKey;
import ua.com.fielden.platform.test.entities.meta.AdviceDispatchedToWorkshopMetaDefiner;
import ua.com.fielden.platform.test.entities.meta.AdviceRoadMetaDefiner;
import ua.com.fielden.platform.test.entities.validators.AdviceCarrierValidator;
import ua.com.fielden.platform.test.entities.validators.AdvicePositionRotableValidator;
import ua.com.fielden.platform.test.entities.validators.AdviceRoadValidator;
import ua.com.fielden.platform.test.ioc.DaoTestHibernateModule;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provider;


/**
 * Provides platform specific implementation of {@link IDbDrivenTestCaseConfiguration}, which is mainly related to the use of {@link DaoTestHibernateModule}.
 *
 * @author TG Team
 *
 * @deprecated Use {@link PlatformDomainDrivenTestCaseConfiguration} instead.
 *
 */
public class PlatformDbDrivenTestCaseConfiguration implements IDbDrivenTestCaseConfiguration {
    protected final EntityFactory entityFactory;
    protected final Injector injector;
    protected final HibernateUtil hibernateUtil;

    private final DaoTestHibernateModule hibernateModule;

    public static final List<Class<? extends AbstractEntity>> testDomain = new ArrayList<Class<? extends AbstractEntity>>();

    public static final Map<Class, Class> hibTypeDefaults = new HashMap<Class, Class>();

    static {
	Rdbms.rdbms = Rdbms.H2;
	hibTypeDefaults.put(boolean.class, YesNoType.class);
	hibTypeDefaults.put(Boolean.class, YesNoType.class);
	hibTypeDefaults.put(Date.class, DateTimeType.class);
	testDomain.addAll(PlatformDomainTypes.types);
	testDomain.add(CompositeEntity.class);
	testDomain.add(CompositeEntityKey.class);
	testDomain.add(ComplexKeyEntity.class);
	testDomain.add(EntityWithMoney.class);
	testDomain.add(EntityWithTaxMoney.class);
	testDomain.add(EntityWithExTaxAndTaxMoney.class);
	testDomain.add(EntityWithSimpleTaxMoney.class);
	testDomain.add(EntityWithSimpleMoney.class);
	testDomain.add(EntityWithDynamicCompositeKey.class);
	testDomain.add(TgTimesheet.class);
	testDomain.add(TgVehicleMake.class);
	testDomain.add(TgVehicleModel.class);
    }


    /**
     * Required for dynamic instantiation by {@link DbDrivenTestCase}
     */
    public PlatformDbDrivenTestCaseConfiguration() {
	// instantiate all the factories and Hibernate utility
	final ProxyInterceptor interceptor = new ProxyInterceptor();
	try {
	    final MappingsGenerator mappingsGenerator = new MappingsGenerator(hibTypeDefaults, Guice.createInjector(new HibernateUserTypesModule()));
	    final Configuration cfg = new Configuration();
	    cfg.addXML(mappingsGenerator.generateMappings(testDomain));

	    hibernateUtil = new HibernateUtil(interceptor, cfg.configure(new URL("file:src/test/resources/hibernate4test.cfg.xml")));
	    hibernateModule = new DaoTestHibernateModule(hibernateUtil.getSessionFactory(), new MappingExtractor(hibernateUtil.getConfiguration()), mappingsGenerator);
	    injector = new ApplicationInjectorFactory().add(hibernateModule).add(new LegacyConnectionModule(new Provider() {
		@Override
		public Object get() {
		    try {
			final String connectionUrl = "jdbc:h2:mem:db;DB_CLOSE_DELAY=-1";
			Class.forName("org.h2.Driver");
			return DriverManager.getConnection(connectionUrl, "sa", "");
		    } catch (final Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		    }
		}
	    })).getInjector();
	    entityFactory = injector.getInstance(EntityFactory.class);
	    interceptor.setFactory(entityFactory);

	    // bind domain specific validation classes
	    hibernateModule.getDomainValidationConfig().setValidator(Advice.class, "road", new AdviceRoadValidator()).setValidator(Advice.class, "carrier", injector.getInstance(AdviceCarrierValidator.class)).setValidator(AdvicePosition.class, "rotable", new AdvicePositionRotableValidator());
	    // bind domain specific meta property configuration classes
	    hibernateModule.getDomainMetaPropertyConfig().setDefiner(Advice.class, "dispatchedToWorkshop", new AdviceDispatchedToWorkshopMetaDefiner()).setDefiner(Advice.class, "road", new AdviceRoadMetaDefiner());
	} catch (final Exception e) {
	    e.printStackTrace();
	    throw new RuntimeException(e);
	}
    }

    @Override
    public EntityFactory getEntityFactory() {
	return entityFactory;
    }

    @Override
    public HibernateUtil getHibernateUtil() {
	return hibernateUtil;
    }

    @Override
    public Injector getInjector() {
	return injector;
    }

    @Override
    public DomainMetaPropertyConfig getDomainMetaPropertyConfig() {
	return hibernateModule.getDomainMetaPropertyConfig();
    }

    @Override
    public DomainValidationConfig getDomainValidationConfig() {
	return hibernateModule.getDomainValidationConfig();
    }

    @Override
    public List<String> getDdl() {
	return null;
    }

}
