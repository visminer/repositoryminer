package org.repositoryminer.metric.clazz;

import java.util.List;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractTypeDeclaration;
import org.repositoryminer.ast.AbstractTypeDeclaration.Archetype;
import org.repositoryminer.ast.FieldDeclaration;
import org.repositoryminer.ast.TypeDeclaration;
import org.repositoryminer.metric.MetricId;

/**
 * <h1>Number of attributes</h1>
 * <p>
 * NOA is defined as the number of attributes in a class.
 */
public class NOA implements IClassMetric {

	private static final String[] UNACCEPTED_TYPES = new String[] { "enum", "class" };

	@Override
	public String getId() {
		return MetricId.NOA;
	}
	
	@Override
	public void calculate(AbstractTypeDeclaration type, AST ast, Document document) {
		if (Archetype.CLASS_OR_INTERFACE == type.getArchetype()) {
			TypeDeclaration cls = (TypeDeclaration) type;
			document.append("name", MetricId.NOA).append("value", new Integer(calculate(cls.getFields())));
		}
	}

	public int calculate(List<FieldDeclaration> fields) {
		int noa = 0;
		for (FieldDeclaration field : fields) {
			if (accept(field.getType())) {
				noa++;
			}
 		}
		
		return noa;
	}
	
	private boolean accept(String type) {
		boolean accept = true;
		
		for (String unaccepted : UNACCEPTED_TYPES) {
			if (type.equals(unaccepted)) {
				accept = false;
				
				break;
			}
		}
		
		return accept;
	}

}