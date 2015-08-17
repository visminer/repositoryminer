package br.edu.ufba.softvis.visminer.metric;

import java.util.List;

import br.edu.ufba.softvis.visminer.ast.AST;
import br.edu.ufba.softvis.visminer.model.business.Commit;
import br.edu.ufba.softvis.visminer.persistence.MetricPersistance;

/**
 * Metrics calculations definition.
 */
public interface IMetric {

	public void calculate(List<AST> astList, List<Commit> commits, MetricPersistance persistence);
	
}
