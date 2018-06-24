package org.repositoryminer.metrics.parser.java;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.repositoryminer.metrics.ast.AST;
import org.repositoryminer.metrics.parser.Language;
import org.repositoryminer.metrics.parser.Parser;

/**
 * Java AST generator
 * 
 * This class has the job to create an abstract AST upon Java source code.
 * 
 * The extensions accepted for this generator are: java
 */
public class JavaParser extends Parser {

	private static final String[] EXTENSIONS = {"java"};

	public JavaParser() {
		super.id = Language.JAVA;
		super.extensions = EXTENSIONS;
	}
	
	@Override
	public AST generate(String filename, String source, String[] srcFolders) {
		AST ast = new AST();
		ast.setFileName(filename);
		ast.setSource(source);

		ASTParser parser = ASTParser.newParser(org.eclipse.jdt.core.dom.AST.JLS10);
		parser.setResolveBindings(true);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setBindingsRecovery(true);
		parser.setCompilerOptions(JavaCore.getOptions());
		parser.setUnitName(filename);
		parser.setSource(source.toCharArray());
	
		parser.setEnvironment(null, srcFolders, null, true);
		
		CompilationUnit cu = (CompilationUnit) parser.createAST(null);

		FileVisitor visitor = new FileVisitor();
		cu.accept(visitor);

		ast.setImports(visitor.getImports());
		ast.setPackageDeclaration(visitor.getPackageName());
		ast.setTypes(visitor.getTypes());
		ast.setLanguage(Language.JAVA);

		return ast;
	}

}
