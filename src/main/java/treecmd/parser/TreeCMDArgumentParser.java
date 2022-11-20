package treecmd.parser;

import treecmd.commands.*;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class TreeCMDArgumentParser implements ArgumentParser {
    @Override
    public Map<String, TreeCMDArgument> parse(String[] args) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        HashMap<String, TreeCMDArgument> arguments = new HashMap<>();
        int argCounter = 0;
        while(argCounter < args.length) {
            List<TreeCMDArgument> argClasses = getArgClasses();
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

    private List<TreeCMDArgument> getArgClasses() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        List<TreeCMDArgument> argClasses = new ArrayList<>();
        File[] files = Objects.requireNonNull(new File("src/main/java/treecmd/commands").listFiles());
        for (File file : files){
            String filename = file.getName().replace(".java", "");
            Class cmdClass = Class.forName("treecmd.commands." + filename);
            if (!filename.equals("TreeCMDArgument")){
                argClasses.add((TreeCMDArgument) cmdClass.getConstructor().newInstance());
            }
        }
        return argClasses;
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
