package br.edu.ufba.softvis.visminer.metric;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import br.edu.ufba.softvis.visminer.annotations.MetricAnnotation;
import br.edu.ufba.softvis.visminer.ast.AST;
import br.edu.ufba.softvis.visminer.ast.MethodDeclaration;
import br.edu.ufba.softvis.visminer.ast.Statement;
import br.edu.ufba.softvis.visminer.ast.TypeDeclaration;
import br.edu.ufba.softvis.visminer.constant.MetricType;
import br.edu.ufba.softvis.visminer.constant.MetricUid;
import br.edu.ufba.softvis.visminer.model.database.CommitDB;
import br.edu.ufba.softvis.visminer.model.database.FileDB;
import br.edu.ufba.softvis.visminer.persistence.MetricPersistance;

@MetricAnnotation(
		name = "Ciclomatic Complexity",
		description = "Cyclomatic complexity is a software metric, used to indicate the complexity of a"
				+ " program. It is a quantitative measure of the number of linearly independent paths through a"
				+ " program's source code.",
				acronym = "CC",
				type = MetricType.SIMPLE,
				uid = MetricUid.CC
		)
public class CCMetric implements IMetric{

	@Override
	public void calculate(Map<FileDB, AST> filesMap, List<CommitDB> commits,
			MetricPersistance persistence) {

		for(Entry<FileDB, AST> entry : filesMap.entrySet()){

			AST ast = entry.getValue();
			
			if(ast == null || ast.getDocument().getTypesDeclarations() == null){
				continue;
			}

			for(TypeDeclaration type : ast.getDocument().getTypesDeclarations()){
				
				if(type.getMethods() == null){
					break;
				}
				
				for(MethodDeclaration method : type.getMethods()){
					int cc = calculate(method);
					persistence.postMetricValue(method.getId(), String.valueOf(cc));
				}
				
			}

		}

	}


	private int calculate(MethodDeclaration method) {

		if(method.getStatements() == null){
			return 1;
		}

		int cc = 1;
		for(Statement statement : method.getStatements()){
			switch(statement.getNodeType()){
			case IF: cc += 1; break;
			case SWITCH_CASE: cc += 1; break;
			case FOR: cc += 1; break;
			case DO: cc += 1; break;
			case WHILE: cc += 1; break;
			case CATCH: cc += 1; break;
			case CONDITIONAL_OR: cc += 1; break;
			case CONDITIONAL_AND: cc += 1; break;
			}
		}

		return cc;
	}

}
