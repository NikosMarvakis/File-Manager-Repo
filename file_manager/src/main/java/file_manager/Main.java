package file_manager;

import file_manager.operations.DirectoryOperations;
import file_manager.operations.FileOperations;
import file_manager.utils.InputUtils;
import file_manager.utils.PathUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * The {@code Main} class is the entry point for the File Manager application.
 * It provides a command-line interface for users to manage files and directories.
 * Users can perform operations such as creating, reading, writing, moving, and deleting
 * files and directories.
 *
 * <p>The application utilizes other classes for specific functionalities:
 *   - {@link DirectoryOperations}: Handles directory-related operations.
 *   - {@link FileOperations}: Handles file-related operations.
 *   - {@link InputUtils}: Provides utility methods for handling user input.
 *   - {@link PathUtils}: Provides utility methods for handling file paths.
 *
 * @author Nikolaos Marvakis
 * @version 1.0
 */
public class Main {
	/**
	 * The command to terminate the application.
	 */
	private static final String EXIT = "exit";

	/**
	 * A map of command codes to their corresponding string representations.
	 * This map is used to validate user input and dispatch commands.
	 */
	private static final Map<String, String> COMMANDS = createCommandMap();

	/**
	 * An instance of {@link DirectoryOperations} for performing directory operations.
	 */
	private static final DirectoryOperations directoryOperations = new DirectoryOperations();

	/**
	 * An instance of {@link FileOperations} for performing file operations.
	 */
	private static final FileOperations fileOperations = new FileOperations();

	/**
	 * An instance of {@link InputUtils} for handling user input.
	 */
	private static final InputUtils inputUtils = new InputUtils();

	/**
	 * An instance of {@link PathUtils} for handling file paths.
	 */
	private static final PathUtils pathUtils = new PathUtils();

	/**
	 * The main method that serves as the application's entry point.
	 * It displays available commands, then enters a loop to process user input until
	 * the user enters the "exit" command.
	 *
	 * @param args Command-line arguments (not used).
	 */
	public static void main(String[] args) {
		InputUtils.getCommands();
		System.out.println();

		String[] paddedInput = new String[4];
		String input = getInput();

		while (!input.equals(EXIT)) {
			processInput(input, paddedInput);
			input = getInput();
		}
	}

	/**
	 * Creates and returns a map of command codes to their string representations.
	 *
	 * @return A map containing command codes as keys and their corresponding command strings as values.
	 */
	private static Map<String, String> createCommandMap() {
		Map<String, String> map = new HashMap<>();
		map.put("LIST", "list");
		map.put("PATH", "path");
		map.put("INFO", "info");
		map.put("CHDIR", "chdir");
		map.put("PREVDIR", "prevdir");
		map.put("MAKE_FILE", "make file");
		map.put("DELETE_FILE", "delete file");
		map.put("RENAME_FILE", "rename file");
		map.put("READ_FILE", "read file");
		map.put("WRITE_FILE", "write file");
		map.put("CLEAR_FILE", "clear file");
		map.put("COPY_FILE", "copy file");
		map.put("MOVE_FILE", "move file");
		map.put("MAKE_DIR", "make dir");
		map.put("DELETE_DIR", "delete dir");
		map.put("RENAME_DIR", "rename dir");
		map.put("MOVE_DIR", "move dir");
		return map;
	}

	/**
	 * Prompts the user for input and validates the entered command.
	 * The method continues to prompt until a valid command or the exit command is entered.
	 *
	 * @return The validated user input string.
	 */
	private static String getInput() {
		while (true) {
			System.out.print("Enter input (use '>' to give multiple input values at once): ");
			String input = InputUtils.inputFunction();
			String command = input.split(">")[0].trim();
			if (COMMANDS.containsValue(command) || command.equals(EXIT)) {
				return input;
			}
			System.out.println("Invalid command. Please enter a supported command.");
		}
	}

	/**
	 * Processes the user input by splitting it into command and arguments.
	 * The arguments array is padded with null values if the input contains fewer arguments
	 * than expected.
	 *
	 * @param input The raw user input string.
	 * @param paddedInput An array to store the split and padded input values.
	 */
	private static void processInput(String input, String[] paddedInput) {
		String[] splitInput = input.split(">");
		for (int i = 0; i < splitInput.length; i++) {
			paddedInput[i] = splitInput[i].trim();
		}
		for (int i = splitInput.length; i < paddedInput.length; i++) {
			paddedInput[i] = null;
		}
		executeCommand(paddedInput);
	}

	/**
	 * Executes the command specified in the {@code paddedInput} array.
	 * This method dispatches the command to the appropriate operation based on the command code.
	 *
	 * @param paddedInput An array containing the command and its arguments.
	 */
	private static void executeCommand(String[] paddedInput) {
		String command = paddedInput[0];
		String commandCode = getCommandCode(command);

		if (commandCode == null) return;

		switch (commandCode) {
			case "LIST":
				DirectoryOperations.listDir();
				break;
			case "PATH":
				System.out.println(PathUtils.getPath());
				break;
			case "INFO":
				InputUtils.getCommands();
				System.out.println();
				break;
			case "CHDIR":
				PathUtils.changeDir(paddedInput[1]);
				break;
			case "PREVDIR":
				PathUtils.prevDir();
				break;
			case "MAKE_FILE":
				FileOperations.newFile(paddedInput[1]);
				break;
			case "DELETE_FILE":
				FileOperations.delFile(paddedInput[1]);
				break;
			case "RENAME_FILE":
				FileOperations.renameFile(paddedInput[1], paddedInput[2]);
				break;
			case "READ_FILE":
				FileOperations.readFile(paddedInput[1]);
				break;
			case "WRITE_FILE":
				FileOperations.writeFile(paddedInput[1], paddedInput[2]);
				break;
			case "CLEAR_FILE":
				FileOperations.clearFile(paddedInput[1]);
				break;
			case "COPY_FILE":
				FileOperations.copy(paddedInput[1], paddedInput[2]);
				break;
			case "MOVE_FILE":
				FileOperations.move(paddedInput[1], paddedInput[2]);
				break;
			case "MAKE_DIR":
				DirectoryOperations.newDir(paddedInput[1]);
				break;
			case "DELETE_DIR":
				DirectoryOperations.delDir(paddedInput[1]);
				break;
			case "RENAME_DIR":
				DirectoryOperations.renameDir(paddedInput[1], paddedInput[2]);
				break;
			case "MOVE_DIR":
				DirectoryOperations.moveDir(paddedInput[1], paddedInput[2]);
				break;
		}
	}

	/**
	 * Retrieves the command code associated with the given command string.
	 *
	 * @param command The command string entered by the user.
	 * @return The command code if found in the {@link #COMMANDS} map; otherwise, {@code null}.
	 */
	private static String getCommandCode(String command) {
		for (Map.Entry<String, String> entry : COMMANDS.entrySet()) {
			if (entry.getValue().equals(command)) {
				return entry.getKey();
			}
		}
		return null;
	}
}
