package br.edu.ufba.softvis.visminer.metric;

import java.util.List;
import java.util.Map;

import br.edu.ufba.softvis.visminer.model.bean.Commit;
import br.edu.ufba.softvis.visminer.model.bean.File;
import br.edu.ufba.softvis.visminer.persistence.MetricPersistance;
/**
 * @author Felipe Gustavo de Souza Gomes (felipegustavo1000@gmail.com)
 * @version 0.9
 * @see LOCMetric
 * Metrics calculations definition.
 */
public interface IMetric {

	public void calculate(Map<File, Object> filesMap, List<Commit> commits, MetricPersistance persistence);
	
}
