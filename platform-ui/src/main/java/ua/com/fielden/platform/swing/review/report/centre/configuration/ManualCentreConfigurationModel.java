package ua.com.fielden.platform.swing.review.report.centre.configuration;

import ua.com.fielden.platform.criteria.generator.ICriteriaGenerator;
import ua.com.fielden.platform.dao.IEntityDao;
import ua.com.fielden.platform.domaintree.centre.ICentreDomainTreeManager.ICentreDomainTreeManagerAndEnhancer;
import ua.com.fielden.platform.entity.AbstractEntity;
import ua.com.fielden.platform.error.Result;
import ua.com.fielden.platform.swing.ei.development.EntityInspectorModel;
import ua.com.fielden.platform.swing.review.IEntityMasterManager;
import ua.com.fielden.platform.swing.review.development.EntityQueryCriteria;
import ua.com.fielden.platform.swing.review.report.ReportMode;
import ua.com.fielden.platform.swing.review.report.centre.EntityCentreModel;
import ua.com.fielden.platform.swing.review.report.centre.binder.CentrePropertyBinder;
import ua.com.fielden.platform.swing.review.report.centre.factory.DefaultAnalysisBuilder;
import ua.com.fielden.platform.swing.review.report.centre.factory.DefaultGridForManualEntityCentreFactory;
import ua.com.fielden.platform.swing.review.report.centre.factory.IAnalysisBuilder;
import ua.com.fielden.platform.swing.review.wizard.tree.editor.DomainTreeEditorModel;

/**
 * A base calass for manual centre-based configuration model.
 * It is used for building details view for compound masters where the relationship between dependent entity and its master should be represented as an entity centre.
 *
 * @author TG TM
 *
 * @param <T> -- dependent entity type
 * @param <M> -- master entity type
 */
public class ManualCentreConfigurationModel<T extends AbstractEntity<?>, M extends AbstractEntity<?>> extends AbstractCentreConfigurationModel<T, ICentreDomainTreeManagerAndEnhancer> {

    private final String linkProperty;

    private final IAnalysisBuilder<T> analysisBuilder;
    private final DefaultGridForManualEntityCentreFactory<T> analysisFactory;
    private ManualCentreConfigurationView<T, M> view;

    /**
     * The {@link ICentreDomainTreeManagerAndEnhancer} instance for this analysis details.
     */
    private final ICentreDomainTreeManagerAndEnhancer cdtme;

    private M linkEntity;

    public ManualCentreConfigurationModel(final Class<T> entityType, //
	    final DefaultGridForManualEntityCentreFactory<T> analysisFactory,//
	    final ICentreDomainTreeManagerAndEnhancer cdtme, //
	    final IEntityMasterManager masterManager, //
	    final ICriteriaGenerator criteriaGenerator,//
	    final String linkProperty) {
	super(entityType, null, null, masterManager, criteriaGenerator);
	this.cdtme = cdtme;
	this.analysisFactory = analysisFactory;
	this.analysisBuilder = new DefaultAnalysisBuilder<>(analysisFactory);
	this.linkProperty = linkProperty;
    }

    @Override
    protected Result canSetMode(final ReportMode mode) {
	if(ReportMode.WIZARD.equals(mode)){
	    return new Result(this, new IllegalArgumentException("The wizard mode can not be set for manual entity centre."));
	}
	return Result.successful(this);
    }

    @Override
    protected DomainTreeEditorModel<T> createDomainTreeEditorModel() {
	throw new UnsupportedOperationException("The manual centre can not be configured!");
    }

    /**
     * Set the binding entity for this manual entity centre.
     *
     * @param masterEntity
     */
    public ManualCentreConfigurationModel<T, M> setLinkPropertyValue(final M linkEntity) {
	this.linkEntity = linkEntity;
	return this;
    }

    /**
     * Returns the binding entity for this manual entity centre.
     *
     * @return
     */
    public M getLinkPropertyValue() {
	return linkEntity;
    }

    /**
     * Returns the property name to which this manual entity centre was binded.
     *
     * @return
     */
    public String getLinkProperty() {
	return linkProperty;
    }

    @Override
    protected EntityCentreModel<T> createEntityCentreModel() {
	return new EntityCentreModel<T>(createInspectorModel(getCriteriaGenerator().generateCentreQueryCriteria(getEntityType(), cdtme)), analysisBuilder, getMasterManager(), getName());
    }

    /**
     * Creates the {@link EntityInspectorModel} for the specified criteria
     *
     * @param criteria
     * @return
     */
    private EntityInspectorModel<EntityQueryCriteria<ICentreDomainTreeManagerAndEnhancer, T, IEntityDao<T>>> createInspectorModel(final EntityQueryCriteria<ICentreDomainTreeManagerAndEnhancer, T, IEntityDao<T>> criteria) {
	return new EntityInspectorModel<EntityQueryCriteria<ICentreDomainTreeManagerAndEnhancer, T, IEntityDao<T>>>(criteria,//
		CentrePropertyBinder.<T> createLocatorPropertyBinder());
    }

    public DefaultGridForManualEntityCentreFactory<T> getAnalysisFactory() {
        return analysisFactory;
    }

    /**
     * Builds and executes query.
     */
    public void refresh() {
	if (view != null && view.getPreviousView() != null) {
	    if (view.getPreviousView().getSingleAnalysis().getModel().getMode() == ReportMode.REPORT) {
		view.getPreviousView().getSingleAnalysis().getPreviousView().getModel().executeAnalysisQuery();
	    }
	}
    }


    public ManualCentreConfigurationView<T, M> getView() {
        return view;
    }

    public void setView(final ManualCentreConfigurationView<T, M> view) {
        this.view = view;
    }
}
