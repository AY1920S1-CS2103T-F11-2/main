package io.xpire.testutil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import io.xpire.commons.core.index.Index;
import io.xpire.model.XpireModel;
import io.xpire.model.item.XpireItem;

/**
 * A utility class for test cases.
 */
public class TestUtil {

    /**
     * Folder used for temp files created during testing. Ignored by Git.
     */
    private static final Path SANDBOX_FOLDER = Paths.get("src", "test", "data", "sandbox");

    /**
     * Appends {@code fileName} to the sandbox folder path and returns the resulting path.
     * Creates the sandbox folder if it doesn't exist.
     */
    public static Path getFilePathInSandboxFolder(String fileName) {
        try {
            Files.createDirectories(SANDBOX_FOLDER);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return SANDBOX_FOLDER.resolve(fileName);
    }

    /**
     * Returns the middle index of the item in the {@code xpireModel}'s item list.
     */
    public static Index getMidIndex(XpireModel xpireModel) {
        return Index.fromOneBased(xpireModel.getFilteredItemList().size() / 2);
    }

    /**
     * Returns the last index of the item in the {@code xpireModel}'s item list.
     */
    public static Index getLastIndex(XpireModel xpireModel) {
        return Index.fromOneBased(xpireModel.getFilteredItemList().size());
    }

    /**
     * Returns the item in the {@code xpireModel}'s item list at {@code index}.
     */
    public static XpireItem getItem(XpireModel xpireModel, Index index) {
        return xpireModel.getFilteredItemList().get(index.getZeroBased());
    }
}
