package org.repositoryminer.codesmell.commit;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractTypeDeclaration;

public interface ICommitCodeSmell {
	
	public void detect(AbstractTypeDeclaration type, AST ast, Document document);
}
