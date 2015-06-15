package br.edu.ufba.softvis.visminer.constant;

public enum MetricId {

	LOC(1);
	
	private int id;
	
	private MetricId(int id){
		this.id = id;
	}
	
	public int getId(){
		return this.id;
	}
	
	public static MetricId parse(int id){
		
		for(MetricId metricId : MetricId.values()){
			if(metricId.getId() == id){
				return metricId;
			}
		}
		
		throw new IllegalArgumentException("Doesn't exists type for id "+id);
		
	}
	
}