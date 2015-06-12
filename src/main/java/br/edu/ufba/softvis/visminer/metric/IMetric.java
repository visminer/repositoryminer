package br.edu.ufba.softvis.visminer.metric;

import java.util.Map;

import br.edu.ufba.softvis.visminer.model.bean.Commit;
import br.edu.ufba.softvis.visminer.model.bean.File;
import br.edu.ufba.softvis.visminer.persistence.MetricPersistance;
import br.edu.ufba.softvis.visminer.utility.DetailAST;

public interface IMetric {

	public void calculate(Map<File, DetailAST> filesMap, Commit commitPrev, MetricPersistance persistence);
	
}
