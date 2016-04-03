package br.edu.ufba.softvis.visminer.metric;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bson.Document;

import br.edu.ufba.softvis.visminer.annotations.MetricAnnotation;
import br.edu.ufba.softvis.visminer.ast.AST;
import br.edu.ufba.softvis.visminer.ast.TypeDeclaration;

@MetricAnnotation(
		name = "Source Lines of Code",
		description = "Source lines of code (SLOC), also known as lines of code (LOC), is a software metric"
				+ " used to measure the size of a computer program by counting the number of lines in the text of"
				+ " the program's source code.",
				acronym = "SLOC")
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
