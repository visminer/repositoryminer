package org.repositoryminer.metric;

public class MetricFactory {

	public static IMetric getMetric(MetricID id) {
		switch (id) {
		case AMW:
			return new AMW();
		case ATFD:
			return new ATFD();
		case CYCLO:
			return new CYCLO();
		case FDP:
			return new FDP();
		case LAA:
			return new LAA();
		case LOC:
			return new LOC();
		case LVAR:
			return new LVAR();
		case MAXNESTING:
			return new MAXNESTING();
		case NOA:
			return new NOA();
		case NOAM:
			return new NOAM();
		case NOAV:
			return new NOAV();
		case NOM:
			return new NOM();
		case NOPA:
			return new NOPA();
		case NProtM:
			return new NProtM();
		case PAR:
			return new PAR();
		case TCC:
			return new TCC();
		case WMC:
			return new WMC();
		case WOC:
			return new WOC();
		default :
			return null;
		}
	}

}