package treecmd.parser;

import treecmd.commands.TreeCMDArgument;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public interface ArgumentParser {
    Map<String, TreeCMDArgument> parse(String[] args) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, ClassNotFoundException;
}
