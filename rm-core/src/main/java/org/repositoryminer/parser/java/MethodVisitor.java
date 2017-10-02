package org.repositoryminer.parser.java;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.ContinueStatement;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.repositoryminer.ast.AbstractFieldAccess;
import org.repositoryminer.ast.AbstractMethodInvocation;
import org.repositoryminer.ast.AbstractStatement;
import org.repositoryminer.ast.AbstractVariableDeclaration;
import org.repositoryminer.ast.NodeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MethodVisitor extends ASTVisitor {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodVisitor.class);
	
	private List<AbstractStatement> statements = new ArrayList<>();
	private int maxDepth = 0;
	private int depth = 0;

	public int getMaxDepth() {
		return maxDepth;
	}

	public List<AbstractStatement> getStatements() {
		return statements;
	}

	@Override
	public boolean visit(Block node) {
		depth++;
		return true;
	}

	@Override
	public void endVisit(Block node) {
		maxDepth = depth > maxDepth ? depth : maxDepth;
		depth--;
	}

	@Override
	public boolean visit(BreakStatement node) {
		statements.add(new AbstractStatement(NodeType.BREAK, null));
		return true;
	}

	@Override
	public boolean visit(ContinueStatement node) {
		statements.add(new AbstractStatement(NodeType.CONTINUE, null));
		return true;
	}

	@Override
	public boolean visit(ReturnStatement node) {
		statements.add(new AbstractStatement(NodeType.RETURN, null));
		return true;
	}

	@Override
	public boolean visit(DoStatement node) {
		statements.add(new AbstractStatement(NodeType.DO_WHILE, node.getExpression().toString()));
		return true;
	}

	@Override
	public boolean visit(EnhancedForStatement node) {
		statements.add(new AbstractStatement(NodeType.FOR,
				node.getExpression() != null ? node.getExpression().toString() : ""));
		return true;
	}

	@Override
	public boolean visit(ForStatement node) {
		statements.add(new AbstractStatement(NodeType.FOR,
				node.getExpression() != null ? node.getExpression().toString() : ""));
		return true;
	}

	@Override
	public boolean visit(IfStatement node) {
		statements.add(new AbstractStatement(NodeType.IF, node.getExpression().toString()));
		return true;
	}

	@Override
	public boolean visit(WhileStatement node) {
		statements.add(new AbstractStatement(NodeType.WHILE, node.getExpression().toString()));
		return true;
	}

	@Override
	public boolean visit(SwitchStatement node) {
		statements.add(new AbstractStatement(NodeType.SWITCH, node.getExpression().toString()));
		return true;
	}

	@Override
	public boolean visit(SwitchCase node) {
		if (node.isDefault()) {
			statements.add(new AbstractStatement(NodeType.SWITCH_DEFAULT, null));
		} else {
			statements.add(new AbstractStatement(NodeType.SWITCH_CASE, node.getExpression().toString()));
		}
		return true;
	}

	@Override
	public boolean visit(CatchClause node) {
		statements.add(new AbstractStatement(NodeType.CATCH, node.getException().toString()));
		return true;
	}

	@Override
	public boolean visit(ConditionalExpression node) {
		statements.add(new AbstractStatement(NodeType.CONDITIONAL_EXPRESSION, node.getExpression().toString()));
		return true;
	}

	@Override
	public boolean visit(ExpressionStatement node) {
		statements.add(new AbstractStatement(NodeType.EXPRESSION, node.getExpression().toString()));
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean visit(VariableDeclarationStatement node) {
		ITypeBinding bind = node.getType().resolveBinding();
		String type = null;
		if (bind != null)
			type = bind.getQualifiedName();
			
		for (VariableDeclarationFragment frag : (List<VariableDeclarationFragment>) node.fragments()) {
			statements.add(new AbstractVariableDeclaration(frag.getName().getIdentifier(), type,
					frag.getInitializer() != null ? frag.getInitializer().toString() : null));
		}
		return true;
	}

	@Override
	public boolean visit(SimpleName node) {
		IBinding bind = node.resolveBinding();
		if (bind == null) {
			LOGGER.warn("Bind not solve to "+node.toString()+" at position "+node.getStartPosition()+".");
			return true;
		}
		
		if (bind.getKind() == IBinding.VARIABLE) {
			IVariableBinding varBind = (IVariableBinding) bind;
			if (varBind.isField()) {
				String type = varBind.getType().getQualifiedName();
				statements.add(new AbstractFieldAccess(varBind.getName(), type,
						varBind.getDeclaringClass() != null ? varBind.getDeclaringClass().getQualifiedName() : null,
						varBind.getType().isPrimitive(),
						type.startsWith("java") || type.startsWith("javax") ? true : false));
			}
		} else if (bind.getKind() == IBinding.METHOD) {
			IMethodBinding mBind = (IMethodBinding) bind;
			analyzeMethodInvocation(mBind);
		}
		return true;
	}

	private void analyzeMethodInvocation(IMethodBinding mBind) {
		AbstractMethodInvocation methodInv = new AbstractMethodInvocation();
		methodInv.setDeclaringClass(mBind.getDeclaringClass().getQualifiedName());

		StringBuilder parameters = new StringBuilder();
		for (ITypeBinding type : mBind.getParameterTypes()) {
			parameters.append(type.getQualifiedName() + ",");
		}

		if (mBind.getParameterTypes().length > 0) {
			parameters.deleteCharAt(parameters.length() - 1);
		}
		methodInv.setExpression(mBind.getName() + '(' + parameters.toString() + ')');

		String fieldName = null;
		if ((mBind.getName().startsWith("get") || mBind.getName().startsWith("set")) && mBind.getName().length() > 3) {
			fieldName = mBind.getName().substring(3);
		} else if (mBind.getName().startsWith("is") && mBind.getName().length() > 2) {
			fieldName = mBind.getName().substring(2);
		} else {
			statements.add(methodInv);
			return;
		}

		methodInv.setAccessor(true);
		methodInv.setAccessedField(Character.toLowerCase(fieldName.charAt(0)) + fieldName.substring(1));
		statements.add(methodInv);
	}

}