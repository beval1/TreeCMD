package treecmd.commands;

import java.lang.reflect.Type;

public class RecursiveLevel extends TreeCMDArgument {
    @Override
    public String getFlag() {
        return "-L";
    }

    @Override
    public boolean requiresValue() {
        return true;
    }

    @Override
    public Type valueType() {
        return Integer.class;
    }
}
