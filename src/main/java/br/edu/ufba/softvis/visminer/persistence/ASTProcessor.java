package br.edu.ufba.softvis.visminer.persistence;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import br.edu.ufba.softvis.visminer.antipattern.IAntiPattern;
import br.edu.ufba.softvis.visminer.ast.AST;
import br.edu.ufba.softvis.visminer.ast.TypeDeclaration;
import br.edu.ufba.softvis.visminer.metric.IMetric;
import br.edu.ufba.softvis.visminer.model.File;
import br.edu.ufba.softvis.visminer.model.Repository;
import br.edu.ufba.softvis.visminer.persistence.dao.MetricDAO;
import br.edu.ufba.softvis.visminer.persistence.impl.MetricDAOImpl;
import br.edu.ufba.softvis.visminer.utility.StringUtils;

/**
 * 
 * @author root
 */
public class ASTProcessor {

	// private SoftwareUnitDAO softUnitDao;
	// private SoftwareUnit projectUnit;
	private Repository repository;

	public Repository getRepository() {
		return this.repository;
	}

	public ASTProcessor(Repository repository) {
		this.repository = repository;
	}


	public void process(File file, AST ast, List<IMetric> metrics, List<IAntiPattern> antiPatterns) {
		MetricDAO dao = new MetricDAOImpl();
		Document doc = new Document();

		List<TypeDeclaration> types = ast.getDocument().getTypes();
		if (types != null) {
			for (TypeDeclaration type : types) {
				String typePath = repository.getPath() + "/" + file.getPath() + "/" + type.getName();
				type.setUid(getUid(typePath));

				Document typeDoc = new Document("uid", type.getUid()).append("name", type.getName()).append("type",
						type.getType().name());

				doc.append("file", file.getUid()).append("type", typeDoc);
				for (IMetric metric : metrics) {
					metric.calculate(type, ast, doc);
				}
				
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
	}

	private String getUid(String path) {
		return StringUtils.sha1(path);
	}

}