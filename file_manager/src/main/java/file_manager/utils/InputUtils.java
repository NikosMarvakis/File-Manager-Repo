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
    private static final Scanner SCANNER = new Scanner(System.in);

    /**
     * Reads a line of input from the user via the command line.
     * <p>
     * This method waits for the user to enter a line of text and returns it as a {@code String}.
     * It is typically used to capture commands or other input required by the File Manager application.
     * </p>
     *
     * @return a {@code String} containing the user's input from the console
     */
    public static String inputFunction() {
        return SCANNER.nextLine();
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
    public static void getCommands() {
        String[] commands = {
            "exit", "list", "path", "info",
            "chdir", "prevdir", "make file",
            "delete file", "rename file", "read file",
            "write file", "clear file", "copy file",
            "move file", "make dir", "delete dir", 
            "rename dir", "move dir"
        };

        final int SEPARATOR_LENGTH = 60;
        final String SEPARATOR = "=".repeat(SEPARATOR_LENGTH);
        final int COLUMNS = 3;
        final int COL_WIDTH = 20;

        System.out.println(SEPARATOR);
        System.out.println("Available Commands:");
        System.out.println(SEPARATOR);

        for (int i = 0; i < commands.length; i++) {
            System.out.printf("%-" + COL_WIDTH + "s", commands[i]);
            if ((i + 1) % COLUMNS == 0 || i == commands.length - 1) {
                System.out.println();
            }
        }

        System.out.println(SEPARATOR);
    }
}
