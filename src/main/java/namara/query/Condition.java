package namara.query;

abstract class Condition extends Expression {
    Condition(Context context) {
        super(context);
    }

    /**
     * Adds the condition string to the Expression
     *
     * @param condition
     */
    public void add(String condition) {
        context.append(getPrefix() + " " + condition);
    }

    /**
     * Creates a BETWEEN condition for the expression.
     * If start is null, but end is not null, expression will become columnName <= end
     * If start is not null, but end is null, expression will become columname >= start
     *
     * @param columnName the column for examination
     * @param start starting timestamp (can be null)
     * @param end ending timestamp (can be null)
     */
    public void between(String columnName, Object start, Object end) {
        if (start == null) {
            if (end != null) {
                add(columnName + " <= " + end);
            }
        } else {
            if (end == null) {
                add(columnName + " >= " + start);
            } else {
                add(columnName + " BETWEEN " + start + " AND " + end);
            }
        }
    }

    /**
     * Creates a NOT BETWEEN condition for expression.
     * If start is null, but end is not null, expression will become columnName > end
     * If start is not null, but end is null, expression will become columnName < start
     *
     * @param columnName the column for examination
     * @param start starting timestamp (can be null)
     * @param end ending timestamp (can be null)
     */
    public void notBetween(String columnName, Object start, Object end) {
        if (start == null) {
            if (end != null) {
                add(columnName + " > " + end);
            }
        } else {
            if (end == null) {
                add(columnName + " < " + start);
            } else {
                add(columnName + " NOT BETWEEN " + start + " AND " + end);
            }
        }
    }

    protected abstract String getPrefix();
}
