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

public class ComplexMethod implements ICommitCodeSmell {
	
	private int ccThreshold = 4;
	
	public ComplexMethod() {}
	
	public ComplexMethod(int ccThreshold) {
		this.ccThreshold = ccThreshold;
	}

	@Override
	public void detect(AbstractTypeDeclaration type, AST ast, ICommitCodeSmellDetectionListener listener) {
		if (type.getArchetype() == Archetype.CLASS_OR_INTERFACE) {
			TypeDeclaration cls = (TypeDeclaration) type;

			Map<String, Boolean> detectionsPerMethod = new HashMap<String, Boolean>();

			for(MethodDeclaration method : cls.getMethods()){
				boolean complexMethod = detect(method);
				detectionsPerMethod.put(method.getName(), new Boolean(complexMethod));
			}

			listener.updateMethodBasedSmellDetection(CodeSmellId.COMPLEX_METHOD, detectionsPerMethod);
		}
	}
	
	public boolean detect(MethodDeclaration method){
		boolean complexMethod = false;
		
		CCMetric ccMetric = new CCMetric();
		
		complexMethod = ccMetric.calculate(method) > ccThreshold;
		
		return complexMethod;
	}

}
