package file_manager.utils;

import java.util.Scanner;

/**
 * Utility class for handling user input and command display in the File Manager application.
 * This class provides methods for reading user input from the command line and displaying
 * the available commands in a formatted menu.
 *
 * @author Nikolaos Marvakis
 * @version 1.0
 */
public class InputUtils {
    
    /**
     * Reads a line of input from the user via the command line.
     * This method creates a Scanner object to read from System.in,
     * captures the next line of input, and properly closes the scanner.
     *
     * @return String containing the user's input
     */
    public static String inputFunction() {
        //create scanner object
        Scanner scan_obj = new Scanner(System.in); 
        //take the next line as input
        String user_input = scan_obj.nextLine();

        scan_obj.close();
        
        return user_input;        
    }

    /**
     * Displays a formatted menu of all available commands in the File Manager application.
     * The commands are displayed in a table format with three columns and a width of 20 characters each.
     * The menu is surrounded by separator lines for better visibility.
     * 
     * Available commands include:
     * - File operations: make file, delete file, rename file, read file, write file, clear file, copy file, move file
     * - Directory operations: make dir, delete dir, rename dir, move dir
     * - Path operations: list, path, chdir, prevdir
     * - System operations: exit, info
     */
    public static void getCommands() {
        String[] commands = new String[] {
            "exit", "list", "path", "info",
            "chdir", "prevdir", "make file",
            "delete file", "rename file", "read file",
            "write file", "clear file", "copy file",
            "move file", "make dir", "delete dir", 
            "rename dir", "move dir"
        };

        int separatorLength = 60;
        String separator = "=".repeat(separatorLength);

        System.out.println(separator);
        System.out.println("Available Commands:");
        System.out.println(separator);

        int columns = 3;
        int colWidth = 20;

        for (int i = 0; i < commands.length; i++) {
            System.out.printf("%-" + colWidth + "s", commands[i]);
            if ((i + 1) % columns == 0 || i == commands.length - 1) {
                System.out.println();
            }
        }

        System.out.println(separator);
    }
} 