package org.repositoryminer.parser.java;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.Document;
import org.repositoryminer.ast.FieldDeclaration;
import org.repositoryminer.ast.ImportDeclaration;
import org.repositoryminer.ast.MethodDeclaration;
import org.repositoryminer.ast.ParameterDeclaration;
import org.repositoryminer.ast.TypeDeclaration;
import org.repositoryminer.parser.Parser;

/**
 * Java AST generator
 * 
 * This class have the job to create an abstract AST upon Java source code. The
 * underlining API is the JDT, the same used by Eclipse IDE.
 * 
 * The extensions accepted for this generator are: java
 * 
 */

public class JavaParser extends Parser {

	public JavaParser() {
		extensions = new HashSet<String>(1);
		extensions.add("java");
	}

	@Override
	public void processSourceFolders(String repositoryPath) {
		sourceFolders = new ArrayList<String>();
		scanRepository(repositoryPath);
	}

	@Override
	public String getLanguage() {
		return "java";
	}

	private void scanRepository(String path) {
		File directory = new File(path);

		File[] fList = directory.listFiles(new FileFilter() {
			public boolean accept(File file) {
				return file.isDirectory() && !file.isHidden();
			}
		});

		for (File file : fList) {
			if (validateSourceFolder(file)) {
				sourceFolders.add(file.getAbsolutePath());
				scanRepository(file.getAbsolutePath());
			}
		}
	}

	private boolean validateSourceFolder(File f) {

		File[] fList = f.listFiles(new FileFilter() {
			public boolean accept(File file) {
				return (file.isDirectory() && !file.isHidden()) || file.getName().endsWith(".java");
			}
		});

		if (fList == null)
			return false;

		for (File f2 : fList) {
			if (f2.getName().endsWith(".java")) {
				return true;
			} else if (validateSourceFolder(f2)) {
				return true;
			}
		}
		return false;

	}

	public AST generate(String filePath, String source) {
		Document document = new Document();
		document.setName(filePath);

		ASTParser parser = ASTParser.newParser(org.eclipse.jdt.core.dom.AST.JLS8);
		parser.setSource(source.toCharArray());
		parser.setResolveBindings(true);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setBindingsRecovery(true);
		parser.setUnitName(filePath.substring(filePath.lastIndexOf("/") + 1));

		String[] classpath = { System.getProperty("java.home").replace("\\", "/") + "/lib/rt.jar" };
		String[] encoding = new String[sourceFolders.size()];
		Arrays.fill(encoding, charset);

		parser.setEnvironment(classpath, sourceFolders.toArray(new String[sourceFolders.size()]), encoding, true);
		CompilationUnit root = null;

		try {
			root = (CompilationUnit) parser.createAST(null);
		} catch (IllegalStateException e) {
			parser.setSource(source.toCharArray());
			parser.setKind(ASTParser.K_COMPILATION_UNIT);
			parser.setResolveBindings(false);
			parser.setBindingsRecovery(false);
			parser.setEnvironment(null, null, null, false);
			root = (CompilationUnit) parser.createAST(null);
		}

		String packageName = null;
		if (root.getPackage() != null) {
			packageName = root.getPackage().getName().getFullyQualifiedName();
		}
		document.setPackageDeclaration(packageName);

		List<ImportDeclaration> importsDecls = new ArrayList<ImportDeclaration>();
		for (int i = 0; i < root.imports().size(); i++) {
			org.eclipse.jdt.core.dom.ImportDeclaration importAux = (org.eclipse.jdt.core.dom.ImportDeclaration) root
					.imports().get(i);
			ImportDeclaration importDecl = new ImportDeclaration();
			importDecl.setName(importAux.getName().getFullyQualifiedName());
			importDecl.setStatic(importAux.isStatic());
			importDecl.setOnDemand(importAux.isOnDemand());
			importsDecls.add(importDecl);
		}
		document.setImports(importsDecls);

		List<org.repositoryminer.ast.AbstractTypeDeclaration> typesDecls = new ArrayList<org.repositoryminer.ast.AbstractTypeDeclaration>();
		for (int i = 0; i < root.types().size(); i++) {
			Object obj = root.types().get(i);
			if (obj instanceof org.eclipse.jdt.core.dom.TypeDeclaration) {
				org.eclipse.jdt.core.dom.TypeDeclaration typeAux = (org.eclipse.jdt.core.dom.TypeDeclaration) obj;
				typesDecls.add(processType(packageName, typeAux));
			}
		}
		document.setTypes(typesDecls);

		AST ast = new AST();
		ast.setDocument(document);
		ast.setSourceCode(source);

		return ast;
	}

