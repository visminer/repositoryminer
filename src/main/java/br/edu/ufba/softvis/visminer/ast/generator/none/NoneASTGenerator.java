package br.edu.ufba.softvis.visminer.ast.generator.none;

import br.edu.ufba.softvis.visminer.annotations.ASTGeneratorAnnotation;
import br.edu.ufba.softvis.visminer.ast.AST;
import br.edu.ufba.softvis.visminer.ast.Document;
import br.edu.ufba.softvis.visminer.ast.generator.IASTGenerator;
import br.edu.ufba.softvis.visminer.constant.LanguageType;

/**
 * None AST generator
 * 
 * This class have the job to create an abstract AST upon various files.
 * The objective of this class is create an AST to programming languages
 * or any file that we don't have parser support or parser is not necessary.
 * 
 * The extensions accepted for this generator are: xml, html, css, txt, md, properties, gradle
 * 
 */


@ASTGeneratorAnnotation(
		language = LanguageType.NONE,
		extensions = {"xml", "html", "css", "txt", "md", "properties", "gradle"}
)
public class NoneASTGenerator implements IASTGenerator{

	@Override
	public AST generate(String filePath, String source, String[] sourceFolders) {
		Document doc = new Document();
		doc.setName(filePath);
		AST ast = new AST();
		ast.setSourceCode(source);
		ast.setDocument(doc);

		return ast;
	}

}
