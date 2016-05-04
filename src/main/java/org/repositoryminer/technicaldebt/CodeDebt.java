package org.repositoryminer.technicaldebt;

import java.util.List;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.DeclarationType;
import org.repositoryminer.ast.TypeDeclaration;

public class CodeDebt implements ITechnicalDebt {
	
	public CodeDebt() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void detect(TypeDeclaration type, AST ast, List<Document> codeSmells, Document document) {
		if (type.getType() == DeclarationType.CLASS_OR_INTERFACE) {
			
			boolean isCodeDebt = false;
			for (Document codeSmell : codeSmells) {
				if (codeSmell.containsKey("GodClass")) {
					if (codeSmell.getBoolean("GodClass")) {
						isCodeDebt = true;
					}					
				}
				//TODO: Implements duplicated code detection and check for its existence when detect CodeDebt 				
			}
			document.append("name", "Code Debt")
			.append("value", new Boolean(isCodeDebt))
			.append("status", 0);
		}
	}

	public boolean detect(Document document) {
		return false;
	}

}
