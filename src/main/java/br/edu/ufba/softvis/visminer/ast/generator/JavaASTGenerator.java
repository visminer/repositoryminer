package br.edu.ufba.softvis.visminer.ast.generator;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.WhileStatement;

import br.edu.ufba.softvis.visminer.ast.AST;
import br.edu.ufba.softvis.visminer.ast.Document;
import br.edu.ufba.softvis.visminer.ast.EnumConstantDeclaration;
import br.edu.ufba.softvis.visminer.ast.MethodDeclaration;
import br.edu.ufba.softvis.visminer.ast.PackageDeclaration;
import br.edu.ufba.softvis.visminer.ast.Statement;
import br.edu.ufba.softvis.visminer.constant.NodeType;

public class JavaASTGenerator {

	public static AST generate(String filePath, byte[] source) {

		Document document = new Document();
		document.setName(filePath);

		if (source == null) {

			AST ast = new AST();
			ast.setDocument(document);
			return ast;

		}

		String sourceCode;
		try {
			sourceCode = new String(source, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}

		ASTParser parser = ASTParser
				.newParser(org.eclipse.jdt.core.dom.AST.JLS3);
		parser.setSource(sourceCode.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setBindingsRecovery(true);

		CompilationUnit root = (CompilationUnit) parser.createAST(null);

		if (root.getPackage() != null) {
			PackageDeclaration pkgDecl = new PackageDeclaration();
			pkgDecl.setName(root.getPackage().getName().getFullyQualifiedName());
			document.setPackageDeclaration(pkgDecl);
		}

		List<br.edu.ufba.softvis.visminer.ast.TypeDeclaration> typesDecl = new ArrayList<br.edu.ufba.softvis.visminer.ast.TypeDeclaration>();
		List<br.edu.ufba.softvis.visminer.ast.EnumDeclaration> enumsDecl = new ArrayList<br.edu.ufba.softvis.visminer.ast.EnumDeclaration>();

		for (int i = 0; i < root.types().size(); i++) {

			Object obj = root.types().get(i);

			if (obj instanceof TypeDeclaration) {

				TypeDeclaration typeAux = (TypeDeclaration) obj;
				typesDecl.add(processType(sourceCode, typeAux));

			} else if (obj instanceof EnumDeclaration) {

				EnumDeclaration enumAux = (EnumDeclaration) obj;
				enumsDecl.add(processEnum(enumAux));

			}

		}

		if (root.types().size() > 0) {
			if (typesDecl.size() > 0) {
				document.setTypesDeclarations(typesDecl);
			}

			if (enumsDecl.size() > 0) {
				document.setEnumsDeclarations(enumsDecl);
			}
		}

		AST ast = new AST();
		ast.setDocument(document);
		ast.setSourceCode(root.toString());

		return ast;
	}

	private static br.edu.ufba.softvis.visminer.ast.TypeDeclaration processType(String sourceCode, 
			TypeDeclaration type) {

		br.edu.ufba.softvis.visminer.ast.TypeDeclaration typeDecl = new br.edu.ufba.softvis.visminer.ast.TypeDeclaration();
		typeDecl.setInterfaceClass(type.isInterface());
		typeDecl.setName(type.getName().getFullyQualifiedName());

		if (type.getMethods().length == 0) {
			return typeDecl;
		}

		List<MethodDeclaration> methodsDecl = new ArrayList<MethodDeclaration>();
		for (org.eclipse.jdt.core.dom.MethodDeclaration method : type
				.getMethods()) {

			MethodDeclaration methodDecl = new MethodDeclaration();
			methodDecl.setName(method.getName().getFullyQualifiedName());
			Block body = method.getBody();
			methodDecl.setStatements(processBlock(sourceCode, body));
			methodsDecl.add(methodDecl);

		}

		typeDecl.setMethods(methodsDecl);
		return typeDecl;

	}

	private static br.edu.ufba.softvis.visminer.ast.EnumDeclaration processEnum(
			EnumDeclaration enumType) {

		br.edu.ufba.softvis.visminer.ast.EnumDeclaration enumDecl = new br.edu.ufba.softvis.visminer.ast.EnumDeclaration();
		enumDecl.setName(enumType.getName().getFullyQualifiedName());

		if (enumType.enumConstants() == null) {
			return enumDecl;
		}

		List<EnumConstantDeclaration> constsDecls = new ArrayList<EnumConstantDeclaration>();
		for (Object elem : enumType.enumConstants()) {
			org.eclipse.jdt.core.dom.EnumConstantDeclaration constEnum = (org.eclipse.jdt.core.dom.EnumConstantDeclaration) elem;
			EnumConstantDeclaration constDecl = new EnumConstantDeclaration();
			constDecl.setName(constEnum.getName().getFullyQualifiedName());
			constsDecls.add(constDecl);
		}

		enumDecl.setDeclarations(constsDecls);
		return enumDecl;
	}

	@SuppressWarnings("unchecked")
	private static List<Statement> processBlock(String sourceCode, Block body) {

		if (body == null || body.statements() == null) {
			return null;
		}

		List<org.eclipse.jdt.core.dom.Statement> statements = body.statements();
		List<Statement> statementsDecl = new ArrayList<Statement>(
				statements.size());
		for (org.eclipse.jdt.core.dom.Statement statement : statements) {

			Statement statementDecl = new Statement();
			switch (statement.getNodeType()) {
			case ASTNode.IF_STATEMENT:
				IfStatement ifstmt = (IfStatement) statement;
				statementDecl.setExpression(inspectExpression(sourceCode, ifstmt
						.getExpression()));
				statementDecl.setNodeType(NodeType.IF);

				break;
			case ASTNode.SWITCH_CASE:
				SwitchStatement switchstmt = (SwitchStatement) statement;
				statementDecl.setExpression(inspectExpression(sourceCode, switchstmt
						.getExpression()));
				statementDecl.setNodeType(NodeType.SWITCH_CASE);

				break;
			case ASTNode.FOR_STATEMENT:
				ForStatement forstmt = (ForStatement) statement;
				statementDecl.setExpression(inspectExpression(sourceCode, forstmt
						.getExpression()));
				statementDecl.setNodeType(NodeType.FOR);
				
				break;
			case ASTNode.DO_STATEMENT:
				DoStatement dostmt = (DoStatement) statement;
				statementDecl.setExpression(inspectExpression(sourceCode, dostmt
						.getExpression()));
				statementDecl.setNodeType(NodeType.DO);

				break;
			case ASTNode.WHILE_STATEMENT:
				WhileStatement whilestmt = (WhileStatement) statement;
				statementDecl.setExpression(inspectExpression(sourceCode, whilestmt
						.getExpression()));
				statementDecl.setNodeType(NodeType.WHILE);

				break;
			default:
				statementDecl.setNodeType(NodeType.NONE);
				break;
			}

			statementsDecl.add(statementDecl);
		}

		return statementsDecl;

	}

	private static String inspectExpression(String sourceCode, Expression expression) {
		String exp = "";

		if (expression != null) {
			int start = expression.getStartPosition();
			int end = start + expression.getLength();
			exp = sourceCode.substring(start, end);
		}

		return exp;
	}

}