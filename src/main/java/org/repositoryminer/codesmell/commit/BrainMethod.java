package org.repositoryminer.codesmell.commit;

import java.util.HashMap;
import java.util.Map;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractTypeDeclaration;
import org.repositoryminer.ast.AbstractTypeDeclaration.Archetype;
import org.repositoryminer.ast.MethodDeclaration;
import org.repositoryminer.ast.TypeDeclaration;
import org.repositoryminer.codesmell.CodeSmellId;
import org.repositoryminer.listener.ICommitCodeSmellDetectionListener;
import org.repositoryminer.metric.CCMetric;
import org.repositoryminer.metric.MLOCMetric;
import org.repositoryminer.metric.MaxNestingMetric;
import org.repositoryminer.metric.NOAVMetric;

public class BrainMethod implements ICommitCodeSmell {
	
	private int mlocThreshold = 65;
	private float ccMlocThreshold = 0.24f;
	private int maxNestingThreshold = 5;
	private int noavThreshold = 5;
	
	public BrainMethod() {}
	
	public BrainMethod(int mlocThreshold, float ccMlocThreshold, int maxNestingThreshold, int noavThreshold) {
		this.mlocThreshold = mlocThreshold;
		this.ccMlocThreshold = ccMlocThreshold;
		this.maxNestingThreshold = maxNestingThreshold;
		this.noavThreshold = noavThreshold;
	}

	@Override
	public void detect(AbstractTypeDeclaration type, AST ast, ICommitCodeSmellDetectionListener listener) {
		if (type.getArchetype() == Archetype.CLASS_OR_INTERFACE) {
			TypeDeclaration cls = (TypeDeclaration) type;
			
			Map<String, Boolean> detectionsPerMethod = new HashMap<String, Boolean>();

			for(MethodDeclaration method : cls.getMethods()){
				boolean brainMethod = detect(method, ast);
				detectionsPerMethod.put(method.getName(), new Boolean(brainMethod));
			}

			listener.updateMethodBasedSmellDetection(CodeSmellId.BRAIN_METHOD, detectionsPerMethod);
		}
	}
	
	public boolean detect(MethodDeclaration method, AST ast){
		boolean brainMethod = false;
		
		MLOCMetric mlocMetric = new MLOCMetric();
		CCMetric ccMetric = new CCMetric();
		NOAVMetric noavMetric = new NOAVMetric();
		MaxNestingMetric maxNestingMetric = new MaxNestingMetric();
		
		int cc = ccMetric.calculate(method);
		int mloc = mlocMetric.calculate(method, ast);
		int noav = noavMetric.calculate(method);
		int maxNesting = maxNestingMetric.calculate(method);
		
		brainMethod = (mloc>mlocThreshold) && (cc/mloc >= ccMlocThreshold) && (maxNesting >= maxNestingThreshold && noav > noavThreshold);
		
		return brainMethod;
	}

}

