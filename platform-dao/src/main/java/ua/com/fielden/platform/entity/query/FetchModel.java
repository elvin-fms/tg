package ua.com.fielden.platform.entity.query;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import ua.com.fielden.platform.dao.DomainMetadataAnalyser;
import ua.com.fielden.platform.dao.PropertyMetadata;
import ua.com.fielden.platform.dao.PropertyMetadata.PropertyCategory;
import ua.com.fielden.platform.entity.AbstractEntity;
import ua.com.fielden.platform.entity.query.fluent.fetch;
import ua.com.fielden.platform.entity.query.fluent.fetch.FetchCategory;
import ua.com.fielden.platform.utils.EntityUtils;

public class FetchModel<T extends AbstractEntity<?>> {
    private final fetch<T> originalFetch;
    private final Map<String, fetch<? extends AbstractEntity<?>>> entityProps = new HashMap<String, fetch<? extends AbstractEntity<?>>>();
    private final Set<String> primProps = new HashSet<String>();
    private DomainMetadataAnalyser domainMetadataAnalyser;

    public FetchModel(final fetch<T> originalFetch, final DomainMetadataAnalyser domainMetadataAnalyser) {
	this.originalFetch = originalFetch;
	this.domainMetadataAnalyser = domainMetadataAnalyser;

	if (!EntityAggregates.class.equals(getEntityType())) {
	    switch (originalFetch.getFetchCategory()) {
	    case ALL:
		includeAllFirstLevelProps();
		break;
	    case MINIMAL:
		includeAllFirstLevelPrimPropsAndKey();
		break;
	    case KEY_AND_DESC:
		includeKeyAndDescOnly();
		break;
	    case NONE:
		if (EntityUtils.isPersistedEntityType(getEntityType())) {
		    includeIdAndVersionOnly();
		}
		break;
	    default:
		throw new IllegalStateException("Unknown fetch category [" + originalFetch.getFetchCategory() + "]");
	    }

	    for (final String propName : originalFetch.getExcludedProps()) {
		without(propName);
	    }
	}

	for (final String propName : originalFetch.getIncudedProps()) {
	    with(propName, false);
	}

	for (final Entry<String, fetch<? extends AbstractEntity<?>>> entry : originalFetch.getIncludedPropsWithModels().entrySet()) {
	    with(entry.getKey(), entry.getValue());
	}
    }

    private void includeAllCompositeKeyMembers() {
	for (final PropertyMetadata ppi : domainMetadataAnalyser.getPropertyMetadatasForEntity(getEntityType())) {
	    if (ppi.isEntityMemberOfCompositeKey()) {
		with(ppi.getName(), false);
	    } else if (ppi.isPrimitiveMemberOfCompositeKey()) {
		with(ppi.getName(), true);
	    }
	}
    }

    private void includeAllFirstLevelPrimPropsAndKey() {
	for (final PropertyMetadata ppi : domainMetadataAnalyser.getPropertyMetadatasForEntity(getEntityType())) {
	    if (!ppi.isCalculated()) {
		with(ppi.getName(), !(ppi.getType().equals(PropertyCategory.ENTITY_MEMBER_OF_COMPOSITE_KEY) || ppi.getType().equals(PropertyCategory.ENTITY_KEY)));
	    }
	}
    }

    private void includeKeyAndDescOnly() {
	if (EntityUtils.isPersistedEntityType(getEntityType())) {
	    includeIdAndVersionOnly();
	}

	with(AbstractEntity.KEY, true);

	if (EntityUtils.hasDescProperty(getEntityType())) {
	    with(AbstractEntity.DESC, true);
	}
    }

    private void includeAllFirstLevelProps() {
	for (final PropertyMetadata ppi : domainMetadataAnalyser.getPropertyMetadatasForEntity(getEntityType())) {
	    if (ppi.isUnionEntity()) {
		with(ppi.getName(), new fetch(ppi.getJavaType(), FetchCategory.ALL));
	    } else if (!ppi.isCalculated() && !ppi.isCollection()) {
		with(ppi.getName(), false);
	    }
	}
    }

