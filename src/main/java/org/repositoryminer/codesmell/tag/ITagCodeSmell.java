package org.repositoryminer.codesmell.tag;

import java.util.List;
import java.util.TreeMap;

import org.repositoryminer.ast.AST;
import org.repositoryminer.listener.ITagCodeSmellDetectionListener;
import org.repositoryminer.parser.Parser;

public interface ITagCodeSmell {

	TreeMap<String, AST> astMap = new TreeMap<String, AST>();

	public void detect(List<Parser> parsers, String repositoryPath, ITagCodeSmellDetectionListener listener);

}