package file_manager.operations;

import java.io.File;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static file_manager.utils.InputUtils.readUserInput;
import static file_manager.utils.PathUtils.getCurrentWorkingDirectory;

/**
 * Utility class for handling directory operations in the File Manager application.
 * Provides methods for managing directories: listing, creating, deleting, renaming, and moving.
 *
 * @author Nikolaos Marvakis
 * @version 1.1
 */
public class DirectoryOperations {

    private final String osPathDelimiter;

    /**
     * Initializes the DirectoryOperations with the correct path delimiter for the OS.
     */
    public DirectoryOperations() {
        String operatingSystemName = System.getProperty("os.name").toLowerCase();
        osPathDelimiter = operatingSystemName.contains("win") ? "\\" : "/";
    }

    /**
     * Returns the path delimiter for the current OS.
     * @return the path delimiter
     */
    public String getCurrentWorkingDirectoryDelimiter() {
        return osPathDelimiter;
    }

    /**
     * Lists all files and directories in the current working directory.
     */
    public static void listDir() {
        File currentWorkingDirectoryFile = new File(System.getProperty("user.dir"));
        String[] directoryContents = currentWorkingDirectoryFile.list();
        if (directoryContents != null) {
            for (String fileNameOrDirName : directoryContents) {
                System.out.println(fileNameOrDirName);
            }
        }
    }

    /**
     * Creates a new directory at the specified path.
     * @param directoryName The name of the directory to create. If null, prompts user for input.
     * @return true if the directory was created successfully, false otherwise
     */
    public static boolean newDir(String directoryName) {
        DirectoryOperations directoryOperationsInstance = new DirectoryOperations();
        String currentWorkingDirectoryPath = getCurrentWorkingDirectory();

        if (directoryName == null) {
            System.out.print("Enter folder name: ");
            directoryName = readUserInput();
        }

        if (!currentWorkingDirectoryPath.endsWith(directoryOperationsInstance.getCurrentWorkingDirectoryDelimiter())) {
            currentWorkingDirectoryPath += directoryOperationsInstance.getCurrentWorkingDirectoryDelimiter();
        }

        File newDirectoryFile = new File(currentWorkingDirectoryPath, directoryName);

        if (!newDirectoryFile.exists()) {
            newDirectoryFile.mkdir();
            return true;
        } else {
            System.out.println("Folder name already exists.");
            return false;
        }
    }

