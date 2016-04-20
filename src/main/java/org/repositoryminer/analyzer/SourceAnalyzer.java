package org.repositoryminer.analyzer;

import java.io.UnsupportedEncodingException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractTypeDeclaration;
import org.repositoryminer.metric.IMetric;
import org.repositoryminer.model.Commit;
import org.repositoryminer.model.Diff;
import org.repositoryminer.parser.IParser;
import org.repositoryminer.scm.SCM;
import org.repositoryminer.scm.SCMRepository;
import org.repositoryminer.utility.HashHandler;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;

public class SourceAnalyzer {

	private SCM scm;
	private SCMRepository repository;
	private Map<String, IParser> parserMap;

	public SourceAnalyzer(SCMRepository repository, SCM scm){
		this.scm = scm;
		this.repository = repository;

		parserMap = new HashMap<String, IParser>();
		for(IParser parser : repository.getParser()){
			for(String ext : parser.getExtensions())
				parserMap.put(ext, parser);
		}
	}

	public void analyze(Commit commit) {
		scm.checkout(commit.getId());
		for (Diff diff : commit.getDiffs())
			processAST(diff.getNewPath(), commit.getId());
	}

	private void processAST(String file, String commit) {
		int index = file.lastIndexOf(".") + 1;
		String ext = file.substring(index);

		IParser parser = parserMap.get(ext);
		if (parser == null) return;

		try{

			byte[] data = scm.getData(commit, file);
			if(data == null) return;

			String source = new String(data, repository.getCharset());
			AST ast = parser.generate(file, source);

		} catch(NullPointerException | UnsupportedEncodingException e){
			e.printStackTrace();
		}
	}
/**
	public void process(String commit, String file, AST ast) {

		Document doc = new Document();
		doc.append("commit", commit);
		doc.append("file", file);

		List<AbstractTypeDeclaration> types = ast.getDocument().getTypes();
		BasicDBList dbList = new BasicDBList();
		for (AbstractTypeDeclaration type : types) {
			Document typeDoc = new Document();
			
			String uid = repository.getPath() + "/" + file + "/" + type.getName();
			typeDoc.append("name", type.getName()).append("declaration", type.getType().toString()).
			append("uid", HashHandler.SHA1(uid));

			List<Document> metricsDoc = new ArrayList<Document>();
			for (IMetric metric : repository.getMetrics()) {
				metric.calculate(type, ast, typeDoc);
			}
			doc.append("metrics", )
			
			List<Document> antiPatternsDoc = new ArrayList<Document>();
			for (IAntiPattern antiPattern : antiPatterns) {
				Document apDoc = new Document();
				antiPattern.detect(type, ast, apDoc);
				antiPatternsDoc.add(apDoc);
			}
			doc.append("antipatterns", antiPatternsDoc);
		}

		dao.save(doc);
	}
**/
}