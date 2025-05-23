package file_manager.utils;

import java.util.Scanner;

/**
 * Utility class for handling user input and command display in the File Manager application.
 * Provides methods for reading user input and displaying available commands.
 *
 * @author Nikolaos Marvakis
 * @version 1.0
 */
public class InputUtils {
    private static final Scanner SCANNER = new Scanner(System.in);

    /**
     * Reads a line of input from the user via the command line.
     *
     * @return String containing the user's input
     */
    public static String inputFunction() {
        return SCANNER.nextLine();
    }

    /**
     * Displays a formatted menu of all available commands in the File Manager application.
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
