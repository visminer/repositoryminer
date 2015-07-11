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
import br.edu.ufba.softvis.visminer.model.bean.Commit;
import br.edu.ufba.softvis.visminer.model.bean.File;
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
	public void calculate(Map<File, AST> filesMap, List<Commit> commits,
			MetricPersistance persistence) {

		for(Entry<File, AST> entry : filesMap.entrySet()){

			AST ast = entry.getValue();
			
			if(ast == null || ast.getDocument().getTypesDeclarations() == null){
				continue;
			}

			for(TypeDeclaration type : ast.getDocument().getTypesDeclarations()){
				
				if(type.getMethods() == null){
					break;
				}
				
				int sumCC = 0;
				int numMethods = type.getMethods().size();
				
				for(MethodDeclaration method : type.getMethods()){
					int cc = calculate(method);
					sumCC += cc;
					persistence.postMetricValue(method.getId(), String.valueOf(cc));
				}
				System.out.println("Commit: "+commits.get(commits.size()-1).getName()+" file "+entry.getKey().getPath()+" vals "+sumCC+" "+numMethods);
				float averageCC = sumCC / numMethods;
				persistence.postMetricValue(type.getId(), String.valueOf(averageCC));
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
			}
		}

		return cc;
	}

}
