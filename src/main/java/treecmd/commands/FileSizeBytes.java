package treecmd.commands;

import java.lang.reflect.Type;

public class FileSizeBytes extends TreeCMDArgument{
    @Override
    public String getFlag() {
        return "-s";
    }

    @Override
    public boolean requiresValue() {
        return false;
    }

    @Override
    public Type valueType() {
        return null;
    }
}
