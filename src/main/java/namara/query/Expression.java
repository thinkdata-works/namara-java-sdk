package namara.query;

abstract class Expression {
    protected final Context context;

    Expression(Context context) {
        this.context = context;
    }

    /**
     * Gets the queryBuilder for the constructed expression
     * @return the resulting QueryBuilder
     */
    public QueryBuilder build() {
        return context.getQueryBuilder();
    }
}
