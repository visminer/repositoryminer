package br.edu.ufba.softvis.visminer.constant;

/**
 * @version 0.9
 * Metric type.
 */
public enum MetricType {

	NONE(0),
	COMMIT(1),
	SNAPSHOT(2);
	
	private int id;
	
	private MetricType(int id){
		this.id = id;
	}
	
	public int getId(){
		return this.id;
	}
	
	public static MetricType parse(int id){
		
		for(MetricType type : MetricType.values()){
			if(type.getId() == id){
				return type;
			}
		}
		
		return null;
		
	}
	
}
