package file_manager.operations;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

import static file_manager.utils.InputUtils.inputFunction;
import static file_manager.utils.PathUtils.getPath;

/**
 * Utility class for performing file operations such as creation, deletion,
 * renaming, reading, writing, copying, and moving files within the File Manager application.
 * <p>
 * All methods handle user input validation and error reporting, and prompt the user
 * for missing parameters if null is provided.
 * </p>
 * <p>
 * This class is not intended to be instantiated.
 * </p>
 *
 * @author Nikolaos Marvakis
 * @version 1.0
 */
public class FileOperations {

    private static final DirectoryOperations directoryInstance = new DirectoryOperations();

    /**
     * Creates a new file with the specified name in the current working directory.
     * If the name is null, prompts the user to enter a file name.
     * Prints a message indicating whether the file was created or if it already exists.
     *
     * @param name the name of the file to create, or null to prompt the user
     */
    public static void newFile(String name) {
        name = promptIfNull(name, "Enter file name: ");
        File file = new File(getPath(), name);
        try {
            if (file.createNewFile()) {
                System.out.println("New file created");
            } else {
                System.out.println("File name already exists");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Removes all contents from the specified file, effectively clearing it.
     * If the file name is null, prompts the user to enter a file name.
     * Prints a message if the file does not exist.
     *
     * @param fileName the name of the file to clear, or null to prompt the user
     */
    public static void clearFile(String fileName) {
        fileName = promptIfNull(fileName, "Enter file name: ");
        File file = new File(getPath(), fileName);
        if (!file.exists()) {
            System.out.println("File not existing");
            return;
        }
        try (FileWriter fw = new FileWriter(file)) {
            fw.write("");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Deletes the specified file from the current working directory.
     * If the file name is null, prompts the user to enter a file name.
     * Prints a message indicating whether the file was deleted or if it does not exist.
     *
     * @param fileName the name of the file to delete, or null to prompt the user
     */
    public static void delFile(String fileName) {
        fileName = promptIfNull(fileName, "Enter file name: ");
        File file = new File(getPath(), fileName);
        if (!file.exists()) {
            System.out.println("File not existing");
            return;
        }
        clearFile(fileName);
        file.delete();
        System.out.println("File deleted");
    }

    /**
     * Renames an existing file to a new name within the current working directory.
     * If either parameter is null, prompts the user for the missing value(s).
     * Prints a message indicating success or failure.
     *
     * @param fileName the current name of the file, or null to prompt the user
     * @param newName  the new name for the file, or null to prompt the user
     */
    public static void renameFile(String fileName, String newName) {
        fileName = promptIfNull(fileName, "Enter file name: ");
        File srcFile = new File(getPath(), fileName);
        if (!srcFile.exists() || !srcFile.isFile()) {
            System.out.println("File not existing");
            return;
        }
        newName = promptIfNull(newName, "Enter new file name: ");
        File destFile = new File(getPath(), newName);
        if (srcFile.renameTo(destFile)) {
            System.out.println("File renamed");
        } else {
            System.out.println("File renaming failed");
        }
    }

    /**
     * Reads and prints the contents of the specified file to the console.
     * If the file name is null, prompts the user to enter a file name.
     * Returns the file contents as a String.
     *
     * @param fileName the name of the file to read, or null to prompt the user
     * @return the contents of the file as a String (empty if file not found)
     */
    public static String readFile(String fileName) {
        fileName = promptIfNull(fileName, "Enter file name: ");
        File file = new File(getPath(), fileName);
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                sb.append(line);
            }
        } catch (Exception e) {
            System.out.println("File not found");
        }
        return sb.toString();
    }

    /**
     * Appends the specified text to the end of the given file.
     * If the file name or text is null, prompts the user for the missing value(s).
     * Prints a message if the file does not exist.
     *
     * @param fileName the name of the file to write to, or null to prompt the user
     * @param text     the text to append, or null to prompt the user
     */
    public static void writeFile(String fileName, String text) {
        fileName = promptIfNull(fileName, "Enter file name: ");
        File file = new File(getPath(), fileName);
        if (!file.exists()) {
            System.out.println("File not existing");
            return;
        }
        text = promptIfNull(text, "Enter text: ");
        int totalLines = countLines(file);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            if (totalLines != 0) writer.newLine();
            writer.write(text);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     * Copies the contents of the source file to a new file.
     * If the target file name is null, generates a unique name based on the source file.
     * Prompts the user for missing parameters if necessary.
     * Prints error messages for invalid paths or existing files.
     *
     * @param srcFileName    the name of the source file to copy, or null to prompt the user
     * @param targetFileName the desired name for the new file, or null to auto-generate
     * @return the name of the newly created file, or null if the operation failed
     */
    public static String copy(String srcFileName, String targetFileName) {
        srcFileName = promptIfNull(srcFileName, "Enter file name: ");
        String newName = targetFileName;
        if (targetFileName == null) {
            newName = generateUniqueFileName(srcFileName);
        } else {
            File targetFile = new File(getPath(), targetFileName);
            try {
                targetFile.createNewFile();
            } catch (InvalidPathException ipe) {
                System.out.println("Path not found");
            } catch (Exception e) {
                System.out.println("File name already exists");
            }
        }
        try {
            File dest = new File(getPath(), newName);
            try {
                dest.createNewFile();
            } catch (Exception e) {
                System.out.println("File name already exists");
            }
            try (FileInputStream in = new FileInputStream(new File(getPath(), srcFileName));
                 FileOutputStream out = new FileOutputStream(dest)) {
                int b;
                while ((b = in.read()) != -1) out.write(b);
            }
            if (targetFileName != null) {
                renameFile(newName, targetFileName);
            }
        } catch (FileNotFoundException fnfe) {
            System.out.println("File not found error fnfe");
        } catch (NullPointerException npe) {
            System.out.println("File not found npe");
        } catch (Exception e) {
            System.out.println(e);
        }
        return newName;
    }

    /**
     * Moves the specified file to a new directory.
     * If the source file name or target path is null, prompts the user for the missing value(s).
     * Handles file overwriting by copying and deleting if the target already exists.
     * Prints error messages for missing files or invalid paths.
     *
     * @param srcFileName the name of the file to move, or null to prompt the user
     * @param targetPath  the destination directory path, or null to prompt the user
     */
    public static void move(String srcFileName, String targetPath) {
        srcFileName = promptIfNull(srcFileName, "Enter file name: ");
        File srcFile = new File(getPath(), srcFileName);
        if (!srcFile.exists()) {
            System.out.println("Source file does not exist.");
            return;
        }
        targetPath = promptIfNull(targetPath, "Enter target path: ");
        String delimiter = directoryInstance.getPathDelimiter();
        String srcPath = getPath() + delimiter + srcFileName;
        String destPath = targetPath + delimiter + srcFileName;
        try {
            Files.move(Paths.get(srcPath), Paths.get(destPath));
        } catch (java.nio.file.NoSuchFileException nsfe) {
            System.out.println("File or path not found error");
        } catch (java.nio.file.FileAlreadyExistsException faee) {
            String copyName = copy(srcFileName, null);
            move(copyName, targetPath);
            delFile(srcFileName);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // --- Helper Methods ---

    /**
     * Prompts the user for input if the provided value is null.
     *
     * @param value       the value to check
     * @param promptText  the prompt to display if value is null
     * @return the original value or user input if value was null
     */
    private static String promptIfNull(String value, String promptText) {
        if (value == null) {
            System.out.print(promptText);
            return inputFunction();
        }
        return value;
    }

    /**
     * Counts the number of lines in a file.
     *
     * @param file the file to count lines in
     * @return the number of lines
     */
    private static int countLines(File file) {
        int lines = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.readLine() != null) lines++;
        } catch (Exception ignored) {}
        return lines;
    }

    /**
     * Generates a unique file name based on the source file name.
     *
     * @param srcFileName the source file name
     * @return a unique file name
     */
    private static String generateUniqueFileName(String srcFileName) {
        String[] parts = srcFileName.split("\\.", 2);
        String base = parts[0];
        String ext = parts.length > 1 ? "." + parts[1] : "";
        int i = 1;
        File candidate = new File(getPath(), base + ext);
        while (candidate.exists()) {
            candidate = new File(getPath(), base + " (" + i + ")" + ext);
            i++;
        }
        return candidate.getName();
    }
}
