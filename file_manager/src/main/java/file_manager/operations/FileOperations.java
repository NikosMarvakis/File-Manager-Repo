package file_manager.operations;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

import static file_manager.utils.InputUtils.inputFunction;
import static file_manager.utils.PathUtils.getPath;

/**
 * Utility class for handling file operations in the File Manager application.
 * This class provides comprehensive methods for managing files, including creating,
 * reading, writing, clearing, deleting, renaming, copying, and moving files.
 * The class handles various file operations with proper error handling and user input validation.
 *
 * @author Nikolaos Marvakis
 * @version 1.0
 */
public class FileOperations {
    
    /**
     * Creates a new file with the specified name in the current directory.
     * If no name is provided, the user will be prompted to enter one.
     * The method checks for existing files to prevent duplicates.
     *
     * @param name The name of the file to create
     */
    public static void newFile(String name) {
        //in case no name is given as a parameter
        if (name == null) {
            System.out.print("enter file name: ");
            name = inputFunction();
        }

        //create new file object
        File file1 = new File(getPath(), name);

        try {
            //if the file did get created
            if (file1.createNewFile()) {
                System.out.println("new file created");
            }
            //in case it didnt create the file
            else {
                System.out.println("File name already exists");
            }
        }
        //if the path cant be found
        catch(Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Clears all contents from a specified file while preserving the file itself.
     * If no file name is provided, the user will be prompted to enter one.
     * The method verifies the file's existence before attempting to clear it.
     *
     * @param file_name The name of the file to clear
     */
    public static void clearFile(String file_name) {
        //if no value is given in parameter
        if (file_name == null) {
            System.out.print("Enter file name: ");
            file_name = inputFunction();
        }
        
        try {
            File file = new File(file_name);
            if (file.exists() == true) {
                //create buffered writer object
                BufferedWriter writer = new BufferedWriter(new FileWriter(file_name));
                
                //replace the contents with empty string 
                writer.write("");
                
                //close writer object
                writer.close();
            }
            else {
                System.out.println("file not found");
            }
        }
        catch(Exception e) {
            System.out.println("clearing file error");
        }
    }
    
    /**
     * Deletes a file after clearing its contents.
     * If no file name is provided, the user will be prompted to enter one.
     * The method first clears the file's contents before deletion.
     *
     * @param name The name of the file to delete
     * @return true if the file was successfully deleted, false otherwise
     */
    public static boolean delFile(String name) {
        //if no name value is given as parameter
        if (name == null) {
            System.out.print("Enter input name: ");
            name = inputFunction();
        }
        
        //clear the file before deleting it
        clearFile(name);
        
        //create a new file object
        File f = new File(name);
        
        //if the got deleted
        if (f.delete() == true) {
            System.out.println("File deleted");
            return true;
        }
        //if the file did not get deleted
        else {
            System.out.println("failed to delete file");

            return false;
        }
    }

    /**
     * Renames a file to a new name.
     * If either the current or new name is not provided, the user will be prompted to enter them.
     * The method verifies the existence of the source file and checks for name conflicts.
     *
     * @param file_name The current name of the file
     * @param new_name The new name for the file
     */
    public static void renameFile(String file_name, String new_name) {
        //if no file is given in parameters
        if (file_name == null) {
            System.out.print("Enter file name: ");
            file_name = inputFunction();
        }
        
        //if no new file name is given in parameters
        if (new_name == null) {
            System.out.print("Enter new file name: ");
            new_name = inputFunction();
        }
        
        //get the paths of the separate files  
        String start_path = getPath() + "\\" + file_name;
        String end_path = getPath() + "\\" + new_name;
        
        //create file objects from the paths
        File start_file = new File(start_path);
        File end_file = new File(end_path);
        
        //if the file got renamed
        if (start_file.renameTo(end_file) == true) {
            System.out.println("file renamed");
        }
        
        //if the file was not found
        else {
            System.out.println("file renaming failed");
        }
    }

    /**
     * Reads and prints the contents of a file.
     * If no file name is provided, the user will be prompted to enter one.
     * The method reads the file line by line and returns the contents as a string.
     *
     * @param file_name The name of the file to read
     * @return String containing the contents of the file
     */
    public static String readFile(String file_name) {
        StringBuilder stringBuilder = new StringBuilder();

        //if no file name is given as parameter
        if (file_name == null) {
            System.out.print("Enter file name: ");
        }
        
        //if the file is found
        try {
            //create bufferReader object
            BufferedReader reader = new BufferedReader(new FileReader(file_name));
            
            //while there are lines to read continue and read them
            String line;
            while((line = reader.readLine()) != null) {
                System.out.println(line);
                stringBuilder.append(line);
            }
            reader.close();
        }
        //in case the file cant be found
        catch (Exception e) {
            System.out.println("file not found");
        }

        return stringBuilder.toString();
    }

    /**
     * Writes text to a file, appending to existing content.
     * If either the file name or text is not provided, the user will be prompted to enter them.
     * The method handles new line insertion between existing content and new text.
     *
     * @param file_name The name of the file to write to
     * @param text The text to write to the file
     */
    public static void writeFile(String file_name, String text) {
        if (file_name == null) {
            System.out.print("Enter file name: ");
            file_name = inputFunction();
        }
        
        if (text == null) {
            System.out.print("Enter text: ");
            text = inputFunction();
        }
        
        int total_lines = 0;

        //count the number of lines in a file
        try {
            //if the files exists
            BufferedReader reader = new BufferedReader(new FileReader(file_name));
            while(reader.readLine() != null) {
                total_lines++;
            }
            reader.close();
        }
        catch(Exception error){
        }
        
        try {
            //create buffered writer object
            BufferedWriter writer = new BufferedWriter(new FileWriter(file_name, true));

            //if the text input is the first input for the file
            if (total_lines != 0) {
                //write a new line
                writer.newLine();
            }

            //write into file
            writer.write(text);

            //close writer object
            writer.close();
        }
        catch(IOException e) {
            System.out.println(e);
        }
    }

    /**
     * Creates a copy of a file with automatic name conflict resolution.
     * If no target name is provided, the method generates a unique name by appending a number.
     * The method handles various file types and preserves the original file extension.
     *
     * @param file_name_param The name of the file to copy
     * @param new_name_param The desired name for the copy (optional)
     * @return String containing the name of the created copy
     * @throws FileNotFoundException if the source file cannot be found
     * @throws InvalidPathException if the target path is invalid
     */
    public static String copy(String file_name_param, String new_name_param) {
        //in case no file name is given as a parameter
        if (file_name_param == null) {
            System.out.print("Enter file name: ");
            file_name_param = inputFunction();
        }

        //copy the new name to use as a object
        String new_name = new_name_param;
        
        //in case no name is given for the copy file
        if (new_name_param == null){
            //split the starting file name into name and extension (text111, .txt)
            String[] split_file_name_param = file_name_param.split("\\.");
                        
            //create the file object for this file 
            File start_file_obj = new File(getPath(), split_file_name_param[0] + "." + split_file_name_param[1]);
            
            //count the number to give to the file copy
            int i = 1;
                        
            //check if file already exists with that name
            while(start_file_obj.exists() == true) {
                //if the filename doesnt already exist just add "(i)" at the end
                if (!(split_file_name_param[0] + "." + split_file_name_param[1]).equals(new_name)) {
                    new_name = split_file_name_param[0] + " (" + i + ")." + split_file_name_param[1];
                }
                else {
                    //get last and third to last characters to check 
                    char third_char = (split_file_name_param[0].charAt(split_file_name_param[0].length() - 3));
                    char last_char = (split_file_name_param[0].charAt(split_file_name_param[0].length() - 1));
                    
                    //convert into strings
                    String third_char_string = Character.toString(third_char);
                    String last_char_string = Character.toString(last_char);
    
                    //check if the file is a copy of a copy or not
                    if ( (third_char_string.equals("(") && last_char_string.equals(")") )){
                        new_name = split_file_name_param[0].substring(0, split_file_name_param[0].length() - 3) + "(" + i + ")." + split_file_name_param[1];
                    }
                }
                
                //update the file object with the new file name (with the number at the end)
                start_file_obj = new File(getPath() + "\\" + new_name);
                i++;
            }
        }
        //in case copy file name is given as a parameter
        else {
            File new_name_obj = new File(getPath(), new_name_param);
            try {
                new_name_obj.createNewFile();
            }
            catch(InvalidPathException ipe) {
                System.out.println("path not found");
            }
            catch(Exception fee) {
                System.out.println("file name already exists");
            }
        }
        
        //copy the bytes from the starting file to the ending (target) file
        try {
            //create new file object
            File end = new File(getPath(), new_name);

            try {
                end.createNewFile();
            }
            catch(Exception e){
                System.out.println("File name already exists");
            }
            
            //create file input stream object in order to be able to copy all types of files like images 
            FileInputStream start_file = new FileInputStream(getPath() + "\\" + file_name_param);
            FileOutputStream end_file = new FileOutputStream(end);

            //read contents of file and write them into the new file
            int i = 0;
            while((i = start_file.read()) != -1) {
                end_file.write(i);
            }

            //close the file objects
            start_file.close();
            end_file.close();

            //after copying the file, if a file name was given, rename the copy into the file name given as a parameter
            if(!(new_name_param == null)) {
                renameFile(new_name, new_name_param);
            }
        }
        catch(FileNotFoundException fnfe) {
            System.out.println("File not found error fnfe");
        }
        catch(NullPointerException npe) {
            System.out.println("File not found npe");
        }
        catch (Exception e) {
            System.out.println(e);
        }
        return new_name;
    }

    /**
     * Moves a file to a new directory, handling name conflicts.
     * If either the file name or target path is not provided, the user will be prompted to enter them.
     * In case of name conflicts, the method creates a copy with a unique name.
     *
     * @param file_name The name of the file to move
     * @param target_path The destination directory path
     * @throws NoSuchFileException if the source file or target path does not exist
     * @throws FileAlreadyExistsException if a file with the same name exists in the target directory
     */
    public static void move(String file_name, String target_path) {
        //if no file name value is given as parameter
        if (file_name == null) {
            System.out.print("Enter file name: ");
            file_name = inputFunction();
        }
        
        //if no path value is given as parameter
        if (target_path == null) {
            System.out.print("Enter path to move the file into: ");
            target_path = inputFunction();
        }
        
        try {
            //combine current file path with file name to get complete file path
            String file_name_path = getPath() + "\\" + file_name;
            
            //create a path object and move files from starting path into target path
            Files.move(Paths.get(file_name_path), Paths.get(target_path + "\\" + file_name));
        }
        catch(java.nio.file.NoSuchFileException nsfe) {
            System.out.println("file or path not found error");
        }
        catch(java.nio.file.FileAlreadyExistsException faee) {
            String file_copy_name = copy(file_name, null);
            move(file_copy_name, target_path);
            delFile(file_name);
        }
        catch(Exception e) {
            System.out.println(e);
        }
    }
} 