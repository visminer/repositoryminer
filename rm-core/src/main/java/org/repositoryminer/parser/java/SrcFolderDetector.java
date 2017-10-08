package org.repositoryminer.parser.java;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;

public class SrcFolderDetector {

	public static Set<String> findJars(List<File> files) {
		Set<String> jars = new HashSet<String>();
		for (File file : files) {
			if (FilenameUtils.isExtension(file.getName(), "jar"))
				jars.add(file.getAbsolutePath());
		}
		return jars;
	}

	public static Set<String> findSrcFolders(List<File> files) {
		Set<String> srcFolders = new HashSet<String>();

		for (File file : files) {
			if (!FilenameUtils.isExtension(file.getName(), "java") || file.getName().equals("package-info.java"))
				continue;

			BufferedReader buffer = null;
			try {
				buffer = new BufferedReader(new FileReader(file));

				String line = null;
				String pkg = null;

				while ((line = buffer.readLine()) != null) {
					line = line.trim();
					if (line.startsWith("package")) {
						pkg = findPackageName(line);
						break;
					}
				}

				if (pkg == null)
					srcFolders.add(file.getParentFile().getAbsolutePath());
				else {
					String folder = file.getParentFile().getAbsolutePath();
					srcFolders.add(folder.substring(0, folder.length() - pkg.length() - 1));
				}

				buffer.close();
			} catch (IOException e) {
				continue;
			} finally {
				if (buffer != null)
					try {
						buffer.close();
					} catch (IOException e) {
						continue;
					}
			}

		}

		return srcFolders;
	}

	private static String findPackageName(String line) {
		int i;
		for (i = 9; i < line.length(); i++)
			if (line.charAt(i) == ';')
				break;
		return line.substring(8, i);
	}

}