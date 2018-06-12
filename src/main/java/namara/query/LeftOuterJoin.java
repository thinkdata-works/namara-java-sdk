package namara.query;

public class LeftOuterJoin extends Join {
    LeftOuterJoin(Context context, Identifier identifier) {
        super(context, identifier);
    }

    @Override
    protected String expression() {
        return "LEFT OUTER JOIN";
    }
}
