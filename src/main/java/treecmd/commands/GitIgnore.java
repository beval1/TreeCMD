package treecmd.commands;

import java.lang.reflect.Type;

public class GitIgnore extends TreeCMDArgument{
    @Override
    public String getFlag() {
        return "--gitignore";
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
