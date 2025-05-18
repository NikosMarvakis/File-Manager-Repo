# File Manager

A Java-based file management system that provides various operations for managing files and directories.

## Project Structure

The project is organized into the following packages:

- `File_Editor.operations`: Contains the core file and directory operations
  - `FileOperations.java`: Handles file-related operations (create, delete, rename, read, write, copy, move)
  - `DirectoryOperations.java`: Handles directory-related operations (create, delete, rename, move, list)
- `File_Editor.utils`: Contains utility classes
  - `java`: Handles path-related operations
  - `InputUtils.java`: Handles user input and command listing
- `File_Editor.tests`: Contains unit tests
  - `FileOperationsTest.java`: Tests for file operations

## Features

- File Operations:
  - Create new files
  - Delete files
  - Rename files
  - Read file contents
  - Write to files
  - Copy files
  - Move files
  - Clear file contents

- Directory Operations:
  - Create new directories
  - Delete directories
  - Rename directories
  - Move directories
  - List directory contents

- Path Operations:
  - Change current directory
  - Navigate to previous directory
  - Get current path

## Building and Testing

The project uses Maven for dependency management and building. To build and test the project:

1. Make sure you have Maven installed
2. Run `mvn clean install` to build the project and run tests
3. Run `mvn test` to run only the tests

## Usage

The file manager provides a command-line interface with the following commands:

- `exit`: Exit the program
- `list`: List contents of current directory
- `path`: Show current path
- `info`: Show available commands
- `chdir`: Change directory
- `prevdir`: Go to previous directory
- `make file`: Create a new file
- `delete file`: Delete a file
- `rename file`: Rename a file
- `read`: Read file contents
- `write file`: Write to a file
- `clear file`: Clear file contents
- `copy file`: Copy a file
- `move file`: Move a file
- `make dir`: Create a new directory
- `delete dir`: Delete a directory
- `rename dir`: Rename a directory
- `move dir`: Move a directory
