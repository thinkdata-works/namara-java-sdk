package namara.query;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class GroupBy extends TerminalExpression {
    private final List<String> columns;

    GroupBy(Context context) {
        super(context);
        context.append("GROUP BY");
        columns = new LinkedList();
    }

    GroupBy(Context context, String... columns) {
        this(context);
        this.columns.addAll(Arrays.asList(columns));
    }

    /**
     * Adds a column to the GROUP BY node
     *
     * @param column column name for grouping
     * @return the resulting expression
     */
    public GroupBy column(String column) {
        columns.add(column);
        return this;
    }

    /**
     * Appends multiple columns to the GROUP BY node
     *
     * @param columns column names (may include aliases)
     * @return the resulting expression
     */
    public GroupBy columns(String... columns) {
        this.columns.addAll(Arrays.asList(columns));
        return this;
    }

    /**
     * Appends a HAVING node to the GROUP BY clause
     *
     * @return the resulting expression
     */
    public Having having() {
        terminate();
        return new Having(context);
    }

    /**
     * Appends a HAVING node to the GROUP BY clause with a given condition
     *
     * @see Having#condition(String)
     * @param condition condition for having
     * @return the resulting expression
     */
    public Having having(String condition) {
        terminate();
        return new Having(context, condition);
    }

    /**
     * Appends a HAVING node to the GROUP BY clause with a given conditions
     *
     * @see Having#conditions(String...)
     * @param conditions list of conditions for having
     * @return the resulting expression
     */
    public Having having(String... conditions) {
        terminate();
        return new Having(context, conditions);
    }

    /**
     * Creates an ORDER BY node for the GROUP BY clause.
     *
     * @return the resulting expression
     */
    public OrderBy orderBy() {
        terminate();
        return new OrderBy(context);
    }

    /**
     * Creates an ORDER BY node for the GROUP BY clause with a list of columns for ordering
     *
     * @param columns columns for ordering
     * @return the resulting expression
     */
    public OrderBy orderBy(String... columns) {
        terminate();
        return new OrderBy(context, columns);
    }

    /**
     * Creates an ORDER BY node for the GROUP BY clause specifying a list of columns and an ordering direction
     *
     * @param order type for ordering
     * @param columns list of columns for ordering
     * @return the resulting expression
     */
    public OrderBy orderBy(OrderByType order, String... columns) {
        terminate();
        return new OrderBy(context, order, columns);
    }

    /**
     * Creates an ORDER BY node for the GROUP BY clause specifying a column and ordering direction
     *
     * @param column column to order on
     * @param order ordering direction
     * @return the resulting expression
     */
    public OrderBy orderBy(String column, OrderByType order) {
        terminate();
        return new OrderBy(context, order, column);
    }

    @Override
    public String toString() {
        terminate();
        return context.toString();
    }

    protected List<String> terminals() { return columns; }
}
