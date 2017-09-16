package org.repositoryminer.parser.java;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.commons.io.filefilter.NotFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractMethod;
import org.repositoryminer.exception.ErrorMessage;
import org.repositoryminer.exception.RepositoryMinerException;
import org.repositoryminer.parser.IParser;

/**
 * Java AST generator
 * 
 * This class has the job to create an abstract AST upon Java source code.
 * 
 * The extensions accepted for this generator are: java
 */

public class JavaParser implements IParser {

	private List<String> srcFolders = new ArrayList<String>(2);
	private String[] classpath;
	private List<String> foundSrcFolders;
	private ASTParser parser = ASTParser.newParser(org.eclipse.jdt.core.dom.AST.JLS8);

	public JavaParser() {
		// Adding commong source folders
		srcFolders.add("src");
		srcFolders.add("src/main/java");
	}

	@Override
	public boolean accept(String filepath) {
		for (String folder : foundSrcFolders) {
			if (filepath.startsWith(folder)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getLanguage() {
		return "java";
	}

	@Override
	public void scanRepository(String repositoryPath) {
		File dir = new File(repositoryPath);
		foundSrcFolders = new ArrayList<String>();

		for (File folder : FileUtils.listFilesAndDirs(dir, new NotFileFilter(TrueFileFilter.INSTANCE),
				HiddenFileFilter.VISIBLE)) {
			String normalizedPath = FilenameUtils.normalize(folder.getAbsolutePath(), true);
			for (String srcFolder : srcFolders) {
				if (normalizedPath.endsWith(srcFolder)) {
					foundSrcFolders.add(normalizedPath);
				}
			}
		}
	}

	@Override
	public AST generate(String filename, String source, String charset) {
		AST ast = new AST();
		ast.setName(filename);
		ast.setSource(source);

		parser.setResolveBindings(true);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setBindingsRecovery(true);
		parser.setCompilerOptions(JavaCore.getOptions());
		parser.setUnitName(filename);

		String[] clsPath;

		if (classpath != null && classpath.length > 0) {
			clsPath = new String[classpath.length+1];
			clsPath[0] = FilenameUtils.normalize(System.getProperty("java.home"), true) + "/lib/rt.jar" ;
			System.arraycopy(classpath, 0, clsPath, 1, classpath.length);
		} else {
			clsPath = new String[1];
			clsPath[0] = FilenameUtils.normalize(System.getProperty("java.home"), true) + "/lib/rt.jar" ;
		}

		String[] encoding = new String[foundSrcFolders.size()];
		Arrays.fill(encoding, charset);

		parser.setEnvironment(clsPath, foundSrcFolders.toArray(new String[foundSrcFolders.size()]),encoding, true);
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

	public List<String> getSrcFolders() {
		return srcFolders;
	}

	public void setSrcFolders(List<String> srcFolders) {
		this.srcFolders = srcFolders;
	}

	public String[] getClasspath() {
		return classpath;
	}

	public void setClasspath(String[] classpath) {
		this.classpath = classpath;
	}

}