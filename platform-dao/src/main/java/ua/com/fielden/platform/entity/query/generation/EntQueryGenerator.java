package ua.com.fielden.platform.entity.query.generation;

import java.util.HashMap;
import java.util.Map;

import ua.com.fielden.platform.entity.query.fluent.QueryTokens;
import ua.com.fielden.platform.entity.query.fluent.TokenCategory;
import ua.com.fielden.platform.entity.query.generation.elements.EntQuery;
import ua.com.fielden.platform.entity.query.generation.elements.QueryCategory;
import ua.com.fielden.platform.entity.query.model.QueryModel;
import ua.com.fielden.platform.utils.Pair;

public class EntQueryGenerator {
    private final DbVersion dbVersion;

    public EntQueryGenerator(final DbVersion dbVersion) {
	this.dbVersion = dbVersion;
    }

    public EntQuery generateEntQueryAsResultQuery(final QueryModel qryModel, final Map<String, Object> paramValues) {
	return generateEntQuery(qryModel, paramValues, QueryCategory.RESULT_QUERY);
    }

    public EntQuery generateEntQueryAsResultQuery(final QueryModel qryModel) {
	return generateEntQueryAsResultQuery(qryModel, new HashMap<String, Object>());
    }

    public EntQuery generateEntQueryAsSourceQuery(final QueryModel qryModel, final Map<String, Object> paramValues) {
	return generateEntQuery(qryModel, paramValues, QueryCategory.SOURCE_QUERY);
    }

    public EntQuery generateEntQueryAsSourceQuery(final QueryModel qryModel) {
	return generateEntQueryAsSourceQuery(qryModel, new HashMap<String, Object>());
    }

    public EntQuery generateEntQueryAsSubquery(final QueryModel qryModel, final Map<String, Object> paramValues) {
	return generateEntQuery(qryModel, paramValues, QueryCategory.SUB_QUERY);
    }

    public EntQuery generateEntQueryAsSubquery(final QueryModel qryModel) {
	return generateEntQueryAsSubquery(qryModel, new HashMap<String, Object>());
    }

    private EntQuery generateEntQuery(final QueryModel qryModel, final Map<String, Object> paramValues, final QueryCategory category) {
	ConditionsBuilder where = null;
	final QrySourcesBuilder from = new QrySourcesBuilder(null, dbVersion, paramValues);
	final QryYieldsBuilder select = new QryYieldsBuilder(null, dbVersion, paramValues);
	final QryGroupsBuilder groupBy = new QryGroupsBuilder(null, dbVersion, paramValues);

	ITokensBuilder active = null;

	for (final Pair<TokenCategory, Object> pair : qryModel.getTokens()) {
	    if (!TokenCategory.QUERY_TOKEN.equals(pair.getKey())) {
		if (active != null) {
		    active.add(pair.getKey(), pair.getValue());
		}
	    } else {
		switch ((QueryTokens) pair.getValue()) {
		case WHERE: //eats token
		    where = new ConditionsBuilder(null, dbVersion, paramValues);
		    active = where;
		    break;
		case FROM: //eats token
		    active = from;
		    break;
		case YIELD: //eats token
		    active = select;
		    select.setChild(new YieldBuilder(select, dbVersion, paramValues));
		    break;
		case GROUP_BY: //eats token
		    active = groupBy;
		    groupBy.setChild(new GroupBuilder(groupBy, dbVersion, paramValues));
		    break;
		default:
		    break;
		}
	    }
	}

	return new EntQuery(from.getModel(), where != null ? where.getModel() : null, select.getModel(), groupBy.getModel(), qryModel.getResultType(), category);
    }
}