    private void includeIdAndVersionOnly() {
	with(AbstractEntity.ID, true);
	with(AbstractEntity.VERSION, true);
    }

    public boolean containsProp(final String propName) {
	return primProps.contains(propName) || entityProps.containsKey(propName);
    }

    private PropertyMetadata getPropMetadata(final String propName) {
	final PropertyMetadata ppi = domainMetadataAnalyser.getPropPersistenceInfoExplicitly(getEntityType(), propName);
	if (ppi != null) {
	    if (ppi.getJavaType() != null) {
		return ppi;
	    } else {
		throw new IllegalStateException("Couldn't determine type of property " + propName + " of entity type " + getEntityType());
	    }
	} else {
	    throw new IllegalArgumentException("Trying fetch entity of type [" + getEntityType() + "] with non-existing property [" + propName + "]");
	}
    }

    private void without(final String propName) {
	final Class propType = getPropMetadata(propName).getJavaType();

	if (AbstractEntity.class.isAssignableFrom(propType)) {
	    final Object removalResult = entityProps.remove(propName);
	    if (removalResult == null) {
		throw new IllegalStateException("Couldn't find property [" + propName + "] to be excluded from fetched entity properties of entity type " + getEntityType());
	    }
	} else {
	    final boolean removalResult = primProps.remove(propName);
	    if (!removalResult) {
		throw new IllegalStateException("Couldn't find property [" + propName + "] to be excluded from fetched primitive properties of entity type " + getEntityType());
	    }
	}
    }

    private void with(final String propName, final boolean skipEntities) {
	if (EntityAggregates.class.equals(getEntityType())) {
	    primProps.add(propName);
	} else {
	    final PropertyMetadata ppi = getPropMetadata(propName);
	    final Class propType = ppi.getJavaType();

	    if (propName.equals("key") && ppi.isVirtual()) {
		includeAllCompositeKeyMembers();
	    } else {
		if (AbstractEntity.class.isAssignableFrom(propType)/* && !ppi.isId()*/) {
		    if (!skipEntities) {
			addEntityPropsModel(propName, new fetch(propType, FetchCategory.MINIMAL));
		    }
		} else {
		    final String singleSubpropertyOfCompositeUserTypeProperty = ppi.getSinglePropertyOfCompositeUserType();
		    if (singleSubpropertyOfCompositeUserTypeProperty != null) {
			primProps.add(propName + "." + singleSubpropertyOfCompositeUserTypeProperty);
		    }
		    primProps.add(propName);
		}
	    }

	}
    }

    private void addEntityPropsModel(final String propName, final fetch<?> model) {
	    final fetch<?> existingFetch = entityProps.get(propName);
	    entityProps.put(propName, existingFetch != null ? existingFetch.unionWith(model) : model);

    }

    private void with(final String propName, final fetch<? extends AbstractEntity<?>> fetchModel) {
	if (getEntityType() != EntityAggregates.class) {
	    final Class propType = getPropMetadata(propName).getJavaType();

	    if (propType != fetchModel.getEntityType()) {
		throw new IllegalArgumentException("Mismatch between actual type [" + propType + "] of property [" + propName + "] in entity type [" + getEntityType()
			+ "] and its fetch model type [" + fetchModel.getEntityType() + "]!");
	    }
	}

	if (AbstractEntity.class.isAssignableFrom(fetchModel.getEntityType())) {
	    addEntityPropsModel(propName, fetchModel);
	} else {
	    throw new IllegalArgumentException(propName + " has fetch model for type " + fetchModel.getEntityType().getName() + ". Fetch model with entity type is required.");
	}
    }

    public Map<String, fetch<? extends AbstractEntity<?>>> getFetchModels() {
	return entityProps;
    }

    public Class<T> getEntityType() {
	return originalFetch.getEntityType();
    }
}