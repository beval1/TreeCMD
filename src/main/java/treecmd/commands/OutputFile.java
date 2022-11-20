package treecmd.commands;

import java.lang.reflect.Type;

public class OutputFile extends TreeCMDArgument{
    @Override
    public String getFlag() {
        return "-o";
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
