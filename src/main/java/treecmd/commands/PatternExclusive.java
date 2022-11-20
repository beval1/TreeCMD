package treecmd.commands;

import java.lang.reflect.Type;

public class PatternExclusive extends TreeCMDArgument {
    @Override
    public String getFlag() {
        return "-I";
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
