package treecmd;

import treecmd.commands.TreeCMDArgument;
import treecmd.parser.ArgumentParser;
import treecmd.parser.TreeCMDArgumentParser;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, ClassNotFoundException, IOException {
        //initialize CommandParser
        ArgumentParser argumentParser = new TreeCMDArgumentParser();
        Map<String, TreeCMDArgument> arguments = argumentParser.parse(args);

        // recursively print all files present in the root directory
        TreeCMD treeCMD = new TreeCMD(arguments);
        treeCMD.listFiles();
    }
}
