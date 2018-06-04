package namara.query;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

abstract class TerminalExpression extends Expression {

    TerminalExpression(Context context) { super(context); }

    protected boolean terminated = false;

    abstract List<String> terminals();

    protected void terminate() {
        if (!terminated) {
            context.append(StringUtils.join(terminals(), ", "));
            terminated = true;
        }
    }

    @Override
    public Query build() {
        terminate();
        return super.build();
    }
}
