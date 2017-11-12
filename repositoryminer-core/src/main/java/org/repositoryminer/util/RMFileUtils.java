package org.repositoryminer.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.NotFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

public class RMFileUtils {

	/**
	 * Retrieves all the sub directories from a root directory.
	 * 
	 * @param path
	 *            the root path.
	 * @return the sub directories.
	 */
	public static List<File> getAllDirs(String path) {
		return (List<File>) FileUtils.listFilesAndDirs(new File(path), new NotFileFilter(TrueFileFilter.INSTANCE),
				DirectoryFileFilter.DIRECTORY);
	}

	/**
	 * Copies a folder to another location with a new name.
	 * 
	 * @param srcFolder
	 *            the source folder.
	 * @param destFolder
	 *            the destiny folder.
	 * @param newName
	 *            the source folder name at the new location.
	 * 
	 * @return the path of the copied folder.
	 * @throws IOException
	 */
	public static String copyFolderTo(String src, String dest, String newName) throws IOException {
		File srcFile = new File(src);
		File destFile = new File(dest, newName);

		if (destFile.exists()) {
			FileUtils.forceDelete(destFile);
		}

		FileUtils.copyDirectory(srcFile, destFile);
		return FilenameUtils.normalize(destFile.getAbsolutePath(), true);
	}

	/**
	 * Copies a folder to the temporary directory with a new name.
	 * 
	 * @param src
	 *            the source folder.
	 * @param newName
	 *            the source folder name at the temporary directory.
	 * @return the path of the copied folder.
	 * @throws IOException
	 */
	public static String copyFolderToTmp(String src, String newName) throws IOException {
		return copyFolderTo(src, System.getProperty("java.io.tmpdir"), newName);
	}

	/**
	 * Deletes some folder.
	 * 
	 * @param folder
	 *            the name of folder that will be deleted.
	 * @throws IOException
	 */
	public static void deleteFolder(String folder) throws IOException {
		final File file = new File(folder);
		if (file.exists()) {
			FileUtils.deleteDirectory(file);
		}
	}

	/**
	 * Concatenates two file paths.
	 * 
	 * @param parent
	 *            the parent file path.
	 * @param child
	 *            the child file path.
	 * @return a new absolute file path compound by the parent and the child
	 *         concatenated.
	 */
	public static String concatFilePath(String parent, String child) {
		return FilenameUtils.normalize(new File(parent, child).getAbsolutePath(), true);
	}

	/**
	 * Concatenates a list of children file paths with a parent file path.
	 * 
	 * @param parent
	 *            the parent file path.
	 * @param children
	 *            the list of children file paths.
	 * @return a list of absolute file path compound by the parent and the children
	 *         concatenated.
	 */
	public static List<String> concatFilePath(String parent, Collection<String> children) {
		List<String> newPaths = new ArrayList<String>();
		for (String child : children) {
			newPaths.add(concatFilePath(parent, child));
		}
		return newPaths;
	}

}