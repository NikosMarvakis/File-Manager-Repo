package file_manager.utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import file_manager.operations.DirectoryOperations;

/**
 * Utility class for path management in the File Manager application.
 * Handles:
 * - Getting the current working directory
 * - Changing directories
 * - Navigating to parent directories
 * 
 * @author Nikolaos Marvakis
 * @version 1.0
 */
public class PathUtils {
    private static final DirectoryOperations dirOps = new DirectoryOperations();

    /**
     * Returns the absolute path of the current working directory.
     */
    public static String getPath() {
        return System.getProperty("user.dir");
    }

    /**
     * Changes the current working directory to the specified path.
     * Handles both absolute and relative paths.
     * If path is null, prompts the user for input.
     *
     * @param path Target directory path. If null, user will be prompted.
     * @return New working directory path if successful, null otherwise.
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
     * Navigates to the parent directory of the current working directory.
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
