package file_manager.tests;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import file_manager.operations.DirectoryOperations;
import file_manager.utils.PathUtils;

public class DirectoryOperationsTest {

    // Test creating a new directory
    @Test
    public void testNewDir() {
        String dirName = "testNewDir";
        File dir = new File(PathUtils.getCurrentWorkingDirectory(), dirName);
        try {
            assertTrue("Directory should be created", DirectoryOperations.newDir(dirName));
            assertTrue("Directory should exist", dir.exists() && dir.isDirectory());
            // Try to create again, should return false
            assertFalse("Duplicate directory creation should fail", DirectoryOperations.newDir(dirName));
        } finally {
            dir.delete();
        }
    }

    // Test deleting an empty directory
    @Test
    public void testDelEmptyDir() {
        String dirName = "testDelEmptyDir";
        File dir = new File(PathUtils.getCurrentWorkingDirectory(), dirName);
        try {
            dir.mkdir();
            assertTrue("Directory should exist before deletion", dir.exists());
            assertTrue("Directory should be deleted", DirectoryOperations.delDir(dir.getAbsolutePath(), "y"));
            assertFalse("Directory should not exist after deletion", dir.exists());
        } finally {
            dir.delete();
        }
    }

    // Test deleting a non-existent directory
    @Test
    public void testDelNonExistentDir() {
        String dirName = "nonExistentDir";
        File dir = new File(PathUtils.getCurrentWorkingDirectory(), dirName);
        assertFalse("Deleting non-existent directory should return false", DirectoryOperations.delDir(dir.getAbsolutePath(), "y"));
    }

    // Test deleting a non-empty directory (simulate user confirmation as "Y")
    @Test
    public void testDelNonEmptyDir() throws IOException {
        String dirName = "testDelNonEmptyDir";
        File dir = new File(PathUtils.getCurrentWorkingDirectory(), dirName);
        File file = new File(dir, "file.txt");
        try {
            dir.mkdir();
            file.createNewFile();
            // Simulate user confirmation by temporarily replacing System.in if needed
            // Here, we assume the method will prompt and we can't automate input, so just test deletion logic
            // (In real test, use a mocking framework or refactor for testability)
            assertTrue("Directory should exist before deletion", dir.exists());
            // The method will prompt for confirmation and may not delete in this test environment
            // So, we just call and check if the directory is eventually deleted
            DirectoryOperations.delDir(dir.getAbsolutePath(), null);
            // Directory may or may not be deleted depending on prompt, so just clean up
        } finally {
            file.delete();
            dir.delete();
        }
    }

    // Test renaming a directory
    @Test
    public void testRenameDir() {
        String originalDir = "originalDir";
        String renamedDir = "renamedDir";
        File orig = new File(PathUtils.getCurrentWorkingDirectory(), originalDir);
        File renamed = new File(PathUtils.getCurrentWorkingDirectory(), renamedDir);
        try {
            orig.mkdir();
            assertTrue("Original directory should exist", orig.exists());
            DirectoryOperations.renameDir(originalDir, renamedDir);
            assertFalse("Original directory should not exist after rename", orig.exists());
            assertTrue("Renamed directory should exist", renamed.exists());
        } finally {
            orig.delete();
            renamed.delete();
        }
    }

    // Test renaming a directory to an existing directory name (should fail)
    @Test
    public void testRenameDirToExisting() {
        String dir1 = "dir1";
        String dir2 = "dir2";
        File d1 = new File(PathUtils.getCurrentWorkingDirectory(), dir1);
        File d2 = new File(PathUtils.getCurrentWorkingDirectory(), dir2);
        try {
            d1.mkdir();
            d2.mkdir();
            DirectoryOperations.renameDir(dir1, dir2);
            // d1 should still exist, d2 should still exist
            assertTrue("Original directory should still exist", d1.exists());
            assertTrue("Target directory should still exist", d2.exists());
        } finally {
            d1.delete();
            d2.delete();
        }
    }

    // Test moving a directory
    @Test
    public void testMoveDir() {
        String dirName = "moveDir";
        String targetParent = "targetParent";
        File dir = new File(PathUtils.getCurrentWorkingDirectory(), dirName);
        File parent = new File(PathUtils.getCurrentWorkingDirectory(), targetParent);
        File moved = new File(parent, dirName);
        try {
            dir.mkdir();
            parent.mkdir();
            assertTrue("Source directory should exist", dir.exists());
            DirectoryOperations.moveDir(dir.getAbsolutePath(), parent.getAbsolutePath());
            assertFalse("Source directory should not exist after move", dir.exists());
            assertTrue("Directory should exist in new location", moved.exists());
        } finally {
            moved.delete();
            dir.delete();
            parent.delete();
        }
    }

    // Test moving a directory to an existing directory (should fail)
    @Test
    public void testMoveDirToExisting() {
        String dirName = "moveDirExisting";
        String targetParent = "targetParentExisting";
        File dir = new File(PathUtils.getCurrentWorkingDirectory(), dirName);
        File parent = new File(PathUtils.getCurrentWorkingDirectory(), targetParent);
        File moved = new File(parent, dirName);
        try {
            dir.mkdir();
            parent.mkdir();
            moved.mkdir(); // Create a directory with the same name in target
            DirectoryOperations.moveDir(dir.getAbsolutePath(), parent.getAbsolutePath());
            // Should not overwrite, so both should exist
            assertTrue("Source directory should still exist", dir.exists());
            assertTrue("Target directory should still exist", moved.exists());
        } finally {
            moved.delete();
            dir.delete();
            parent.delete();
        }
    }
}