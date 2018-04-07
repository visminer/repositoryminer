package org.repositoryminer.metrics.parser.cppParser;

import java.util.ArrayList;
import java.util.List;


import org.eclipse.cdt.core.dom.ast.ASTVisitor;
import org.eclipse.cdt.core.dom.ast.DOMException;
import org.eclipse.cdt.core.dom.ast.IASTDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTName;
import org.eclipse.cdt.core.dom.ast.IASTExpression;
import org.eclipse.cdt.core.dom.ast.IASTStatement;
import org.eclipse.cdt.core.dom.ast.IASTForStatement;
import org.eclipse.cdt.core.dom.ast.IASTDoStatement;
import org.eclipse.cdt.core.dom.ast.IASTIfStatement;
import org.eclipse.cdt.core.dom.ast.IASTSwitchStatement;
import org.eclipse.cdt.core.dom.ast.IASTCaseStatement;
import org.eclipse.cdt.core.dom.ast.IASTWhileStatement;
import org.eclipse.cdt.core.dom.ast.IASTBreakStatement;
import org.eclipse.cdt.core.dom.ast.IASTGotoStatement;
import org.eclipse.cdt.core.dom.ast.IASTReturnStatement;
import org.eclipse.cdt.core.dom.ast.IASTContinueStatement;
import org.eclipse.cdt.core.dom.ast.IASTExpressionStatement;
import org.eclipse.cdt.core.dom.ast.IASTConditionalExpression;
import org.eclipse.cdt.core.dom.ast.IBinding;
import org.eclipse.cdt.core.dom.ast.EScopeKind;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTBinaryExpression;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPASTCatchHandler;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPASTTryBlockStatement;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPASTRangeBasedForStatement;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTFunctionDeclarator;
import org.repositoryminer.metrics.ast.AbstractFieldAccess;
import org.repositoryminer.metrics.ast.AbstractStatement;
import org.repositoryminer.metrics.ast.NodeType;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

public class MethodVisitor extends ASTVisitor {

	//private static final Logger LOGGER = LoggerFactory.getLogger(MethodVisitor.class);
	
	private List<AbstractStatement> statements = new ArrayList<>();
	private int maxDepth = 0;
	//private int depth = 0;

	public int getMaxDepth() {
		return maxDepth;
	}

	public List<AbstractStatement> getStatements() {
		return statements;
	}

	
				public int visit(IASTStatement statement)
				{
					if((statement instanceof IASTBreakStatement)){
					
						statements.add(new AbstractStatement(NodeType.BREAK, null));
					}else
				
					if((statement instanceof IASTContinueStatement)){	
					
						statements.add(new AbstractStatement(NodeType.CONTINUE, null));
					}else
				
					if((statement instanceof IASTReturnStatement)){
					
						statements.add(new AbstractStatement(NodeType.RETURN, null));
					}else
					
					if ((statement instanceof IASTDoStatement)) {	
					
						IASTExpression expression = ((IASTDoStatement) statement).getCondition();
						statements.add(new AbstractStatement(NodeType.DO, expression.getRawSignature()));
					}else
				
					if ((statement instanceof IASTForStatement)){
												
						IASTStatement init = ((IASTForStatement) statement).getInitializerStatement();                      
						IASTExpression condExpression = ((IASTForStatement) statement).getConditionExpression();
						IASTExpression iterExpression = ((IASTForStatement) statement).getIterationExpression();
						
						String expression = init.getRawSignature()+condExpression.getRawSignature()+";"+iterExpression.getRawSignature();
						statements.add(new AbstractStatement(NodeType.FOR, expression));	
					}else
						
					if ((statement instanceof IASTIfStatement)) {
			
						IASTExpression expression = ((IASTIfStatement) statement).getConditionExpression();
						statements.add(new AbstractStatement(NodeType.IF, expression.getRawSignature()));		
					}else
					
					if ((statement instanceof IASTWhileStatement)) {
						
						IASTExpression expression = ((IASTWhileStatement) statement).getCondition();
						statements.add(new AbstractStatement(NodeType.WHILE, expression.getRawSignature()));	
					}else
					
					if ((statement instanceof IASTSwitchStatement)) {
						
						IASTExpression expression = ((IASTSwitchStatement) statement).getControllerExpression();
						statements.add(new AbstractStatement(NodeType.SWITCH, expression.getRawSignature()));
					}else
					
					if ((statement instanceof IASTCaseStatement)) {
						
						IASTExpression expression = ((IASTCaseStatement) statement).getExpression();
						statements.add(new AbstractStatement(NodeType.CASE, expression.getRawSignature()));
					}else
					
					if ((statement instanceof IASTGotoStatement)) {
						
						statements.add(new AbstractStatement(NodeType.GOTO, statement.getRawSignature()));
					}else
					
					if ((statement instanceof IASTExpressionStatement)) {
						
						statements.add(new AbstractStatement(NodeType.EXPRESSION, statement.getRawSignature()));
					}else

					if ((statement instanceof ICPPASTCatchHandler)) {
						
						IASTDeclaration declaration = ((ICPPASTCatchHandler) statement).getDeclaration();
						statements.add(new AbstractStatement(NodeType.CATCH, declaration.getRawSignature()));
					}else
					
					if ((statement instanceof ICPPASTTryBlockStatement)) {
						
						statements.add(new AbstractStatement(NodeType.TRY, null));
					}else
					
					if ((statement instanceof ICPPASTRangeBasedForStatement)) {
						
						IASTDeclaration declaration = ((ICPPASTRangeBasedForStatement) statement).getDeclaration();
						statements.add(new AbstractStatement(NodeType.RANGEDBASEDFOR, declaration.getRawSignature()));
					}
					
					return 3;
				}
				
				public int visit(IASTExpression expression){
					
					if ((expression instanceof IASTConditionalExpression)) {
						statements.add(new AbstractStatement(NodeType.CONDITIONAL_EXPRESSION, expression.getRawSignature()));
					}else 
					
					if ((expression instanceof CPPASTBinaryExpression)) {
						statements.add(new AbstractStatement(NodeType.BINARY_EXPRESSION, expression.getRawSignature()));
											
					}else{
						statements.add(new AbstractStatement(NodeType.EXPRESSION, expression.getRawSignature()));

					}
					
					return 3;
				}
				
				public int visit(IASTName name) {
					
					if ((name.getParent() instanceof CPPASTFunctionDeclarator)) {
							IBinding bind = name.resolveBinding();
							String type = null;
							
						if (bind == null) {
							System.out.println("Bind not solve to "+name.getClass().getName());
							return 3;
						}
						
						try {
								if (bind.getScope().getKind() == EScopeKind.eClassType ) {
									type = bind.getName();
								}
							} catch (DOMException e) {
								e.printStackTrace();
							}
							statements.add(new AbstractFieldAccess(bind.getName(), type,
										bind.getClass().getCanonicalName(),
										true,
										type.startsWith("c") || type.startsWith("cpp") 
										|| type.startsWith("h") ? true : false));
						}
						
					return 3;
				}
				
}

				