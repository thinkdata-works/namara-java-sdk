package namara.query;

public class Exists extends Expression {
    Exists(Context context) {
        super(context);
        context.append("EXISTS");
    }

    /**
     * Creates an EXIST node for a WHERE expression
     *
     * @param expression the evaluating expression
     * @return the resulting expression
     */
    public Exists exists(Expression expression) {
        context.append("(" + expression.toString().trim() + ")");
        return this;
    }
}
