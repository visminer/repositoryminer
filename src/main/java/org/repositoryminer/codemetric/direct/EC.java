package org.repositoryminer.codemetric.direct;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractClassDeclaration;
import org.repositoryminer.ast.ClassArchetype;
import org.repositoryminer.ast.MethodDeclaration;
import org.repositoryminer.codemetric.CodeMetricId;

public class EC implements IDirectCodeMetric {

	private Map<String, Integer> typeECMap = new HashMap<>();
	
	@Override
	public Document calculate(AbstractClassDeclaration type, AST ast) {
		typeECMap.clear();
		Document doc = new Document("metric", getId().toString());
		if(type.getArchetype() == ClassArchetype.CLASS_OR_INTERFACE){
			calculateImports(type,ast);
			calculateFieldEC(type);
			calculateMethodEC(type);
			
			List<Document> classesDocument = new ArrayList<>();
			typeECMap.entrySet().stream().forEach( entry -> classesDocument.add(new Document("class",entry.getKey()).append("value", entry.getValue())));
			doc.append("classes", classesDocument);
		}
		return doc;
	}

	
	private void calculateImports(AbstractClassDeclaration type, AST ast) {
		ast.getDocument().getImports().stream().forEach( imporT -> increaseECCount(imporT.getName()));
	}


	public Map<String, Integer> calculate (AbstractClassDeclaration type){
		typeECMap.clear();

		calculateFieldEC(type);
		calculateMethodEC(type);
		return typeECMap;
	}
	
	private void calculateFieldEC(AbstractClassDeclaration type) {
		type.getFields().stream().forEach(field ->  increaseECCount(field.getType()));
	}
	
	private void increaseECCount(String key){
		
		typeECMap.put(key, typeECMap.getOrDefault(key, 0) + 1);		
	}

	private void calculateMethodEC(AbstractClassDeclaration type) {
		for(MethodDeclaration md: type.getMethods()){
			md.getParameters().stream().forEach( param -> increaseECCount(param.getType()));
			increaseECCount(md.getReturnType());
			md.getThrownsExceptions().stream().forEach( thr -> increaseECCount(thr));
		}
	}

	@Override
	public CodeMetricId getId() {
		return CodeMetricId.EC;
	}
}