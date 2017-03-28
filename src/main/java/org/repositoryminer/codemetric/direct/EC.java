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
		type.getFields().stream().forEach(field ->  {
			if(field.isGeneric() || field.isPrimitiveType()){
				return;
			}
			if(field.isArrayType())				
				increaseECCount(field.getArrayTypeName());
			else if(field.isParametrizedType())
				field.getParamTypes().forEach(param -> increaseECCount(param));
			else
				increaseECCount(field.getType());
				
		});
	}
	
	private void increaseECCount(String key){
		
		typeECMap.put(key, typeECMap.getOrDefault(key, 0) + 1);		
	}

	private void calculateMethodEC(AbstractClassDeclaration type) {
		for(MethodDeclaration md: type.getMethods()){
			md.getParameters().stream().forEach( param -> {
				if(param.isPrimitiveType() || param.isGeneric())
					return;
				if(param.isArrayType())
					increaseECCount(param.getArrayTypeName());
				else if (param.isParametrizedType())
					param.getParamTypes().forEach(parameter -> increaseECCount(parameter));
				else 
					increaseECCount(param.getType());
			});
			if(!md.isReturnGeneric() && !md.isReturnPrimitive() && !md.isConstructor()){
				if(md.isReturnArray())
					increaseECCount(md.getReturnArrayType());
				else if(md.isReturnParametrized())
					md.getReturnParameters().forEach(parameter -> increaseECCount(parameter));
				else
					increaseECCount(md.getReturnType());
			}
			md.getThrownsExceptions().stream().forEach( thr -> increaseECCount(thr));
		}
	}

	@Override
	public CodeMetricId getId() {
		return CodeMetricId.EC;
	}
}