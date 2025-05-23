package file_manager.utils;

import java.util.Scanner;

/**
 * <h1>InputUtils</h1>
 * <p>
 *   The {@code InputUtils} class provides utility methods for handling user input
 *   and displaying available commands in the File Manager application.
 * </p>
 * <p>
 *   This class encapsulates a {@link Scanner} instance for reading input from the console
 *   and offers methods to retrieve user input and present a formatted list of commands.
 * </p>
 *
 * @author Nikolaos Marvakis
 * @version 1.0
 */
public class InputUtils {
    private static final Scanner CONSOLE_SCANNER = new Scanner(System.in);

    /**
     * Reads a line of input from the user via the command line.
     * <p>
     * This method waits for the user to enter a line of text and returns it as a {@code String}.
     * It is typically used to capture commands or other input required by the File Manager application.
     * </p>
     * @param userMessage the message to display to the user before reading input
     * @return a {@code String} containing the user's input from the console
     */
    public static String readUserInput(String userMessage) {
        System.out.print(userMessage);
        return CONSOLE_SCANNER.nextLine();
    }

    /**
     * Displays a formatted menu of all available commands in the File Manager application.
     * <p>
     * This method prints a list of supported commands to the console, organized in columns for readability.
     * It helps users understand the available operations they can perform within the application.
     * </p>
     * <p>
     * The commands include file and directory operations such as creating, deleting, renaming, moving,
     * reading, and writing files or directories, as well as navigation commands.
     * </p>
     */
    public static void displayAvailableCommands() {
        String[] availableCommands = {
            "exit", "list", "path", "info",
            "chdir", "prevdir", "make file",
            "delete file", "rename file", "read file",
            "write file", "clear file", "copy file",
            "move file", "make dir", "delete dir", 
            "rename dir", "move dir"
        };

        final int COMMAND_MENU_WIDTH = 60;
        final String COMMAND_MENU_SEPARATOR = "=".repeat(COMMAND_MENU_WIDTH);
        final int COMMANDS_PER_ROW = 3;
        final int COMMAND_COLUMN_WIDTH = 20;

        System.out.println(COMMAND_MENU_SEPARATOR);
        System.out.println("Available commands:");
        System.out.println(COMMAND_MENU_SEPARATOR);

        for (int commandIndex = 0; commandIndex < availableCommands.length; commandIndex++) {
            System.out.printf("%-" + COMMAND_COLUMN_WIDTH + "s", availableCommands[commandIndex]);
            if ((commandIndex + 1) % COMMANDS_PER_ROW == 0 || commandIndex == availableCommands.length - 1) {
                System.out.println();
            }
        }

        System.out.println(COMMAND_MENU_SEPARATOR);
    }
}
