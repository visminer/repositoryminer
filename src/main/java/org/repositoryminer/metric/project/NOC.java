package org.repositoryminer.metric.project;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractTypeDeclaration;
import org.repositoryminer.metric.MetricId;
import org.repositoryminer.parser.IParser;

public class NOC implements IProjectMetric {

	private Map<String, String> classes;

	@Override
	public void calculate(List<IParser> parsers, String repositoryPath, String charset, Document document) {
		List<Document> classDocs = new ArrayList<Document>();
		
		for (IParser parser : parsers) {
			classes = new HashMap<String, String>();
			Collection<File> files = FileUtils.listFiles(new File(repositoryPath), parser.getExtensions(), true);

			for (File f : files)
				analyzeFile(parser, f, charset);

			List<Document> foundClassesDoc = new ArrayList<Document>();
			for (Entry<String, String> entry : classes.entrySet()) {
				foundClassesDoc.add(new Document("name", entry.getKey()).append("type", entry.getValue()));
			}

			Document doc = new Document();
			doc.append("language", parser.getLanguage().toString()).append("quantity", classes.size()).append("classes",
					foundClassesDoc);
			classDocs.add(doc);
		}

		document.append("name", MetricId.NOC.toString()).append("value", classDocs);
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

		for (AbstractTypeDeclaration type : ast.getDocument().getTypes()) {
			classes.put(type.getName(), type.getArchetype().toString());
		}
	}

	@Override
	public MetricId getId() {
		return MetricId.NOC;
	}

}