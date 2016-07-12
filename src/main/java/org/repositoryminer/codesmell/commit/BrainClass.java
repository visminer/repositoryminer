package org.repositoryminer.codesmell.commit;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractTypeDeclaration;
import org.repositoryminer.ast.AbstractTypeDeclaration.Archetype;
import org.repositoryminer.ast.MethodDeclaration;
import org.repositoryminer.ast.TypeDeclaration;
import org.repositoryminer.codesmell.CodeSmellId;
import org.repositoryminer.metric.SLOCMetric;
import org.repositoryminer.metric.TCCMetric;
import org.repositoryminer.metric.WMCMetric;

public class BrainClass implements ICommitCodeSmell {
	
	private int wmcThreshold = 47;
	private float tccThreshold = 0.5f;
	private int nbmThreshold = 1;
	private int slocThreshold = 197;
	
	public BrainClass() {}
	
	public BrainClass(int wmcThreshold, float tccThreshold, int nbmThreshold, int slocThreshold) {
		this.wmcThreshold = wmcThreshold;
		this.tccThreshold = tccThreshold;
		this.nbmThreshold = nbmThreshold;
		this.slocThreshold = slocThreshold;
	}

	@Override
	public void detect(AbstractTypeDeclaration type, AST ast, Document document) {
		if (type.getArchetype() == Archetype.CLASS_OR_INTERFACE) {
			TypeDeclaration cls = (TypeDeclaration) type;
			boolean brainClass = detect(ast, type, cls);
			document.append("name", new String(CodeSmellId.BRAIN_CLASS)).append("value", new Boolean(brainClass));
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
		
		brainClass = isGodClass && (wmc >=wmcThreshold && tcc < tccThreshold) && 
					((nbm > nbmThreshold && sloc >= slocThreshold) || (nbm == nbmThreshold && sloc >= (2*slocThreshold) && wmc >= (2*wmcThreshold) ));
		
		return brainClass;
	}

}