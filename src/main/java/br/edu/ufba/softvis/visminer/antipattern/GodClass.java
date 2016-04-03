package br.edu.ufba.softvis.visminer.antipattern;

import org.bson.Document;

import br.edu.ufba.softvis.visminer.annotations.AntiPatternAnnotation;
import br.edu.ufba.softvis.visminer.ast.AST;
import br.edu.ufba.softvis.visminer.ast.ClassOrInterfaceDeclaration;
import br.edu.ufba.softvis.visminer.ast.TypeDeclaration;
import br.edu.ufba.softvis.visminer.constant.SoftwareUnitType;
import br.edu.ufba.softvis.visminer.metric.ATFDMetric;
import br.edu.ufba.softvis.visminer.metric.NOAMetric;
import br.edu.ufba.softvis.visminer.metric.TCCMetric;
import br.edu.ufba.softvis.visminer.metric.WMCMetric;

@AntiPatternAnnotation(name = "God Class", description = "A God Class is an object that controls way too many other objects in the system "
		+ "and has grown beyond all logic to become The Class That Does Everything")
public class GodClass implements IAntiPattern {

	private int atfdThreshold = 40;
	private int wmcThreshold = 75;
	private float tccThreshold = 0.2f;
	private int noaThreshold = 20;

	public GodClass() {
	};

	public GodClass(int atfdThreshold, int wmcThreshold, float tccThreshold, int noaThreshold) {
		this.atfdThreshold = atfdThreshold;
		this.wmcThreshold = wmcThreshold;
		this.tccThreshold = tccThreshold;
		this.noaThreshold = noaThreshold;
	}

	@Override
	public void detect(TypeDeclaration type, AST ast, Document document) {
		if (type.getType() == SoftwareUnitType.CLASS_OR_INTERFACE) {
			ClassOrInterfaceDeclaration cls = (ClassOrInterfaceDeclaration) type;

			ATFDMetric atfdMetric = new ATFDMetric();
			WMCMetric wmcMetric = new WMCMetric();
			TCCMetric tccMetric = new TCCMetric();
			NOAMetric noaMetric = new NOAMetric();

			int atfd = atfdMetric.calculate(type, cls.getMethods(), false);
			float tcc = tccMetric.calculate(type, cls.getMethods());
			int wmc = wmcMetric.calculate(cls.getMethods());
			int noa = noaMetric.calculate(cls.getFields());

			boolean godClass = (atfd > atfdThreshold)
					&& ((wmc > wmcThreshold) || ((tcc < tccThreshold) && (noa > noaThreshold)));

			document.append("name", new String("God Class")).append("value", new Boolean(godClass));
		}
	}

	public boolean detect(Document document) {
		boolean godClass = false;

		return godClass;
	}
}
