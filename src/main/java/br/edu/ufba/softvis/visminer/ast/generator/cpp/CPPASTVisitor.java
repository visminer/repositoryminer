package br.edu.ufba.softvis.visminer.ast.generator.cpp;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.cdt.core.dom.ast.ASTVisitor;
import org.eclipse.cdt.core.dom.ast.IASTDeclSpecifier;
import org.eclipse.cdt.core.dom.ast.IASTDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTDeclarator;
import org.eclipse.cdt.core.dom.ast.IASTEnumerationSpecifier.IASTEnumerator;
import org.eclipse.cdt.core.dom.ast.IASTExpression;
import org.eclipse.cdt.core.dom.ast.IASTNode;
import org.eclipse.cdt.core.dom.ast.IASTStatement;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPASTCatchHandler;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPASTFunctionDeclarator;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPASTParameterDeclaration;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTCaseStatement;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTCompositeTypeSpecifier;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTCompoundStatement;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTDoStatement;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTEnumerationSpecifier;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTExpressionStatement;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTForStatement;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTFunctionDeclarator;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTFunctionDefinition;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTIfStatement;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTNamedTypeSpecifier;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTReturnStatement;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTSimpleDeclSpecifier;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTSimpleDeclaration;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTSwitchStatement;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTTryBlockStatement;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTWhileStatement;

import br.edu.ufba.softvis.visminer.ast.ClassOrInterfaceDeclaration;
import br.edu.ufba.softvis.visminer.ast.EnumConstantDeclaration;
import br.edu.ufba.softvis.visminer.ast.EnumDeclaration;
import br.edu.ufba.softvis.visminer.ast.FieldDeclaration;
import br.edu.ufba.softvis.visminer.ast.MethodDeclaration;
import br.edu.ufba.softvis.visminer.ast.ParameterDeclaration;
import br.edu.ufba.softvis.visminer.ast.Statement;
import br.edu.ufba.softvis.visminer.constant.NodeType;

/**
 * A C++ AST Visitor
 * 
 * It is meant to visit C++ TranslationUnit ,@link
 * br.edu.ifba.softvis.visminer.generator.cpp.CPPASTGenerator, to produce
 * elements for Visminer's Abstract Sintax Tree
 * 
 * C++ projects may store code around in several types of files (*.h, *.hpp,
 * *.cpp) while showing little regard to balancing of declarations and
 * definitions. This meaning, a "perfect" approach would rather be: header files
 * (*.h) are used to encompass declarations which are later implemented in *.cpp
 * files. However, this is not mandatory and both types of file might show a mix
 * of such elements (declarations + definitions). Moreover, *.hpp are natively
 * declarative and "implementative".
 * 
 * Contrary to JAVA, C++ allows developement of non pure object-oriented source
 * codes. As an example, one might feel like storing instructions in methods
 * placed not inside a class. Such unested methods must as well be considered
 * and they will be inputed in the instance of the Document class associated
 * with the current mined file. As a consequence, {@link #visit(IASTStatement)}
 * encloses the routines that decides which is the targeted entity to get a hold
 * of a method (class or document).
 * 
 * @author Luis Paulo
 */
public class CPPASTVisitor extends ASTVisitor {

	private CPPASTGenerator generator;
	private ClassOrInterfaceDeclaration currClassDecl;

	public CPPASTVisitor(CPPASTGenerator generator) {
		super(true);

		this.generator = generator;

		this.shouldVisitAmbiguousNodes = true;
		this.includeInactiveNodes = true;
		this.shouldVisitImplicitNames = true;
		this.shouldVisitTokens = true;
	}

	/**
	 * From a given Method declaration, it extracts all of its parameters. Each
	 * item of the resulting collection must pair one parameter and type.
	 * 
	 * @param funcDeclr
	 *            enclosure for CPP method declaration
	 * @return all of method's parameters
	 */
	private List<ParameterDeclaration> extractParameters(
			ICPPASTFunctionDeclarator funcDecl) {
		List<ParameterDeclaration> parameters = new ArrayList<ParameterDeclaration>();

		ICPPASTParameterDeclaration[] prmDecls = funcDecl.getParameters();
		for (ICPPASTParameterDeclaration prmDecl : prmDecls) {
			ParameterDeclaration parameter = new ParameterDeclaration();
			parameter.setName(prmDecl.getDeclarator().getName().toString());
			parameter.setType(prmDecl.getDeclSpecifier().toString());

			parameters.add(parameter);
		}

		return parameters;
	}
	
