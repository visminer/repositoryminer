package org.repositoryminer.codesmell.clazz;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractTypeDeclaration;
import org.repositoryminer.ast.AbstractTypeDeclaration.Archetype;
import org.repositoryminer.ast.MethodDeclaration;
import org.repositoryminer.ast.TypeDeclaration;
import org.repositoryminer.codesmell.CodeSmellId;
import org.repositoryminer.metric.clazz.CYCLO;
import org.repositoryminer.metric.clazz.LVAR;
import org.repositoryminer.metric.clazz.MLOC;
import org.repositoryminer.metric.clazz.PAR;

public class LongMethod implements IClassCodeSmell {

	private List<Document> methodsDoc;
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
	public void detect(AbstractTypeDeclaration type, AST ast, Document document) {
		if (type.getArchetype() == Archetype.CLASS_OR_INTERFACE) {
			TypeDeclaration cls = (TypeDeclaration) type;
			
			methodsDoc = new ArrayList<Document>();

			for(MethodDeclaration method : cls.getMethods()){
				boolean longMethod = detect(method, ast);
				methodsDoc.add(new Document("method", method.getName()).append("value", new Boolean(longMethod)));
			}

			document.append("name", CodeSmellId.LONG_METHOD).append("methods", methodsDoc);
		}
	}
	
	public boolean detect(MethodDeclaration method, AST ast){
		boolean longMethod = false;
		
		CYCLO ccMetric = new CYCLO();
		MLOC mlocMetric = new MLOC();
		PAR parMetric = new PAR();
		LVAR lvarMetric = new LVAR();
		
		int cc = ccMetric.calculate(method);
		int mloc = mlocMetric.calculate(method, ast);
		int par = parMetric.calculate(method);
		int lvar = lvarMetric.calculate(method);
		
		longMethod = (cc > ccThreshold && mloc > mlocThreshold && par > parThreshold && lvar > lvarThreshold);	
		return longMethod;
	}

}