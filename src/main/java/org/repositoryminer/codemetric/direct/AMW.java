package org.repositoryminer.codemetric.direct;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractClassDeclaration;
import org.repositoryminer.ast.ClassArchetype;
import org.repositoryminer.ast.ClassDeclaration;
import org.repositoryminer.codemetric.CodeMetricId;

public class AMW implements IDirectCodeMetric {

	private WMC wmcMetric;
	
	public AMW() {
		wmcMetric = new WMC();
	}
	
	@Override
	public Document calculate(AbstractClassDeclaration type, AST ast) {
		if (type.getArchetype() != ClassArchetype.CLASS_OR_INTERFACE) {
			return null;
		}
		
		ClassDeclaration cls = (ClassDeclaration) type;
		
		int wmc = wmcMetric.calculate(cls.getMethods());
		int nom = cls.getMethods().size();
		
		return new Document("metric", CodeMetricId.AMW.toString()).append("value", calculate(wmc, nom));
	}

	public float calculate(int wmc, int nom) {
		if (nom == 0) {
			return 0l;
		}
		
		return wmc * 1l / nom;
	}
	
	@Override
	public CodeMetricId getId() {
		return CodeMetricId.AMW;
	}

}