    /**
     * Deletes a directory and all its contents recursively.
     * @param directoryPath The path of the directory to delete. If null, prompts user for input.
     * @return true if the directory was deleted successfully, false otherwise
     */
    public static boolean delDir(String directoryPath) {
        if (directoryPath == null) {
            System.out.print("Enter path to folder to delete: ");
            directoryPath = readUserInput();
        }

        DirectoryOperations directoryOperationsInstance = new DirectoryOperations();
        String pathDelimiter = directoryOperationsInstance.getCurrentWorkingDirectoryDelimiter();
        File directoryToDelete = resolveFolder(directoryPath, pathDelimiter);

        if (directoryToDelete == null) {
            System.out.println("The specified path does not exist or is not a directory.");
            return false;
        }

        try {
            if (!directoryToDelete.exists()) {
                System.out.println("The specified path does not exist.");
                return false;
            }
            if (!directoryToDelete.isDirectory()) {
                System.out.println("The specified path is not a directory.");
                return false;
            }

            String[] directoryContents = directoryToDelete.list();
            if (directoryContents != null && directoryContents.length > 0) {
                System.out.print("The folder is not empty. Are you sure you want to delete it? (Y/N): ");
                String userConfirmation = readUserInput();
                if (!userConfirmation.equalsIgnoreCase("Y")) {
                    System.out.println("Deletion cancelled.");
                    return false;
                }
                for (File fileOrSubdirectory : directoryToDelete.listFiles()) {
                    if (fileOrSubdirectory.isDirectory()) {
                        delDir(fileOrSubdirectory.getAbsolutePath());
                    } else {
                        fileOrSubdirectory.delete();
                    }
                }
            }
            if (!directoryToDelete.delete()) {
                System.out.println("Failed to delete the folder: " + directoryToDelete.getAbsolutePath());
                return false;
            }
            return true;
        } catch (NullPointerException npe) {
            System.out.println("Folder name must be given as a path.");
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    /**
     * Renames a directory to a new name.
     * @param sourceDirectoryNameOrPath The current name/path of the directory. If null, prompts user for input.
     * @param targetDirectoryNameOrPath The new name/path for the directory. If null, prompts user for input.
     */
    public static void renameDir(String sourceDirectoryNameOrPath, String targetDirectoryNameOrPath) {
        DirectoryOperations directoryOperationsInstance = new DirectoryOperations();

        if (sourceDirectoryNameOrPath == null) {
            System.out.print("Enter starting folder name: ");
            sourceDirectoryNameOrPath = readUserInput();
        }

        File sourceDirectoryFile = new File(getCurrentWorkingDirectory() + directoryOperationsInstance.getCurrentWorkingDirectoryDelimiter() + sourceDirectoryNameOrPath);
        if (!sourceDirectoryFile.exists() || !sourceDirectoryFile.isDirectory()) {
            System.out.println("Starting directory does not exist or is not a directory.");
            return;
        }

        if (targetDirectoryNameOrPath == null) {
            System.out.print("Enter target folder name: ");
            targetDirectoryNameOrPath = readUserInput();
        }

        File targetDirectoryFile = new File(getCurrentWorkingDirectory() + directoryOperationsInstance.getCurrentWorkingDirectoryDelimiter() + targetDirectoryNameOrPath);
        if (targetDirectoryFile.exists()) {
            System.out.println("Target name already exists.");
            return;
        }

        try {
            Files.move(sourceDirectoryFile.toPath(), targetDirectoryFile.toPath());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Moves a directory from one location to another.
     * @param sourceDirectoryPath The current path of the directory. If null, prompts user for input.
     * @param targetParentDirectoryPath The destination path where the directory should be moved. If null, prompts user for input.
     */
    public static void moveDir(String sourceDirectoryPath, String targetParentDirectoryPath) {
        DirectoryOperations directoryOperationsInstance = new DirectoryOperations();
        String pathDelimiter = directoryOperationsInstance.getCurrentWorkingDirectoryDelimiter();

        if (sourceDirectoryPath == null) {
            System.out.print("Enter starting path: ");
            sourceDirectoryPath = readUserInput();
        }

        File sourceDirectoryFile = resolveFolder(sourceDirectoryPath, pathDelimiter);

        if (sourceDirectoryFile == null || !sourceDirectoryFile.exists() || !sourceDirectoryFile.isDirectory()) {
            System.out.println("The specified starting path does not exist or is not a directory.");
            return;
        }

        if (targetParentDirectoryPath == null) {
            System.out.print("Enter target path: ");
            targetParentDirectoryPath = readUserInput();
        }

        String targetFullDirectoryPath = targetParentDirectoryPath + pathDelimiter + sourceDirectoryFile.getName();
        Path sourceDirectoryPathObj = sourceDirectoryFile.toPath();
        Path targetDirectoryPathObj = Paths.get(targetFullDirectoryPath);

        try {
            Files.move(sourceDirectoryPathObj, targetDirectoryPathObj);
        } catch (NoSuchFileException nsfe) {
            System.out.println("Path not found.");
        } catch (FileAlreadyExistsException faee) {
            System.out.println("Folder name already exists in path.");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Resolves a folder File object from a given path or folder name.
     * @param folderPathOrName The path or folder name.
     * @param pathDelimiter The OS-specific path delimiter.
     * @return The File object representing the folder, or null if not found.
     */
    private static File resolveFolder(String folderPathOrName, String pathDelimiter) {
        if (folderPathOrName.contains(pathDelimiter)) {
            File folderFile = new File(folderPathOrName);
            if (folderFile.exists() && folderFile.isDirectory()) {
                return folderFile;
            }
        } else {
            File currentWorkingDirectoryFile = new File(getCurrentWorkingDirectory());
            File[] filesInCurrentDirectory = currentWorkingDirectoryFile.listFiles();
            if (filesInCurrentDirectory != null) {
                for (File directoryFile : filesInCurrentDirectory) {
                    if (directoryFile.isDirectory() && directoryFile.getName().equals(folderPathOrName)) {
                        return directoryFile;
                    }
                }
            }
        }
        return null;
    }
}
