package file_manager.operations;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

import static file_manager.utils.InputUtils.inputFunction;
import static file_manager.utils.PathUtils.getPath;

/**
 * Utility class for handling file operations in the File Manager application.
 * Provides methods for creating, reading, writing, clearing, deleting, renaming, copying, and moving files.
 * Handles file operations with error handling and user input validation.
 *
 * @author Nikolaos Marvakis
 * @version 1.0
 */
public class FileOperations {

    private static final DirectoryOperations directoryInstance = new DirectoryOperations();

    // --- File Creation ---
    public static void newFile(String name) {
        if (name == null) {
            System.out.print("Enter file name: ");
            name = inputFunction();
        }
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

    // --- Clear File Contents ---
    public static void clearFile(String fileName) {
        if (fileName == null) {
            System.out.print("Enter file name: ");
            fileName = inputFunction();
        }
        File file = new File(getPath(), fileName);
        try {
            if (file.exists()) {
                try (FileWriter fw = new FileWriter(file)) {
                    fw.write("");
                }
            } else {
                System.out.println("File not existing");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // --- Delete File ---
    public static void delFile(String fileName) {
        if (fileName == null) {
            System.out.print("Enter file name: ");
            fileName = inputFunction();
        }
        File file = new File(getPath(), fileName);
        try {
            if (file.exists()) {
                clearFile(fileName);
                file.delete();
                System.out.println("File deleted");
            } else {
                System.out.println("File not existing");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // --- Rename File ---
    public static void renameFile(String fileName, String newName) {
        if (fileName == null) {
            System.out.print("Enter file name: ");
            fileName = inputFunction();
        }
        File srcFile = new File(getPath(), fileName);
        if (!srcFile.exists() || !srcFile.isFile()) {
            System.out.println("File not existing");
            return;
        }
        if (newName == null) {
            System.out.print("Enter new file name: ");
            newName = inputFunction();
        }
        File destFile = new File(getPath(), newName);
        if (srcFile.renameTo(destFile)) {
            System.out.println("File renamed");
        } else {
            System.out.println("File renaming failed");
        }
    }

    // --- Read File ---
    public static String readFile(String fileName) {
        StringBuilder sb = new StringBuilder();
        if (fileName == null) {
            System.out.print("Enter file name: ");
            fileName = inputFunction();
        }
        File file = new File(getPath(), fileName);
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

    // --- Write to File (Append) ---
    public static void writeFile(String fileName, String text) {
        if (fileName == null) {
            System.out.print("Enter file name: ");
            fileName = inputFunction();
        }
        File file = new File(getPath(), fileName);
        if (!file.exists()) {
            System.out.println("File not existing");
            return;
        }
        if (text == null) {
            System.out.print("Enter text: ");
            text = inputFunction();
        }
        int totalLines = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.readLine() != null) totalLines++;
        } catch (Exception ignored) {}
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            if (totalLines != 0) writer.newLine();
            writer.write(text);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    // --- Copy File ---
    public static String copy(String srcFileName, String targetFileName) {
        if (srcFileName == null) {
            System.out.print("Enter file name: ");
            srcFileName = inputFunction();
        }
        String newName = targetFileName;
        if (targetFileName == null) {
            String[] parts = srcFileName.split("\\.");
            String base = parts[0], ext = parts.length > 1 ? parts[1] : "";
            int i = 1;
            File candidate = new File(getPath(), base + "." + ext);
            while (candidate.exists()) {
                newName = base + " (" + i + ")." + ext;
                candidate = new File(getPath(), newName);
                i++;
            }
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

    // --- Move File ---
    public static void move(String srcFileName, String targetPath) {
        if (srcFileName == null) {
            System.out.print("Enter file name: ");
            srcFileName = inputFunction();
        }
        File srcFile = new File(getPath(), srcFileName);
        if (!srcFile.exists()) {
            System.out.println("Source file does not exist.");
            return;
        }
        if (targetPath == null) {
            System.out.print("Enter target path: ");
            targetPath = inputFunction();
        }
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
}
