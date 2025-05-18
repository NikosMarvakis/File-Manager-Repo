package File_Editor.tests;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import File_Editor.operations.FileOperations;
import File_Editor.utils.PathUtils;

public class FileOperationsTest {
    
    @Test
    public void testNewFile() {
        // Test creating a new file
        String testFileName = "test_file.txt";
        FileOperations.newFile(testFileName);
        
        // Verify the file was created
        File file = new File(PathUtils.getPath(), testFileName);
        assertTrue("File should exist", file.exists());
        
        // Clean up
        file.delete();
    }
    
    @Test
    public void testWriteAndReadFile() {
        // Test writing to and reading from a file
        String testFileName = "test_file.txt";
        String testContent = "Hello, World!";
        
        // Create and write to file
        FileOperations.newFile(testFileName);
        FileOperations.writeFile(testFileName, testContent);
        
        // Read the file content
        File file = new File(PathUtils.getPath(), testFileName);
        assertTrue("File should exist", file.exists());
        
        // Clean up
        file.delete();
    }
} 