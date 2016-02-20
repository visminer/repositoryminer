package br.edu.ufba.softvis.visminer.ast.generator.java;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import br.edu.ufba.softvis.visminer.annotations.ASTGeneratorAnnotation;
import br.edu.ufba.softvis.visminer.ast.AST;
import br.edu.ufba.softvis.visminer.ast.ClassOrInterfaceDeclaration;
import br.edu.ufba.softvis.visminer.ast.Document;
import br.edu.ufba.softvis.visminer.ast.EnumConstantDeclaration;
import br.edu.ufba.softvis.visminer.ast.FieldDeclaration;
import br.edu.ufba.softvis.visminer.ast.ImportDeclaration;
import br.edu.ufba.softvis.visminer.ast.MethodDeclaration;
import br.edu.ufba.softvis.visminer.ast.PackageDeclaration;
import br.edu.ufba.softvis.visminer.ast.ParameterDeclaration;
import br.edu.ufba.softvis.visminer.ast.generator.IASTGenerator;
import br.edu.ufba.softvis.visminer.constant.LanguageType;

/**
 * Java AST generator
 * 
 * This class have the job to create an abstract AST upon Java source code.
 * The underlining API is the JDT, the same used by Eclipse IDE.
 * 
 * The extensions accepted for this generator are: java
 * 
 */

@ASTGeneratorAnnotation(
		language = LanguageType.JAVA,
		extensions = {"java"}
		)
public class JavaASTGenerator implements IASTGenerator{

	public AST generate(String filePath, String source, String[] sourceFolders){

		Document document = new  Document();
		document.setName(filePath);

		ASTParser parser = ASTParser.newParser(org.eclipse.jdt.core.dom.AST.JLS8);
		parser.setSource(source.toCharArray());
		parser.setResolveBindings(true);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setBindingsRecovery(true);		
		parser.setUnitName(filePath.substring(filePath.lastIndexOf("/") + 1));

		String[] classpath = {System.getProperty("java.home").replace("\\", "/")+"/lib/rt.jar"};
		String[] encoding = new String[sourceFolders.length];
		Arrays.fill(encoding, "UTF-8");		

		parser.setEnvironment(classpath, sourceFolders, encoding, true);
		CompilationUnit root = null;

		try{
			root = (CompilationUnit) parser.createAST(null);
		}catch(IllegalStateException e){
			
			parser.setSource(source.toCharArray());
			parser.setKind(ASTParser.K_COMPILATION_UNIT);
			parser.setResolveBindings(false);
			parser.setBindingsRecovery(false);
			parser.setEnvironment(null, null, null, false);
			
			root = (CompilationUnit) parser.createAST(null);
			
		}

		String pkgName = null;
		if(root.getPackage() != null){
			PackageDeclaration pkgDecl = new PackageDeclaration();
			pkgDecl.setName(root.getPackage().getName().getFullyQualifiedName());
			document.setPackageDeclaration(pkgDecl);
			pkgName = pkgDecl.getName();
		}

		List<ImportDeclaration> importsDecls = new ArrayList<ImportDeclaration>();
		for(int i = 0; i < root.imports().size(); i++){
			org.eclipse.jdt.core.dom.ImportDeclaration importAux = (org.eclipse.jdt.core.dom.ImportDeclaration) root.imports().get(i);
			ImportDeclaration importDecl = new ImportDeclaration();
			importDecl.setName(importAux.getName().getFullyQualifiedName());
			importDecl.setStatic(importAux.isStatic());
			importDecl.setOnDemand(importAux.isOnDemand());
			importsDecls.add(importDecl);
		}
		document.setImports(importsDecls);

		List<br.edu.ufba.softvis.visminer.ast.TypeDeclaration> typesDecls = new ArrayList<br.edu.ufba.softvis.visminer.ast.TypeDeclaration>();
		for(int i = 0; i < root.types().size(); i++){

			Object obj = root.types().get(i);
			if(obj instanceof TypeDeclaration){
				TypeDeclaration typeAux = (TypeDeclaration) obj;
				typesDecls.add(processType(typeAux, pkgName));
			}else if(obj instanceof EnumDeclaration){
				EnumDeclaration enumAux = (EnumDeclaration) obj;
				typesDecls.add(processEnum(enumAux, pkgName));
			}

		}
		document.setTypes(typesDecls);

		AST ast = new AST();
		ast.setDocument(document);
		ast.setSourceCode(root.toString());

		return ast;

	}

