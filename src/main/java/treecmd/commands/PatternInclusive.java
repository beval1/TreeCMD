package treecmd.commands;

import java.lang.reflect.Type;

public class PatternInclusive extends TreeCMDArgument {
    @Override
    public String getFlag() {
        return "-P";
    }

    @Override
    public boolean requiresValue() {
        return true;
    }

    @Override
    public Type valueType() {
        return String.class;
    }
}
