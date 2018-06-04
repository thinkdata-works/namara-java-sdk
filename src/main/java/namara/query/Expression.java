package namara.query;

abstract class Expression {
    protected final Context context;

    Expression(Context context) {
        this.context = context;
    }

    /**
     * Builds the final Query from an expression, or chain of expressions
     * @return the resulting Query
     */
    public Query build() {
        return new Query(context.getQueryBuilder());
    }
}
