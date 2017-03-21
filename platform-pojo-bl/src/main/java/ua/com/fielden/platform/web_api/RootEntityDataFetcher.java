package ua.com.fielden.platform.web_api;

import static ua.com.fielden.platform.entity.query.fluent.EntityQueryUtils.from;
import static ua.com.fielden.platform.entity_centre.review.DynamicQueryBuilder.createQuery;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import graphql.language.Argument;
import graphql.language.BooleanValue;
import graphql.language.Field;
import graphql.language.Selection;
import graphql.language.SelectionSet;
import graphql.language.Value;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import ua.com.fielden.platform.dao.IEntityDao;
import ua.com.fielden.platform.entity.AbstractEntity;
import ua.com.fielden.platform.entity.factory.ICompanionObjectFinder;
import ua.com.fielden.platform.entity.query.fluent.fetch;
import ua.com.fielden.platform.entity.query.fluent.EntityQueryProgressiveInterfaces.ICompleted;
import ua.com.fielden.platform.entity.query.model.EntityResultQueryModel;
import ua.com.fielden.platform.entity_centre.review.DynamicQueryBuilder.QueryProperty;
import ua.com.fielden.platform.utils.EntityUtils;

public class RootEntityDataFetcher implements DataFetcher {
    private static final Logger LOGGER = Logger.getLogger(RootEntityDataFetcher.class);
    private final Class<? extends AbstractEntity<?>> entityType;
    private final ICompanionObjectFinder coFinder;
    
    public RootEntityDataFetcher(final Class<? extends AbstractEntity<?>> entityType, final ICompanionObjectFinder coFinder) {
        this.entityType = entityType;
        this.coFinder = coFinder;
    }
    
    @Override
    public Object get(final DataFetchingEnvironment environment) {
//        final Object source = environment.getSource();
//        if (source == null) {
//            return null;
//        }
//        if (source instanceof Map) {
//            return ((Map<?, ?>) source).get(propertyName);
//        }
        try {
            LOGGER.error(String.format("Arguments [%s]", environment.getArguments()));
            LOGGER.error(String.format("Context [%s]", environment.getContext()));
            final List<Field> fields = environment.getFields();
            LOGGER.error(String.format("Fields [%s]", fields));
            LOGGER.error(String.format("FieldType [%s]", environment.getFieldType()));
            LOGGER.error(String.format("ParentType [%s]", environment.getParentType()));
            LOGGER.error(String.format("Source [%s]", environment.getSource()));
            
            final List<Field> innerFieldsForEntityQuery = toFields(fields.get(0).getSelectionSet()); // TODO fields could be empty? could contain more than one?
            final LinkedHashMap<String, List<Argument>> properties = properties(null, innerFieldsForEntityQuery);
            
            final Map<String, QueryProperty> queryProperties = new LinkedHashMap<>();
            for (final Map.Entry<String, List<Argument>> propertyAndArguments: properties.entrySet()) {
                final String propertyName = propertyAndArguments.getKey();
                final List<Argument> propertyArguments = propertyAndArguments.getValue();
                if (!propertyArguments.isEmpty()) {
                    final QueryProperty queryProperty = new QueryProperty(entityType, propertyName);
                    
                    queryProperties.put(propertyName, queryProperty);
                    
                    final Argument firstArgument = propertyArguments.get(0);
                    // TODO handle other arguments
                    
                    // TODO check also firstArgument.getName();
                    final Value value = firstArgument.getValue();
                    LOGGER.error(String.format("Arg value [%s]", value));
                    
                    // TODO provide more type safety here 
                    if (value instanceof BooleanValue) {
                        final BooleanValue booleanValue = (BooleanValue) value;
                        if (booleanValue.isValue()) {
                            queryProperty.setValue(true);
                            queryProperty.setValue2(false);
                        } else {
                            queryProperty.setValue(false);
                            queryProperty.setValue2(true);
                        }
                    } else {
                        // TODO implement other cases
                    }
                }
            }
            
            final ICompleted<? extends AbstractEntity<?>> query = createQuery(entityType, new ArrayList<QueryProperty>(queryProperties.values()));
            final EntityResultQueryModel eqlQuery = query.model();
            final IEntityDao<? extends AbstractEntity> co = coFinder.find(entityType);
            
            final fetch<? extends AbstractEntity> fetchModel = EntityUtils.fetchNotInstrumented(entityType).with(properties.keySet()).fetchModel();
            final List entities = co.getAllEntities(from(eqlQuery).with(fetchModel).model()); // TODO fetch order etc.
            return entities;
        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }
    
    private static LinkedHashMap<String, List<Argument>> properties(final String prefix, final List<Field> graphQLFields) {
        final LinkedHashMap<String, List<Argument>> properties = new LinkedHashMap<>();
        for (final Field graphQLField: graphQLFields) {
            final List<Argument> args = graphQLField.getArguments();
            final String property = prefix == null ? graphQLField.getName() : prefix + "." + graphQLField.getName();
            properties.put(property, args);
            
            properties.putAll(properties(property, toFields(graphQLField.getSelectionSet())));
        }
        LOGGER.error(String.format("Fetching props [%s]", properties));
        return properties;
    }

    private static List<Field> toFields(final SelectionSet selectionSet) {
        final List<Field> selectionFields = new ArrayList<>();
        if (selectionSet != null) {
            final List<Selection> selections = selectionSet.getSelections();
            for (final Selection selection: selections) {
                if (selection instanceof Field) {
                    selectionFields.add((Field) selection);
                } else {
                    // TODO investigate what needs to be done here
                }
            }
        }
        return selectionFields;
    }
}