package org.repositoryminer.codemetric.direct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractClassDeclaration;
import org.repositoryminer.ast.ClassArchetype;
import org.repositoryminer.ast.ClassDeclaration;
import org.repositoryminer.ast.MethodDeclaration;
import org.repositoryminer.ast.SuperClassDeclaration;
import org.repositoryminer.codemetric.CodeMetricId;

public class EC implements IDirectCodeMetric {

	private Map<String, Integer> typeECMap = new HashMap<>();
	
	@Override
	public Document calculate(AbstractClassDeclaration type, AST ast) {
		typeECMap.clear();
		Document doc = new Document("metric", getId().toString());
		if(type.getArchetype() == ClassArchetype.CLASS_OR_INTERFACE){
			calculateSuperEC(type);
			calculateFieldEC(type);
			calculateMethodEC(type);
			
			List<Document> classesDocument = new ArrayList<>();
			typeECMap.entrySet().stream().forEach( entry -> classesDocument.add(new Document("class",entry.getKey()).append("value", entry.getValue())));
			doc.append("classes", classesDocument)
			
			.append("efferentCount", typeECMap.keySet().size());
		}
		return doc;
	}

	
	private void calculateSuperEC(AbstractClassDeclaration type) {
		ClassDeclaration clazz = (ClassDeclaration)type;
		SuperClassDeclaration sclazz = clazz.getSuperClass();
		if(sclazz != null){
			increaseECCount(sclazz.getName());
			if(sclazz.isGeneric())
				sclazz.getParameters().forEach( parameter -> increaseECCount(parameter));
		}
		clazz.getInterfaces().forEach( inter -> increaseECCount(inter.getName()));
	}


	public Map<String, Integer> calculate (AbstractClassDeclaration type){
		typeECMap.clear();
		calculateSuperEC(type);
		calculateFieldEC(type);
		calculateMethodEC(type);
		Map<String, Integer> calculo = new HashMap<>(typeECMap);
		typeECMap.clear();
		return calculo;
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
		if(key == null)
			return;
		typeECMap.put(key, typeECMap.getOrDefault(key, 0) + 1);		
	}

	private void calculateMethodEC(AbstractClassDeclaration type) {
		for(MethodDeclaration md: type.getMethods()){
			md.getParameters().stream().forEach( param -> {
				if(param.isPrimitiveType() || param.isGeneric())
					return;
				if(param.isArrayType())
					increaseECCount(param.getArrayTypeName());
				else if (param.isParametrizedType()) {
					param.getParamTypes().forEach(parameter -> increaseECCount(parameter));
				}
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