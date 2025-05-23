package file_manager.utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import file_manager.operations.DirectoryOperations;

/**
 * Utility class for managing file system paths in the File Manager application.
 * <p>
 * This class provides static methods to:
 * <ul>
 *   <li>Retrieve the current working directory</li>
 *   <li>Change the current working directory to a specified path</li>
 *   <li>Navigate to the parent directory</li>
 * </ul>
 * <p>
 * It supports both absolute and relative paths, and updates the "user.dir" system property
 * to reflect changes in the working directory. Path validation is performed using
 * {@link java.nio.file.Files} and {@link java.nio.file.Path}.
 * <p>
 * The class also interacts with {@link DirectoryOperations} for platform-specific path delimiters,
 * and with {@code InputUtils} for user input when a path is not provided.
 * </p>
 * 
 * @author Nikolaos Marvakis
 * @version 1.0
 */
public class PathUtils {
    private static final DirectoryOperations dirOps = new DirectoryOperations();

    /**
     * Retrieves the absolute path of the current working directory.
     *
     * @return the current working directory as a String
     */
    public static String getPath() {
        return System.getProperty("user.dir");
    }

    /**
     * Changes the current working directory to the specified path.
     * <p>
     * If the provided path is {@code null}, the user is prompted to enter a path.
     * The method supports both absolute and relative paths, and validates whether
     * the target directory exists before updating the working directory.
     * </p>
     *
     * @param path the target directory path, or {@code null} to prompt the user
     * @return the new working directory path if successful, or {@code null} if the operation fails
     */
    public static String changeDir(String path) {
        if (path == null) {
            System.out.print("Enter path: ");
            path = InputUtils.inputFunction();
        }

        try {
            Path currentDir = Paths.get(getPath());
            Path localPath = currentDir.resolve(path).normalize();

            if (Files.isDirectory(localPath)) {
                System.setProperty("user.dir", localPath.toString());
                return localPath.toString();
            }

            if (path.contains(dirOps.getPathDelimiter())) {
                Path fullPath = Paths.get(path).normalize();
                if (Files.isDirectory(fullPath)) {
                    System.setProperty("user.dir", fullPath.toString());
                    return fullPath.toString();
                }
            }

            System.out.println("Directory not found: " + path);
        } catch (java.nio.file.InvalidPathException ipe) {
            System.out.println("Invalid path: " + path);
        }
        return null;
    }

    /**
     * Changes the current working directory to its parent directory.
     * <p>
     * If the current directory is already the root, this method does nothing.
     * </p>
     */
    public static void prevDir() {
        String[] parts = getPath().split(dirOps.getPathDelimiter());
        if (parts.length <= 1) {
            // Already at root, do nothing
            return;
        }
        StringBuilder parentPath = new StringBuilder();
        for (int i = 0; i < parts.length - 1; i++) {
            parentPath.append(parts[i]);
            if (i < parts.length - 2) {
                parentPath.append(dirOps.getPathDelimiter());
            }
        }
        changeDir(parentPath.toString());
    }
}
