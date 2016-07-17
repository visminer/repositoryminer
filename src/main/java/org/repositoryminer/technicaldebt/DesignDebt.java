package org.repositoryminer.technicaldebt;

import java.util.List;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractTypeDeclaration;
import org.repositoryminer.ast.AbstractTypeDeclaration.Archetype;
import org.repositoryminer.codesmell.CodeSmellId;
import org.repositoryminer.listener.ITechnicalDebtDetectionListener;

public class DesignDebt implements ITechnicalDebt {

	@SuppressWarnings("unchecked")
	@Override
	public void detect(AbstractTypeDeclaration type, AST ast, List<Document> antiPatterns, ITechnicalDebtDetectionListener listener) {
		if (type.getArchetype() == Archetype.CLASS_OR_INTERFACE) {
			boolean isDesignDebt = false;
			for (Document antiPattern : antiPatterns) {
				if (antiPattern.containsValue(CodeSmellId.GOD_CLASS)) {
					if (antiPattern.getBoolean("value")) {
						isDesignDebt = true;
					}
				}
				if (antiPattern.containsValue(CodeSmellId.LONG_METHOD)) {
					List<Document> methods = ((List<Document>) antiPattern.get("methods"));
					for (Document method : methods) {
						if (method.getBoolean("value")) {
							isDesignDebt = true;
						}
					}
				}
			}
			listener.updateDebtDetection(TechnicalDebtId.DESIGN_DEBT, new Boolean(isDesignDebt));
		}
	}

	public boolean detect(Document document) {
		return false;
	}

}