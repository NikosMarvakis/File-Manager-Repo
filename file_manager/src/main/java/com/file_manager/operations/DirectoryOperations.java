package com.file_manager.operations;

import java.io.File;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.file_manager.utils.InputUtils;
import com.file_manager.utils.PathUtils;

public class DirectoryOperations {
    public static void listDir() {
        //create file object
        File f = new File(System.getProperty("user.dir"));
        
        //turn the files names into a list of names
        String[] all_files = f.list();
        for (int i = 0; i < all_files.length; i++) {
            System.out.println(all_files[i]);
        }
    }

    public static void newDir(String dir_name, String path) {
        if (path == null) {
            System.out.print("Enter path: ");
            path = InputUtils.inputFunction();
        }

        if (path.equals("current")) {
            path = PathUtils.getPath();
        }
        
        if(dir_name == null) {
            System.out.print("enter folder name: ");
            dir_name = InputUtils.inputFunction();
        }
        
        char path_last_char = path.charAt(path.length()-1);
        if (!(path_last_char == '/')) {
            path = path + '/';
        }
        
        //create new file object
        File folder = new File(path, dir_name);

        //check if the folder name already exists and create the folder if not 
        if (!(folder.exists())) {
            folder.mkdir();
        }
        else {
            System.out.println("folder name already exists");
        }
    }

    public static void delDir(String path) {
        if (path == null) {
            System.out.print("enter path to folder to delete: ");
            path = InputUtils.inputFunction();
        }
        try {
            //create a file object from the folder_name 
            File folder = new File(path);
                        
            String[] folder_list = folder.list();

            //check if the folder is empty
            if(folder_list.length > 1) {
                //loop for each file in the selected folder
                for(File file : folder.listFiles()) {
                    //check if the selected file is a folder or not
                    if (file.isDirectory() == true) {
                        //call the same function but change path to the inside folder
                        delDir(file.toString());
                    }
                    //if the selected file is a single file
                    file.delete();
                }
            }
            //in case the folder is empty
            folder.delete();
        }
        catch(NullPointerException npe) {
            System.out.println("folder name must be given as a path");
        }
        catch(Exception e) {
            System.out.println(e);
        }
    }

    public static void renameDir(String starting_string, String target_string) {
        //if no value is given for starting folder path (original folder name / path)
        if(starting_string == null) {
            System.out.print("enter starting folder name: ");
            starting_string = InputUtils.inputFunction();
        }
        
        //if no value is given for target path (new name of the folder/directory)
        if(target_string == null) {
            System.out.print("enter target folder name: ");
            target_string = InputUtils.inputFunction();
        }
        
        //turn the string inputs into path objects
        Path starting_path = Paths.get(PathUtils.getPath() + "\\" + starting_string);
        Path target_path = Paths.get(PathUtils.getPath() + "\\" + target_string);

        //create file objects from the paths
        File starting_path_file = new File(PathUtils.getPath() + "\\" + starting_string);
        File target_path_file = new File(PathUtils.getPath() + "\\" + target_string);
        
        //if the starting folder name exists and the target folder name doesnt exist
        if ((starting_path_file.exists() == true) && (target_path_file.exists() == false)) {
            try {
                //move files from starting path to target path
                Files.move(starting_path, target_path);
            }
            catch(Exception e) {
                System.out.println(e);
            }
        }
        //if the starting folder name doesnt exist
        else if(starting_path_file.exists() == false) {
            System.out.println("starting file not existing");
        }
        //if the target folder name already exists
        else if (target_path_file.exists() == true) {
            System.out.println("name already exists");
        }
    }

    public static void moveDir(String starting_path, String target_path) {
        //in case no starting path name is given as parameter
        if(starting_path == null) {
            System.out.print("enter starting path: ");
            starting_path = InputUtils.inputFunction();
        }
        
        //in case no target path name is given as a parameter
        if(target_path == null) {
            System.out.print("enter target path: ");
            target_path = InputUtils.inputFunction();
        }
        
        String[] split_starting_path = starting_path.split("/");
        String starting_folder_name = split_starting_path[split_starting_path.length - 1];

        target_path = target_path + "\\" + starting_folder_name;

        //create path objects
        Path starting_path_obj = Paths.get(starting_path);
        Path target_path_obj = Paths.get(target_path);

        //move directory from starting path to target path
        try {
            Files.move(starting_path_obj, target_path_obj);
        }
        catch(NoSuchFileException nsfe) {
            System.out.println("path not found");
        }
        catch(FileAlreadyExistsException faee) {
            System.out.println("folder name already exist in path");
        }
        catch(Exception e) {
            System.out.println(e);
        }
    }
} 