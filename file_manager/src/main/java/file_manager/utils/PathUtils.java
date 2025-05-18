package file_manager.utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PathUtils {
    
    public static String getPath() {
        //return the currently running directory
        return System.getProperty("user.dir");
    }

    public static void changeDir(String path) {
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
            }
            else {
                System.out.println("directory not existing");
            }
        }
        catch(java.nio.file.InvalidPathException ipe) {
            System.out.println("invalid path");
        }
    }

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