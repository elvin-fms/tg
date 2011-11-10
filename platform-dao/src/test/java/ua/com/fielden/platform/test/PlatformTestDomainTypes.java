package ua.com.fielden.platform.test;

import java.util.ArrayList;
import java.util.List;

import ua.com.fielden.platform.domain.PlatformDomainTypes;
import ua.com.fielden.platform.entity.AbstractEntity;
import ua.com.fielden.platform.sample.domain.TgTimesheet;

/**
 * A class to enlist platform test domain entities. Should be replaced with runtime generation via reflection.
 *
 * @author TG Team
 *
 */
public class PlatformTestDomainTypes {
    public static final List<Class<? extends AbstractEntity>> entityTypes = new ArrayList<Class<? extends AbstractEntity>>();

    static void add(final Class<? extends AbstractEntity> domainType) {
	entityTypes.add(domainType);
    }

    static{
	entityTypes.addAll(PlatformDomainTypes.types);
	    add(TgTimesheet.class);
    }
}