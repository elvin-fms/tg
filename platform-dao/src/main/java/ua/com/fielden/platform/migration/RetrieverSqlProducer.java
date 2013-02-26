package ua.com.fielden.platform.migration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import ua.com.fielden.platform.dao.DomainMetadataAnalyser;
import ua.com.fielden.platform.entity.AbstractEntity;
import ua.com.fielden.platform.reflection.Finder;

public class RetrieverSqlProducer {
    private final IRetriever<? extends AbstractEntity<?>> retriever;
    private final DomainMetadataAnalyser dma;

    public RetrieverSqlProducer(final DomainMetadataAnalyser dma, final IRetriever<? extends AbstractEntity<?>> retriever) {
	this.dma = dma;
	this.retriever = retriever;
    }

    public String getSql() {
	final StringBuffer sb = new StringBuffer();
	sb.append(getYieldsSql(getKeyPropsYields(retriever.resultFields().keySet())));
	sb.append(getFromSql());
	sb.append(getWhereSql());
	sb.append(getGroupBySql());
	sb.append(getOrderBySql());
	return sb.toString();
    }

    private String getKeyResultsOnlySql(final Set<String> keyProps) {
	final StringBuffer sb = new StringBuffer();
	sb.append(getYieldsSql(getKeyPropsYields(keyProps)));
	sb.append(getFromSql());
	sb.append(getWhereSql());
	sb.append(getGroupBySql());
	return sb.toString();
    }

    private StringBuffer getWhereSql() {
	final StringBuffer sb = new StringBuffer();
	if (retriever.whereSql() != null) {
	    sb.append("\nWHERE ");
	    sb.append(retriever.whereSql());
	}
	return sb;
    }

    private StringBuffer getOrderBySql() {
	return new StringBuffer();
    }

    private StringBuffer getFromSql() {
	final StringBuffer sb = new StringBuffer();
	sb.append("\nFROM ");
	sb.append(retriever.fromSql());
	return sb;
    }

    private String getYieldsSql(final Map<String, String> resultProps) {
	final StringBuffer sb = new StringBuffer();
	sb.append("\nSELECT ");
	for (final Iterator<Entry<String, String>> iterator = resultProps.entrySet().iterator(); iterator.hasNext();) {
	    final Map.Entry<String, String> keyPropName = iterator.next();
	    sb.append(keyPropName.getValue() + " \"" + keyPropName.getKey() + "\"" + (iterator.hasNext() ? ", " : ""));
	}
	return sb.toString();
    }

    private StringBuffer getGroupBySql() {
	final StringBuffer sb = new StringBuffer();

	if (retriever.groupSql() != null) {
	    sb.append("\nGROUP BY ");
	    for (final Iterator<String> iterator = retriever.groupSql().iterator(); iterator.hasNext();) {
		final String keyPropName = iterator.next();
		sb.append(keyPropName + (iterator.hasNext() ? ", " : ""));
	    }
	}

	return sb;
    }

    private Map<String, String> getKeyPropsYields (final Set<String> keyProps) {
	final Map<String, String> result = new HashMap<String, String>();
	for (final Map.Entry<String, String> resultField : retriever.resultFields().entrySet()) {
	    if (keyProps.contains(resultField.getKey())) {
		result.put(resultField.getKey(), resultField.getValue());
	    }
	}
	return result;
    }

    public String getKeyUniquenessViolationSql() {
	// get number of records that are not unique
	// print out sql for getting these records (for troubleshooting purposes)

	//)

	final Set<String> keyProps = dma.getLeafPropsFromFirstLevelProps(null, retriever.type(), new HashSet<String>(Finder.getFieldNames(Finder.getKeyMembers(retriever.type()))));

	final StringBuffer sb = new StringBuffer();
	final String baseSql = getKeyResultsOnlySql(keyProps);
	sb.append("SELECT 1 WHERE EXISTS (SELECT * FROM (" + baseSql + ") MT WHERE 1 < (SELECT COUNT(*) FROM (" + baseSql + ") AA WHERE ");
	for (final Iterator<String> iterator = keyProps.iterator(); iterator.hasNext();) {
	    final String keyPropName = iterator.next();
	    sb.append("AA.\"" + keyPropName + "\" = MT.\"" + keyPropName + "\"" + (iterator.hasNext() ? " AND " : ""));
	}
	sb.append("))");
	return sb.toString();
    }
}