package org.visminer.metric;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.visminer.constants.Metrics;
import org.visminer.util.DetailAST;

public class CCMetric implements IMetric{

	private int accumCC = 0;

	@Override
	public Metrics getId(){
		return Metrics.CC;
	}
	
	@Override
	public int getAccumulatedValue() {
		return accumCC;
	}
	
	@Override
	public int calculate(DetailAST ast){
		
		int ccTotal = 0;
		
		for(int i = 0; i < ast.getRoot().types().size(); i++){
			TypeDeclaration type = (TypeDeclaration) ast.getRoot().types().get(i);
			
			for(MethodDeclaration method : type.getMethods()){
				
				Block body = method.getBody();
				ccTotal += processBlock(body);
				
			}
			
		}
		
		accumCC += ccTotal;
		return ccTotal;
		
	}
	
	private int processBlock(Block body){
		
		int cc = 1;
		
		if(body == null){
			return 1;
		}
		
		@SuppressWarnings("unchecked")
		List<Statement> statements = body.statements();
		if(statements == null){
			return 1;
		}
		
		for(Statement statement : statements){
			cc += processStatement(statement);
		}
		
		return cc;
		
	}
	
	private int processStatement(Statement statement){
		switch(statement.getNodeType()){
		
		case ASTNode.IF_STATEMENT:
		case ASTNode.SWITCH_CASE:
		case ASTNode.FOR_STATEMENT:
		case ASTNode.DO_STATEMENT:
		case ASTNode.WHILE_STATEMENT:
			return 1;
			
		default:
			return 0;
		}
		
	}

	
}
