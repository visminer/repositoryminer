package org.repositoryminer.codesmell;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractTypeDeclaration;
import org.repositoryminer.ast.DeclarationType;
import org.repositoryminer.ast.MethodDeclaration;
import org.repositoryminer.ast.TypeDeclaration;
import org.repositoryminer.metric.SLOCMetric;
import org.repositoryminer.metric.TCCMetric;
import org.repositoryminer.metric.WMCMetric;

public class BrainClass implements ICodeSmell {

	@Override
	public void detect(AbstractTypeDeclaration type, AST ast, Document document) {
		if (type.getType() == DeclarationType.CLASS_OR_INTERFACE) {
			TypeDeclaration cls = (TypeDeclaration) type;
		
			boolean brainClass = detect(ast, type, cls);
			document.append("name", new String("Brain Class")).append("value", new Boolean(brainClass));
		}
	}
	
	public boolean detect(AST ast, AbstractTypeDeclaration type, TypeDeclaration cls){
		boolean brainClass  = false;
		
		WMCMetric wmcMetric = new WMCMetric();
		GodClass godClass = new GodClass();
		BrainMethod brainMethod = new BrainMethod();
		TCCMetric tccMetric = new TCCMetric();
		SLOCMetric slocMetric = new SLOCMetric();
		
		int wmc = wmcMetric.calculate(cls.getMethods());
		boolean isGodClass = godClass.detect(type, cls);
		int nbm = 0; //number of brain methods
		float tcc = tccMetric.calculate(type, cls.getMethods());
		int sloc = slocMetric.calculate(ast.getSourceCode());
	
		for(MethodDeclaration method : cls.getMethods()){
			if(brainMethod.detect(method, ast))
				nbm++;
		}
		
		brainClass = isGodClass && (wmc >=47 && tcc < 0.5) && ((nbm > 1 && sloc >= 197) || (nbm == 1 && sloc >= (2*197) && wmc >= (2*47) ));
		
		return brainClass;
	}
	

}