	/**
	 * From a given Method declaration, it extracts all of its modifiers.
	 * 
	 * @param funcDeclr
	 *            enclosure for CPP method declaration
	 * @return all of method's modifiers
	 */
	private List<String> extractModifiers(
			ICPPASTFunctionDeclarator funcDecl) {
		
		List<String> modifiers = new ArrayList<String>();
		
		if(funcDecl.isConst())
			modifiers.add("const");
		if(funcDecl.isFinal())
			modifiers.add("final");
		if(funcDecl.isOverride())
			modifiers.add("override");
		if(funcDecl.isMutable())
			modifiers.add("mutable");
		if(funcDecl.isPureVirtual())
			modifiers.add("purevirtual");
		if(funcDecl.isVolatile())
			modifiers.add("volatile");

		return modifiers;
	}


	/**
	 * It extracts a method declaration from the CDT enclosure for CPP methods.
	 * 
	 * @param funcDeclr
	 *            enclosure for CPP method declaration
	 * @return a new MethodDeclaration
	 */
	private MethodDeclaration extractMethodDeclaration(
			ICPPASTFunctionDeclarator funcDecl, IASTDeclSpecifier funcSpec) {
		MethodDeclaration currMethod = new MethodDeclaration();
		currMethod.setName(funcDecl.getName().toString());
		currMethod.setModifiers(extractModifiers(funcDecl));
		currMethod.setParameters(extractParameters(funcDecl));
		currMethod.setStatements(null);
		currMethod.setThrownsExceptions(null);
		currMethod.setVarargs(funcDecl.takesVarArgs());
		currMethod.setConstructor((currClassDecl != null)
				&& (currClassDecl.getName().equals(currMethod.getName())));
		currMethod.setStatements(new ArrayList<Statement>());

		if (funcSpec != null) {
			currMethod.setReturnType(funcSpec.toString());
		}

		return currMethod;
	}

	/**
	 * Compare two methods
	 * 
	 * @param method1
	 * @param method2
	 * @return true if they have the same name && return type && parameters
	 */
	private boolean areEqual(MethodDeclaration method1,
			MethodDeclaration method2) {
		boolean are = (method1.getName().equals(method2.getName())
				&& (method1.getReturnType().equals(method1.getReturnType())) && (method1
				.getParameters().size() == method2.getParameters().size()));

		if (are) {
			for (int i = 0; (are) && (i < method1.getParameters().size()); i++) {
				are = (are
						&& (method1.getParameters().get(i).getName()
								.equals(method2.getParameters().get(i)
										.getName())) && (method1
						.getParameters().get(i).getType().equals(method2
						.getParameters().get(i).getType())));
			}
		}

		return are;
	}

	/**
	 * Usual switch set of options (cases) are hard to tackle with as to perform
	 * metrics calculations (e.g. CC). One way around is to lump the cases into
	 * a single sequence of conditional tests in an IF-LIKE fashion:
	 * 
	 * switch ( c ) { case 1: break; case 2: break; default: break;}
	 * 
	 * Becomes:
	 * 
	 * (c == 1) || (c == 2)
	 * 
	 * @param switchStmt
	 *            original switch statement
	 * @return resulting conditional expression
	 */
	private String extractSwitchExpression(CPPASTSwitchStatement switchStmt) {
		String expression = "";

		IASTExpression exp = switchStmt.getControllerExpression();

		IASTNode[] nodes = switchStmt.getBody().getChildren();
		for (IASTNode node : nodes) {
			if (node instanceof CPPASTCaseStatement) {
				CPPASTCaseStatement cases = (CPPASTCaseStatement) node;
				expression += (!expression.equals("") ? "||" : "") + "("
						+ exp.toString() + " == "
						+ cases.getExpression().toString() + ")";
			}
		}

		return expression;
	}

