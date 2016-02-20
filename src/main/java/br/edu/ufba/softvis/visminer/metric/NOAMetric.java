package br.edu.ufba.softvis.visminer.metric;

import java.util.List;

import org.bson.Document;

import br.edu.ufba.softvis.visminer.annotations.MetricAnnotation;
import br.edu.ufba.softvis.visminer.ast.AST;
import br.edu.ufba.softvis.visminer.ast.ClassOrInterfaceDeclaration;
import br.edu.ufba.softvis.visminer.ast.FieldDeclaration;
import br.edu.ufba.softvis.visminer.ast.TypeDeclaration;
import br.edu.ufba.softvis.visminer.constant.SoftwareUnitType;

@MetricAnnotation(name = "Number of Attributes", 
	description = "The Number Of Attributes metric is used to count the average number of attributes for a class in the model.", acronym = "NOA")
public class NOAMetric implements IMetric {

	@Override
	public void calculate(TypeDeclaration type, AST ast, Document document) {
		if (SoftwareUnitType.CLASS_OR_INTERFACE.equals(type.getType())) {
			ClassOrInterfaceDeclaration cls = (ClassOrInterfaceDeclaration) type;
			document.append("NOA", new Document("accumulated", new Integer(calculate(cls.getFields()))));
		}
	}

	public int calculate(List<FieldDeclaration> fields) {
		int noa = 0;

		if (fields != null) {
			noa += fields.size();
		}

		return noa;
	}

}
