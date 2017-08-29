package org.repositoryminer.parser.java;

import java.io.File;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.repositoryminer.ast.AST;
import org.repositoryminer.exception.ErrorMessage;
import org.repositoryminer.exception.RepositoryMinerException;
import org.repositoryminer.parser.IParser;

/**
 * Java AST generator
 * 
 * This class has the job to create an abstract AST upon Java source code. The
 * underlining API is the JDT, the same used by Eclipse IDE.
 * 
 * The extensions accepted for this generator are: java
 */

public class JavaParser implements IParser {

	private static final String[] EXTENSIONS = { "java" };
	private List<String> srcFolders;
	private String[] classpath;
	
	public JavaParser(String[] classpath) {
		this.classpath = classpath;
	}
	
	@Override
	public String[] getExtensions() {
		return EXTENSIONS;
	}

	@Override
	public String getLanguage() {
		return "Java";
	}

	@Override
	public void scanRepository(String repositoryPath) {
		File rootDir = new File(repositoryPath);
		Collection<File> files = FileUtils.listFiles(rootDir, EXTENSIONS, true);
		for (File f : files) {
			srcFolders.add(f.getParentFile().getAbsolutePath());
		}
	}

	@Override
	public AST generate(String filename, String source) {
		AST ast = new AST();
		ast.setName(filename);
		ast.setSource(source);

		ASTParser parser = ASTParser.newParser(org.eclipse.jdt.core.dom.AST.JLS8);
		parser.setResolveBindings(true);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setBindingsRecovery(true);
		parser.setCompilerOptions(JavaCore.getOptions());
		parser.setUnitName(filename);

		String[] clsPath;
		
		if (classpath != null && classpath.length > 0) {
			clsPath = new String[classpath.length+1];
			clsPath[0] = System.getProperty("java.home") + "/lib/rt.jar" ;
			System.arraycopy(classpath, 0, clsPath, 1, classpath.length);
		} else {
			clsPath = new String[1];
			clsPath[0] = System.getProperty("java.home") + "/lib/rt.jar" ;
		}

		parser.setEnvironment(clsPath, srcFolders.toArray(new String[srcFolders.size()]), new String[] { "UTF-8" },
				true);
		parser.setSource(source.toCharArray());

		CompilationUnit cu = (CompilationUnit) parser.createAST(null);

		if (!cu.getAST().hasBindingsRecovery()) {
			throw new RepositoryMinerException(ErrorMessage.JDT_HAS_NO_BINDING_RECOVERY);
		}

		FileVisitor visitor = new FileVisitor();
		cu.accept(visitor);

		return ast;
	}

}