package namara.query;

public class FullOuterJoin extends Join {
    FullOuterJoin(Context context, Identifier identifier) { super(context, identifier); }

    @Override
    protected String expression() { return "FULL OUTER JOIN"; }
}