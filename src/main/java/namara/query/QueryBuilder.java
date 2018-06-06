package namara.query;

public class QueryBuilder {
    private final Context context;

    /*
     * Declared by the querying user
     * Will be used to getBuilder sql statement and create pagination for resultSet
     * since query results will limit to 250 rows
     */
    private Integer limit;
    private Integer offset;

    /**
     * Make a new query. Will set up the root context
     */
    public QueryBuilder() {
        this.context = new Context(this);
    }

    /**
     * Creates a new query builder with limit
     *
     * @param limit
     */
    public QueryBuilder(int limit) {
        this();
        this.limit = Math.abs(limit);
    }

    /**
     * Creates a new query builder with limit and offset
     *
     * @param limit
     * @param offset
     */
    public QueryBuilder(int limit, int offset) {
        this(limit);
        this.offset = Math.abs(offset);
    }

    /**
     * Create a new Select statement, from which the rest can be built out of
     * @return New select statement
     */
    public Select select() { return new Select(context); }

    @Override
    public String toString() {
        return context.toString();
    }

    public String buildQuery(int limit, int offset) {
        return toString() + " LIMIT " + limit + " OFFSET " + offset;
    }

    /**
     * Returns the query context for this builder
     *
     * @return The Context for this query builder
     */
    public Context getContext() { return this.context; }

    /**
     * Gets the set limit for the query builder
     *
     * @return the limit
     */
    public Integer getLimit() { return this.limit; }

    /**
     * Gets the set offset for the query builder
     *
     * @return the offset
     */
    public Integer getOffset() { return this.offset; }
}