	private static org.repositoryminer.ast.AbstractTypeDeclaration processType(String packageName, 
			org.eclipse.jdt.core.dom.TypeDeclaration type) {
		TypeDeclaration clsDecl = new TypeDeclaration();
		clsDecl.setInterface(type.isInterface());

		clsDecl.setName(packageName + "." + type.getName().getIdentifier());

		List<FieldDeclaration> fields = new ArrayList<FieldDeclaration>();
		for (org.eclipse.jdt.core.dom.FieldDeclaration field : type.getFields()) {
			fields.add(processField(field));
		}
		clsDecl.setFields(fields);

		List<MethodDeclaration> methods = new ArrayList<MethodDeclaration>();
		for (org.eclipse.jdt.core.dom.MethodDeclaration methodDecl : type.getMethods()) {
			methods.add(processMethod(methodDecl));
		}

		clsDecl.setMethods(methods);
		return clsDecl;
	}

	@SuppressWarnings("unchecked")
	private static MethodDeclaration processMethod(org.eclipse.jdt.core.dom.MethodDeclaration methodDecl) {
		MethodDeclaration m = new MethodDeclaration();
		m.setConstructor(methodDecl.isConstructor());
		m.setVarargs(methodDecl.isVarargs());
		m.setName(methodDecl.getName().getFullyQualifiedName());
		StringBuilder builder = null;

		builder = new StringBuilder();
		builder.append(methodDecl.getName().getFullyQualifiedName()).append("(");

		List<ParameterDeclaration> params = new ArrayList<ParameterDeclaration>();
		for (SingleVariableDeclaration var : (List<SingleVariableDeclaration>) methodDecl.parameters()) {
			ParameterDeclaration param = new ParameterDeclaration();
			param.setName(var.getName().getFullyQualifiedName());
			param.setType(var.getType().toString());
			params.add(param);
			builder.append(param.getType() + " " + param.getName() + ",");
		}

		if (builder.substring(builder.length() - 1).equals(","))
			builder.replace(builder.length() - 1, builder.length(), ")");
		else
			builder.append(")");

		m.setName(builder.toString());
		m.setParameters(params);

		List<String> throwsList = new ArrayList<String>();
		List<Type> types = methodDecl.thrownExceptionTypes();
		for (Type type : types) {
			throwsList.add(type.toString());
		}
		m.setThrownsExceptions(throwsList);

		List<String> modifiers = new ArrayList<String>();
		for (Object modifier : methodDecl.modifiers()) {
			modifiers.add(modifier.toString());
		}
		m.setModifiers(modifiers);

		MethodVisitor visitor = new MethodVisitor();
		methodDecl.accept(visitor);
		m.setStatements(visitor.getStatements());

		if (methodDecl.getReturnType2() != null)
			m.setReturnType(methodDecl.getReturnType2().toString());

		m.setStartPositionInSourceCode(methodDecl.getStartPosition());
		m.setEndPositionInSourceCode(methodDecl.getStartPosition() + methodDecl.getLength());

		return m;
	}

	@SuppressWarnings("unchecked")
	private static FieldDeclaration processField(org.eclipse.jdt.core.dom.FieldDeclaration field) {
		FieldDeclaration fieldDecl = new FieldDeclaration();
		fieldDecl.setType(field.getType().toString());

		for (VariableDeclarationFragment vdf : (List<VariableDeclarationFragment>) field.fragments())
			fieldDecl.setName(vdf.getName().getIdentifier());

		ModifierKeyword modifier = ModifierKeyword.fromFlagValue(field.getModifiers());
		if (modifier != null)
			fieldDecl.setModifier(modifier.toString());

		return fieldDecl;
	}

}