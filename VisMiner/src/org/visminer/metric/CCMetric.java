package org.visminer.metric;

import java.util.List;

import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.visminer.constants.Metrics;
import org.visminer.util.DetailAST;

public class CCMetric implements IMetric{


	public Metrics getId(){
		return Metrics.CC;
	}
	
	public int calculate(DetailAST ast){
		
		int ccTotal = 0;
		
		for(int i = 0; i < ast.getRoot().types().size(); i++){
			TypeDeclaration type = (TypeDeclaration) ast.getRoot().types().get(i);
			
			for(MethodDeclaration method : type.getMethods()){
				
				Block body = method.getBody();
				ccTotal += processBlock(body);
				
			}
			
		}
		
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
		
		case Statement.IF_STATEMENT:
		case Statement.SWITCH_CASE:
		case Statement.FOR_STATEMENT:
		case Statement.DO_STATEMENT:
		case Statement.WHILE_STATEMENT:
			return 1;
			
		default:
			return 0;
		}
		
	}
	
}
