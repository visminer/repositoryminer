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
		name = "Number of Classes",
		description = "Number of Classes (NOC) is a software metric used to mesure the number of "
				+ "classes in a package and in a project.",
		acronym = "NOC",
		type = MetricType.COMPLEX,
		uid = MetricUid.NOC
	)
public class NOCMetric implements IMetric{

	@Override
	public void calculate(Map<File, Object> filesMap, List<Commit> commits, Repository repository,
			MetricPersistance persistence) {
		
	}

}
