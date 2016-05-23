package ua.com.fielden.platform.persistence.types;

import ua.com.fielden.platform.dao.EntityBasedOnAbstractPersistentEntity2Dao;
import ua.com.fielden.platform.entity.AbstractPersistentEntity;
import ua.com.fielden.platform.entity.annotation.CompanionObject;
import ua.com.fielden.platform.entity.annotation.KeyType;
import ua.com.fielden.platform.entity.annotation.MapEntityTo;
import ua.com.fielden.platform.entity.annotation.Observable;

/**
 * This is an entity for testing of {@link AbstractPersistentEntity} property related auto-assignments.
 *
 * @author TG Team
 *
 */
@KeyType(String.class)
@MapEntityTo
@CompanionObject(EntityBasedOnAbstractPersistentEntity2Dao.class)
public class EntityBasedOnAbstractPersistentEntity2 extends AbstractPersistentEntity<String> {
    private static final long serialVersionUID = 1L;
    
    @Override
    @Observable
    public EntityBasedOnAbstractPersistentEntity2 setKey(String key) {
        super.setKey(key);
        return this;
    }
    
}