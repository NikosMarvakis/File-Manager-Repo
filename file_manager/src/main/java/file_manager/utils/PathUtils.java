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
    private static final DirectoryOperations directoryOperations = new DirectoryOperations();

    /**
     * Retrieves the absolute path of the current working directory.
     *
     * @return the current working directory as a String
     */
    public static String getCurrentWorkingDirectory() {
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
     * @param targetDirectoryPath the target directory path, or {@code null} to prompt the user
     * @return the new working directory path if successful, or {@code null} if the operation fails
     */
    public static String changeWorkingDirectory(String targetDirectoryPath) {
        if (targetDirectoryPath == null) {
            System.out.print("Enter path: ");
            targetDirectoryPath = InputUtils.readUserInput();
        }

        try {
            Path currentDirectoryPath = Paths.get(getCurrentWorkingDirectory());
            Path resolvedTargetCurrentWorkingDirectory = currentDirectoryPath.resolve(targetDirectoryPath).normalize();

            if (Files.isDirectory(resolvedTargetCurrentWorkingDirectory)) {
                System.setProperty("user.dir", resolvedTargetCurrentWorkingDirectory.toString());
                return resolvedTargetCurrentWorkingDirectory.toString();
            }

            if (targetDirectoryPath.contains(directoryOperations.getCurrentWorkingDirectoryDelimiter())) {
                Path absoluteTargetCurrentWorkingDirectory = Paths.get(targetDirectoryPath).normalize();
                if (Files.isDirectory(absoluteTargetCurrentWorkingDirectory)) {
                    System.setProperty("user.dir", absoluteTargetCurrentWorkingDirectory.toString());
                    return absoluteTargetCurrentWorkingDirectory.toString();
                }
            }

            System.out.println("Directory not found: " + targetDirectoryPath);
        } catch (java.nio.file.InvalidPathException invalidPathException) {
            System.out.println("Invalid path: " + targetDirectoryPath);
        }
        return null;
    }

    /**
     * Changes the current working directory to its parent directory.
     * <p>
     * If the current directory is already the root, this method does nothing.
     * </p>
     */
    public static void changeToParentDirectory() {
        String[] currentDirectoryParts = getCurrentWorkingDirectory().split(directoryOperations.getCurrentWorkingDirectoryDelimiter());
        if (currentDirectoryParts.length <= 1) {
            // Already at root, do nothing
            return;
        }
        StringBuilder parentDirectoryPathBuilder = new StringBuilder();
        for (int partIndex = 0; partIndex < currentDirectoryParts.length - 1; partIndex++) {
            parentDirectoryPathBuilder.append(currentDirectoryParts[partIndex]);
            if (partIndex < currentDirectoryParts.length - 2) {
                parentDirectoryPathBuilder.append(directoryOperations.getCurrentWorkingDirectoryDelimiter());
            }
        }
        changeWorkingDirectory(parentDirectoryPathBuilder.toString());
    }
}
