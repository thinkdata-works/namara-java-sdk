package namara.query;

public class OrCondition extends Condition {
    OrCondition(Context context) { super(context); }

    @Override
    protected String getPrefix() { return "OR"; }
}
