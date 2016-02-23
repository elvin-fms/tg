package ua.com.fielden.platform.serialisation.jackson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ua.com.fielden.platform.entity.AbstractEntity;

/**
 * Class representing references to instances already serialised or deserialised.
 *
 * It is used mainly as a cache to resolve circular references during serialisation and provide better performance during deserialisation.
 *
 * @author TG Team
 *
 */
public class References {
    private final IdentityHashMap<AbstractEntity<?>, String> entityToReference = new IdentityHashMap<>();
    private final Map<String, AbstractEntity<?>> referenceToEntity = new LinkedHashMap<>();
    private final Map<String, Long> typeToRefCount = new HashMap<>();

    public void reset() {
        entityToReference.clear();
        referenceToEntity.clear();
        typeToRefCount.clear();
    }

    /**
     * Generates and returns the next reference number for the entity of the specified <code>type</code>.
     *
     * @param type
     * @return
     */
    public Long addNewId(final Class<? extends AbstractEntity<?>> type) {
        final Long count = typeToRefCount.get(type.getName());
        if (count == null) {
            typeToRefCount.put(type.getName(), 1L); // starting from 1
        } else {
            typeToRefCount.put(type.getName(), count + 1); // takes the next integer
        }
        return typeToRefCount.get(type.getName());
    }

    public AbstractEntity<?> getEntity(final String reference) {
        final AbstractEntity<?> entity = referenceToEntity.get(reference);
        if (entity == null) {
            throw new IllegalStateException("EntityJsonDeserialiser has tried to get entity with serialisationId [" + reference + "] from the cache, but that entity does not exist.");
        }
        return entity;
    }

    public References putEntity(final String reference, final AbstractEntity<?> entity) {
        if (referenceToEntity.containsKey(reference)) {
            throw new IllegalStateException("EntityJsonDeserialiser has tried to put entity of type [" + entity.getType().getName() + "] with existing serialisationId [" + reference + "] to the cache.");
        }
        referenceToEntity.put(reference, entity);
        return this;
    }

    public String getReference(final AbstractEntity<?> entity) {
        return entityToReference.get(entity);
    }

    public String putReference(final AbstractEntity<?> entity, final String reference) {
        if (entityToReference.get(entity) != null) {
            throw new IllegalStateException("EntityJsonSerialiser has tried to put reference for existing entity [" + entity + "] of type [" + entity.getType().getName() + "] to the cache.");
        }
        return entityToReference.put(entity, reference);
    }
    
    public List<AbstractEntity<?>> getAllEntities() {
        final List<AbstractEntity<?>> result = new ArrayList<>();
        for (final AbstractEntity<?> entity : referenceToEntity.values()) {
            if (entity != null) {
                result.add(entity);
            }
        }
        return result;
    }
}
