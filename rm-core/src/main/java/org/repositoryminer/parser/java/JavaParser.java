package org.repositoryminer.parser.java;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

	private static final String[] EXTENSIONS = {"java"};
	
	private List<String[]> srcFolders;
	private String[] classpath = new String[1];
	
	private String[] classpathSrcFolder;
	private String[] currSrcFolder;
	private String[] encoding;
	private ASTParser parser = ASTParser.newParser(org.eclipse.jdt.core.dom.AST.JLS8);

	public JavaParser() {
		classpath[0] = FilenameUtils.normalize(System.getProperty("java.home"), true) + "/lib/rt.jar";
	}

	@Override
	public String[] getExtensions() {
		return EXTENSIONS;
	}
	
	@Override
	public boolean accept(String filepath) {
		if(filepath.endsWith(".java")) {
				if (currSrcFolder == null || currSrcFolder.length == 0) {
					return true;
				}
				
				for (String folder : currSrcFolder) {
					if (filepath.startsWith(folder)) {
						return true;
					}
				}
			}
		return false;
	}

	@Override
	public Language getLanguage() {
		return Language.JAVA;
	}

	@Override
	public void scanRepository(String repositoryPath) {
		currSrcFolder = null;
		if (srcFolders == null || srcFolders.size() == 0) {
			return;
		}
		
		for (String[] folders : srcFolders) {
			boolean selected = true;
			for (String folder : folders) {
				if (!new File(repositoryPath, folder).exists()) {
					selected = false;
					break;
				}
			}
			
			if (selected) {
				currSrcFolder = folders;
				encoding = new String[currSrcFolder.length];
				Arrays.fill(encoding, "utf-8");
				
				classpathSrcFolder = new String[currSrcFolder.length];
				for (int i = 0; i < currSrcFolder.length; i++)
					classpathSrcFolder[i] = repositoryPath+"/"+currSrcFolder[i];
				break;
			}
		}
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

		if (currSrcFolder != null)
			parser.setEnvironment(classpath, classpathSrcFolder, encoding, true);
		else
			parser.setEnvironment(classpath, null, null, true);
		
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

	public List<String[]> getSrcFolders() {
		return srcFolders;
	}

	public void setSrcFolders(List<String[]> srcFolders) {
		this.srcFolders = srcFolders;
	}

	public String[] getClasspath() {
		return classpath;
	}

	public void setClasspath(String[] classpath) {
		this.classpath = Arrays.copyOf(classpath, classpath.length + 1);
		this.classpath[this.classpath.length - 1] = FilenameUtils.normalize(System.getProperty("java.home"), true) + "/lib/rt.jar";
	}

}