package br.edu.ufba.softvis.visminer.metric;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jface.text.Document;

import br.edu.ufba.softvis.visminer.annotations.MetricAnnotation;
import br.edu.ufba.softvis.visminer.constant.MetricType;
import br.edu.ufba.softvis.visminer.constant.MetricUid;
import br.edu.ufba.softvis.visminer.constant.SoftwareUnitType;
import br.edu.ufba.softvis.visminer.model.bean.Commit;
import br.edu.ufba.softvis.visminer.model.bean.File;
import br.edu.ufba.softvis.visminer.model.bean.Repository;
import br.edu.ufba.softvis.visminer.model.bean.SoftwareUnit;
import br.edu.ufba.softvis.visminer.persistence.MetricPersistance;
import br.edu.ufba.softvis.visminer.utility.JavaAST;

@MetricAnnotation(
		name = "Source Lines of Code",
		description = "Source lines of code (SLOC), also known as lines of code (LOC), is a software "
				+ "metric used to measure the size of a computer program by counting the number of lines"
				+ " in the text of the program's source code. SLOC is typically used to predict the amount"
				+ " of effort that will be required to develop a program, as well as to estimate programming"
				+ " productivity or maintainability once the software is produced.",
		acronym = "SLOC",
		type = MetricType.SIMPLE,
		uid = MetricUid.SLOC
	)
public class SLOCMetric implements IMetric{

	@Override
	public void calculate(Map<File, Object> filesMap, List<Commit> commits, Repository repository,
			MetricPersistance persistence){
		
		SoftwareUnit project = new SoftwareUnit();
		project.setName(repository.getName());
		project.setType(SoftwareUnitType.PROJECT);
		persistence.saveSoftwareUnit(null, project);
		
		
		for(Entry<File, Object> entry : filesMap.entrySet()){
			
			if(!(entry.getValue() instanceof JavaAST)){
				continue;
			}
			
			JavaAST ast = (JavaAST) entry.getValue();
			CompilationUnit unit = ast.getRoot();
			
			if(unit == null){
				continue;
			}
			
			SoftwareUnit pkg = null;
			
			if(unit.getPackage() != null){
				PackageDeclaration pkgDecl = unit.getPackage();
				pkg = new SoftwareUnit();
				pkg.setParentUnit(project);
				pkg.setName(pkgDecl.getName().getFullyQualifiedName());
				pkg.setType(SoftwareUnitType.PACKAGE);
				persistence.saveSoftwareUnit(null, pkg);
			}
			
			SoftwareUnit file = new SoftwareUnit();
			file.setParentUnit(pkg);
			file.setName(entry.getKey().getPath());
			file.setType(SoftwareUnitType.FILE);
			
			Document document = new Document(ast.toString());
			String value = String.valueOf(document.getNumberOfLines());
			persistence.saveMetricValue(entry.getKey(), file, value);
			
		}
		
	}

}