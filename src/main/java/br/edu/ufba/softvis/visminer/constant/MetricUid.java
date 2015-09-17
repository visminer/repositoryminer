package br.edu.ufba.softvis.visminer.constant;

/**
 * @version 0.9
 * Metrics unique id.
 */
public enum MetricUid {

	NONE(0),
	CC(1),
	SLOC(2),
	NOCAI(3),
	WMC(4),
	TCC(5),
	ATFD(6);
	
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