	@Override
	public int visit(IASTDeclSpecifier declSpec) {
		if (declSpec instanceof CPPASTCompositeTypeSpecifier) {
			CPPASTCompositeTypeSpecifier typeSpec = (CPPASTCompositeTypeSpecifier) declSpec;

			currClassDecl = new ClassOrInterfaceDeclaration();
			currClassDecl.setName(typeSpec.getName().toString());
			currClassDecl.setInterface(typeSpec.isVirtual());
			currClassDecl.setMethods(new ArrayList<MethodDeclaration>());
			currClassDecl.setFields(new ArrayList<FieldDeclaration>());

			IASTDeclaration[] members = typeSpec.getMembers();
			for (IASTDeclaration member : members) {
				if (member instanceof CPPASTSimpleDeclaration) {
					CPPASTSimpleDeclaration simpDecl = (CPPASTSimpleDeclaration) member;

					if ((simpDecl.getDeclarators() != null)
							&& (simpDecl.getDeclarators().length > 0)) {
						IASTDeclarator declarator = simpDecl.getDeclarators()[0];
						IASTDeclSpecifier specifier = simpDecl
								.getDeclSpecifier();

						if (declarator instanceof CPPASTFunctionDeclarator) {
							currClassDecl
									.getMethods()
									.add(extractMethodDeclaration(
											(CPPASTFunctionDeclarator) declarator,
											specifier));
						} else if (((specifier instanceof CPPASTSimpleDeclSpecifier))
								|| (specifier instanceof CPPASTNamedTypeSpecifier)) {
							FieldDeclaration field = new FieldDeclaration();
							field.setName(declarator.getName().toString());
							field.setType(specifier.toString());

							currClassDecl.getFields().add(field);
						}
					}
				} else if (member instanceof CPPASTFunctionDefinition) {
					CPPASTFunctionDefinition funcDef = (CPPASTFunctionDefinition) member;

					CPPASTFunctionDeclarator funcDcl = (CPPASTFunctionDeclarator) funcDef
							.getDeclarator();
					IASTDeclSpecifier funcSpec = funcDef.getDeclSpecifier();

					currClassDecl.getMethods().add(
							extractMethodDeclaration(funcDcl, funcSpec));
				}
			}

			generator.addType(currClassDecl);
		} else if (declSpec instanceof CPPASTEnumerationSpecifier) {
			CPPASTEnumerationSpecifier enumSpec = (CPPASTEnumerationSpecifier) declSpec;

			EnumDeclaration enumm = new EnumDeclaration();
			enumm.setName(enumSpec.getName().toString());

			List<EnumConstantDeclaration> enumConstants = new ArrayList<EnumConstantDeclaration>();

			IASTEnumerator[] enumerators = enumSpec.getEnumerators();
			for (IASTEnumerator enumerator : enumerators) {
				EnumConstantDeclaration enumConstant = new EnumConstantDeclaration();
				enumConstant.setName(enumerator.getName().toString());

				enumConstants.add(enumConstant);
			}
			enumm.setEnumConsts(enumConstants);

			generator.addEnum(enumm);
		}

		return super.visit(declSpec);
	}

