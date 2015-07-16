package br.edu.ufba.softvis.visminer.metric;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import br.edu.ufba.softvis.visminer.ast.AST;
import br.edu.ufba.softvis.visminer.ast.MethodDeclaration;
import br.edu.ufba.softvis.visminer.ast.TypeDeclaration;
import br.edu.ufba.softvis.visminer.model.database.CommitDB;
import br.edu.ufba.softvis.visminer.model.database.FileDB;
import br.edu.ufba.softvis.visminer.persistence.MetricPersistance;

public abstract class MethodBasedMetric implements IMetric {
	
	protected List<CommitDB> commits;
	protected MetricPersistance persistence;
	protected TypeDeclaration currentType;

	@Override
	public void calculate(Map<FileDB, AST> filesMap, List<CommitDB> commits,
			MetricPersistance persistence) {
		
		this.commits = commits;
		this.persistence = persistence;
		
		for(Entry<FileDB, AST> entry : filesMap.entrySet()){

			AST ast = entry.getValue();
			
			if(ast == null || ast.getDocument().getTypesDeclarations() == null){
				continue;
			}

			for(TypeDeclaration type : ast.getDocument().getTypesDeclarations()){
				
				if(type.getMethods() == null){
					break;
				}

				currentType = type;
				
				calculate(type.getMethods());
			}

		}
	}
	
	public abstract void calculate(List<MethodDeclaration> methods);

}
