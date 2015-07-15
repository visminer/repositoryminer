package br.edu.ufba.softvis.visminer.constant;

/**
 * @version 0.9
 * Metrics unique id.
 */
public enum MetricUid {

	CC(1),
	SLOC(2),
	NOC(3),
	NOP(4),
	NOM(5),
	NOE(6),
	WMC(7);
	
	private int id;
	
	private MetricUid(int id){
		this.id = id;
	}
	
	/**
	 * 
	 * @return Metric id
	 */
	public int getId(){
		return this.id;
	}
	
	/**
	 * 
	 * @param id
	 * @return Metric with given id.
	 */
	public static MetricUid parse(int id){
		
		for(MetricUid metricUid : MetricUid.values()){
			if(metricUid.getId() == id){
				return metricUid;
			}
		}
		
		return null;
		
	}
	
}