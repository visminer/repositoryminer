package br.edu.ufba.softvis.visminer.metric;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jdt.core.dom.CompilationUnit;

import br.edu.ufba.softvis.visminer.annotations.MetricAnnotation;
import br.edu.ufba.softvis.visminer.constant.MetricId;
import br.edu.ufba.softvis.visminer.constant.SoftwareUnitType;
import br.edu.ufba.softvis.visminer.model.bean.Commit;
import br.edu.ufba.softvis.visminer.model.bean.File;
import br.edu.ufba.softvis.visminer.model.bean.SoftwareUnit;
import br.edu.ufba.softvis.visminer.persistence.MetricPersistance;
import br.edu.ufba.softvis.visminer.utility.DetailAST;

@MetricAnnotation(
		id = MetricId.LOC,
		name = "Lines of code",
		description = "This Metric calculates number of code lines classes and packages",
		acronym = "LOC"
		)
public class LOCMetric implements IMetric{

	@Override
	public void calculate(Map<File, DetailAST> filesMap, Commit commitPrev, MetricPersistance persistence) {

		Map<String, SoftwareUnit> packMap = new HashMap<String, SoftwareUnit>();
		Map<String, Integer> packLocMap = new HashMap<String, Integer>();

		for(Entry<File, DetailAST> entry : filesMap.entrySet()){

			CompilationUnit ast = entry.getValue().getRoot();
			if(ast == null){
				continue;
			}

			if(ast.getPackage() != null){

				String packName = ast.getPackage().getName().toString();
				SoftwareUnit pack = null;
				if(!packMap.containsKey(packName)){

					SoftwareUnit unit = new SoftwareUnit();
					unit.setType(SoftwareUnitType.PACKAGE);
					unit.setFullName(packName);

					int index = packName.lastIndexOf(".")+1;
					unit.setName(packName.substring(index));

					pack = persistence.saveSoftwareUnit(null, unit);
					packMap.put(packName, pack);
					packLocMap.put(packName, 0);

				}else{
					pack = packMap.get(packName);
				}

				SoftwareUnit doc = new SoftwareUnit();
				String path = entry.getKey().getPath();

				doc.setFullName(path);
				doc.setType(SoftwareUnitType.FILE);
				doc.setParentUnit(pack);

				int index = path.lastIndexOf("/")+1;
				doc.setName(path.substring(index));

				persistence.saveMetricValue(entry.getKey(), doc, "100");
				packLocMap.put(packName, packLocMap.get(packName)+100);


			}

		}

		for(Entry<String, SoftwareUnit> entry : packMap.entrySet()){
			persistence.saveMetricValue(null, entry.getValue(), String.valueOf(packLocMap.get(entry.getKey()) ) ); 
		}

	}


}
