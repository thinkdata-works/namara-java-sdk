package namara.query;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class From extends TerminalExpression {

    private List<String> tables = new ArrayList();

    From(Context context) {
        super(context);
        this.context.append("FROM");
    }

    /**
     * Specifies a data set identifier for the FROM clause in expression
     *
     * @param identifier
     * @return the resulting expression
     */
    public From dataSet(Identifier identifier) {
        this.tables.add(identifier.getTableName());
        return this;
    }

    /**
     * Specifies multiple data sets (for Join) for FROM clause in expression.
     * It is advised to use explicit `.join()` calls (see Join and it's implementations)
     *
     * @param identifiers
     * @return the resulting expression
     */
    public From dataSets(Identifier... identifiers) {
        for(Identifier identifier : identifiers) {
            this.tables.add(identifier.getTableName());
        }
        return this;
    }

    /**
     * Creates a subselect for FROM clause in expression.
     *
     * @param expression the expression to use as subselect
     * @return the resulting expression
     */
    public From select(Expression expression) {
        this.tables.add("(" + expression.toString().trim() + ")");
        return this;
    }

    /**
     * Creates a subselect for FROM clause in expression
     *
     * @param expression the expression to use as subselect
     * @param alias alias for resulting table view
     * @return the resulting expression
     */
    public From select(Expression expression, String alias) {
        this.tables.add("(" + expression.toString().trim() + ") AS " + alias);
        return this;
    }

    /**
     * Creates a UNION node for FROM clause in expression
     *
     * @return the resulting expression
     */
    public Union union() {
        terminate();
        return new Union(context);
    }

    /**
     * Creates a UNION ALL node for FROM clause in expression
     *
     * @return the resulting expression
     */
    public UnionAll unionAll() {
        terminate();
        return new UnionAll(context);
    }

    /**
     * Creates an empty WHERE node for FROM clause in expression. This appends no value, so should be used
     * for subsequent EXISTS node, else use `where(String condition)` method
     *
     * @return the resulting expression
     */
    public Where where() {
        terminate();
        return new Where(context);
    }

    /**
     * Creates a WHERE node for FROM clause in expression with given condition string
     *
     * @param condition
     * @return the resulting expression
     */
    public Where where(String condition) {
        terminate();
        return new Where(context, condition);
    }

    /**
     * Creates GROUP BY node for FROM clause
     *
     * @return the resulting expression
     */
    public GroupBy groupBy() {
        terminate();
        return new GroupBy(context);
    }

    /**
     * Creates a GROUP BY node for FROM clause, specifying multiple columns.
     * See also `GroupBy#column` and `GroupBy#columns`
     *
     * @param columns
     * @return
     */
    public GroupBy groupBy(String... columns) {
        terminate();
        return new GroupBy(context, columns);
    }

    /**
     * Creates a LEFT OUTER JOIN node for FROM clause.
     *
     * @param identifier
     * @return the resulting expression
     */
    public Join leftOuterJoin(Identifier identifier) {
        terminate();
        return new LeftOuterJoin(context, identifier);
    }

    /**
     * Creates a RIGHT OUTER JOIN node for FROM clause
     *
     * @param identifier
     * @return the resulting expression
     */
    public Join rightOuterJoin(Identifier identifier) {
        terminate();
        return new RightOuterJoin(context, identifier);
    }

    /**
     * Creates an INNER JOIN node for FROM clause
     *
     * @param identifier
     * @return the resulting expression
     */
    public Join innerJoin(Identifier identifier) {
        terminate();
        return new InnerJoin(context, identifier);
    }

    /**
     * Creates a FULL OUTER JOIN node for FROM clause
     *
     * @param identifier
     * @return the resulting expression
     */
    public Join fullOuterJoin(Identifier identifier) {
        terminate();
        return new FullOuterJoin(context, identifier);
    }

    /**
     * Creates an ORDER BY node for FROM clause
     *
     * @return the resulting expression
     */
    public OrderBy orderBy() {
        terminate();
        return new OrderBy(context);
    }

    @Override
    public String toString() {
        terminate();
        return context.toString();
    }

    @Override
    protected void terminate() {
        if(!terminated) {
            this.context.append(StringUtils.join(tables, ", "));
            terminated = true;
        }
    }

    protected List<String> terminals() { return tables; }
}
