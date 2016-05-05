package org.repositoryminer.technicaldebt;

import java.util.List;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractTypeDeclaration;
import org.repositoryminer.ast.DeclarationType;

public class DesignDebt implements ITechnicalDebt {
	
	@SuppressWarnings("unchecked")
	@Override
	public void detect(AbstractTypeDeclaration type, AST ast, List<Document> antiPatterns, Document document) {
		if (type.getType() == DeclarationType.CLASS_OR_INTERFACE) {
			
			boolean isDesignDebt = false;
			for (Document antiPattern : antiPatterns) {
				if (antiPattern.containsKey("GodClass")) {
					if (antiPattern.getBoolean("GodClass")) {
						isDesignDebt = true;
					}					
				}
				if (antiPattern.containsKey("LongMethod")) {
					List<Document> methods = ((List<Document>) antiPattern.get("methods"));
					for (Document method : methods) {
						if (method.getBoolean("value")) {
							isDesignDebt = true;
						}
					}
				}				
			}
			document.append("name", "Design Debt")
			.append("value", new Boolean(isDesignDebt))
			.append("status", 0);
		}
	}

	public boolean detect(Document document) {
		return false;
	}
	
}
