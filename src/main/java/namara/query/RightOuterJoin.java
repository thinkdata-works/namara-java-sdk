package namara.query;

public class RightOuterJoin extends Join {
    RightOuterJoin(Context context, Identifier identifier) {
        super(context, identifier);
    }

    @Override
    protected String expression() {
        return "RIGHT OUTER JOIN";
    }
}
