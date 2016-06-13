package org.repositoryminer.parser.java;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.ContinueStatement;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.repositoryminer.ast.Statement;
import org.repositoryminer.ast.Statement.NodeType;

public class MethodVisitor extends ASTVisitor {

	private List<Statement> statements = new ArrayList<Statement>();

	@Override
	public boolean visit(BreakStatement node) {
		return addStatement(NodeType.BREAK, null);
	}

	@Override
	public boolean visit(CatchClause node) {
		return addStatement(NodeType.CATCH, node.getException().getName().getFullyQualifiedName());
	}

	@Override
	public boolean visit(ContinueStatement node) {
		return addStatement(NodeType.CONTINUE, null);
	}

	@Override
	public boolean visit(DoStatement node) {
		return addStatement(NodeType.DO_WHILE, node.getExpression().toString());
	}

	@Override
	public boolean visit(EnhancedForStatement node) {
		return addStatement(NodeType.FOR, node.getExpression().toString());
	}

	@Override
	public boolean visit(ForStatement node) {
		String expression = node.getExpression() != null ? node.getExpression().toString() : "";
		return addStatement(NodeType.FOR, expression);
	}

	@Override
	public boolean visit(IfStatement node) {
		addStatement(NodeType.IF, node.getExpression().toString());
		if (node.getElseStatement() != null)
			addStatement(NodeType.ELSE, null);
		return true;
	}

	@Override
	public boolean visit(ReturnStatement node) {
		return addStatement(NodeType.RETURN, null);
	}

	@Override
	public boolean visit(SwitchCase node) {
		if (node.isDefault())
			return addStatement(NodeType.SWITCH_DEFAULT, null);
		else
			return addStatement(NodeType.SWITCH_CASE, node.getExpression().toString());

	}

	@Override
	public boolean visit(ThrowStatement node) {
		return addStatement(NodeType.THROW, node.getExpression().toString());
	}

	@Override
	public boolean visit(TryStatement node) {
		addStatement(NodeType.TRY, null);
		if (node.getFinally() != null)
			addStatement(NodeType.FINALLY, null);
		return true;
	}

	@Override
	public boolean visit(WhileStatement node) {
		return addStatement(NodeType.WHILE, node.getExpression().toString());
	}

	@Override
	public boolean visit(ExpressionStatement node) {
		if (node.getNodeType() == ASTNode.CONDITIONAL_EXPRESSION)
			return addStatement(NodeType.CONDITIONAL_EXPRESSION, node.getExpression().toString());
		return true;
	}

	@Override
	public boolean visit(MethodInvocation node) {
		if (node.resolveMethodBinding() != null) {
			ITypeBinding type = node.resolveMethodBinding().getDeclaringClass();
			if (type.isFromSource()) {
				String expression = type.getQualifiedName() + "." + node.resolveMethodBinding().getName();
				return addStatement(NodeType.METHOD_INVOCATION, expression);
			}
		}
		return true;
	}

	@Override
	public boolean visit(SimpleName node) {
		if (node.resolveBinding() != null) {
			if (node.resolveBinding().getKind() == IBinding.VARIABLE) {
				IVariableBinding variable = (IVariableBinding) node.resolveBinding();
				if (variable.getDeclaringClass() != null) {
					if (variable.isField() && variable.getDeclaringClass().isFromSource()) {
						String expression = variable.getDeclaringClass().getQualifiedName() + "." + variable.getName();
						return addStatement(NodeType.FIELD_ACCESS, expression);
					}
				}
			}
		}
		return true;
	}

	public List<Statement> getStatements() {
		return statements;
	}

	private boolean addStatement(NodeType type, String expression) {
		Statement stmt = new Statement();
		stmt.setNodeType(type);
		stmt.setExpression(expression);
		statements.add(stmt);
		return true;
	}

}