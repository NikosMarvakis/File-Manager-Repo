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
	private static final String EXIT_COMMAND = "exit";

	/**
	 * A map of command codes to their corresponding string representations.
	 * This map is used to validate user input and dispatch commands.
	 */
	private static final Map<String, String> COMMAND_CODE_TO_STRING_MAP = createCommandMap();

	/**
	 * An instance of {@link DirectoryOperations} for performing directory operations.
	 */
	private static final DirectoryOperations directoryOperationsInstance = new DirectoryOperations();

	/**
	 * An instance of {@link FileOperations} for performing file operations.
	 */
	private static final FileOperations fileOperationsInstance = new FileOperations();

	/**
	 * An instance of {@link InputUtils} for handling user input.
	 */
	private static final InputUtils inputUtilsInstance = new InputUtils();

	/**
	 * An instance of {@link PathUtils} for handling file paths.
	 */
	private static final PathUtils pathUtilsInstance = new PathUtils();

	/**
	 * The main method that serves as the application's entry point.
	 * It displays available commands, then enters a loop to process user input until
	 * the user enters the "exit" command.
	 *
	 * @param args Command-line arguments (not used).
	 */
	public static void main(String[] args) {
		System.out.println("Welcome to the File Manager!");
		System.out.println("Type 'info' to see the list of available commands.");
		System.out.println("Type 'exit' at any time to close the application.");
		System.out.println();
		InputUtils.displayAvailableCommands();
		System.out.println();

		String[] parsedUserInputArguments = new String[4];
		
		// System.out.print("Enter a command (use '>' for multiple input): ");
		String rawUserInput = promptAndValidateUserInput();
		
		while (!rawUserInput.equals(EXIT_COMMAND)) {
			processUserInput(rawUserInput, parsedUserInputArguments);
			// System.out.print("Enter a command (use '>' for multiple input): ");
			rawUserInput = promptAndValidateUserInput();
		}
		System.out.println("Thank you for using the File Manager. Goodbye!");
	}

	/**
	 * Creates and returns a map of command codes to their string representations.
	 *
	 * @return A map containing command codes as keys and their corresponding command strings as values.
	 */
	private static Map<String, String> createCommandMap() {
		Map<String, String> commandMap = new HashMap<>();
		commandMap.put("LIST", "list");
		commandMap.put("PATH", "path");
		commandMap.put("INFO", "info");
		commandMap.put("CHDIR", "chdir");
		commandMap.put("PREVDIR", "prevdir");
		commandMap.put("MAKE_FILE", "make file");
		commandMap.put("DELETE_FILE", "delete file");
		commandMap.put("RENAME_FILE", "rename file");
		commandMap.put("READ_FILE", "read file");
		commandMap.put("WRITE_FILE", "write file");
		commandMap.put("CLEAR_FILE", "clear file");
		commandMap.put("COPY_FILE", "copy file");
		commandMap.put("MOVE_FILE", "move file");
		commandMap.put("MAKE_DIR", "make dir");
		commandMap.put("DELETE_DIR", "delete dir");
		commandMap.put("RENAME_DIR", "rename dir");
		commandMap.put("MOVE_DIR", "move dir");
		return commandMap;
	}

	/**
	 * Prompts the user for input and validates the entered command.
	 * The method continues to prompt until a valid command or the exit command is entered.
	 *
	 * @return The validated user input string.
	 */
	private static String promptAndValidateUserInput() {
		while (true) {
			String userInput = InputUtils.readUserInput("Enter a command (use '>' for multiple input): ");
			String userCommand = userInput.split(">")[0].trim();
			if (COMMAND_CODE_TO_STRING_MAP.containsValue(userCommand) || userCommand.equals(EXIT_COMMAND)) {
				return userInput;
			}
			System.out.println("⚠️  Unrecognized command. Please enter a valid command or type 'info' for help.");
			// userInput = InputUtils.readUserInput("Enter a command (use '>' for multiple input): ");
		}
	}

	/**
	 * Processes the user input by splitting it into command and arguments.
	 * The arguments array is padded with null values if the input contains fewer arguments
	 * than expected.
	 *
	 * @param rawUserInput The raw user input string.
	 * @param parsedUserInputArguments An array to store the split and padded input values.
	 */
	private static void processUserInput(String rawUserInput, String[] parsedUserInputArguments) {
		String[] splitInputArguments = rawUserInput.split(">");
		for (int i = 0; i < splitInputArguments.length; i++) {
			parsedUserInputArguments[i] = splitInputArguments[i].trim();
		}
		for (int i = splitInputArguments.length; i < parsedUserInputArguments.length; i++) {
			parsedUserInputArguments[i] = null;
		}
		executeParsedCommand(parsedUserInputArguments);
	}

	/**
	 * Executes the command specified in the {@code parsedUserInputArguments} array.
	 * This method dispatches the command to the appropriate operation based on the command code.
	 *
	 * @param parsedUserInputArguments An array containing the command and its arguments.
	 */
	private static void executeParsedCommand(String[] parsedUserInputArguments) {
		String userCommandString = parsedUserInputArguments[0];
		String resolvedCommandCode = resolveCommandCodeFromString(userCommandString);

		if (resolvedCommandCode == null) return;

		switch (resolvedCommandCode) {
			case "LIST":
				DirectoryOperations.listDir();
				break;
			case "PATH":
				System.out.println("Current working directory: " + PathUtils.getCurrentWorkingDirectory());
				break;
			case "INFO":
				InputUtils.displayAvailableCommands();
				System.out.println();
				break;
			case "CHDIR":
				System.out.println("Changing directory...");
				PathUtils.changeWorkingDirectory(parsedUserInputArguments[1]);
				break;
			case "PREVDIR":
				System.out.println("Moving to parent directory...");
				PathUtils.changeToParentDirectory();
				break;
			case "MAKE_FILE":
				System.out.println("Creating new file...");
				FileOperations.newFile(parsedUserInputArguments[1]);
				break;
			case "DELETE_FILE":
				System.out.println("Deleting file...");
				FileOperations.delFile(parsedUserInputArguments[1]);
				break;
			case "RENAME_FILE":
				System.out.println("Renaming file...");
				FileOperations.renameFile(parsedUserInputArguments[1], parsedUserInputArguments[2]);
				break;
			case "READ_FILE":
				System.out.println("Reading file...");
				FileOperations.readFile(parsedUserInputArguments[1]);
				break;
			case "WRITE_FILE":
				System.out.println("Writing to file...");
				FileOperations.writeFile(parsedUserInputArguments[1], parsedUserInputArguments[2]);
				break;
			case "CLEAR_FILE":
				System.out.println("Clearing file contents...");
				FileOperations.clearFile(parsedUserInputArguments[1]);
				break;
			case "COPY_FILE":
				System.out.println("Copying file...");
				FileOperations.copy(parsedUserInputArguments[1], parsedUserInputArguments[2]);
				break;
			case "MOVE_FILE":
				System.out.println("Moving file...");
				FileOperations.move(parsedUserInputArguments[1], parsedUserInputArguments[2]);
				break;
			case "MAKE_DIR":
				System.out.println("Creating new directory...");
				DirectoryOperations.newDir(parsedUserInputArguments[1]);
				break;
			case "DELETE_DIR":
				System.out.println("Deleting directory...");
				DirectoryOperations.delDir(parsedUserInputArguments[1]);
				break;
			case "RENAME_DIR":
				System.out.println("Renaming directory...");
				DirectoryOperations.renameDir(parsedUserInputArguments[1], parsedUserInputArguments[2]);
				break;
			case "MOVE_DIR":
				System.out.println("Moving directory...");
				DirectoryOperations.moveDir(parsedUserInputArguments[1], parsedUserInputArguments[2]);
				break;
		}
	}

	/**
	 * Retrieves the command code associated with the given command string.
	 *
	 * @param userCommandString The command string entered by the user.
	 * @return The command code if found in the {@link #COMMAND_CODE_TO_STRING_MAP} map; otherwise, {@code null}.
	 */
	private static String resolveCommandCodeFromString(String userCommandString) {
		for (Map.Entry<String, String> entry : COMMAND_CODE_TO_STRING_MAP.entrySet()) {
			if (entry.getValue().equals(userCommandString)) {
				return entry.getKey();
			}
		}
		return null;
	}
}
