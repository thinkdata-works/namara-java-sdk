package namara.query;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Having extends TerminalExpression {

    private final List<String> conditions;

    Having(Context context) {
        super(context);
        this.context.append("HAVING");
        conditions = new LinkedList();
    }

    Having(Context context, String... conditions) {
        this(context);
        this.conditions.addAll(Arrays.asList(conditions));
    }

    /**
     * Creates a HAVING node for a GROUP BY clause in expression with given condition string
     *
     * @param condition condition string
     * @return the resulting expression
     */
    public Having condition(String condition) {
        conditions.add(condition);
        return this;
    }

    /**
     * Creates a HAVING node for a GROUP BY caluse in expression with multiple conditions
     *
     * @param conditions list condition strings
     * @return the resulting expression
     */
    public Having conditions(String... conditions) {
        this.conditions.addAll(Arrays.asList(conditions));
        return this;
    }

    /**
     * Creates an ORDER BY node for a GROUP BY clause by a single column and an ordering direction
     *
     * @param column column to order by
     * @param order the ordering direction
     * @return the resulting expression
     */
    public OrderBy orderBy(String column, OrderByType order) {
        terminate();
        return new OrderBy(context, order, column);
    }

    /**
     * Creates an ORDER BY node for a GROUP BY clause by multiple columns
     *
     * @param columns list of columns to order by
     * @return the resuilting expression
     */
    public OrderBy orderBy(String... columns) {
        terminate();
        return new OrderBy(context, columns);
    }

    /**
     * Creates an ORDER BY node for a GROUP BY clause by multiple columns and an order direction
     *
     * @param order the ordering direction
     * @param columns the columns to order by
     * @return the resulting expression
     */
    public OrderBy orderBy(OrderByType order, String... columns) {
        terminate();
        return new OrderBy(context, order, columns);
    }

    @Override
    public String toString() {
        terminate();
        return context.toString();
    }

    protected List<String> terminals() { return conditions; }
}
