package br.edu.ufba.softvis.visminer.metric;

import java.util.List;
import java.util.Map;

import br.edu.ufba.softvis.visminer.annotations.MetricAnnotation;
import br.edu.ufba.softvis.visminer.constant.MetricType;
import br.edu.ufba.softvis.visminer.constant.MetricUid;
import br.edu.ufba.softvis.visminer.constant.SoftwareUnitType;
import br.edu.ufba.softvis.visminer.model.bean.Commit;
import br.edu.ufba.softvis.visminer.model.bean.File;
import br.edu.ufba.softvis.visminer.model.bean.SoftwareUnit;
import br.edu.ufba.softvis.visminer.persistence.MetricPersistance;

@MetricAnnotation(
		uid = MetricUid.LOC,
		name = "Lines of code",
		description = "This Metric calculates number of code lines classes and packages",
		acronym = "LOC", 
		type = MetricType.SIMPLE
		)
public class LOCMetric implements IMetric{

	@Override
	public void calculate(Map<File, Object> filesMap, List<Commit> commits,
			MetricPersistance persistence) {

		SoftwareUnit s = new SoftwareUnit(0, "sdsdfsf", "sdsdfdsf", "", SoftwareUnitType.PROJECT);
		persistence.saveSoftwareUnit(null, s);

	}


}
