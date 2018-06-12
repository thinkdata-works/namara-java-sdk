package namara.query;

public class InnerJoin extends Join {
    InnerJoin(Context context, Identifier identifier) {
        super(context, identifier);
    }

    @Override
    protected String expression() {
        return "INNER JOIN";
    }
}
