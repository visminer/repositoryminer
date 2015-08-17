package br.edu.ufba.softvis.visminer.metric;

import java.util.List;

import br.edu.ufba.softvis.visminer.ast.AST;
import br.edu.ufba.softvis.visminer.ast.ClassOrInterfaceDeclaration;
import br.edu.ufba.softvis.visminer.ast.MethodDeclaration;
import br.edu.ufba.softvis.visminer.ast.TypeDeclaration;
import br.edu.ufba.softvis.visminer.constant.SoftwareUnitType;
import br.edu.ufba.softvis.visminer.model.business.Commit;
import br.edu.ufba.softvis.visminer.persistence.MetricPersistance;

public abstract class MethodBasedMetricTemplate  implements IMetric {
	
	protected List<Commit> commits;
	protected MetricPersistance persistence;
	protected TypeDeclaration currentType;

	@Override
	public void calculate(List<AST> astList, List<Commit> commits, MetricPersistance persistence){
		
		this.commits = commits;
		this.persistence = persistence;
		
		for(AST ast : astList){
			
			if(ast.getDocument().getTypes() == null){
				continue;
			}

			for(TypeDeclaration type : ast.getDocument().getTypes()){
				
				ClassOrInterfaceDeclaration cls = null;
				if(type.getType() == SoftwareUnitType.CLASS_OR_INTERFACE){
					cls = (ClassOrInterfaceDeclaration) type;
				}else{
					continue;
				}
				
				if(cls.getMethods() == null){
					continue;
				}

				currentType = type;
				
				calculate(cls.getMethods());
			}

		}
	}
	
	public abstract void calculate(List<MethodDeclaration> methods);

}
