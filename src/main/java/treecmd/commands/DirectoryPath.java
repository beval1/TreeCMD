package treecmd.commands;

import java.lang.reflect.Type;

public class DirectoryPath extends TreeCMDArgument {
    @Override
    public String getFlag() {
        return "-Dir";
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
