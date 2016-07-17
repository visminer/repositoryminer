package org.repositoryminer.technicaldebt;

import java.util.List;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractTypeDeclaration;
import org.repositoryminer.ast.AbstractTypeDeclaration.Archetype;
import org.repositoryminer.codesmell.CodeSmellId;
import org.repositoryminer.listener.ITechnicalDebtDetectionListener;

public class CodeDebt implements ITechnicalDebt {
	
	@Override
	public void detect(AbstractTypeDeclaration type, AST ast, List<Document> codeSmells, ITechnicalDebtDetectionListener listener) {
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
			listener.updateDebtDetection(TechnicalDebtId.CODE_DEBT, new Boolean(isCodeDebt));
		}
	}

	public boolean detect(Document document) {
		return false;
	}

}
