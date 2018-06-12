package namara.query;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OrderBy extends TerminalExpression {
    private final List<String> columns = new ArrayList();

    private OrderByType order;

    OrderBy(Context context) {
        super(context);
        this.order = OrderByType.ASC;
        context.append("ORDER BY");
    }

    List<String> terminals() { return columns; }

    OrderBy(Context context, String... columns) {
        this(context);
        this.columns.addAll(Arrays.asList(columns));
    }

    OrderBy(Context context, OrderByType order, String... columns) {
        this(context, columns);
        this.order = order;
    }

    /**
     * Creates an ORDER BY node for a single column with the default direction
     *
     * @see OrderByType#ASC
     * @param column column to order by
     * @return the resulting expression
     */
    public OrderBy column(String column) {
        return column(column, OrderByType.ASC);
    }

    /**
     * Creates an ORDER BY node for multiple columns with the default ordering direction
     *
     * @see OrderByType#ASC
     * @param columns list of columns to order by
     * @return the resulting expression
     */
    public OrderBy columns(String... columns) {
        this.columns.addAll(Arrays.asList(columns));
        this.order = OrderByType.ASC;
        return this;
    }

    /**
     * Creates an ORDER BY node for a column with an ordering direction
     *
     * @param column the column to order by
     * @param order the ordering direction
     * @return the resulting expression
     */
    public OrderBy column(String column, OrderByType order) {
        if (order == null) {
            return column(column);
        }

        columns.add(column);
        this.order = order;
        return this;
    }

    /**
     * Creates an ORDER BY node for multiple columns with a specified ordering direction
     *
     * @param order ordering direction
     * @param columns list of columns to order by
     * @return the resulting expression
     */
    public OrderBy columns(OrderByType order, String... columns) {
        columns(columns);
        this.order = order;
        return this;
    }

    @Override
    public String toString() {
        terminate();
        return context.toString();
    }

    @Override
    protected void terminate() {
        if (!terminated) {
            context.append(StringUtils.join(columns, ", "));
            context.append(order.name());
        }
    }
}
