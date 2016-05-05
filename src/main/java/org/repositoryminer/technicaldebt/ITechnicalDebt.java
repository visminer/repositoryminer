package org.repositoryminer.technicaldebt;

import java.util.List;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractTypeDeclaration;

public interface ITechnicalDebt {

	public void detect(AbstractTypeDeclaration type, AST ast, List<Document> antiPatterns, Document document);
}
