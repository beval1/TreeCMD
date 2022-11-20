package treecmd.commands;

import java.lang.reflect.Type;

public class FileSizeBytesCompact extends TreeCMDArgument{
    @Override
    public String getFlag() {
        return "-h";
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
