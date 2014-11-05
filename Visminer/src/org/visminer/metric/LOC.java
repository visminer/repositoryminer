package org.visminer.metric;

public class LOC implements IMetric<Integer> {

	private final String NAME = "LOC";
	private final String DESCRIPTION = "This metric calculates number of lines in code";
	
	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}

	@Override
	public Integer calculate(byte[] data) {
		
		String content = new String(data);
		String [] lines = content.split("\n|\r|\r\n");
		int lineCount = 0;
		
		for(int i = 0; i < lines.length; i++){

			lines[i].replaceAll("\t", "");
			
			if(lines[i].length() > 0){
				lineCount++;
			}
		}
		
		return lineCount;
		
	}

}
