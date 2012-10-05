package ua.com.fielden.platform.swing.review.report.centre.factory;

import java.util.Map;

import ua.com.fielden.platform.dao.IEntityDao;
import ua.com.fielden.platform.domaintree.centre.ICentreDomainTreeManager.ICentreDomainTreeManagerAndEnhancer;
import ua.com.fielden.platform.entity.AbstractEntity;
import ua.com.fielden.platform.swing.analysis.DetailsFrame;
import ua.com.fielden.platform.swing.components.blocking.BlockingIndefiniteProgressLayer;
import ua.com.fielden.platform.swing.review.development.EntityQueryCriteria;
import ua.com.fielden.platform.swing.review.report.analysis.lifecycle.configuration.LifecycleAnalysisConfigurationModel;
import ua.com.fielden.platform.swing.review.report.analysis.lifecycle.configuration.LifecycleAnalysisConfigurationView;
import ua.com.fielden.platform.swing.review.report.centre.AbstractEntityCentre;

public class DefaultLifecycleAnalysisFactory<T extends AbstractEntity<?>> implements IAnalysisFactory<T, LifecycleAnalysisConfigurationView<T>> {

    @Override
        public LifecycleAnalysisConfigurationView<T> createAnalysis(//
            final AbstractEntityCentre<T, ICentreDomainTreeManagerAndEnhancer> owner, //
	    final EntityQueryCriteria<ICentreDomainTreeManagerAndEnhancer, T, IEntityDao<T>> criteria, //
	    final String name, //
	    final Map<Object, DetailsFrame> detailsCache,//
	    final BlockingIndefiniteProgressLayer progressLayer) {
	final LifecycleAnalysisConfigurationModel<T> analysisModel = new LifecycleAnalysisConfigurationModel<T>(criteria, name);
	return new LifecycleAnalysisConfigurationView<T>(analysisModel, detailsCache, owner, progressLayer);
        }
}