package org.repositoryminer.codesmell.commit;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractTypeDeclaration;
import org.repositoryminer.listener.ICommitCodeSmellDetectionListener;

public interface ICommitCodeSmell {
	
	public void detect(AbstractTypeDeclaration type, AST ast, ICommitCodeSmellDetectionListener listener);
	
}
