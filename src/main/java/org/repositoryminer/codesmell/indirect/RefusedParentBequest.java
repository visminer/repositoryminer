package org.repositoryminer.codesmell.indirect;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractClassDeclaration;
import org.repositoryminer.ast.ClassArchetype;
import org.repositoryminer.ast.ClassDeclaration;
import org.repositoryminer.codemetric.CodeMetricId;
import org.repositoryminer.codemetric.direct.AMW;
import org.repositoryminer.codemetric.direct.NProtM;
import org.repositoryminer.codemetric.direct.WMC;
import org.repositoryminer.codemetric.indirect.BOvR;
import org.repositoryminer.codemetric.indirect.BUR;
import org.repositoryminer.codesmell.CodeSmellId;

public class RefusedParentBequest implements IIndirectCodeSmell {

	private int nprotmThreshold = 3;
	private float burThreshold = 1 / 3f;
	private float bovrThreshold = 1 / 3f;
	private float amwThreshold = 1 / 3f;
	private int wmcThreshold = 47;
	private int nomThreshold = 5;

	private NProtM nprotmMetric = new NProtM();
	private BUR burMetric = new BUR();
	private BOvR bovrMetric = new BOvR();
	private AMW amwMetric = new AMW();
	private WMC wmcMetric = new WMC();

	private Map<String, Integer> nprotm = new HashMap<String, Integer>();
	private Map<String, Integer> wmc = new HashMap<String, Integer>();
	private Map<String, Integer> nom = new HashMap<String, Integer>();
	private Map<String, Float> amw = new HashMap<String, Float>();

	private Map<String, Float> bur;
	private Map<String, Float> bovr;

	public RefusedParentBequest() {
	}

	public RefusedParentBequest(int nprotmThreshold, float burThreshold, float bovrThreshold, float amwThreshold,
			int wmcThreshold, int nomThreshold) {
		super();
		this.nprotmThreshold = nprotmThreshold;
		this.burThreshold = burThreshold;
		this.bovrThreshold = bovrThreshold;
		this.amwThreshold = amwThreshold;
		this.wmcThreshold = wmcThreshold;
		this.nomThreshold = nomThreshold;
	}

	@Override
	public void detect(AbstractClassDeclaration type, AST ast) {
		if (!type.getArchetype().equals(ClassArchetype.CLASS_OR_INTERFACE)) {
			return;
		}

		ClassDeclaration cls = (ClassDeclaration) type;

		if (cls.getSuperClass() == null) {
			return;
		}

		burMetric.calculate(type, ast);
		bovrMetric.calculate(type, ast);

		int wmcValue = wmcMetric.calculate(cls.getMethods());
		int nomValue = cls.getMethods().size();

		nprotm.put(cls.getName(), nprotmMetric.calculate(cls.getMethods(), cls.getFields()));
		wmc.put(cls.getName(), wmcValue);
		nom.put(cls.getName(), nomValue);
		amw.put(cls.getName(), amwMetric.calculate(wmcValue, nomValue));
	}

	@Override
	public CodeSmellId getId() {
		return CodeSmellId.REFUSED_PARENT_BEQUEST;
	}

	@Override
	public Document getThresholds() {
		Document doc = new Document();
		doc.append(CodeMetricId.NProtM.toString(), nprotmThreshold);
		doc.append(CodeMetricId.BUR.toString(), burThreshold);
		doc.append(CodeMetricId.BOvR.toString(), bovrThreshold);
		doc.append(CodeMetricId.AMW.toString(), amwThreshold);
		doc.append(CodeMetricId.WMC.toString(), wmcThreshold);
		doc.append(CodeMetricId.NOM.toString(), nomThreshold);

		return new Document("codesmell", CodeSmellId.REFUSED_PARENT_BEQUEST.toString()).append("thresholds", doc);
	}

	@Override
	public Map<String, Document> getResult() {
		burMetric.calculateBUR();
		bovrMetric.calculateBOvR();

		bur = burMetric.getBUR();
		bovr = bovrMetric.getBOvR();

		Map<String, Document> result = new HashMap<String, Document>();

		for (Entry<String, Integer> nprotmMap : nprotm.entrySet()) {
			String clazz = nprotmMap.getKey();
			if (bur.get(clazz) == null || bovr.get(clazz) == null || bovr.get(clazz) == -1f || bovr.get(clazz) == -1f) {
				continue;
			}

			int nprotmValue = nprotmMap.getValue();
			float burValue = bur.get(clazz);
			float bovrValue = bovr.get(clazz);
			float amwValue = amw.get(clazz);
			int wmcValue = wmc.get(clazz);
			int nomValue = nom.get(clazz);

			if (detect(nprotmValue, burValue, bovrValue, amwValue, wmcValue, nomValue)) {
				Document metrics = new Document();
				metrics.append(CodeMetricId.NProtM.toString(), nprotmValue);
				metrics.append(CodeMetricId.BUR.toString(), burValue);
				metrics.append(CodeMetricId.BOvR.toString(), bovrValue);
				metrics.append(CodeMetricId.AMW.toString(), amwValue);
				metrics.append(CodeMetricId.WMC.toString(), wmcValue);
				metrics.append(CodeMetricId.NOM.toString(), nomValue);

				result.put(clazz, new Document("codesmell", CodeSmellId.REFUSED_PARENT_BEQUEST.toString())
						.append("metrics", metrics));
			}
		}

		clean();
		return result;
	}

	private boolean detect(int nprotmValue, float burValue, float bovrValue, float amwValue, int wmcValue,
			int nomValue) {
		boolean ignoreBequest = (nprotmValue > nomThreshold && burValue < burThreshold) || bovrValue < bovrThreshold;
		boolean complexChild = (amwValue > amwThreshold || wmcValue > wmcThreshold) && nomValue > nomThreshold;
		return ignoreBequest && complexChild;
	}

	private void clean() {
		burMetric.clean();
		bovrMetric.clean();
		nom.clear();
		nprotm.clear();
		wmc.clear();
		amw.clear();
	}

}