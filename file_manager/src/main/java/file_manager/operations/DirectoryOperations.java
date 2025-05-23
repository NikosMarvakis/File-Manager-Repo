package file_manager.operations;

import java.io.File;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static file_manager.utils.InputUtils.inputFunction;
import static file_manager.utils.PathUtils.getPath;

/**
 * Utility class for handling directory operations in the File Manager application.
 * Provides methods for managing directories: listing, creating, deleting, renaming, and moving.
 *
 * @author Nikolaos Marvakis
 * @version 1.0
 */
public class DirectoryOperations {

    private final String pathDelimiter;

    public DirectoryOperations() {
        String os = System.getProperty("os.name").toLowerCase();
        pathDelimiter = os.contains("win") ? "\\" : "/";
    }

    public String getPathDelimiter() {
        return pathDelimiter;
    }

    /**
     * Lists all files and directories in the current working directory.
     */
    public static void listDir() {
        File dir = new File(System.getProperty("user.dir"));
        String[] files = dir.list();
        if (files != null) {
            for (String name : files) {
                System.out.println(name);
            }
        }
    }

    /**
     * Creates a new directory at the specified path.
     * @param dirName The name of the directory to create
     * @return true if the directory was created successfully, false otherwise
     */
    public static boolean newDir(String dirName) {
        DirectoryOperations ops = new DirectoryOperations();
        String path = getPath();

        if (dirName == null) {
            System.out.print("enter folder name: ");
            dirName = inputFunction();
        }

        if (!path.endsWith(ops.getPathDelimiter())) {
            path += ops.getPathDelimiter();
        }

        File folder = new File(path, dirName);

        if (!folder.exists()) {
            folder.mkdir();
            return true;
        } else {
            System.out.println("folder name already exists");
            return false;
        }
    }

    /**
     * Deletes a directory and all its contents recursively.
     * @param path The path of the directory to delete
     * @return true if the directory was deleted successfully, false otherwise
     */
    public static boolean delDir(String path) {
        if (path == null) {
            System.out.print("enter path to folder to delete: ");
            path = inputFunction();
        }

        DirectoryOperations ops = new DirectoryOperations();
        String delimiter = ops.getPathDelimiter();
        File folder = null;

        if (path.contains(delimiter)) {
            folder = new File(path);
        } else {
            File currentDir = new File(getPath());
            File[] files = currentDir.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.isDirectory() && f.getName().equals(path)) {
                        folder = f;
                        break;
                    }
                }
            }
            if (folder == null) {
                File possibleDir = new File(path);
                if (possibleDir.exists() && possibleDir.isDirectory()) {
                    folder = possibleDir;
                }
            }
        }

        if (folder == null) {
            System.out.println("The specified path does not exist or is not a directory.");
            return false;
        }

        try {
            if (!folder.exists()) {
                System.out.println("The specified path does not exist.");
                return false;
            }
            if (!folder.isDirectory()) {
                System.out.println("The specified path is not a directory.");
                return false;
            }

            String[] folderList = folder.list();
            if (folderList != null && folderList.length > 0) {
                System.out.print("The folder is not empty. Are you sure you want to delete it? (Y/N): ");
                String confirmation = inputFunction();
                if (!confirmation.equalsIgnoreCase("Y")) {
                    System.out.println("Deletion cancelled.");
                    return false;
                }
                for (File file : folder.listFiles()) {
                    if (file.isDirectory()) {
                        delDir(file.getAbsolutePath());
                    } else {
                        file.delete();
                    }
                }
            }
            if (!folder.delete()) {
                System.out.println("Failed to delete the folder: " + folder.getAbsolutePath());
                return false;
            }
            return true;
        } catch (NullPointerException npe) {
            System.out.println("folder name must be given as a path");
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    /**
     * Renames a directory to a new name.
     * @param source The current name/path of the directory
     * @param target The new name/path for the directory
     */
    public static void renameDir(String source, String target) {
        DirectoryOperations ops = new DirectoryOperations();

        if (source == null) {
            System.out.print("enter starting folder name: ");
            source = inputFunction();
        }

        File sourceFile = new File(getPath() + ops.getPathDelimiter() + source);
        if (!sourceFile.exists() || !sourceFile.isDirectory()) {
            System.out.println("Starting directory does not exist or is not a directory.");
            return;
        }

        if (target == null) {
            System.out.print("enter target folder name: ");
            target = inputFunction();
        }

        File targetFile = new File(getPath() + ops.getPathDelimiter() + target);
        if (targetFile.exists()) {
            System.out.println("Target name already exists.");
            return;
        }

        try {
            Files.move(sourceFile.toPath(), targetFile.toPath());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Moves a directory from one location to another.
     * @param sourcePath The current path of the directory
     * @param targetPath The destination path where the directory should be moved
     */
    public static void moveDir(String sourcePath, String targetPath) {
        DirectoryOperations ops = new DirectoryOperations();
        String delimiter = ops.getPathDelimiter();

        if (sourcePath == null) {
            System.out.print("enter starting path: ");
            sourcePath = inputFunction();
        }

        File currentDir = new File(getPath());
        File[] files = currentDir.listFiles();
        File sourceFile = null;

        if (files != null) {
            for (File f : files) {
                if (f.isDirectory() && f.getName().equals(sourcePath)) {
                    sourceFile = f;
                    break;
                }
            }
        }
        if (sourceFile == null) {
            if (sourcePath.contains(delimiter)) {
                sourceFile = new File(sourcePath);
                if (!sourceFile.exists() || !sourceFile.isDirectory()) {
                    System.out.println("The specified starting path does not exist or is not a directory.");
                    return;
                }
            } else {
                System.out.println("Invalid starting path. Please provide a valid directory name or path.");
                return;
            }
        }

        if (targetPath == null) {
            System.out.print("enter target path: ");
            targetPath = inputFunction();
        }

        String targetFullPath = targetPath + delimiter + sourceFile.getName();
        Path sourceObj = sourceFile.toPath();
        Path targetObj = Paths.get(targetFullPath);

        try {
            Files.move(sourceObj, targetObj);
        } catch (NoSuchFileException nsfe) {
            System.out.println("path not found");
        } catch (FileAlreadyExistsException faee) {
            System.out.println("folder name already exist in path");
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
