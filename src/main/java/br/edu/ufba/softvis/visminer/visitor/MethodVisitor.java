package br.edu.ufba.softvis.visminer.visitor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.InfixExpression.Operator;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.WhileStatement;

import br.edu.ufba.softvis.visminer.ast.Statement;
import br.edu.ufba.softvis.visminer.constant.NodeType;

public class MethodVisitor extends ASTVisitor {

	private List<Statement> statements=new ArrayList<Statement>();	
	
	public boolean visit(ForStatement node){
		Statement statement = new Statement();
		statement.setNodeType(NodeType.FOR);
		statements.add(statement);
		return super.visit(node);
	}
	
	public boolean visit(EnhancedForStatement node){
		Statement statement = new Statement();
		statement.setNodeType(NodeType.FOR);
		statements.add(statement);
		return super.visit(node);
	}
	
	public boolean visit(WhileStatement node){
		Statement statement = new Statement();
		statement.setNodeType(NodeType.WHILE);
		statements.add(statement);
		return super.visit(node);
	}
	
	public boolean visit(DoStatement node){
		Statement statement = new Statement();
		statement.setNodeType(NodeType.DO);
		statements.add(statement);
		return super.visit(node);
	}
	
	public boolean visit(SwitchCase node){
		Statement statement = new Statement();
		statement.setNodeType(NodeType.SWITCH_CASE);
		statements.add(statement);
		return super.visit(node);
	}
	
	public boolean visit(IfStatement node){
		Statement statement = new Statement();
		statement.setNodeType(NodeType.IF);
		statements.add(statement);
		return super.visit(node);
	}
	
	public boolean visit(CatchClause node){
		Statement statement = new Statement();
		statement.setNodeType(NodeType.CATCH);
		statements.add(statement);
		return super.visit(node);
	}	
	
	public boolean visit(InfixExpression node){
		Statement statement = new Statement();
		if(node.getOperator()==Operator.CONDITIONAL_AND){
			statement.setNodeType(NodeType.CONDITIONAL_AND);
			statements.add(statement);
		}else if(node.getOperator()==Operator.CONDITIONAL_OR){
			statement.setNodeType(NodeType.CONDITIONAL_OR);
			statements.add(statement);
		}	
		return super.visit(node);
	}
	
	
	@Override
	public void endVisit(MethodInvocation node) {
		
		super.endVisit(node);

	}	

	public List<Statement> getStatements() {
		return statements;
	}
	
}
