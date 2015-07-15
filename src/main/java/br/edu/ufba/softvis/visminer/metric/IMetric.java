package br.edu.ufba.softvis.visminer.metric;

import java.util.List;
import java.util.Map;

import br.edu.ufba.softvis.visminer.ast.AST;
import br.edu.ufba.softvis.visminer.model.database.CommitDB;
import br.edu.ufba.softvis.visminer.model.database.FileDB;
import br.edu.ufba.softvis.visminer.persistence.MetricPersistance;
/**
 * @version 0.9
 * @see LOCMetric
 * Metrics calculations definition.
 */
public interface IMetric {

	public void calculate(Map<FileDB, AST> filesMap, List<CommitDB> commits, MetricPersistance persistence);
	
}
