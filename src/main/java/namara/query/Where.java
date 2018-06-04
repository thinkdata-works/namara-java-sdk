package namara.query;

// Also needs terminal expression - going to just define fields here
public class Where extends Condition {
    Where(Context context) {
        super(context);
        context.append(getPrefix());
    }

    Where(Context context, String condition) {
        super(context);
        add(condition);
    }

    /**
     * Creates a GROUP BY node for this WHERE clause
     *
     * @return the resulting expression
     */
    public GroupBy groupBy() {
        return new GroupBy(context);
    }

    /**
     * Creates a GROUP BY node for this WHERE clause
     *
     * @param columns the column names to group by
     * @return the resulting expression
     */
    public GroupBy groupBy(String... columns) {
        return new GroupBy(context, columns);
    }

    /**
     * Creates a new ORDER BY node for this WHERE clause
     *
     * @return the resulting expression
     */
    public OrderBy orderBy() {
        return new OrderBy(context);
    }

    /**
     * Creates a new ORDER BY node for this WHERE clause with given columns
     *
     * @param columns
     * @return the resulting expression
     */
    public OrderBy orderBy(String... columns) {
        return new OrderBy(context, columns);
    }

    /**
     * Creates a new ORDER BY node for this WHERE clause with given columns and direction
     *
     * @param order
     * @param columns
     * @return the resulting expression
     */
    public OrderBy orderBy(OrderByType order, String... columns) {
        return new OrderBy(context, order, columns);
    }

    /**
     * Conjuctively appends a condition to WHERE clause
     *
     * @param condition condition string
     * @return the resulting expression
     */
    public Where and(String condition) {
        new AndCondition(context).add(condition);
        return this;
    }

    /**
     * Conjunctively appends a timestamp condition to WHERE clause. See docs for `Condition`
     *
     * @param columnName timestamp field
     * @param start start timestamp (can be null)
     * @param end end timestamp (can be null
     * @return the resulting expression
     */
    public Where andBetween(String columnName, Object start, Object end) {
        new AndCondition(context).between(columnName, start, end);
        return this;
    }

    /**
     * Conjunctively appends a timestamp condition to WHERE clause. See docs for `Condition`
     *
     * @param columnName timestamp field
     * @param start start timestamp (can be null)
     * @param end
     * @return the resulting expression
     */
    public Where andNotBetween(String columnName, Object start, Object end) {
        new AndCondition(context).notBetween(columnName, start, end);
        return this;
    }

    /**
     * Disjunctively appends condition to WHERE clause
     *
     * @param condition condition string
     * @return the resulting expression
     */
    public Where or(String condition) {
        new OrCondition(context).add(condition);
        return this;
    }

    /**
     * Disjunctively appends a timestamp condition to WHERE clause. See docs for `Condition`
     *
     * @param columnName timestamp field
     * @param start start timestamp (can be null)
     * @param end end timestamp (can be null)
     * @return the resulting expression
     */
    public Where orBetween(String columnName, Object start, Object end) {
        new OrCondition(context).between(columnName, start, end);
        return this;
    }

    /**
     * Disjunctively appends a timestamp condition to WHERE clause. See docs for `Condition`
     *
     * @param columnName timestamp field
     * @param start start timestamp (can be null)
     * @param end end timestamp (can be null)
     * @return the resulting expression
     */
    public Where orNotBetween(String columnName, Object start, Object end) {
        new OrCondition(context).notBetween(columnName, start, end);
        return this;
    }

    /**
     * Appends an EXISTS node to the where clause. Should be used with empty where like `.where().exists(...)`
     *
     * @param expression expression to seed existance query with
     * @return the resulting expression
     */
    public Where exists(Expression expression) {
        new Exists(context).exists(expression);
        return this;
    }

    /**
     * Condition modifier for WHERE clause, to be used with condition param like `.where("field_name >").any(...)`
     *
     * @param expression the expression for input to the condition
     * @return the resulting expression
     */
    public Where any(Expression expression) {
        new Any(context).any(expression);
        return this;
    }

    /**
     * Condition modifier for WHERE clause, to be used with condition param like `.where("field_name >").all(...)`
     * @param expression the expression for input to the condition
     * @return the resulting expression
     */
    public Where all(Expression expression) {
        new All(context).all(expression);
        return this;
    }

    @Override
    protected String getPrefix() {
        return "WHERE";
    }

    @Override
    public String toString() {
        return context.toString();
    }
}
