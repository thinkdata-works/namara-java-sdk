package namara.query;

public class AndCondition extends Condition {

    /**
     * Creates an AND condition to the WHERE query node
     * @param context
     */
    AndCondition(Context context) {
        super(context);
    }

    @Override
    protected String getPrefix() {
        return "AND";
    }
}
