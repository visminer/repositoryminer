package org.repositoryminer.utility;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;

public class FileUtils extends org.apache.commons.io.FileUtils {

	public static String copyFolderToTmp(String srcFolder, String destFolderName) throws IOException {
		File src = new File(srcFolder);
		File dest = new File(System.getProperty("java.io.tmpdir"), destFolderName);
		copyDirectory(src, dest);
		return FilenameUtils.normalize(dest.getAbsolutePath(), true);
	}

	public static void deleteFolder(String folderName) throws IOException {
		File folder = new File(folderName);
		deleteDirectory(folder);
	}
	
}