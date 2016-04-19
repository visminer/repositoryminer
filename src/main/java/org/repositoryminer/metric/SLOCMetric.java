package org.repositoryminer.metric;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.TypeDeclaration;

public class SLOCMetric implements IMetric{

	private Pattern pattern;

	public SLOCMetric(){
		pattern = Pattern.compile("(\r\n)|(\r)|(\n)");
	}

	@Override
	public void calculate(TypeDeclaration type, AST ast, Document document) {
		int sloc = calculate(ast.getSourceCode());
		
		document.append("SLOC", new Document("accumulated", new Integer(sloc)));
	}
	
	public int calculate(String source){
		if(source == null || source.length() == 0)
			return 0;

		Matcher m = pattern.matcher(source);

		//starts from 1 because the matcher doesn't count the last line (only line breaks)
		int i = 1;

		while(m.find())
			i++;

		return i;
	}

}
