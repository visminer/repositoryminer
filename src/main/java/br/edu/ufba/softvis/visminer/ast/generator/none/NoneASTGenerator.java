package br.edu.ufba.softvis.visminer.ast.generator.none;

import java.io.UnsupportedEncodingException;

import br.edu.ufba.softvis.visminer.annotations.ASTGeneratorAnnotation;
import br.edu.ufba.softvis.visminer.ast.AST;
import br.edu.ufba.softvis.visminer.ast.Document;
import br.edu.ufba.softvis.visminer.ast.generator.IASTGenerator;
import br.edu.ufba.softvis.visminer.constant.LanguageType;

@ASTGeneratorAnnotation(
		language = LanguageType.NONE,
		extensions = {"xml", "html", "css", "txt", "md"}
)
public class NoneASTGenerator implements IASTGenerator{

	@Override
	public AST generate(String filePath, byte[] source, String charset) {
		
		Document doc = new Document();
		doc.setName(filePath);
		AST ast = new AST();
		
		if(source == null){
			ast.setSourceCode(null);
		}else{
			try {
				ast.setSourceCode(new String(source, charset));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		
		ast.setDocument(doc);
		return ast;
		
	}

}
