package file_manager;

import static file_manager.operations.DirectoryOperations.delDir;
import static file_manager.operations.DirectoryOperations.listDir;
import static file_manager.operations.DirectoryOperations.moveDir;
import static file_manager.operations.DirectoryOperations.newDir;
import static file_manager.operations.DirectoryOperations.renameDir;
import static file_manager.operations.FileOperations.clearFile;
import static file_manager.operations.FileOperations.copy;
import static file_manager.operations.FileOperations.delFile;
import static file_manager.operations.FileOperations.move;
import static file_manager.operations.FileOperations.newFile;
import static file_manager.operations.FileOperations.readFile;
import static file_manager.operations.FileOperations.renameFile;
import static file_manager.operations.FileOperations.writeFile;
import static file_manager.utils.InputUtils.getCommands;
import static file_manager.utils.InputUtils.inputFunction;
import static file_manager.utils.PathUtils.changeDir;
import static file_manager.utils.PathUtils.getPath;
import static file_manager.utils.PathUtils.prevDir;

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
	private static final String EXIT = "exit";
	private static final String LIST = "list";
	private static final String PATH = "path";
	private static final String INFO = "info";
	private static final String CHDIR = "chdir";
	private static final String PREVDIR = "prevdir";
	private static final String MAKE_FILE = "make file";
	private static final String DELETE_FILE = "delete file";
	private static final String RENAME_FILE = "rename file";
	private static final String READ_FILE = "read file";
	private static final String WRITE_FILE = "write file";
	private static final String CLEAR_FILE = "clear file";
	private static final String COPY_FILE = "copy file";
	private static final String MOVE_FILE = "move file";
	private static final String MAKE_DIR = "make dir";
	private static final String DELETE_DIR = "delete dir";
	private static final String RENAME_DIR = "rename dir";
	private static final String MOVE_DIR = "move dir";


	/**
	 * @param args
	 * @author Nikolaos Marvakis
	 * @version 1.0
	 * <p>This App is designed to manage different File, Directory and Path functions<p>
	 */

	/**
	 * The entry point of the File Manager application.
	 * <p>
	 * This method initializes the application and enters a command processing loop.
	 * It displays available commands and continuously processes user input until
	 * the exit command is received. The application supports various file and directory
	 * operations through a simple command-line interface.
	 * </p>
	 * <p>
	 * The input format supports multiple parameters separated by '>' character,
	 * allowing for complex operations with multiple arguments.
	 * </p>
	 *
	 * @param args Command-line arguments (not used in this application)
	 */
	public static void main(String[] args) {		
		// Show possible commands to choose from
		getCommands();
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

	/**
	 * Prompts the user for input and retrieves the entered command.
	 * <p>
	 * This method displays a prompt message indicating that multiple input values
	 * can be provided using the '>' character as a separator. It delegates the actual
	 * input retrieval to the inputFunction() method.
	 * </p>
	 *
	 * @return String containing the user's input command and parameters
	 */
	private static String getInput() {
		System.out.print("Enter input (use '>' to give multiple input values at once): ");
		return inputFunction();
	}

	/**
	 * Processes the user input by splitting it into command and parameters.
	 * <p>
	 * This method takes the raw input string, splits it into components using the '>'
	 * delimiter, and prepares it for command execution. It handles up to 4 parameters
	 * for each command.
	 * </p>
	 *
	 * @param input The raw input string from the user
	 * @param padded_input Array to store the processed command and parameters
	 */
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

	/**
	 * Executes the command based on the processed input parameters.
	 * <p>
	 * This method interprets the command and its parameters, then performs the
	 * corresponding file or directory operation. It supports various operations
	 * including:
	 * </p>
	 * <ul>
	 *     <li>File operations: create, delete, rename, read, write, clear, copy, move</li>
	 *     <li>Directory operations: create, delete, rename, move</li>
	 *     <li>Path operations: list contents, show current path, change directory</li>
	 *     <li>System operations: show available commands, exit application</li>
	 * </ul>
	 *
	 * @param padded_input Array containing the command and its parameters
	 */
	private static void executeCommand(String[] padded_input) {
		String command = padded_input[0];
		
		switch(command) {
			//shows the files in the given path
			case LIST:
				listDir();
				break;

			//shows the current path
			case PATH:
				System.out.println(getPath());
				break;

			//shows the available commands to choose from
			case INFO:
				getCommands();
				System.out.println();
				break;

			//change the current directory with another one
			case CHDIR:
				changeDir(padded_input[1]);
				break;

			//go one directory outside
			case PREVDIR:
				prevDir();
				break;

			//create a new file
			case MAKE_FILE:
				newFile(padded_input[1]);
				break;

			//delete a file
			case DELETE_FILE:
				delFile(padded_input[1]);
				break;
			
			//rename a file
			case RENAME_FILE:
				renameFile(padded_input[1], padded_input[2]);
				break;
				
			//read a file
			case READ_FILE:
				readFile(padded_input[1]);
				break;
				
			//write into a file
			case WRITE_FILE:
				writeFile(padded_input[1], padded_input[2]);
				break;
				
			//clear a file
			case CLEAR_FILE:
				clearFile(padded_input[1]);
				break;
				
			//copy a file
			case COPY_FILE:
				copy(padded_input[1], padded_input[2]);
				break;
				
			//move a file
			case MOVE_FILE:
				move(padded_input[1], padded_input[2]);
				break;	
				
			//create a new directory (folder)
			case MAKE_DIR:
				newDir(padded_input[1], padded_input[2]);
				break;
				
			//delete a directory (folder)
			case DELETE_DIR:
				delDir(padded_input[1]);
				break;
				
			//rename a directory / folder
			case RENAME_DIR:
				renameDir(padded_input[1], padded_input[2]);
				break;
				
			//move a folder to another path / location
			case MOVE_DIR:
				moveDir(padded_input[1], padded_input[2]);
				break;
		}
	}
}
