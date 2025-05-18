package file_manager.utils;

import java.util.Scanner;

public class InputUtils {
    
    // Function to handle the user input from the Command line
    public static String inputFunction() {
        //create scanner object
        Scanner scan_obj = new Scanner(System.in); 
        //take the next line as input
        String user_input = scan_obj.nextLine();
        return user_input;        
    }

    // Function to show the menu of possible supported options
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