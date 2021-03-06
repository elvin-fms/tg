package ua.com.fielden.platform.web.centre.api.impl;

import java.util.Optional;

import org.apache.commons.lang.StringUtils;

import ua.com.fielden.platform.entity.AbstractEntity;
import ua.com.fielden.platform.utils.EntityUtils;
import ua.com.fielden.platform.utils.Pair;
import ua.com.fielden.platform.web.centre.api.actions.EntityActionConfig;
import ua.com.fielden.platform.web.centre.api.crit.ISelectionCritKindSelector;
import ua.com.fielden.platform.web.centre.api.top_level_actions.IAlsoCentreTopLevelActions;
import ua.com.fielden.platform.web.centre.api.top_level_actions.ICentreTopLevelActions;
import ua.com.fielden.platform.web.centre.api.top_level_actions.ICentreTopLevelActionsInGroup;
import ua.com.fielden.platform.web.centre.api.top_level_actions.ICentreTopLevelActionsInGroup0;
import ua.com.fielden.platform.web.centre.api.top_level_actions.ICentreTopLevelActionsWithEnforcePostSaveRefreshConfig;
import ua.com.fielden.platform.web.centre.api.top_level_actions.ICentreTopLevelActionsWithRunConfig;
import ua.com.fielden.platform.web.centre.api.top_level_actions.ICentreTopLevelActionsWithSse;

/**
 * A package private helper class to decompose the task of implementing the Entity Centre DSL. It has direct access to protected fields in {@link EntityCentreBuilder}.
 *
 * @author TG Team
 *
 * @param <T>
 */
class TopLevelActionsBuilder<T extends AbstractEntity<?>> extends ResultSetBuilder<T> implements
        ICentreTopLevelActionsWithRunConfig<T>, ICentreTopLevelActions<T>, ICentreTopLevelActionsInGroup<T>, ICentreTopLevelActionsInGroup0<T>, IAlsoCentreTopLevelActions<T> {

    private final EntityCentreBuilder<T> builder;

    public TopLevelActionsBuilder(final EntityCentreBuilder<T> builder) {
        super(builder);
        this.builder = builder;
    }

    @Override
    public IAlsoCentreTopLevelActions<T> addTopAction(final EntityActionConfig actionConfig) {
        builder.topLevelActions.add(new Pair<>(actionConfig, builder.currGroup));
        return this;
    }

    @Override
    public ICentreTopLevelActionsInGroup<T> beginTopActionsGroup(final String desc) {
        builder.currGroup = Optional.of(desc);
        return this;
    }

    @Override
    public ICentreTopLevelActionsInGroup0<T> addGroupAction(final EntityActionConfig actionConfig) {
        builder.topLevelActions.add(new Pair<>(actionConfig, builder.currGroup));
        return this;
    }

    @Override
    public IAlsoCentreTopLevelActions<T> endTopActionsGroup() {
        builder.currGroup = Optional.empty();
        return this;
    }

    @Override
    public ICentreTopLevelActions<T> also() {
        // this could be a genuine also call to add more top level actions
        // or a polymorphic call inherited from ResultSetBuilder to add more properties into the result set
        // they need to be differentiated
        if (propName.isPresent() || propDef.isPresent()) {
            super.also();
        }

        return this;
    }

    @Override
    public ISelectionCritKindSelector<T> addCrit(final String propName) {
        if (StringUtils.isEmpty(propName)) {
            throw new IllegalArgumentException("Property name should not be empty.");
        }

        if (!"this".equals(propName) && !EntityUtils.isProperty(this.builder.getEntityType(), propName)) {
            throw new IllegalArgumentException(String.format("Provided value '%s' is not a valid property expression for entity '%s'", propName, builder.getEntityType().getSimpleName()));
        }

        if (builder.selectionCriteria.contains(propName)) {
            throw new IllegalArgumentException(String.format("Provided value '%s' has been already added as a selection criterion for entity '%s'", propName, builder.getEntityType().getSimpleName()));
        }

        builder.currSelectionCrit = Optional.of(propName);
        builder.selectionCriteria.add(propName);
        return new SelectionCriteriaBuilder<T>(builder, this);
    }

    @Override
    public ICentreTopLevelActionsWithSse<T> runAutomatically() {
        builder.runAutomatically = true;
        return this;
    }
    
	@Override
	public ICentreTopLevelActionsWithEnforcePostSaveRefreshConfig<T> hasEventSourceAt(final String uri) {
		if (StringUtils.isEmpty(uri)) {
			throw new IllegalArgumentException("Server-Side Eventing URI should not be empty.");
		}
        builder.sseUri = uri;
        return this;
	}

    @Override
    public ICentreTopLevelActions<T> enforcePostSaveRefresh() {
        builder.enforcePostSaveRefresh = true;
        return this;
    }
	
}
