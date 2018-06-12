package namara.query;

import org.apache.commons.lang3.StringUtils;

import java.util.LinkedList;
import java.util.List;

public class Select {
    private Context context;
    private List<String> columns;

    Select(Context context) {
        this.context = context;
        this.context.append("SELECT");
        this.columns = new LinkedList();
    }

    /**
     * Creates a FROM node for the SELECT expression
     *
     * @return the resulting expression
     */
    public From from() {
        this.context.append(StringUtils.join(columns, ", "));
        return new From(context);
    }

    /**
     * Specifies selecting all values for expression
     *
     * @return the resulting expression
     */
    public Select all() {
        this.columns.add("*");
        return this;
    }

    /**
     * Specifies a column to include in the selecting expression
     *
     * @param column by name (can include AS alias)
     * @return the resulting expression
     */
    public Select column(String column) {
        this.columns.add(column);
        return this;
    }

    /**
     * Specifies columns to include in the selecting expression
     *
     * @param columns by name (can include AS alias)
     * @return the resulting expression
     */
    public Select columns(String... columns) {
        for (String column : columns) {
            this.columns.add(column);
        }
        return this;
    }

    @Override
    public String toString() { return context.toString(); }
}