package org.repositoryminer.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

/**
 * This class handles some utility file operations.
 */
public class RMFileUtils extends FileUtils {

	/**
	 * Copies some folder to temporary directory.
	 * 
	 * @param srcFolder
	 *            the source folder.
	 * @param destFolder
	 *            the folder name in the temporary directory.
	 * @return the path of the copied folder.
	 * @throws IOException
	 */
	public static String copyFolderToTmp(final String srcFolder, final String destFolder) throws IOException {
		final File src = new File(srcFolder);
		final File dest = new File(System.getProperty("java.io.tmpdir"), destFolder);

		if (dest.exists()) {
			FileUtils.forceDelete(dest);
		}

		copyDirectory(src, dest);
		return FilenameUtils.normalize(dest.getAbsolutePath(), true);
	}

	/**
	 * Deletes some folder.
	 * 
	 * @param folder
	 *            the name of folder that will be deleted.
	 * @throws IOException
	 */
	public static void deleteFolder(final String folder) throws IOException {
		final File file = new File(folder);

		if (file.exists()) {
			deleteDirectory(file);
		}
	}

}