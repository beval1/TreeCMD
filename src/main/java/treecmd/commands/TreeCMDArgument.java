package treecmd.commands;

import java.lang.reflect.Type;

public abstract class TreeCMDArgument {
    private Object value;

//    public static List<Class> instantiatedDerivedTypes = new ArrayList<>();
    protected TreeCMDArgument() {
//        Class derivedClass = this.getClass();
//        if (!instantiatedDerivedTypes.contains(derivedClass)) {
//            instantiatedDerivedTypes.add(this.getClass());
//        }
    }

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
