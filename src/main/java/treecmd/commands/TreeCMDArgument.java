package treecmd.commands;

import java.lang.reflect.Type;

public abstract class TreeCMDArgument {
    private Object value;

    public void setValue(Object value){
        this.value = value;
    }

    public abstract String getFlag();
    public Object getValue(){
        return this.value;
    }
    public abstract boolean requiresValue();
    public abstract Type valueType();

    @Override
    public String toString() {
        return "TreeCMDArgument{" +
                "value=" + value +
                " | " +
                "flag=" + getFlag() +
                '}';
    }
}
