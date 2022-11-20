package treecmd.parser;

import treecmd.commands.*;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreeCMDArgumentParser implements ArgumentParser {
    public static final List<TreeCMDArgument> argClasses = List.of(
            new PatternExclusive(),
            new PatternInclusive(),
            new RecursiveLevel(),
            new DirectoryPath(),
            new GitIgnore(),
            new FileSizeBytes(),
            new FileSizeBytesCompact(),
            new OutputFile()
    );

    @Override
    public Map<String, TreeCMDArgument> parse(String[] args) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        HashMap<String, TreeCMDArgument> arguments = new HashMap<>();
        int argCounter = 0;
        while(argCounter < args.length) {
//            List<TreeCMDArgument> argClasses = new ArrayList<>();
//            TreeCMDArgument.instantiatedDerivedTypes.forEach(c -> {
//                TreeCMDArgument arg = TreeCMDArgument.class.cast(c);
//                argClasses.add(arg);
//            });
            boolean isValid = false;
            for (TreeCMDArgument cmdClass : argClasses) {
                if (cmdClass.getFlag().equals(args[argCounter])) {
                    if (cmdClass.requiresValue() && argCounter == args.length - 1) {
                        throw new IllegalArgumentException("You have not specified flag value");
                    }

                    TreeCMDArgument newClass = cmdClass.getClass().getConstructor().newInstance();
                    if (cmdClass.requiresValue()) {
                        String value = args[argCounter + 1];

                        //type checking
                        isValidArgument(cmdClass, value);

                        newClass.setValue(value);
                        argCounter = argCounter + 2;
                    } else {
                        newClass.setValue(true);
                        argCounter++;
                    }
                    isValid = true;
                    arguments.put(newClass.getFlag(), newClass);
                    break;
                }
            }
            if (!isValid){
                throw new IllegalArgumentException("Illegal argument supplied");
            }
        }
        return arguments;
    }

    private void isValidArgument(TreeCMDArgument cmdClass, String value) {
        try {
            if (cmdClass.valueType() == Integer.class) {
                Integer.parseInt(value);
            } else if (cmdClass.valueType() == Double.class) {
                Double.parseDouble(value);
            }
        } catch (NumberFormatException nfe){
            throw new IllegalArgumentException("Wrong value for argument");
        }
    }
}