	private static br.edu.ufba.softvis.visminer.ast.TypeDeclaration processType(TypeDeclaration type, String pkgName){

		ClassOrInterfaceDeclaration clsDecl = new ClassOrInterfaceDeclaration();
		clsDecl.setInterface(type.isInterface());

		if(pkgName != null)
			clsDecl.setName(pkgName+".");

		clsDecl.setName(clsDecl.getName() + type.getName().getFullyQualifiedName());

		List<FieldDeclaration> fields = new ArrayList<FieldDeclaration>();
		for(org.eclipse.jdt.core.dom.FieldDeclaration field : type.getFields()){
			fields.add(processField(field));
		}
		clsDecl.setFields(fields);

		List<MethodDeclaration> methods = new ArrayList<MethodDeclaration>();
		for(org.eclipse.jdt.core.dom.MethodDeclaration methodDecl : type.getMethods()){
			methods.add(processMethod(methodDecl));
		}
		clsDecl.setMethods(methods);

		return clsDecl;

	}

	@SuppressWarnings("unchecked")
	private static MethodDeclaration processMethod(org.eclipse.jdt.core.dom.MethodDeclaration methodDecl){

		MethodDeclaration m = new MethodDeclaration();

		m.setConstructor(methodDecl.isConstructor());
		m.setVarargs(methodDecl.isVarargs());
		m.setName(methodDecl.getName().getFullyQualifiedName());
		StringBuilder builder = null;

		builder = new StringBuilder();
		builder.append(methodDecl.getName().getFullyQualifiedName()).append("(");

		List<ParameterDeclaration> params = new ArrayList<ParameterDeclaration>();
		for(SingleVariableDeclaration var : (List<SingleVariableDeclaration>) methodDecl.parameters()){
			ParameterDeclaration param = new ParameterDeclaration();
			param.setName(var.getName().getFullyQualifiedName());
			param.setType(var.getType().toString());
			params.add(param);
			builder.append(param.getType()+" "+param.getName()+",");
		}

		if(builder.substring(builder.length()-1).equals(","))
			builder.replace(builder.length()-1, builder.length(), ")");
		else
			builder.append(")");

		m.setName(builder.toString());
		m.setParameters(params);

		List<String> throwsList = new ArrayList<String>();
		List<Type> types = methodDecl.thrownExceptionTypes();
		for(Type type : types){
			throwsList.add(type.toString());
		}
		m.setThrownsExceptions(throwsList);

		List<String> modifiers = new ArrayList<String>();
		for(Object modifier : methodDecl.modifiers()){
			modifiers.add(modifier.toString());
		}
		m.setModifiers(modifiers);

		MethodVisitor visitor = new MethodVisitor();
		methodDecl.accept(visitor);
		m.setStatements(visitor.getStatements());

		if(methodDecl.getReturnType2() != null)
			m.setReturnType(methodDecl.getReturnType2().toString());

		return m;

	}

	@SuppressWarnings("unchecked")
	private static FieldDeclaration processField(org.eclipse.jdt.core.dom.FieldDeclaration field){

		FieldDeclaration fieldDecl = new FieldDeclaration();		
		fieldDecl.setType(field.getType().toString());

		for(VariableDeclarationFragment vdf : (List<VariableDeclarationFragment>)field.fragments())
			fieldDecl.setName(vdf.getName().getIdentifier());

		ModifierKeyword modifier = ModifierKeyword.fromFlagValue(field.getModifiers());
		if(modifier!=null)
			fieldDecl.setModifier(modifier.toString());

		return fieldDecl;

	}

	private static br.edu.ufba.softvis.visminer.ast.EnumDeclaration processEnum(EnumDeclaration enumType, String pkgName){

		br.edu.ufba.softvis.visminer.ast.EnumDeclaration enumDecl = new br.edu.ufba.softvis.visminer.ast.EnumDeclaration();

		if(pkgName != null)
			enumDecl.setName(pkgName+".");

		enumDecl.setName(enumDecl.getName() + enumType.getName().getFullyQualifiedName());

		List<EnumConstantDeclaration> constsDecls = new ArrayList<EnumConstantDeclaration>();
		for(Object elem : enumType.enumConstants()){
			org.eclipse.jdt.core.dom.EnumConstantDeclaration constEnum = (org.eclipse.jdt.core.dom.EnumConstantDeclaration) elem;
			EnumConstantDeclaration constDecl = new EnumConstantDeclaration();
			constDecl.setName(enumDecl.getName() + "." + constEnum.getName().getFullyQualifiedName());
			constsDecls.add(constDecl);
		}
		enumDecl.setEnumConsts(constsDecls);

		return enumDecl;
	}

}