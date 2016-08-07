package org.repositoryminer.codesmell.project;

import java.util.List;
import java.util.TreeMap;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.parser.Parser;

/**
 * This interface defines how to implement code smell detection in project level.
 */
public interface IProjectCodeSmell {

	TreeMap<String, AST> astMap = new TreeMap<String, AST>();

	public void detect(List<Parser> parsers, String repositoryPath, Document document);

}