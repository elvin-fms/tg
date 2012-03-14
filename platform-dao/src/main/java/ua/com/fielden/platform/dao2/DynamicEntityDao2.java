package ua.com.fielden.platform.dao2;

import ua.com.fielden.platform.entity.AbstractEntity;
import ua.com.fielden.platform.entity.query.IFilter;
import ua.com.fielden.platform.reflection.AnnotationReflector;
import ua.com.fielden.platform.swing.review.annotations.EntityType;

import com.google.inject.Inject;

/**
 * This DAO is applicable for instantiation of any entity class specified at runtime. However, it is not type safe -- there is no way at compile time to ensure correct values for
 * entity and key types.
 *
 * @author TG Team
 *
 */
@EntityType(AbstractEntity.class)
public class DynamicEntityDao2 extends CommonEntityDao2 {

    private Class<? extends AbstractEntity<?>> entityType;
    private Class<? extends Comparable> keyType;

    /**
     * Needed for reflective instantiation.
     */
    @Inject
    protected DynamicEntityDao2(final IFilter filter) {
	super(filter);
    }

    public void setEntityType(final Class<? extends AbstractEntity<?>> type) {
	this.entityType = type;
	this.keyType = AnnotationReflector.getKeyType(entityType);
    }

    @Override
    public Class<?> getEntityType() {
	return entityType;
    }

    @Override
    public Class<? extends Comparable> getKeyType() {
	return keyType;
    }
}