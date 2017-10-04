package org.repositoryminer.parser.java;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractMethod;
import org.repositoryminer.exception.ErrorMessage;
import org.repositoryminer.exception.RepositoryMinerException;
import org.repositoryminer.parser.IParser;
import org.repositoryminer.parser.Language;

/**
 * Java AST generator
 * 
 * This class has the job to create an abstract AST upon Java source code.
 * 
 * The extensions accepted for this generator are: java
 */

public class JavaParser implements IParser {

	private static final String[] EXTENSIONS = { "java", "jar" };

	private ASTParser parser = ASTParser.newParser(org.eclipse.jdt.core.dom.AST.JLS8);
	private String[] classpath;
	private String[] srcFolders;
	private String[] encoding;

	private List<String> userClasspath;
	
	@Override
	public String[] getExtensions() {
		return EXTENSIONS;
	}

	@Override
	public boolean accept(String filepath) {
		return filepath.endsWith(".java");
	}

	@Override
	public Language getLanguage() {
		return Language.JAVA;
	}

	@Override
	public void scanRepository(String repositoryPath) {
		List<File> files = (List<File>) FileUtils.listFiles(new File(repositoryPath), EXTENSIONS, true);
		Set<String> jars = SrcFolderDetector.findJars(files);
		Set<String> folders = SrcFolderDetector.findSrcFolders(files);

		encoding = new String[folders.size()];
		Arrays.fill(encoding, "UTF-8");
		srcFolders = folders.toArray(new String[folders.size()]);

		List<String> classpathTemp = new ArrayList<String>();
		classpathTemp.add(System.getProperty("java.home").replace("\\", "/") + "/lib/rt.jar");
		classpathTemp.addAll(jars);
		if (userClasspath != null) {
			classpathTemp.addAll(userClasspath);
		}
		classpath = classpathTemp.toArray(new String[classpathTemp.size()]);
	}

	@Override
	public AST generate(String filename, String source) {
		AST ast = new AST();
		ast.setName(filename);
		ast.setSource(source);

		parser.setResolveBindings(true);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setBindingsRecovery(true);
		parser.setCompilerOptions(JavaCore.getOptions());
		parser.setUnitName(filename);

		parser.setEnvironment(classpath, srcFolders, encoding, true);
		parser.setSource(source.toCharArray());

		CompilationUnit cu = (CompilationUnit) parser.createAST(null);

		if (!cu.getAST().hasBindingsRecovery()) {
			throw new RepositoryMinerException(ErrorMessage.JDT_HAS_NO_BINDING_RECOVERY);
		}

		FileVisitor visitor = new FileVisitor();
		cu.accept(visitor);

		ast.setImports(visitor.getImports());
		ast.setPackageDeclaration(visitor.getPackageName());
		ast.setTypes(visitor.getTypes());
		ast.setMethods(new ArrayList<AbstractMethod>());

		return ast;
	}

	public List<String> getUserClasspath() {
		return userClasspath;
	}

	public void setUserClasspath(List<String> userClasspath) {
		if (userClasspath == null)
			return;

		String[] exts = { "jar" };
		List<String> jarFiles = new ArrayList<String>();

		for (String file : userClasspath) {
			if (FilenameUtils.isExtension(file, "jar")) {
				jarFiles.add(file);
			} else {
				List<File> jars = (List<File>) FileUtils.listFiles(new File(file), exts, true);
				for (File jar : jars)
					jarFiles.add(jar.getAbsolutePath());
			}
		}

		this.userClasspath = jarFiles;
	}

}