package File_Editor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Functions {
	
	static void get_commands() {
		//available commands to choose from
		String[] commands = new String[] {"exit", "list", "path", "info",
											"chdir", "prevdir", "make file",
											"delete file", "rename file", "read", 
											"write file", "clear file", "copy file",
											"move file", "make dir", "delete dir", 
											"rename dir", "move dir"};

		int separator_length = 70;
		for(int i = 0; i < separator_length; i++) {
			System.out.print("=");
		}

		System.out.println();

		//show available commands to choose from
		for (int i = 0; i < commands.length; i++) {
			if (i % 6 == 0 & i > 1) {
				System.out.println(commands[i]);
			}
			else {
				System.out.print(commands[i]);
				if (i < commands.length - 1) {
					System.out.print(", ");
				}
			}
		}
		
		System.out.println();
		for(int i = 0; i < separator_length; i++) {
			System.out.print("=");
		}
	}

	static String get_path() {
		//return the currently running directory
		return System.getProperty("user.dir");
	}

	static String input_function() {
		//create scanner object
		Scanner scan_obj = new Scanner(System.in); 
		//take the next line as input
		String user_input = scan_obj.nextLine();
		return user_input;		
	}
	
	static void list_dir() {
		//create file object
		File f = new File(get_path());
		
		//turn the files names into a list of names
		String[] all_files = f.list();
		for (int i = 0; i < all_files.length; i++) {
			System.out.println(all_files[i]);
		}
	}

	static void change_dir(String path) {
		//if path value is not given as parameter
		if (path == null) {
			System.out.print("Enter path: ");
			path = input_function();
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

	static void prev_dir() {
		//split the initial directory into separate parts, split (\) values
		String[] current_dir_split = get_path().toString().split("\\\\");
		
		StringBuilder new_path = new StringBuilder();
		//reconnect the previously split parts into new path without the last part
		for (int i = 0; i < current_dir_split.length - 1; i++) {
			new_path.append(current_dir_split[i] + "/");
		}
		change_dir(new_path.toString());
	}

	static void new_file(String name) {
		//in case no name is given as a parameter
		if (name == null) {
			System.out.print("enter file name: ");
			name = input_function();
		}

		//create new file object
		File file1 = new File(get_path(), name);

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

	static void clear_file(String file_name) {
		//if no value is given in parameter
		if (file_name == null) {
			System.out.print("Enter file name: ");
			file_name = input_function();
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
	
	static void del_file(String name) {
		//if no name value is given as parameter
		if (name == null) {
			System.out.print("Enter input name: ");
			name = input_function();
		}
		
		//clear the file before deleting it
		clear_file(name);
		
		//create a new file object
		File f = new File(name);
		
		//if the got deleted
		if (f.delete() == true) {
			System.out.println("File deleted");
		}
		//if the file did not get deleted
		else {
			System.out.println("failed to delete file");
		}
		
	}

	static void rename_file(String file_name, String new_name) {
		
		//if no file is given in parameters
		if (file_name == null) {
			System.out.print("Enter file name: ");
			file_name = input_function();
		}
		
		//if no new file name is given in parameters
		if (new_name == null) {
			System.out.print("Enter new file name: ");
			new_name = input_function();
		}
		
		//get the paths of the separate files  
		String start_path = get_path() + "\\" + file_name;
		String end_path = get_path() + "\\" + new_name;
		
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

	static void read_file(String file_name) {
		
		//if no file name is given as parameter
		if (file_name == null) {
			System.out.print("Enter file name: ");
			file_name = input_function();
		}
		
		//if the file is found
		try {
			//create bufferReader object
			BufferedReader reader = new BufferedReader(new FileReader(file_name));
			
			//while there are lines to read continue and read them
			String line;
			while((line = reader.readLine()) != null) {
				System.out.println(line);
			}
			reader.close();
		}
		//in case the file cant be found
		catch (Exception e) {
			System.out.println("file not found");
		}
	}

	static void write_file(String file_name, String text) {
		
		if (file_name == null) {
			System.out.print("Enter file name: ");
			file_name = input_function();
		}
		
		if (text == null) {
			System.out.print("Enter text: ");
			text = input_function();
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

	static String copy(String file_name_param, String new_name_param) {

		//creating the file in case the copied file name is given as a parameter or not
		
		//in case no file name is given as a parameter
		if (file_name_param == null) {
			System.out.print("Enter file name: ");
			file_name_param = input_function();
		}

		//copy the new name to use as a object
		String new_name = new_name_param;
		
		//in case no name is given for the copy file
		if (new_name_param == null){
			
			//split the starting file name into name and extension (text111, .txt)
			String[] split_file_name_param = file_name_param.split("\\.");
						
			//create the file object for this file 
			File start_file_obj = new File(get_path(), split_file_name_param[0] + "." + split_file_name_param[1]);
			
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
				start_file_obj = new File(get_path() + "\\" + new_name);
				i++;
			}
		}
		//in case copy file name is given as a parameter
		else {
			File new_name_obj = new File(get_path(), new_name_param);
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
			File end = new File(get_path(), new_name);

			try {
				end.createNewFile();
			}
			catch(Exception e){
				System.out.println("File name already exists");
			}
			
			//create file input stream object in order to be able to copy all types of files like images 
			FileInputStream start_file = new FileInputStream(get_path() + "\\" + file_name_param);
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
				rename_file(new_name, new_name_param);
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

	static void move(String file_name, String target_path) {
		
		//if no file name value is given as a parameter
		if (file_name == null) {
			System.out.print("Enter file name: ");
			file_name = input_function();
		}
		
		//if no path value is given as a parameter
		if (target_path == null) {
			System.out.print("Enter path to move the file into: ");
			target_path = input_function();
		}
		
		try {
			//combine current file path with file name to get complete file path
			String file_name_path = get_path() + "\\" + file_name;
			
			//create a path object and move files from starting path into target path
			Files.move(Paths.get(file_name_path), Paths.get(target_path + "\\" + file_name));
		}
		
		catch(java.nio.file.NoSuchFileException nsfe) {
			System.out.println("file or path not found error");
		}
		
		catch(java.nio.file.FileAlreadyExistsException faee) {
			String file_copy_name = copy(file_name, null);
			move(file_copy_name, target_path);
			del_file(file_name);

		}

		catch(Exception e) {
			System.out.println(e);
		}
	}

	static void new_dir(String dir_name, String path) {
		
		if (path == null) {
			System.out.print("Enter path: ");
			path = input_function();
		}

		if (path.equals("current")) {
			path = get_path();
		}
		
		if(dir_name == null) {
			System.out.print("enter folder name: ");
			dir_name = input_function();
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

	static void del_dir(String path) {
		
		if (path == null) {
			System.out.print("enter path to folder to delete: ");
			path = input_function();
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
						del_dir(file.toString());
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

	static void rename_dir(String starting_string, String target_string) {
		
		//if no value is given for starting folder path (original folder name / path)
		if(starting_string == null) {
			System.out.print("enter starting folder name: ");
			starting_string = input_function();
		}
		
		//if no value is given for target path (new name of the folder/directory)
		if(target_string == null) {
			System.out.print("enter target folder name: ");
			target_string = input_function();
		}
		
		
		//turn the string inputs into path objects
		Path starting_path = Paths.get(get_path() + "\\" + starting_string);
		Path target_path = Paths.get(get_path() + "\\" + target_string);

		
		//create file objects from the paths
		File starting_path_file = new File(get_path() + "\\" + starting_string);
		File target_path_file = new File(get_path() + "\\" + target_string);
		
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

	static void move_dir(String starting_path, String target_path) {

		//in case no starting path name is given as parameter
		if(starting_path == null) {
			System.out.print("enter starting path: ");
			starting_path = input_function();
		}
		
		//in case no target path name is given as a parameter
		if(target_path == null) {
			System.out.print("enter target path: ");
			target_path = input_function();
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
