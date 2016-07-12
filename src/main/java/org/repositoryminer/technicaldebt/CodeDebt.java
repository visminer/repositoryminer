package org.repositoryminer.technicaldebt;

import java.util.List;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractTypeDeclaration;
import org.repositoryminer.ast.AbstractTypeDeclaration.Archetype;
import org.repositoryminer.codesmell.CodeSmellId;

public class CodeDebt implements ITechnicalDebt {
	
	@Override
	public void detect(AbstractTypeDeclaration type, AST ast, List<Document> codeSmells, Document document) {
		if (type.getArchetype() == Archetype.CLASS_OR_INTERFACE) {
			
			boolean isCodeDebt = false;
			for (Document codeSmell : codeSmells) {
				if (codeSmell.containsValue(CodeSmellId.GOD_CLASS)) {
					if (codeSmell.getBoolean("value")) {
						isCodeDebt = true;
					}					
				}
				//TODO: Implements duplicated code detection and check for its existence when detect CodeDebt 				
			}
			document.append("name", TechnicalDebtId.CODE_DEBT)
			.append("value", new Boolean(isCodeDebt))
			.append("status", 0);
		}
	}

	public boolean detect(Document document) {
		return false;
	}

}
