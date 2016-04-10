package br.edu.ufba.softvis.visminer.antipattern;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import br.edu.ufba.softvis.visminer.annotations.AntiPatternAnnotation;
import br.edu.ufba.softvis.visminer.ast.AST;
import br.edu.ufba.softvis.visminer.ast.ClassOrInterfaceDeclaration;
import br.edu.ufba.softvis.visminer.ast.MethodDeclaration;
import br.edu.ufba.softvis.visminer.ast.TypeDeclaration;
import br.edu.ufba.softvis.visminer.constant.SoftwareUnitType;
import br.edu.ufba.softvis.visminer.metric.CCMetric;
import br.edu.ufba.softvis.visminer.metric.LVARMetric;
import br.edu.ufba.softvis.visminer.metric.MLOCMetric;
import br.edu.ufba.softvis.visminer.metric.PARMetric;

@AntiPatternAnnotation(name = "Long Method", description = "A Long Method characterizes the methods of software that contains "
		+ "many lines of code, as well as a lot of parameters and local variables, and a high cyclomatic complexity.")
public class LongMethod implements IAntiPattern {

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
	public void detect(TypeDeclaration type, AST ast, Document document) {
		if (type.getType() == SoftwareUnitType.CLASS_OR_INTERFACE) {
			ClassOrInterfaceDeclaration cls = (ClassOrInterfaceDeclaration) type;
			
			methodsDoc = new ArrayList<Document>();

			for(MethodDeclaration method : cls.getMethods()){
				boolean longMethod = detect(method, ast);
				methodsDoc.add(new Document("method", method.getName()).append("value", new Boolean(longMethod)));
			}

			document.append("name", new String("Long Method")).append("methods", methodsDoc);
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
