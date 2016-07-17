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
import org.repositoryminer.metric.LVARMetric;
import org.repositoryminer.metric.MLOCMetric;
import org.repositoryminer.metric.PARMetric;

public class LongMethod implements ICommitCodeSmell {

	private int ccThreshold = 4;
	private int mlocThreshold = 30;
	private int parThreshold = 4;
	private int lvarThreshold = 8;
	
	public LongMethod() {}
	
	public LongMethod(int ccThreshold, int mlocThreshold, int parThreshold, int lvarThreshold) {
		this.ccThreshold = ccThreshold;
		this.mlocThreshold = mlocThreshold;
		this.parThreshold = parThreshold;
		this.lvarThreshold = lvarThreshold;
	}

	@Override
	public void detect(AbstractTypeDeclaration type, AST ast, ICommitCodeSmellDetectionListener listener) {
		if (type.getArchetype() == Archetype.CLASS_OR_INTERFACE) {
			TypeDeclaration cls = (TypeDeclaration) type;
			
			Map<String, Boolean> detectionsPerMethod = new HashMap<String, Boolean>();

			for(MethodDeclaration method : cls.getMethods()){
				boolean longMethod = detect(method, ast);
				detectionsPerMethod.put(method.getName(), new Boolean(longMethod));
			}

			listener.updateMethodBasedSmellDetection(CodeSmellId.LONG_METHOD, detectionsPerMethod);
		}
	}
	
	public boolean detect(MethodDeclaration method, AST ast){
		boolean longMethod = false;
		
		CCMetric ccMetric = new CCMetric();
		MLOCMetric mlocMetric = new MLOCMetric();
		PARMetric parMetric = new PARMetric();
		LVARMetric lvarMetric = new LVARMetric();
		
		int cc = ccMetric.calculate(method);
		int mloc = mlocMetric.calculate(method, ast);
		int par = parMetric.calculate(method);
		int lvar = lvarMetric.calculate(method);
		
		longMethod = (cc > ccThreshold && mloc > mlocThreshold && par > parThreshold && lvar > lvarThreshold);	
		return longMethod;
	}

}