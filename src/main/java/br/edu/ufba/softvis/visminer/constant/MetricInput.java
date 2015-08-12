package br.edu.ufba.softvis.visminer.constant;

/**
 * @version 0.9
 * Metric type.
 */
public enum MetricInput {

	COMMIT(1), SNAPSHOT(2);
	
	private int id;
	
	private MetricInput(int id){
		this.id = id;
	}
	
	public int getId(){
		return this.id;
	}
	
	public static MetricInput parse(int id){
		
		for(MetricInput type : MetricInput.values()){
			if(type.getId() == id){
				return type;
			}
		}
		
		return null;
		
	}
	
}
