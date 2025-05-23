package file_manager.tests;

import file_manager.operations.FileOperations;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.nio.file.Files;
import static file_manager.utils.PathUtils.getCurrentWorkingDirectory;
import static org.junit.Assert.*;


public class FileOperationsTest {

    private final String testFileName = "testFile.txt";
    private final String testFileName2 = "testFile2.txt";
    private final String testFileCopy = "testFileCopy.txt";
    private final String testFileMove = "testFileMove.txt";
    private final String testDir = "testDir";

    @Before
    public void setUp() throws Exception {
        // Clean up before each test
        deleteFileIfExists(testFileName);
        deleteFileIfExists(testFileName2);
        deleteFileIfExists(testFileCopy);
        deleteFileIfExists(testFileMove);
        deleteFileIfExists(testDir + File.separator + testFileMove);
        File dir = new File(getCurrentWorkingDirectory(), testDir);
        if (dir.exists()) {
            for (File f : dir.listFiles()) {
                f.delete();
            }
            dir.delete();
        }
    }

    @After
    public void tearDown() throws Exception {
        // Clean up after each test
        deleteFileIfExists(testFileName);
        deleteFileIfExists(testFileName2);
        deleteFileIfExists(testFileCopy);
        deleteFileIfExists(testFileMove);
        File dir = new File(getCurrentWorkingDirectory(), testDir);
        if (dir.exists()) {
            for (File f : dir.listFiles()) {
                f.delete();
            }
            dir.delete();
        }
    }

    private void deleteFileIfExists(String fileName) {
        File file = new File(getCurrentWorkingDirectory(), fileName);
        if (file.exists()) file.delete();
    }

    @Test
    public void testNewFileCreatesFile() {
        FileOperations.newFile(testFileName);
        File file = new File(getCurrentWorkingDirectory(), testFileName);
        assertTrue(file.exists());
    }

    @Test
    public void testNewFileAlreadyExists() {
        File file = new File(getCurrentWorkingDirectory(), testFileName);
        try {
            file.createNewFile();
        } catch (Exception ignored) {}
        FileOperations.newFile(testFileName);
        assertTrue(file.exists());
    }

    @Test
    public void testClearFile() throws Exception {
        File file = new File(getCurrentWorkingDirectory(), testFileName);
        Files.write(file.toPath(), "Hello\nWorld".getBytes());
        FileOperations.clearFile(testFileName);
        String content = new String(Files.readAllBytes(file.toPath()));
        assertEquals("", content);
    }

    @Test
    public void testClearFileNonExistent() {
        FileOperations.clearFile("nonexistent.txt");
        File file = new File(getCurrentWorkingDirectory(), "nonexistent.txt");
        assertFalse(file.exists());
    }

    @Test
    public void testDelFile() throws Exception {
        File file = new File(getCurrentWorkingDirectory(), testFileName);
        file.createNewFile();
        FileOperations.delFile(testFileName);
        assertFalse(file.exists());
    }

    @Test
    public void testDelFileNonExistent() {
        FileOperations.delFile("nonexistent.txt");
        File file = new File(getCurrentWorkingDirectory(), "nonexistent.txt");
        assertFalse(file.exists());
    }

    @Test
    public void testRenameFile() throws Exception {
        File file = new File(getCurrentWorkingDirectory(), testFileName);
        file.createNewFile();
        FileOperations.renameFile(testFileName, testFileName2);
        File renamed = new File(getCurrentWorkingDirectory(), testFileName2);
        assertTrue(renamed.exists());
        assertFalse(file.exists());
    }

    @Test
    public void testRenameFileNonExistent() {
        FileOperations.renameFile("nonexistent.txt", testFileName2);
        File file = new File(getCurrentWorkingDirectory(), testFileName2);
        assertFalse(file.exists());
    }

    @Test
    public void testReadFile() throws Exception {
        File file = new File(getCurrentWorkingDirectory(), testFileName);
        Files.write(file.toPath(), "Hello\nWorld".getBytes());
        String content = FileOperations.readFile(testFileName);
        assertTrue(content.contains("Hello"));
        assertTrue(content.contains("World"));
    }

    @Test
    public void testReadFileNonExistent() {
        String content = FileOperations.readFile("nonexistent.txt");
        assertEquals("", content);
    }

    @Test
    public void testWriteFile() throws Exception {
        File file = new File(getCurrentWorkingDirectory(), testFileName);
        Files.write(file.toPath(), "Line1".getBytes());
        FileOperations.writeFile(testFileName, "Line2");
        String content = new String(Files.readAllBytes(file.toPath()));
        assertTrue(content.contains("Line1"));
        assertTrue(content.contains("Line2"));
    }

    @Test
    public void testWriteFileNonExistent() {
        FileOperations.writeFile("nonexistent.txt", "Should not write");
        File file = new File(getCurrentWorkingDirectory(), "nonexistent.txt");
        assertFalse(file.exists());
    }

    @Test
    public void testCopyFile() throws Exception {
        File file = new File(getCurrentWorkingDirectory(), testFileName);
        Files.write(file.toPath(), "CopyMe".getBytes());
        FileOperations.copy(testFileName, testFileCopy);
        File copied = new File(getCurrentWorkingDirectory(), testFileCopy);
        assertTrue(copied.exists());
        String content = new String(Files.readAllBytes(copied.toPath()));
        assertEquals("CopyMe", content);
    }

    @Test
    public void testCopyFileNonExistent() {
        String result = FileOperations.copy("nonexistent.txt", testFileCopy);
        File copied = new File(getCurrentWorkingDirectory(), testFileCopy);
        assertTrue(copied.exists());
        assertEquals(testFileCopy, result); // The method returns the name even if not created
    }

    @Test
    public void testMoveFile() throws Exception {
        File file = new File(getCurrentWorkingDirectory(), testFileMove);
        Files.write(file.toPath(), "MoveMe".getBytes());
        File dir = new File(getCurrentWorkingDirectory(), testDir);
        dir.mkdir();
        FileOperations.move(testFileMove, dir.getAbsolutePath());
        File moved = new File(dir, testFileMove);
        assertTrue(moved.exists());
        assertFalse(file.exists());
    }

    @Test
    public void testMoveFileNonExistent() {
        File dir = new File(getCurrentWorkingDirectory(), testDir);
        dir.mkdir();
        FileOperations.move("nonexistent.txt", dir.getAbsolutePath());
        File moved = new File(dir, "nonexistent.txt");
        assertFalse(moved.exists());
    }
}
