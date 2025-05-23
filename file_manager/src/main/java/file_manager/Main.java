package file_manager;

// Remove static imports and use class instances instead

import file_manager.operations.DirectoryOperations;
import file_manager.operations.FileOperations;
import file_manager.utils.InputUtils;
import file_manager.utils.PathUtils;

/**
 * The Main class serves as the entry point for the File Manager application.
 * This application provides a command-line interface for managing files and directories,
 * offering various operations such as creating, reading, writing, moving, and deleting files
 * and directories.
 *
 * @author Nikolaos Marvakis
 * @version 1.0
 */
public class Main {
	// Constants for command strings
	// Command constants as keys for the command map
	private static final String EXIT = "exit";

	// Command map: command code -> command string
	private static final java.util.Map<String, String> COMMANDS = new java.util.HashMap<>();
	static {
		COMMANDS.put("LIST", "list");
		COMMANDS.put("PATH", "path");
		COMMANDS.put("INFO", "info");
		COMMANDS.put("CHDIR", "chdir");
		COMMANDS.put("PREVDIR", "prevdir");
		COMMANDS.put("MAKE_FILE", "make file");
		COMMANDS.put("DELETE_FILE", "delete file");
		COMMANDS.put("RENAME_FILE", "rename file");
		COMMANDS.put("READ_FILE", "read file");
		COMMANDS.put("WRITE_FILE", "write file");
		COMMANDS.put("CLEAR_FILE", "clear file");
		COMMANDS.put("COPY_FILE", "copy file");
		COMMANDS.put("MOVE_FILE", "move file");
		COMMANDS.put("MAKE_DIR", "make dir");
		COMMANDS.put("DELETE_DIR", "delete dir");
		COMMANDS.put("RENAME_DIR", "rename dir");
		COMMANDS.put("MOVE_DIR", "move dir");
	}

	// Create instances of the utility and operation classes
	private static final DirectoryOperations directoryOperations = new DirectoryOperations();
	private static final FileOperations fileOperations = new FileOperations();
	private static final InputUtils inputUtils = new InputUtils();
	private static final PathUtils pathUtils = new PathUtils();


	public static void main(String[] args) {
		// Show possible commands to choose from
		InputUtils.getCommands();
		System.out.println();
		
		// Initialize input handling
		String input = getInput();
		String[] padded_input = new String[4];  // Array for command parameters

		// Main program loop
		while (!input.equals(EXIT)) {
			// Process input and execute command
			processInput(input, padded_input);
			
			// Get next input
			input = getInput();
		}
	}

	private static String getInput() {
		String input;
		while (true) {
			System.out.print("Enter input (use '>' to give multiple input values at once): ");
			input = InputUtils.inputFunction();
			String command = input.split(">")[0].trim();
			if (COMMANDS.containsValue(command) || command.equals(EXIT)) {
				break;
			} else {
				System.out.println("Invalid command. Please enter a supported command.");
			}
		}
		return input;
	}

	private static void processInput(String input, String[] padded_input) {
		// Split and clean input
		String[] split_input = input.split(">");
		for(int i = 0; i < split_input.length; i++) {
			split_input[i] = split_input[i].trim();
		}

		// Copy to padded array
		System.arraycopy(split_input, 0, padded_input, 0, split_input.length);

		// Execute command
		executeCommand(padded_input);

		// Clear padded array
		for(int i = 0; i < padded_input.length; i++) {
			padded_input[i] = null;
		}
	}

	private static void executeCommand(String[] padded_input) {
		String command = padded_input[0];

		// Find the command code from the hashmap
		String commandCode = null;
		for (java.util.Map.Entry<String, String> entry : COMMANDS.entrySet()) {
			if (entry.getValue().equals(command)) {
				commandCode = entry.getKey();
				break;
			}
		}
		if (commandCode == null) {
			// Unknown command, do nothing or print error
			return;
		}

		switch(commandCode) {
			//shows the files in the given path
			case "LIST":
				DirectoryOperations.listDir();
				break;

			//shows the current path
			case "PATH":
				System.out.println(PathUtils.getPath());
				break;

			//shows the available commands to choose from
			case "INFO":
				InputUtils.getCommands();
				System.out.println();
				break;

			//change the current directory with another one
			case "CHDIR":
				PathUtils.changeDir(padded_input[1]);
				break;

			//go one directory outside
			case "PREVDIR":
				PathUtils.prevDir();
				break;

			//create a new file
			case "MAKE_FILE":
				FileOperations.newFile(padded_input[1]);
				break;

			//delete a file
			case "DELETE_FILE":
				FileOperations.delFile(padded_input[1]);
				break;
			
			//rename a file
			case "RENAME_FILE":
				FileOperations.renameFile(padded_input[1], padded_input[2]);
				break;
				
			//read a file
			case "READ_FILE":
				FileOperations.readFile(padded_input[1]);
				break;
				
			//write into a file
			case "WRITE_FILE":
				FileOperations.writeFile(padded_input[1], padded_input[2]);
				break;
				
			//clear a file
			case "CLEAR_FILE":
				FileOperations.clearFile(padded_input[1]);
				break;
				
			//copy a file
			case "COPY_FILE":
				FileOperations.copy(padded_input[1], padded_input[2]);
				break;
				
			//move a file
			case "MOVE_FILE":
				FileOperations.move(padded_input[1], padded_input[2]);
				break;	
				
			//create a new directory (folder)
			case "MAKE_DIR":
				DirectoryOperations.newDir(padded_input[1]);
				break;
				
			//delete a directory (folder)
			case "DELETE_DIR":
				DirectoryOperations.delDir(padded_input[1]);
				break;
				
			//rename a directory / folder
			case "RENAME_DIR":
				DirectoryOperations.renameDir(padded_input[1], padded_input[2]);
				break;
				
			//move a folder to another path / location
			case "MOVE_DIR":
				DirectoryOperations.moveDir(padded_input[1], padded_input[2]);
				break;
		}
	}
}
