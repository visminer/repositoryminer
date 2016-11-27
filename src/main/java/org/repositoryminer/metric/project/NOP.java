package org.repositoryminer.metric.project;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.metric.MetricId;
import org.repositoryminer.parser.IParser;

public class NOP implements IProjectClass {

	private Set<String> packages;
	
	@Override
	public void calculate(List<IParser> parsers, String repositoryPath, String charset, Document document) {
		List<Document> packageDocs = new ArrayList<Document>();
		
		for (IParser parser : parsers) {
			packages = new HashSet<String>();
			Collection<File> files = FileUtils.listFiles(new File(repositoryPath), parser.getExtensions(), true);
			
			for (File f : files) 
				analyzeFile(parser, f, charset);
			
			Document doc = new Document();
			doc.append("language", parser.getLanguage().toString()).append("quantity", packages.size()).append("packages", packages);
			packageDocs.add(doc);
		}
		
		document.append("name", MetricId.NOP.toString()).append("value", packageDocs);
	}

	private void analyzeFile(IParser parser, File file, String charset) {
		String text;
		try {
			text = FileUtils.readFileToString(file, charset);
		} catch (IOException e) {
			return;
		}
		
		AST ast = parser.generate(file.getAbsolutePath(), text, charset);
		if (ast == null)
			return;
		
		packages.add(ast.getDocument().getPackageDeclaration());
	}

	@Override
	public MetricId getId() {
		return MetricId.NOP;
	}

}
