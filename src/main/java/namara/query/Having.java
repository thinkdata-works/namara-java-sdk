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

    public Having condition(String condition) {
        conditions.add(condition);
        return this;
    }

    public Having conditions(String... conditions) {
        this.conditions.addAll(Arrays.asList(conditions));
        return this;
    }

    public OrderBy orderBy(String... columns) {
        terminate();
        return new OrderBy(context, columns);
    }

    public OrderBy orderBy(OrderByType order, String... columns) {
        terminate();
        return new OrderBy(context, order, columns);
    }

    public OrderBy orderBy(String column, OrderByType order) {
        terminate();
        return new OrderBy(context, order, column);
    }

    @Override
    public String toString() {
        terminate();
        return context.toString();
    }

    protected List<String> terminals() { return conditions; }
}
