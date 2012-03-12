package ua.com.fielden.platform.dao2;

import java.util.HashMap;
import java.util.Map;

import ua.com.fielden.platform.entity.AbstractEntity;
import ua.com.fielden.platform.entity.query.fetch;
import ua.com.fielden.platform.entity.query.model.EntityResultQueryModel;
import ua.com.fielden.platform.entity.query.model.OrderingModel;

public class QueryExecutionModel<T extends AbstractEntity<?>> {
    private final EntityResultQueryModel<T> queryModel;
    private final OrderingModel orderModel;
    private final fetch<T> fetchModel;
    private final Map<String, Object> paramValues;
    private final boolean lightweight;

    private QueryExecutionModel(final Builder<T> builder) {
	queryModel = builder.queryModel;
	orderModel = builder.orderModel;
	fetchModel = builder.fetchModel;
	paramValues = builder.paramValues;
	lightweight = builder.lightweight;
    }

    public EntityResultQueryModel<T> getQueryModel() {
        return queryModel;
    }

    public OrderingModel getOrderModel() {
        return orderModel;
    }

    public fetch<T> getFetchModel() {
        return fetchModel;
    }

    public Map<String, Object> getParamValues() {
        return paramValues;
    }

    public boolean isLightweight() {
        return lightweight;
    }

    public static class Builder<T extends AbstractEntity<?>> {
	    private EntityResultQueryModel<T> queryModel;
	    private OrderingModel orderModel;
	    private fetch<T> fetchModel;
	    private Map<String, Object> paramValues = new HashMap<String, Object>();
	    private boolean lightweight = false;

	public QueryExecutionModel<T> build() {
	    return new QueryExecutionModel<T>(this);
	}

	public Builder(final EntityResultQueryModel<T> queryModel) {
	    this.queryModel = queryModel;
	}

	public Builder<T> orderModel(final OrderingModel val) {
	    orderModel = val;
	    return this;
	}

	public Builder<T> fetchModel(final fetch<T> val) {
	    fetchModel = val;
	    return this;
	}

	public Builder<T> paramValues(final Map<String, Object> val) {
	    paramValues.putAll(val);
	    return this;
	}

	public Builder<T> paramValue(final String name, final Object value) {
	    paramValues.put(name, value);
	    return this;
	}

	public Builder<T> lightweight(final boolean val) {
	    lightweight = val;
	    return this;
	}
    }
}