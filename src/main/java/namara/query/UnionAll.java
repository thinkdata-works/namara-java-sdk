package namara.query;

public class UnionAll extends Expression {

    UnionAll(Context context) {
        super(context);
        this.context.append("UNION ALL");
    }

    /**
     * Creates a new SELECT node for UNION ALL clause
     *
     * @return the resulting expression
     */
    public Select select() {
        return new Select(context);
    }
}
