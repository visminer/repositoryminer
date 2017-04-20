package org.repositoryminer.codesmell.direct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractClassDeclaration;
import org.repositoryminer.ast.MethodDeclaration;
import org.repositoryminer.ast.Statement;
import org.repositoryminer.ast.Statement.NodeType;
import org.repositoryminer.codemetric.CodeMetricId;
import org.repositoryminer.codemetric.direct.ATFD;
import org.repositoryminer.codemetric.direct.FDP;
import org.repositoryminer.codemetric.direct.LAA;
import org.repositoryminer.codesmell.CodeSmellId;

/**
 * <h1>Feature Envy</h1>
 * <p>
 * The Feature Envy code smell refers to methods that seem more interested
 * in the data of other classes than that of their own class. These methods access
 * directly or via accessor methods a lot of data of other classes. This might be 
 * a sign that the method was misplaced and that it should be moved to another class.
 * <p>
 * The expression used to evaluate if a method is affected by Feature Envy is:<br>
 * FeatureEnvy = (ATFD > FEW) AND (LAA < ONE THIRD) AND (FDP <= FEW)
 * </i>
 * <p>
 * The metrics used are:
 * <ul>
 * <li>LAA: Locality Attribute Accesses</li>
 * <li>ATFD: Access To Foreign Data</li>
 * <li>FDP: Foreign Data Provider</li>
 * </ul>
 * The default thresholds used are:
 * <ul>
 * <li>laaThreshold = 1 / 3f</li>
 * <li>atfdThreshold = 3</li>
 * <li>fdpThreshold = 1/3</li>
 * </ul>
 */
public class FeatureEnvy implements IDirectCodeSmell {

	private float laaThreshold = 1 / 3f;
	private int atfdThreshold = 3;
	private int fdpThreshold = 3;
	
	private LAA laaMetric = new LAA();
	private ATFD atfdMetric = new ATFD();
	private FDP fdpMetric = new FDP();
	
	public FeatureEnvy() {
	
	}
	
	public FeatureEnvy(float laaThreshold, int atfdThreshold, int fdpThreshold) {
		super();
		this.laaThreshold = laaThreshold;
		this.atfdThreshold = atfdThreshold;
		this.fdpThreshold = fdpThreshold;
	}

	@Override
	public Document detect(AbstractClassDeclaration type, AST ast) {
		List<Document> methodsFeatureEnvy = new ArrayList<Document>();

		for (MethodDeclaration methodDeclaration : type.getMethods()) {
			float laaValue = laaMetric.calculate(type, methodDeclaration);
			int fdpValue = fdpMetric.calculate(type, methodDeclaration);

			List<MethodDeclaration> methods = new ArrayList<MethodDeclaration>();
			methods.add(methodDeclaration);
			int atfdValue = atfdMetric.calculate(type,methods,true);
			
			if (detect(laaValue, atfdValue, fdpValue)) {
				Document metrics = new Document(CodeMetricId.LAA.toString(), laaValue)
						.append(CodeMetricId.FDP.toString(), fdpValue)
						.append(CodeMetricId.ATFD.toString(), atfdValue);
				List<Document> accessedClasses = getAccessedClassesDocument(type, methodDeclaration);
				Document mDoc = new Document("signature", methodDeclaration.getName()).append("metrics", metrics).append("accessedClasses", accessedClasses);
				methodsFeatureEnvy.add(mDoc);
			}
			
		}
		
		if (methodsFeatureEnvy.size() > 0) {
			return new Document("codesmell", CodeSmellId.FEATURE_ENVY.toString()).append("methods", methodsFeatureEnvy);
		}
		
		return null;
	}

	@Override
	public CodeSmellId getId() {
		return CodeSmellId.FEATURE_ENVY;
	}

	@Override
	public Document getThresholds() {
		Document doc = new Document();
		doc.append(CodeMetricId.LAA.toString(), laaThreshold);
		doc.append(CodeMetricId.FDP.toString(), fdpThreshold);
		doc.append(CodeMetricId.ATFD.toString(), atfdThreshold);

		return new Document("codesmell", CodeSmellId.FEATURE_ENVY.toString()).append("thresholds", doc);
	}

	/**
	 * Detection Strategy for Feature Envy
	 * (ATFD > FEW) AND (LAA < ONE THIRD) AND (FDP <= FEW)
	 * @param laaValue
	 * @param atfdValue
	 * @param fdpValue
	 * @return
	 */
	private boolean detect(float laaValue, int atfdValue, int fdpValue) {
		return (atfdValue > atfdThreshold && laaValue < laaThreshold && fdpValue <= fdpThreshold);
		
	}
	
	/**
	 * Returns the classes in which the attributes was accessed. 
	 * It is necessary for visualization. The classes represent the right nodes in the metaphor visualization.
	 * @param currType
	 * @param methodDeclaration
	 * @return Map<String, Integer>
	 */
	private Map<String, Integer> getAccessedClasses(AbstractClassDeclaration currType,
			MethodDeclaration methodDeclaration) {
			
		Map<String, Integer> accessedClasses = new HashMap<String, Integer>(); 

		for (Statement stmt : methodDeclaration.getStatements()) {
			String accessedClass=null, exp, type;
			 
			if (stmt.getNodeType().equals(NodeType.FIELD_ACCESS) || stmt.getNodeType().equals(NodeType.METHOD_INVOCATION)) {
				exp = stmt.getExpression();
				if( stmt.getNodeType().equals(NodeType.METHOD_INVOCATION)){
					exp = exp.substring(0, exp.indexOf("("));
				}
				type = exp.substring(0, exp.lastIndexOf("."));
			} else {
				continue;
			}

			if (stmt.getNodeType().equals(NodeType.FIELD_ACCESS)) {
				if (!currType.getName().equals(type))
					accessedClass = type.toLowerCase();
			} else if (stmt.getNodeType().equals(NodeType.METHOD_INVOCATION)) {
				String methodInv = exp.substring(exp.lastIndexOf(".") + 1);
				if (!currType.getName().equals(type)) {
					if ((methodInv.startsWith("get") || methodInv.startsWith("set")) && methodInv.length() > 3) {
						accessedClass = type.toLowerCase();
					} else if (methodInv.startsWith("is") && methodInv.length() > 2)
						accessedClass = type.toLowerCase();
				}
			}
			
			if (accessedClass != null) {
				if (accessedClasses.containsKey(accessedClass)){
					Integer value = accessedClasses.get(accessedClass);
					accessedClasses.replace(accessedClass,++value);
				}else{
					accessedClasses.put(accessedClass, 1);
				}
			}
						
		}

		return accessedClasses;
	}
	
	/**
	 * Returns the document with the accessed classes for persist in mongoDB.
	 * @param currType
	 * @param methodDeclaration
	 * @return List<Document>
	 */
	private List<Document> getAccessedClassesDocument(AbstractClassDeclaration currType,
			MethodDeclaration methodDeclaration){
		List<Document> docs = new ArrayList<Document>();
		
		Map<String, Integer> accessedClasses = getAccessedClasses(currType, methodDeclaration);
		
		for(Map.Entry<String, Integer> entry: accessedClasses.entrySet()){
			Document mDoc = new Document();
			mDoc.append("name",entry.getKey().toString()).append("value",entry.getValue().toString());
			docs.add(mDoc);
		}
		
		return docs;
	}

	
}
