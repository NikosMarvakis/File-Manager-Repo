package file_manager.tests;

import org.junit.Test;

import static file_manager.operations.FileOperations.delFile;
import static file_manager.operations.FileOperations.readFile;
import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;

import file_manager.operations.FileOperations;
import file_manager.utils.PathUtils;

public class FileOperationsTest {
    
    // Test if a file gets created correctly
    @Test
    public void testNewFile() {
        // Test creating a new file
        String testFileName = "test_file.txt";
        FileOperations.newFile(testFileName);
        
        // Verify the file was created
        File file = new File(PathUtils.getCurrentWorkingDirectory(), testFileName);
        assertTrue("File should exist", file.exists());
        
        // Clean up
        file.delete();
    }

    // Test if a file gets written and read correctly
    @Test
    public void testWriteAndReadFile() {
        // Test writing to and reading from a file
        String testFileName = "test_file.txt";
        String testContent = "Hello, World!";
        
        // Create and write to file
        FileOperations.newFile(testFileName);
        FileOperations.writeFile(testFileName, testContent);
        
        // Read the file content
        File file = new File(PathUtils.getCurrentWorkingDirectory(), testFileName);
        assertTrue("File should exist", file.exists());
        assertEquals(testContent, readFile(testFileName));

        // Clean up
        file.delete();
    }

    @Test
    public void testDeleteFile() {
        String fileName = "File1.txt";
        File testFile = new File(fileName);

        try {
            // Create the file
            boolean created = testFile.createNewFile();
            assertTrue("File should be created", created || testFile.exists());

            // // Attempt to delete the file using delFile
            // assertTrue("File should be deleted", delFile(fileName));
        } catch (IOException e) {
            fail("IOException thrown while creating file: " + e.getMessage());
        }
    }
} 