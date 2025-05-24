package file_manager.operations;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

import static file_manager.utils.InputUtils.readUserInput;
import static file_manager.utils.PathUtils.getCurrentWorkingDirectory;

/**
 * Utility class for performing file operations such as creation, deletion,
 * renaming, reading, writing, copying, and moving files within the File Manager application.
 * <p>
 * All methods handle user input validation and error reporting, and prompt the user
 * for missing parameters if null is provided.
 * </p>
 * <p>
 * This class is not intended to be instantiated.
 * </p>
 *
 * @author Nikolaos Marvakis
 * @version 1.0
 */
public class FileOperations {

    private FileOperations() {
        // Private constructor to prevent instantiation
    }

    private static final DirectoryOperations directoryOperationsInstance = new DirectoryOperations();

    /**
     * Creates a new file with the specified name in the current working directory.
     * If the name is null, prompts the user to enter a file name.
     * Prints a message indicating whether the file was created or if it already exists.
     *
     * @param fileNameToCreate the name of the file to create, or null to prompt the user
     */
    public static void newFile(String fileNameToCreate) {
        fileNameToCreate = promptIfNull(fileNameToCreate, "Please enter the name for the new file: ");
        File fileToCreate = new File(getCurrentWorkingDirectory(), fileNameToCreate);
        try {
            if (fileToCreate.createNewFile()) {
                System.out.println("File created successfully: " + fileNameToCreate);
            } else {
                System.out.println("A file with this name already exists: " + fileNameToCreate);
            }
        } catch (Exception exception) {
            System.out.println("Error creating file: " + exception.getMessage());
        }
    }

    /**
     * Removes all contents from the specified file, effectively clearing it.
     * If the file name is null, prompts the user to enter a file name.
     * Prints a message if the file does not exist.
     *
     * @param fileNameToClear the name of the file to clear, or null to prompt the user
     */
    public static void clearFile(String fileNameToClear) {
        fileNameToClear = promptIfNull(fileNameToClear, "Please enter the name of the file to clear: ");
        File fileToClear = new File(getCurrentWorkingDirectory(), fileNameToClear);
        if (!fileToClear.exists()) {
            System.out.println("The specified file does not exist: " + fileNameToClear);
            return;
        }
        try (FileWriter fileWriter = new FileWriter(fileToClear)) {
            fileWriter.write("");
            System.out.println("File cleared successfully: " + fileNameToClear);
        } catch (Exception exception) {
            System.out.println("Error clearing file: " + exception.getMessage());
        }
    }

    /**
     * Deletes the specified file from the current working directory.
     * If the file name is null, prompts the user to enter a file name.
     * Prints a message indicating whether the file was deleted or if it does not exist.
     *
     * @param fileNameToDelete the name of the file to delete, or null to prompt the user
     */
    public static void delFile(String fileNameToDelete) {
        fileNameToDelete = promptIfNull(fileNameToDelete, "Please enter the name of the file to delete: ");
        File fileToDelete = new File(getCurrentWorkingDirectory(), fileNameToDelete);
        if (!fileToDelete.exists()) {
            System.out.println("The specified file does not exist: " + fileNameToDelete);
            return;
        }
        clearFile(fileNameToDelete);
        fileToDelete.delete();
        System.out.println("File deleted successfully: " + fileNameToDelete);
    }

    /**
     * Renames an existing file to a new name within the current working directory.
     * If either parameter is null, prompts the user for the missing value(s).
     * Prints a message indicating success or failure.
     *
     * @param originalFileName the current name of the file, or null to prompt the user
     * @param newFileName      the new name for the file, or null to prompt the user
     */
    public static void renameFile(String originalFileName, String newFileName) {
        originalFileName = promptIfNull(originalFileName, "Please enter the current file name: ");
        File sourceFile = new File(getCurrentWorkingDirectory(), originalFileName);
        if (!sourceFile.exists() || !sourceFile.isFile()) {
            System.out.println("The specified file does not exist: " + originalFileName);
            return;
        }
        newFileName = promptIfNull(newFileName, "Please enter the new name for the file: ");
        File destinationFile = new File(getCurrentWorkingDirectory(), newFileName);
        if (sourceFile.renameTo(destinationFile)) {
            System.out.println("File renamed successfully to: " + newFileName);
        } else {
            System.out.println("Failed to rename file: " + originalFileName);
        }
    }

