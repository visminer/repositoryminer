package br.edu.ufba.softvis.visminer.metric;

import java.util.List;
import java.util.Map;

import br.edu.ufba.softvis.visminer.annotations.MetricAnnotation;
import br.edu.ufba.softvis.visminer.constant.MetricType;
import br.edu.ufba.softvis.visminer.constant.MetricUid;
import br.edu.ufba.softvis.visminer.model.bean.Commit;
import br.edu.ufba.softvis.visminer.model.bean.File;
import br.edu.ufba.softvis.visminer.model.bean.Repository;
import br.edu.ufba.softvis.visminer.persistence.MetricPersistance;

@MetricAnnotation(
	name = "Ciclomatic Complexity",
	description = "Cyclomatic complexity is a software metric, used to indicate the complexity of a"
	+ " program. It is a quantitative measure of the number of linearly independent paths through a"
	+ " program's source code.",
	acronym = "CC",
	type = MetricType.SIMPLE,
	uid = MetricUid.CC
)
public class CCMetric implements IMetric{

	@Override
	public void calculate(Map<File, Object> filesMap, List<Commit> commits, Repository repository,
			MetricPersistance persistence){
	}


}
