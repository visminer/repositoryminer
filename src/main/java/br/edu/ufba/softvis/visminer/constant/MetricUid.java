package br.edu.ufba.softvis.visminer.constant;

/**
 * @author Felipe Gustavo de Souza Gomes (felipegustavo1000@gmail.com)
 * @version 0.9
 * Metrics unique id.
 */
public enum MetricUid {

	LOC(1);
	
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
		
		throw new IllegalArgumentException("Does not exists MetricUid with id "+id);
		
	}
	
}