	@Override
	public int visit(IASTStatement statement) {
		if (statement instanceof CPPASTCompoundStatement) {
			CPPASTCompoundStatement compElement = (CPPASTCompoundStatement) statement;
			IASTStatement[] stmts = compElement.getStatements();

			List<Statement> statements = new ArrayList<Statement>();

			for (IASTStatement stmt : stmts) {
				if (stmt instanceof CPPASTIfStatement) {
					CPPASTIfStatement ifStmt = (CPPASTIfStatement) stmt;

					Statement ifs = new Statement();
					ifs.setExpression(ifStmt.getConditionExpression()
							.getRawSignature());
					ifs.setNodeType(NodeType.IF);

					statements.add(ifs);

					IASTStatement elseStmt = ifStmt.getElseClause();
					if (elseStmt != null) {
						Statement elses = new Statement();
						elses.setExpression(elseStmt.getRawSignature());
						elses.setNodeType(NodeType.ELSE);

						statements.add(elses);
					}
				} else if (stmt instanceof CPPASTWhileStatement) {
					CPPASTWhileStatement whileStmt = (CPPASTWhileStatement) stmt;

					Statement whiles = new Statement();
					whiles.setExpression(whileStmt.getCondition()
							.getRawSignature());
					whiles.setNodeType(NodeType.WHILE);

					statements.add(whiles);
				} else if (stmt instanceof CPPASTReturnStatement) {
					CPPASTReturnStatement returnStmt = (CPPASTReturnStatement) stmt;

					Statement returns = new Statement();
					returns.setExpression(returnStmt.getRawSignature());
					returns.setNodeType(NodeType.RETURN);

					statements.add(returns);
				} else if (stmt instanceof CPPASTForStatement) {
					CPPASTForStatement forStmt = (CPPASTForStatement) stmt;

					Statement fors = new Statement();
					IASTExpression expression = forStmt
							.getConditionExpression();
					if (expression != null) {
						fors.setExpression(forStmt.getConditionExpression()
								.getRawSignature());
					}
					fors.setNodeType(NodeType.FOR);

					statements.add(fors);
				} else if (stmt instanceof CPPASTSwitchStatement) {
					CPPASTSwitchStatement switchStmt = (CPPASTSwitchStatement) stmt;

					extractSwitchExpression(switchStmt);

					Statement switchs = new Statement();
					switchs.setExpression(extractSwitchExpression(switchStmt));
					switchs.setNodeType(NodeType.SWITCH_CASE);

					statements.add(switchs);
				} else if (stmt instanceof CPPASTDoStatement) {
					CPPASTDoStatement doStmt = (CPPASTDoStatement) stmt;

					Statement dos = new Statement();
					dos.setExpression(doStmt.getCondition().getRawSignature());
					dos.setNodeType(NodeType.DO_WHILE);

					statements.add(dos);
				} else if (stmt instanceof CPPASTTryBlockStatement) {
					CPPASTTryBlockStatement tryStmt = (CPPASTTryBlockStatement) stmt;
					ICPPASTCatchHandler[] catchHandlers = tryStmt
							.getCatchHandlers();

					Statement trys = new Statement();
					trys.setExpression(null);
					trys.setNodeType(NodeType.TRY);

					statements.add(trys);

					for (int i = 0; i < catchHandlers.length; i++) {
						Statement catchs = new Statement();
						catchs.setExpression(null);
						catchs.setNodeType(NodeType.CATCH);

						statements.add(catchs);
					}
				} else if (stmt instanceof CPPASTExpressionStatement) {
					CPPASTExpressionStatement exprStmt = (CPPASTExpressionStatement) stmt;

					String sign = exprStmt.getExpression().getRawSignature();
					if (sign.startsWith("finally")) {
						Statement finallys = new Statement();
						finallys.setNodeType(NodeType.FINALLY);
						statements.add(finallys);
					}
				}
			}

			if (statement.getParent() instanceof CPPASTFunctionDefinition) {
				CPPASTFunctionDefinition funcDef = (CPPASTFunctionDefinition) statement
						.getParent();
				ICPPASTFunctionDeclarator funcDecl = (ICPPASTFunctionDeclarator) funcDef
						.getDeclarator();
				IASTDeclSpecifier funcSpec = funcDef.getDeclSpecifier();
				// is the method contained in a class?
				if (currClassDecl != null) {
					// find the method declaration added earlier to the class
					for (MethodDeclaration method : currClassDecl.getMethods()) {
						if (areEqual(method,
								extractMethodDeclaration(funcDecl, funcSpec))) {
							method.setStatements(statements);
						}
					}
				} // it is not (contained in a class). Let's input it in the
					// document
				else {
					MethodDeclaration method = extractMethodDeclaration(
							funcDecl, funcSpec);
					method.setStatements(statements);

					generator.getDocument().getMethods().add(method);
				}
			}
		}

		return super.visit(statement);
	}
}