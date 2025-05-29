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
 * @version 1.0
 */
public class DirectoryOperations {

    private final String osPathDelimiter;

    /**
     * Initializes the DirectoryOperations with the correct path delimiter for the OS.
     */
    public DirectoryOperations() {
        String operatingSystemName = System.getProperty("os.name").toLowerCase();
        osPathDelimiter = operatingSystemName.contains("win") ? "\\\\" : "/";
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
            System.out.println("Contents of the current directory:");
            for (String fileNameOrDirName : directoryContents) {
                System.out.println(" - " + fileNameOrDirName);
            }
        } else {
            System.out.println("Unable to list directory contents.");
        }
    }

    /**
     * Creates a new directory with the specified name in the current working directory.
     * <p>
     * If {@code directoryName} is {@code null}, the user is prompted to enter a name.
     * The method checks if a directory with the given name already exists; if not, it creates the directory.
     * </p>
     *
     * @param directoryName the name of the new directory to create, or {@code null} to prompt the user for input
     * @return {@code true} if the directory was created successfully, {@code false} if a directory with the same name already exists
     */
    public static boolean newDir(String directoryName) {
        DirectoryOperations directoryOperationsInstance = new DirectoryOperations();
        String currentWorkingDirectoryPath = getCurrentWorkingDirectory();

        if (directoryName == null) {
            directoryName = readUserInput("Please enter the name for the new folder: ");
        }

        if (!currentWorkingDirectoryPath.endsWith(directoryOperationsInstance.getCurrentWorkingDirectoryDelimiter())) {
            currentWorkingDirectoryPath += directoryOperationsInstance.getCurrentWorkingDirectoryDelimiter();
        }

        File newDirectoryFile = new File(currentWorkingDirectoryPath, directoryName);

        if (!newDirectoryFile.exists()) {
            newDirectoryFile.mkdir();
            System.out.println("Folder '" + directoryName + "' created successfully.");
            return true;
        } else {
            System.out.println("A folder with this name already exists. Please choose a different name.");
            return false;
        }
    }

    /**
     * Deletes a directory and all its contents recursively.
     * @param directoryPath The path of the directory to delete. If null, prompts user for input.
     * @return true if the directory was deleted successfully, false otherwise
     */
    public static boolean delDir(String directoryPath, String autoConfirmString) {
        if (directoryPath == null) {
            directoryPath = readUserInput("Please enter the path of the folder you want to delete: ");
        }

        DirectoryOperations directoryOperationsInstance = new DirectoryOperations();
        String pathDelimiter = directoryOperationsInstance.getCurrentWorkingDirectoryDelimiter();
        File directoryToDelete = resolveFolder(directoryPath, pathDelimiter);

        if (directoryToDelete == null) {
            System.out.println("Error: The specified folder does not exist or is not a directory.");
            return false;
        }

        try {
            if (!directoryToDelete.exists()) {
                System.out.println("Error: The specified folder does not exist.");
                return false;
            }
            if (!directoryToDelete.isDirectory()) {
                System.out.println("Error: The specified path is not a folder.");
                return false;
            }

            String[] directoryContents = directoryToDelete.list();
            if (directoryContents != null && directoryContents.length > 0) {
                if(autoConfirmString.equals("n")){
                    String userConfirmation = readUserInput("Warning: The folder is not empty. Do you want to delete it and all its contents? (Y/N): ");
                    if (!userConfirmation.equalsIgnoreCase("y")) {
                        System.out.println("Folder deletion cancelled by user.");
                        return false;
                    }
                }
                else{
                    for (File fileOrSubdirectory : directoryToDelete.listFiles()) {
                        if (fileOrSubdirectory.isDirectory()) {
                            delDir(fileOrSubdirectory.getAbsolutePath(), autoConfirmString);
                        } else {
                            fileOrSubdirectory.delete();
                        }
                    }
                }
            }
            if (!directoryToDelete.delete()) {
                System.out.println("Error: Unable to delete the folder: " + directoryToDelete.getAbsolutePath());
                return false;
            }
            System.out.println("Folder deleted successfully.");
            return true;
        } catch (NullPointerException npe) {
            System.out.println("Error: Please provide a valid folder path.");
        } catch (Exception e) {
            System.out.println("An error occurred: " + e);
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
            sourceDirectoryNameOrPath = readUserInput("Enter the current name of the folder you want to rename: ");
        }

        File sourceDirectoryFile = new File(getCurrentWorkingDirectory() + directoryOperationsInstance.getCurrentWorkingDirectoryDelimiter() + sourceDirectoryNameOrPath);
        if (!sourceDirectoryFile.exists() || !sourceDirectoryFile.isDirectory()) {
            System.out.println("Error: The folder you want to rename does not exist or is not a directory.");
            return;
        }

        if (targetDirectoryNameOrPath == null) {
            targetDirectoryNameOrPath = readUserInput("Enter the new name for the folder: ");
        }

        File targetDirectoryFile = new File(getCurrentWorkingDirectory() + directoryOperationsInstance.getCurrentWorkingDirectoryDelimiter() + targetDirectoryNameOrPath);
        if (targetDirectoryFile.exists()) {
            System.out.println("Error: A folder with the new name already exists. Please choose a different name.");
            return;
        }

        try {
            Files.move(sourceDirectoryFile.toPath(), targetDirectoryFile.toPath());
            System.out.println("Folder renamed successfully.");
        } catch (NoSuchFileException nsfe) {
            System.out.println("Error: Source or destination path not found.");
        } catch (FileAlreadyExistsException faee) {
            System.out.println("Error: A folder with the new name already exists.");
        } catch (SecurityException se) {
            System.out.println("Error: Permission denied while renaming the folder.");
        } catch (java.io.IOException ioe) {
            System.out.println("An I/O error occurred while renaming the folder: " + ioe);
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
            sourceDirectoryPath = readUserInput("Enter the path of the folder you want to move: ");
        }

        File sourceDirectoryFile = resolveFolder(sourceDirectoryPath, pathDelimiter);

        if (sourceDirectoryFile == null || !sourceDirectoryFile.exists() || !sourceDirectoryFile.isDirectory()) {
            System.out.println("Error: The folder you want to move does not exist or is not a directory.");
            return;
        }

        if (targetParentDirectoryPath == null) {
            targetParentDirectoryPath = readUserInput("Enter the destination path where you want to move the folder: ");
        }

        String targetFullDirectoryPath = targetParentDirectoryPath + pathDelimiter + sourceDirectoryFile.getName();
        Path sourceDirectoryPathObj = sourceDirectoryFile.toPath();
        Path targetDirectoryPathObj = Paths.get(targetFullDirectoryPath);

        try {
            Files.move(sourceDirectoryPathObj, targetDirectoryPathObj);
            System.out.println("Folder moved successfully.");
        } catch (NoSuchFileException nsfe) {
            System.out.println("Error: Destination path not found.");
        } catch (FileAlreadyExistsException faee) {
            System.out.println("Error: A folder with the same name already exists at the destination.");
        } catch (SecurityException se) {
            System.out.println("Error: Permission denied while moving the folder.");
        } catch (java.io.IOException ioe) {
            System.out.println("An I/O error occurred while moving the folder: " + ioe);
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
