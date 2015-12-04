package ua.com.fielden.platform.entity.query.fluent;

public enum QueryTokens {
    SELECT("SELECT"),
    FROM("FROM"),
    WHERE("WHERE"), 
    GROUP_BY("GROUP BY"), 
    ORDER_BY("ORDER BY");

    private final String value;

    QueryTokens(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}