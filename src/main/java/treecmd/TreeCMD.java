package treecmd;

import treecmd.commands.TreeCMDArgument;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class TreeCMD {
    private static final StringBuilder bobTheBuilder = new StringBuilder();
    private int directoriesCounter;
    private int filesCounter;
    private final int maxDepth;
    private final Map<String, TreeCMDArgument> args;
    private final Map<Integer, String> gitignore = new HashMap<>();

    public TreeCMD(Map<String, TreeCMDArgument> arguments) {
        this.args = arguments;
        if (args.containsKey("-L")) {
            maxDepth = Integer.parseInt((String)args.get("-L").getValue());
        } else {
            maxDepth = -1;
        }
    }

    public void listFilesRecursively(File root, int level) throws IOException {
        File[] listOfFilesAndDirectory = root.listFiles();

        scanGitignore(listOfFilesAndDirectory, level);

        if (listOfFilesAndDirectory != null) {
            for (File file : listOfFilesAndDirectory) {
                if (fileInGitignore(file, level)){
                    continue;
                }

                if (file.isDirectory()) {
                    String directoryName = addIndent(file, level);
                    directoriesCounter++;
                    bobTheBuilder.append(String.format("%s%n", directoryName));

                    if (maxDepth > level + 1 || maxDepth == -1) {
                        listFilesRecursively(file, level + 1);
                    }
                }
                else {
                    boolean valid = isValidFile(file.getName());

                    if (valid) {
                        String fileName = addIndent(file, level);
                        filesCounter++;
                        bobTheBuilder.append(String.format("%s%n", fileName));
                    }
                }
            }
        }
    }

    private boolean fileInGitignore(File file, int level) {
        if (!args.containsKey("--gitignore")){
            return false;
        }

        while (level >= 0) {
            if (gitignore.containsKey(level)){
                List<String> lines = Arrays.stream(gitignore.get(level).split("\r\n")).toList();
                for(String line : lines){
                    if (file.getPath().equals(line)){
                        return true;
                    }
                    if (file.getName().equals(line)){
                        return true;
                    }
                    if (isMatch(line, file.getName())){
                        return true;
                    }
                }
            }
            level--;
        }
        return false;
    }

    private void scanGitignore(File[] filesList, int level) throws FileNotFoundException {
        List<File> gitignoreFiles = Arrays.stream(filesList).toList().stream()
                .filter(f -> f.getName().equals(".gitignore")).toList();
        if (!gitignoreFiles.isEmpty()){
            StringBuilder sb = new StringBuilder();
            //open file and append content
            Scanner in = new Scanner(new FileReader(gitignoreFiles.get(0).getPath()));
            while(in.hasNext()) {
                sb.append(in.next());
                sb.append(System.lineSeparator());
            }
            in.close();

            //assign gitignore to the new level
            gitignore.put(level, sb.toString());
        }
    }

    private boolean isValidFile(String filename) {
        boolean valid = true;
        if (args.containsKey("-P")){
            String pattern = String.valueOf(args.get("-P").getValue());
            valid = isMatch(pattern, filename);
        }
        if (args.containsKey("-I")){
            String pattern = String.valueOf(args.get("-I").getValue());
            valid = !isMatch(pattern, filename);
        }
        return valid;
    }

    private boolean isMatch(String pattern, String filename) {
        if (pattern.startsWith("*")){
            String patternSuffix = pattern.substring(pattern.indexOf("*")+1);
            return filename.endsWith(patternSuffix);
        } else {
            return filename.equals(pattern);
        }
    }

    public void listFiles() throws IOException {
        String currentUserDir = args.containsKey("-Dir") ? String.valueOf(args.get("-Dir").getValue())
                : System.getProperty("user.dir");
        File currentUserDirFile = new File(currentUserDir);
        listFilesRecursively(currentUserDirFile, 0);
        bobTheBuilder.append(String.format("%n%d directories, %d files%n", directoriesCounter, filesCounter));
        System.out.println(bobTheBuilder);
        if (args.containsKey("-o")){
            String filePath = String.valueOf(args.get("-o").getValue());
            outputToFile(filePath);
        }
    }

    private void outputToFile(String filePath) throws IOException {
        //automatic resource management
        try (FileWriter fileWriter = new FileWriter(filePath)) {
            fileWriter.write(bobTheBuilder.toString());
        } catch (IOException ex) {
            System.out.println("Can't write to file");
            ex.printStackTrace();
        }
    }

    private String calculateFileSize(File file) throws IOException {
        String output = "";
        long fileSize = Files.size(Path.of(file.getPath()));
        if (args.containsKey("-s")){
            output += String.format(" [ %d ] ", fileSize);
        } else if (args.containsKey("-h")) {
            double fileSizeRounded = 0;
            String subfix = "";
            int numLength = String.valueOf(fileSize).length();
            if (numLength > 6) {
                fileSizeRounded = Math.ceil((double) fileSize / 100000);
                subfix = "M";
            } else if (numLength > 3) {
                fileSizeRounded = Math.ceil((double) fileSize / 1000);
                subfix = "K";
            }
            output += String.format(" [ %.0f%s ] ", fileSizeRounded, subfix);
        }
        return output;

    }

    private String addIndent(File file, int level) throws IOException {
        String indentedStr = "";
        indentedStr += "\t".repeat(level);
        indentedStr += "|___";
        indentedStr += calculateFileSize(file);
        indentedStr += file.getName();
        return indentedStr;
    }
}
