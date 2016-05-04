package org.repositoryminer.technicaldebt;

import java.util.List;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.TypeDeclaration;

public interface ITechnicalDebt {

	public void detect(TypeDeclaration type, AST ast, List<Document> antiPatterns, Document document);
}