    /**
     * Reads and prints the contents of the specified file to the console.
     * If the file name is null, prompts the user to enter a file name.
     * Returns the file contents as a String.
     *
     * @param fileNameToRead the name of the file to read, or null to prompt the user
     * @return the contents of the file as a String (empty if file not found)
     */
    public static String readFile(String fileNameToRead) {
        fileNameToRead = promptIfNull(fileNameToRead, "Please enter the name of the file to read: ");
        File fileToRead = new File(getCurrentWorkingDirectory(), fileNameToRead);
        StringBuilder fileContentsBuilder = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileToRead))) {
            String currentLine;
            System.out.println("Contents of " + fileNameToRead + ":");
            while ((currentLine = bufferedReader.readLine()) != null) {
                System.out.println(currentLine);
                fileContentsBuilder.append(currentLine);
            }
        } catch (Exception exception) {
            System.out.println("Unable to read file. File not found: " + fileNameToRead);
        }
        return fileContentsBuilder.toString();
    }

    /**
     * Appends the specified text to the end of the given file.
     * If the file name or text is null, prompts the user for the missing value(s).
     * Prints a message if the file does not exist.
     *
     * @param fileNameToWrite the name of the file to write to, or null to prompt the user
     * @param textToAppend    the text to append, or null to prompt the user
     */
    public static void writeFile(String fileNameToWrite, String textToAppend) {
        fileNameToWrite = promptIfNull(fileNameToWrite, "Please enter the name of the file to write to: ");
        File fileToWrite = new File(getCurrentWorkingDirectory(), fileNameToWrite);
        if (!fileToWrite.exists()) {
            System.out.println("The specified file does not exist: " + fileNameToWrite);
            return;
        }
        textToAppend = promptIfNull(textToAppend, "Please enter the text to append: ");
        int existingLineCount = countLines(fileToWrite);
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileToWrite, true))) {
            if (existingLineCount != 0) bufferedWriter.newLine();
            bufferedWriter.write(textToAppend);
            System.out.println("Text appended successfully to file: " + fileNameToWrite);
        } catch (IOException ioException) {
            System.out.println("Error writing to file: " + ioException.getMessage());
        }
    }

    /**
     * Copies the contents of the source file to a new file.
     * If the target file name is null, generates a unique name based on the source file.
     * Prompts the user for missing parameters if necessary.
     * Prints error messages for invalid paths or existing files.
     *
     * @param sourceFileName      the name of the source file to copy, or null to prompt the user
     * @param destinationFileName the desired name for the new file, or null to auto-generate
     * @return the name of the newly created file, or null if the operation failed
     */
    public static String copy(String sourceFileName, String destinationFileName) {
        sourceFileName = promptIfNull(sourceFileName, "Please enter the name of the file to copy: ");
        String generatedDestinationName = destinationFileName;
        if (destinationFileName == null) {
            generatedDestinationName = generateUniqueFileName(sourceFileName);
        } else {
            File destinationFile = new File(getCurrentWorkingDirectory(), destinationFileName);
            try {
                destinationFile.createNewFile();
            } catch (InvalidPathException invalidPathException) {
                System.out.println("Invalid path specified for the destination file.");
            } catch (Exception exception) {
                System.out.println("A file with the destination name already exists: " + destinationFileName);
            }
        }
        try {
            File destinationFile = new File(getCurrentWorkingDirectory(), generatedDestinationName);
            try {
                destinationFile.createNewFile();
            } catch (Exception exception) {
                System.out.println("A file with the generated name already exists: " + generatedDestinationName);
            }
            try (FileInputStream fileInputStream = new FileInputStream(new File(getCurrentWorkingDirectory(), sourceFileName));
                 FileOutputStream fileOutputStream = new FileOutputStream(destinationFile)) {
                int byteRead;
                while ((byteRead = fileInputStream.read()) != -1) fileOutputStream.write(byteRead);
            }
            if (destinationFileName != null) {
                renameFile(generatedDestinationName, destinationFileName);
            }
            System.out.println("File copied successfully to: " + generatedDestinationName);
        } catch (FileNotFoundException fileNotFoundException) {
            System.out.println("Source file not found: " + sourceFileName);
        } catch (NullPointerException nullPointerException) {
            System.out.println("Source file not found: " + sourceFileName);
        } catch (Exception exception) {
            System.out.println("Error copying file: " + exception.getMessage());
        }
        return generatedDestinationName;
    }

    /**
     * Moves the specified file to a new directory.
     * If the source file name or target path is null, prompts the user for the missing value(s).
     * Handles file overwriting by copying and deleting if the target already exists.
     * Prints error messages for missing files or invalid paths.
     *
     * @param sourceFileNameToMove         the name of the file to move, or null to prompt the user
     * @param destinationDirectoryPath the destination directory path, or null to prompt the user
     */
    public static void move(String sourceFileNameToMove, String destinationDirectoryPath) {
        sourceFileNameToMove = promptIfNull(sourceFileNameToMove, "Please enter the name of the file to move: ");
        File sourceFileToMove = new File(getCurrentWorkingDirectory(), sourceFileNameToMove);
        if (!sourceFileToMove.exists()) {
            System.out.println("The specified source file does not exist: " + sourceFileNameToMove);
            return;
        }
        destinationDirectoryPath = promptIfNull(destinationDirectoryPath, "Please enter the destination directory path: ");
        String pathDelimiter = directoryOperationsInstance.getCurrentWorkingDirectoryDelimiter();
        String sourceFilePath = getCurrentWorkingDirectory() + pathDelimiter + sourceFileNameToMove;
        String destinationFilePath = destinationDirectoryPath + pathDelimiter + sourceFileNameToMove;
        try {
            Files.move(Paths.get(sourceFilePath), Paths.get(destinationFilePath));
            System.out.println("File moved successfully to: " + destinationDirectoryPath);
        } catch (java.nio.file.NoSuchFileException noSuchFileException) {
            System.out.println("File or destination path not found.");
        } catch (java.nio.file.FileAlreadyExistsException fileAlreadyExistsException) {
            System.out.println("A file with the same name already exists at the destination. Overwriting...");
            String copiedFileName = copy(sourceFileNameToMove, null);
            move(copiedFileName, destinationDirectoryPath);
            delFile(sourceFileNameToMove);
        } catch (Exception exception) {
            System.out.println("Error moving file: " + exception.getMessage());
        }
    }

    // --- Helper Methods ---

    /**
     * Prompts the user for input if the provided value is null.
     *
     * @param valueToCheck the value to check
     * @param promptText   the prompt to display if value is null
     * @return the original value or user input if value was null
     */
    private static String promptIfNull(String valueToCheck, String promptText) {
        if (valueToCheck == null) {
            return readUserInput(promptText);
        }
        return valueToCheck;
    }

    /**
     * Counts the number of lines in a file.
     *
     * @param fileToCountLines the file to count lines in
     * @return the number of lines
     */
    private static int countLines(File fileToCountLines) {
        int lineCount = 0;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileToCountLines))) {
            while (bufferedReader.readLine() != null) lineCount++;
        } catch (Exception ignored) {}
        return lineCount;
    }

    /**
     * Generates a unique file name based on the source file name.
     *
     * @param sourceFileNameForUnique the source file name
     * @return a unique file name
     */
    private static String generateUniqueFileName(String sourceFileNameForUnique) {
        String[] fileNameParts = sourceFileNameForUnique.split("\\.", 2);
        String baseFileName = fileNameParts[0];
        String fileExtension = fileNameParts.length > 1 ? "." + fileNameParts[1] : "";
        int duplicateIndex = 1;
        File candidateFile = new File(getCurrentWorkingDirectory(), baseFileName + fileExtension);
        while (candidateFile.exists()) {
            candidateFile = new File(getCurrentWorkingDirectory(), baseFileName + " (" + duplicateIndex + ")" + fileExtension);
            duplicateIndex++;
        }
        return candidateFile.getName();
    }
}
