package br.edu.ufba.softvis.visminer.ast.generator;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import br.edu.ufba.softvis.visminer.ast.AST;
import br.edu.ufba.softvis.visminer.ast.Document;
import br.edu.ufba.softvis.visminer.ast.EnumConstantDeclaration;
import br.edu.ufba.softvis.visminer.ast.MethodDeclaration;
import br.edu.ufba.softvis.visminer.ast.FieldDeclaration;
import br.edu.ufba.softvis.visminer.ast.PackageDeclaration;
import br.edu.ufba.softvis.visminer.ast.Statement;
import br.edu.ufba.softvis.visminer.constant.NodeType;
import br.edu.ufba.softvis.visminer.visitor.MethodVisitor;

public class JavaASTGenerator {

	public static AST generate(String filePath, byte[] source, String charset){
		
		Document document = new  Document();
		document.setName(filePath);
		
		if(source == null){
			
			AST ast = new AST();
			ast.setDocument(document);
			return ast;
			
		}
		
		String sourceCode;
		try {
			sourceCode = new String(source, charset);
		} catch (UnsupportedEncodingException e) {
			e.getMessage();
			System.exit(1);
			return null;
		}
		
		ASTParser parser = ASTParser.newParser(org.eclipse.jdt.core.dom.AST.JLS3);
		parser.setSource(sourceCode.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setBindingsRecovery(true);
		
		CompilationUnit root = (CompilationUnit) parser.createAST(null);
		
		if(root.getPackage() != null){
			PackageDeclaration pkgDecl = new PackageDeclaration();
			pkgDecl.setName(root.getPackage().getName().getFullyQualifiedName());
			document.setPackageDeclaration(pkgDecl);
		}
		
		List<br.edu.ufba.softvis.visminer.ast.TypeDeclaration> typesDecl = new ArrayList<br.edu.ufba.softvis.visminer.ast.TypeDeclaration>();
		List<br.edu.ufba.softvis.visminer.ast.EnumDeclaration> enumsDecl = new ArrayList<br.edu.ufba.softvis.visminer.ast.EnumDeclaration>();
		
		for(int i = 0; i < root.types().size(); i++){
			
			Object obj = root.types().get(i);
			
			if(obj instanceof TypeDeclaration){
				
				TypeDeclaration typeAux = (TypeDeclaration) obj;
				typesDecl.add(processType(typeAux));
				
			}else if(obj instanceof EnumDeclaration){
				
				EnumDeclaration enumAux = (EnumDeclaration) obj;
				enumsDecl.add(processEnum(enumAux));
				
			}
			
		}
		
		if(root.types().size() > 0){
			if(typesDecl.size() > 0){
				document.setTypesDeclarations(typesDecl);
			}
			
			if(enumsDecl.size() > 0){
				document.setEnumsDeclarations(enumsDecl);
			}
		}
		
		AST ast = new AST();
		ast.setDocument(document);
		ast.setSourceCode(root.toString());
		
		return ast;
	}
	
	private static br.edu.ufba.softvis.visminer.ast.TypeDeclaration processType(TypeDeclaration type){
		
		br.edu.ufba.softvis.visminer.ast.TypeDeclaration typeDecl = new br.edu.ufba.softvis.visminer.ast.TypeDeclaration();
		typeDecl.setInterfaceClass(type.isInterface());
		typeDecl.setName(type.getName().getFullyQualifiedName());
		
		
		if(type.getFields().length>0){
			List<FieldDeclaration> fields = new ArrayList<FieldDeclaration>();
			for(org.eclipse.jdt.core.dom.FieldDeclaration field : type.getFields()){
				fields.add(processField(field));
			}
			typeDecl.setFields(fields);
		}		
		
		
		if(type.getMethods().length == 0){
			return typeDecl;
		}
		
		List<MethodDeclaration> methodsDecl = new ArrayList<MethodDeclaration>();
		for(org.eclipse.jdt.core.dom.MethodDeclaration method : type.getMethods())
			methodsDecl.add(processMethod(method));		
		
		typeDecl.setMethods(methodsDecl);
		return typeDecl;
		
	}
	
	private static MethodDeclaration processMethod(org.eclipse.jdt.core.dom.MethodDeclaration method){
		
		MethodVisitor mVisitor = new MethodVisitor();
		method.accept(mVisitor);	
		
		MethodDeclaration methodDecl = new MethodDeclaration();
		methodDecl.setName(method.getName().getFullyQualifiedName());
		methodDecl.setConstructor(method.isConstructor());
		ModifierKeyword modifier = ModifierKeyword.fromFlagValue(method.getModifiers());
		if(modifier!=null)
			methodDecl.setModifier(modifier.toString());			
		
		methodDecl.setStatements(mVisitor.getStatements());
		
		return methodDecl;
	}
	
	
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
	
	private static br.edu.ufba.softvis.visminer.ast.EnumDeclaration processEnum(EnumDeclaration enumType){
		
		br.edu.ufba.softvis.visminer.ast.EnumDeclaration enumDecl = new br.edu.ufba.softvis.visminer.ast.EnumDeclaration();
		enumDecl.setName(enumType.getName().getFullyQualifiedName());
		
		if(enumType.enumConstants() == null){
			return enumDecl;
		}
		
		List<EnumConstantDeclaration> constsDecls = new ArrayList<EnumConstantDeclaration>();
		for(Object elem : enumType.enumConstants()){
			org.eclipse.jdt.core.dom.EnumConstantDeclaration constEnum = (org.eclipse.jdt.core.dom.EnumConstantDeclaration) elem;
			EnumConstantDeclaration constDecl = new EnumConstantDeclaration();
			constDecl.setName(constEnum.getName().getFullyQualifiedName());
			constsDecls.add(constDecl);
		}
		
		enumDecl.setDeclarations(constsDecls);
		return enumDecl;
	}
	
}