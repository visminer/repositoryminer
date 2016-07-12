package org.repositoryminer.codesmell.snapshot;

import java.util.List;
import java.util.TreeMap;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.parser.Parser;

public interface ISnapshotCodeSmell {

	TreeMap<String, AST> astMap = new TreeMap<String, AST>();

	public void detect(List<Parser> parsers, String repositoryPath, Document document);

}