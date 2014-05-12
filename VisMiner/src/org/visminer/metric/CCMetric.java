package org.visminer.metric;

import java.util.List;

import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.visminer.constants.Metrics;
import org.visminer.util.DetailAST;

/**
 * <p>
 * Calculates Complexity Cyclomatic metric
 * </p>
 * 
 * @author Felipe
 * @version 1.0
 */
public class CCMetric implements IMetric{

	private int accumCC = 0;

	public Metrics getId(){
		return Metrics.CC;
	}
	
	public int getAccumulatedValue() {
		return accumCC;
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
		
		accumCC += ccTotal;
		return ccTotal;
		
	}
	
	//process mthod code
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
	
	//verify if a statement count or not count to cc value
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
