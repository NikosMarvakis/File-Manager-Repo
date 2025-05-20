package file_manager.utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * PathUtils is a utility class that provides essential path management functionality for the File Manager application.
 * It handles all directory-related operations including:
 * - Getting the current working directory
 * - Changing directories
 * - Navigating to parent directories
 * 
 * @author Nikolaos Marvakis
 * @version 1.0
 */
public class PathUtils {
    
    /**
     * Retrieves and returns the absolute path of the current working directory.
     * This method uses System.getProperty("user.dir") to get the current directory
     * where the application is running.
     *
     * @return String representing the current working directory path
     */
    public static String getPath() {
        //return the currently running directory
        return System.getProperty("user.dir");
    }

    /**
     * Changes the current working directory to a new specified path.
     * The method handles both absolute and relative paths, and includes
     * validation to ensure the target path exists and is a directory.
     * If no path is provided, it will prompt the user for input.
     *
     * @param path The target directory path to change to. If null, user will be prompted for input.
     * @return String representing the new working directory path if successful, null otherwise
     * @throws java.nio.file.InvalidPathException if the provided path is invalid
     */
    public static String changeDir(String path) {
        //if path value is not given as parameter
        if (path == null) {
            System.out.print("Enter path: ");
            path = InputUtils.inputFunction();
        }
        
        try {
            //create path object
            Path path1 = Paths.get(path);
            
            //check if directory exists
            if (Files.isDirectory(path1) == true) {
                System.setProperty("user.dir", path1.toString());
                return path1.toString();
            }
            else {
                System.out.println("directory not existing");
            }
        }
        catch(java.nio.file.InvalidPathException ipe) {
            System.out.println("invalid path");
        }
        return null;
    }

    /**
     * Navigates to the parent directory of the current working directory.
     * This method is useful for moving up one level in the directory hierarchy.
     * It works by:
     * 1. Getting the current path
     * 2. Splitting it into components
     * 3. Removing the last component
     * 4. Reconstructing the path to the parent directory
     */
    public static void prevDir() {
        //split the initial directory into separate parts, split (\) values
        String[] current_dir_split = getPath().toString().split("\\\\");
        
        StringBuilder new_path = new StringBuilder();
        //reconnect the previously split parts into new path without the last part
        for (int i = 0; i < current_dir_split.length - 1; i++) {
            new_path.append(current_dir_split[i] + "/");
        }
        changeDir(new_path.toString());
    }
} 