package org.repositoryminer.codesmell.commit;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractTypeDeclaration;
import org.repositoryminer.ast.AbstractTypeDeclaration.Archetype;
import org.repositoryminer.ast.TypeDeclaration;
import org.repositoryminer.codesmell.CodeSmellId;
import org.repositoryminer.listener.ICommitCodeSmellDetectionListener;
import org.repositoryminer.metric.ATFDMetric;
import org.repositoryminer.metric.NOAMetric;
import org.repositoryminer.metric.TCCMetric;
import org.repositoryminer.metric.WMCMetric;

public class GodClass implements ICommitCodeSmell {

	private int atfdThreshold = 40;
	private int wmcThreshold = 75;
	private float tccThreshold = 0.2f;
	private int noaThreshold = 20;
	private boolean useNoa = false;

	public GodClass() {}

	public GodClass(int atfdThreshold, int wmcThreshold, float tccThreshold, int noaThreshold, boolean useNoa) {
		this.atfdThreshold = atfdThreshold;
		this.wmcThreshold = wmcThreshold;
		this.tccThreshold = tccThreshold;
		this.noaThreshold = noaThreshold;
		this.useNoa = useNoa;
	}

	@Override
	public void detect(AbstractTypeDeclaration type, AST ast, ICommitCodeSmellDetectionListener listener) {
		if (type.getArchetype() == Archetype.CLASS_OR_INTERFACE) {
			TypeDeclaration cls = (TypeDeclaration) type;

			boolean godClass = detect(type, cls);
			listener.updateSmellDetection(CodeSmellId.GOD_CLASS, godClass);
		}
	}

	public boolean detect(AbstractTypeDeclaration type, TypeDeclaration cls) {
		boolean godClass = false;
		
		ATFDMetric atfdMetric = new ATFDMetric();
		WMCMetric wmcMetric = new WMCMetric();
		TCCMetric tccMetric = new TCCMetric();
		NOAMetric noaMetric = new NOAMetric();

		int atfd = atfdMetric.calculate(type, cls.getMethods(), false);
		float tcc = tccMetric.calculate(type, cls.getMethods());
		int wmc = wmcMetric.calculate(cls.getMethods());
		int noa = noaMetric.calculate(cls.getFields());

		if(useNoa)
			godClass = (atfd > atfdThreshold) && ((wmc > wmcThreshold) || ((tcc < tccThreshold) && (noa > noaThreshold)));
		else
			godClass = (atfd > atfdThreshold) && ((wmc > wmcThreshold) || ((tcc < tccThreshold)));
		
		return godClass;
	}
}
