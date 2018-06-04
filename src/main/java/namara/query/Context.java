package namara.query;

import java.util.logging.Logger;

class Context {
    private StringBuilder sqlStatement;
    private QueryBuilder builder;
    private Logger log;

    Context(QueryBuilder builder) {
        this.builder = builder;
        this.log = Logger.getLogger(getClass().getName());
        this.sqlStatement = new StringBuilder();
    }

    @Override
    public String toString() {
        return sqlStatement.toString();
    }

    Context append(String expression) {
        sqlStatement.append(expression + " ");
        return this;
    }

    QueryBuilder getQueryBuilder() {
        return this.builder;
    }
}
