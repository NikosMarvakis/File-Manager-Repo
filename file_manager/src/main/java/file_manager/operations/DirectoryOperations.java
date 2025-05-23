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
 * This class provides methods for managing directories, including listing contents,
 * creating, deleting, renaming, and moving directories.
 *
 * @author Nikolaos Marvakis
 * @version 1.0
 */
public class DirectoryOperations {

    private String pathDelimiter;

    public DirectoryOperations() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            pathDelimiter = "\\";
        } else {
            pathDelimiter = "/";
        }
    }

    public String getPathDelimiter() {
        return pathDelimiter;
    }

    /**
     * Lists all files and directories in the current working directory.
     * This method retrieves the current directory path and prints the names
     * of all files and subdirectories contained within it.
     */
    public static void listDir() {
        //create file object
        File f = new File(System.getProperty("user.dir"));
        
        //turn the files names into a list of names
        String[] all_files = f.list();
        for (int i = 0; i < all_files.length; i++) {
            System.out.println(all_files[i]);
        }
    }

    /**
     * Creates a new directory at the specified path.
     * If the path is not provided, the user will be prompted to enter it.
     * If the directory name is not provided, the user will be prompted to enter it.
     * The method ensures the path ends with a slash and checks for existing directories
     * to prevent duplicates.
     *
     * @param dir_name The name of the directory to create
     * @param path The path where the directory should be created. Use "current" for current directory
     * @return true if the directory was created successfully, false otherwise
     */
    public static boolean newDir(String dir_name) {
        DirectoryOperations ops = new DirectoryOperations();

        String path = getPath();
        
        // Prompt user for directory name if not provided
        if(dir_name == null) {
            System.out.print("enter folder name: ");
            dir_name = inputFunction();
        }
        
        // Ensure path ends with a delimiter
        char path_last_char = path.charAt(path.length()-1);
        if (!(path_last_char == ops.getPathDelimiter().charAt(0))) {
            path = path + ops.getPathDelimiter();
        }
        
        //create new file object
        File folder = new File(path, dir_name);

        //check if the folder name already exists and create the folder if not 
        if (!(folder.exists())) {
            folder.mkdir();
            return true;
        }
        else {
            System.out.println("folder name already exists");
            return false;
        }
    }

    /**
     * Deletes a directory and all its contents recursively.
     * If the path is not provided, the user will be prompted to enter it.
     * The method handles both empty and non-empty directories, deleting all
     * files and subdirectories within the target directory.
     *
     * @param path The path of the directory to delete
     * @return true if the directory was deleted successfully, false otherwise
     * @throws NullPointerException if the path is invalid
     */
    public static boolean delDir(String path) {
        // Prompt user for path if not provided
        if (path == null) {
            System.out.print("enter path to folder to delete: ");
            path = inputFunction();
        }

        DirectoryOperations ops = new DirectoryOperations();
        String delimiter = ops.getPathDelimiter();

        File folder = null;

        // Check if the delimiter is present in the path
        if (path.contains(delimiter)) {
            folder = new File(path);
        } else {
            // Check if a folder with the given name exists in the current directory
            File currentDir = new File(getPath());
            File[] files = currentDir.listFiles();
            boolean found = false;
            if (files != null) {
                for (File f : files) {
                    if (f.isDirectory() && f.getName().equals(path)) {
                        folder = f;
                        found = true;
                        break;
                    }
                }
            }
            // If not found, check if the path is a valid directory path
            if (!found) {
                File possibleDir = new File(path);
                if (possibleDir.exists() && possibleDir.isDirectory()) {
                    folder = possibleDir;
                }
            }
        }

        // If folder is still null, path is invalid
        if (folder == null) {
            System.out.println("The specified path does not exist or is not a directory.");
            return false;
        }

        try {
            // Check if the folder exists and is a directory
            if (!folder.exists()) {
                System.out.println("The specified path does not exist.");
                return false;
            }
            if (!folder.isDirectory()) {
                System.out.println("The specified path is not a directory.");
                return false;
            }

            String[] folder_list = folder.list();

            // Check if the folder list is not null and not empty
            if (folder_list != null && folder_list.length > 0) {
                System.out.print("The folder is not empty. Are you sure you want to delete it? (Y/N): ");
                String confirmation = inputFunction();
                if (!confirmation.equalsIgnoreCase("Y")) {
                    System.out.println("Deletion cancelled.");
                    return false;
                }
                // Loop for each file in the selected folder
                for (File file : folder.listFiles()) {
                    // Check if the selected file is a folder or not
                    if (file.isDirectory()) {
                        // Call the same function but change path to the inside folder
                        delDir(file.getAbsolutePath());
                    } else {
                        // If the selected file is a single file
                        file.delete();
                    }
                }
            }
            // Delete the folder itself (whether empty or just cleaned)
            boolean deleted = folder.delete();
            if (!deleted) {
                System.out.println("Failed to delete the folder: " + folder.getAbsolutePath());
                return false;
            }

            return true;
        }
        catch(NullPointerException npe) {
            System.out.println("folder name must be given as a path");
        }
        catch(Exception e) {
            System.out.println(e);
        }
        return false;
    }

    /**
     * Renames a directory to a new name.
     * If either the source or target name is not provided, the user will be prompted to enter it.
     * The method checks for the existence of both source and target directories
     * to prevent errors and duplicates.
     *
     * @param starting_string The current name/path of the directory
     * @param target_string The new name/path for the directory
     */
    public static void renameDir(String starting_string, String target_string) {
        DirectoryOperations ops = new DirectoryOperations();

        // Prompt for starting folder name/path if not provided
        if (starting_string == null) {
            System.out.print("enter starting folder name: ");
            starting_string = inputFunction();
        }

        // Build starting and target File objects
        File starting_path_file = new File(getPath() + ops.getPathDelimiter() + starting_string);

        // Check if the starting directory exists and is a directory
        if (!starting_path_file.exists() || !starting_path_file.isDirectory()) {
            System.out.println("Starting directory does not exist or is not a directory.");
            return;
        }    

        // Prompt for target folder name/path if not provided
        if (target_string == null) {
            System.out.print("enter target folder name: ");
            target_string = inputFunction();
        }

        File target_path_file = new File(getPath() + ops.getPathDelimiter() + target_string);

        // If the target folder name already exists
        if (target_path_file.exists()) {
            System.out.println("Target name already exists.");
            return;
        }

        // Turn the string inputs into path objects
        Path starting_path = starting_path_file.toPath();
        Path target_path = target_path_file.toPath();

        try {
            // Move (rename) directory
            Files.move(starting_path, target_path);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Moves a directory from one location to another.
     * If either the source or target path is not provided, the user will be prompted to enter it.
     * The method preserves the original directory name in the new location and handles
     * various error conditions such as non-existent paths or duplicate directories.
     *
     * @param starting_path The current path of the directory
     * @param target_path The destination path where the directory should be moved
     * @throws NoSuchFileException if the source path does not exist
     * @throws FileAlreadyExistsException if a directory with the same name exists at the target path
     */
    public static void moveDir(String starting_path, String target_path) {
        DirectoryOperations ops = new DirectoryOperations();
        
        String delimiter = ops.getPathDelimiter();
        File currentDir = new File(getPath());
        File[] files = currentDir.listFiles();
        boolean found = false;
        File startingFile = null;
        
        // Prompt for starting path if not provided
        if (starting_path == null) {
            System.out.print("enter starting path: ");
            starting_path = inputFunction();
        }
        
        if (!found) {
            // Check if a directory with the given name exists in the current directory
            if (files != null) {
            for (File f : files) {
                if (f.isDirectory() && f.getName().equals(starting_path)) {
                startingFile = f;
                found = true;
                break;
                }
            }
            }
            // If still not found, check if the string contains the PathDelimiter
            if (!found) {
            if (starting_path.contains(delimiter)) {
                startingFile = new File(starting_path);
                if (!startingFile.exists() || !startingFile.isDirectory()) {
                System.out.println("The specified starting path does not exist or is not a directory.");
                return;
                }
            } else {
                System.out.println("Invalid starting path. Please provide a valid directory name or path.");
                return;
            }
            }
        }

        // Prompt for target path if not provided
        if (target_path == null) {
            System.out.print("enter target path: ");
            target_path = inputFunction();
        }

        // Check if starting_path is a folder inside the current directory
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory() && f.getName().equals(starting_path)) {
                    startingFile = f;
                    found = true;
                    break;
                }
            }
        }


        // Extract folder name
        String starting_folder_name = startingFile.getName();
        String targetFullPath = target_path + delimiter + starting_folder_name;

        // Create path objects
        Path starting_path_obj = startingFile.toPath();
        Path target_path_obj = Paths.get(targetFullPath);

        // Move directory from starting path to target path
        try {
            Files.move(starting_path_obj, target_path_obj);
        } catch (NoSuchFileException nsfe) {
            System.out.println("path not found");
        } catch (FileAlreadyExistsException faee) {
            System.out.println("folder name already exist in path");
        } catch (Exception e) {
            System.out.println(e);
        }
    }
} 
