package namara.query;

public class QueryBuilder {
    private final Context context;

    // Declared by the querying user
    // Will be used to build sql statement and create pagination for resultSet
    // since query results will limit to 250 rows
    private double limit;
    private double offset;

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
    public QueryBuilder(double limit) {
        this();
        this.limit = limit;
    }

    /**
     * Creates a new query builder with limit and offset
     *
     * @param limit
     * @param offset
     */
    public QueryBuilder(double limit, double offset) {
        this(limit);
        this.offset = offset;
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

    /**
     * Returns the query context for this builder
     *
     * @return The Context for this query builder
     */
    public Context getContext() { return this.context; }
}
