package namara.query;

public class Union extends Expression {

    Union(Context context) {
        super(context);
        this.context.append("UNION");
    }

    /**
     * Creates a new SELECT node for this union clause
     *
     * @return the resulting expression
     */
    public Select select() {
        return new Select(context);
    }
}
