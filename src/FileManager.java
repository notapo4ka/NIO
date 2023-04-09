import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileManager {

    private static Path currentDirectory = Paths.get(System.getProperty("user.dir"));

    public static void run() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String command;
        
        while (true) {
            System.out.println(currentDirectory.toAbsolutePath() + " $ ");
            try {
                command = reader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (command.equals("exit")) {
                break;
            }

            String[] keys = command.split("\\s+");

            switch (keys[0]) {
                case "cd":
                    changeDirectory(keys);
                    break;
                case "cp":
                    copyFile(keys);
                    break;
                case "ls":
                    localStorage(keys);
                    break;
                case "pwd":
                    printWorkingDirectory(keys);
                    break;
                default:
                    System.out.println("The wrong command is entered");
                    break;
            }
        }
    }

    private static void changeDirectory(String[] keys) {
        if (keys.length == 1) {
            System.out.println("Specify the directory");
        }

        String targetDirectory = keys[1];

        if (targetDirectory.equals("..")) {
            Path parentDirectory = currentDirectory.getParent();
            if (parentDirectory == null) {
                System.out.println("You are already in the root directory");
            }
            currentDirectory = parentDirectory;
        } else if (targetDirectory.startsWith("/")) {
            currentDirectory = Paths.get(targetDirectory);
        } else {
            currentDirectory = currentDirectory.resolve(targetDirectory);
        }

        if (!Files.exists(currentDirectory)) {
            System.out.println("Directory does not exist");
            currentDirectory = currentDirectory.getParent();
        }
    }

    private static void copyFile(String[] keys) {
        if (keys.length < 3) {
            System.out.println("Please use this type form: cp [source] [target]");
        }

        Path sourcePath = Paths.get(keys[1]);
        Path targetPath = Paths.get(keys[2]);

        if (!Files.exists(sourcePath)) {
            System.out.println("Source file does not exist");
        }

        if (Files.isDirectory(targetPath)) {
            targetPath = targetPath.resolve(sourcePath.getFileName());
        }

        try {
            Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void localStorage(String[] keys) {
        try {
            Files.list(currentDirectory).forEach(System.out::println);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void printWorkingDirectory(String[] keys) {
        System.out.println(currentDirectory);
    }
}