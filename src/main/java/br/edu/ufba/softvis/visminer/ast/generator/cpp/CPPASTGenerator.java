package br.edu.ufba.softvis.visminer.ast.generator.cpp;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.cdt.core.dom.ast.IASTPreprocessorIncludeStatement;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.core.dom.parser.ISourceCodeParser;
import org.eclipse.cdt.core.dom.parser.cpp.ANSICPPParserExtensionConfiguration;
import org.eclipse.cdt.core.dom.parser.cpp.GPPScannerExtensionConfiguration;
import org.eclipse.cdt.core.dom.parser.cpp.ICPPParserExtensionConfiguration;
import org.eclipse.cdt.core.parser.FileContent;
import org.eclipse.cdt.core.parser.IScanner;
import org.eclipse.cdt.core.parser.ParserLanguage;
import org.eclipse.cdt.core.parser.ParserMode;
import org.eclipse.cdt.core.parser.ScannerInfo;
import org.eclipse.cdt.internal.core.dom.parser.cpp.GNUCPPSourceParser;
import org.eclipse.cdt.internal.core.parser.scanner.CPreprocessor;

import br.edu.ufba.softvis.visminer.annotations.ASTGeneratorAnnotation;
import br.edu.ufba.softvis.visminer.ast.AST;
import br.edu.ufba.softvis.visminer.ast.ClassOrInterfaceDeclaration;
import br.edu.ufba.softvis.visminer.ast.Document;
import br.edu.ufba.softvis.visminer.ast.EnumDeclaration;
import br.edu.ufba.softvis.visminer.ast.ImportDeclaration;
import br.edu.ufba.softvis.visminer.ast.MethodDeclaration;
import br.edu.ufba.softvis.visminer.ast.generator.IASTGenerator;
import br.edu.ufba.softvis.visminer.constant.LanguageType;

/**
 * A CPP AST generator
 * 
 * This class is intended to front the generation of AST upon C++ projects.
 * Mining works on both headers and source codes. The underlining API is the
 * CORE.CDT (the same that powers ECLIPSE CDT IDE).
 * 
 * Visitor pattern is used to interact with the underline mechanisms of the API
 * that perform the mining of the source code. This is achieved by inputting an
 * instance of @link br.edu.ifba.softvis.visminer.generator.cpp.CPPASTVisitor
 * into the IASTTranslationUnit returned from the C++ parser.
 * 
 * The extensions accepted for this generator are: h, cpp, hpp
 * 
 * @author Luis Paulo
 */

@ASTGeneratorAnnotation(
		language = LanguageType.CPP,
		extensions = {"h", "cpp", "hpp"}
		)
public class CPPASTGenerator implements IASTGenerator {

	private Document document;
	private ClassOrInterfaceDeclaration currClass;

	private List<br.edu.ufba.softvis.visminer.ast.TypeDeclaration> classes = new ArrayList<br.edu.ufba.softvis.visminer.ast.TypeDeclaration>();
	private List<EnumDeclaration> enumerators = new ArrayList<EnumDeclaration>();

	private IScanner createScanner(String filePath) {
		
		FileContent fContent = FileContent
				.createForExternalFileLocation(filePath);

		IScanner scanner = new CPreprocessor(fContent, new ScannerInfo(),
				ParserLanguage.CPP, null,
				new GPPScannerExtensionConfiguration(), null);

		return scanner;
		
	}

	private String parseInclude(String rawInclude) {
		
		String include = "";

		if (rawInclude != null) {
			include = rawInclude.replace("#", "").replace("include", "")
					.replace("<", "").replace(">", "").replace("\"", "");
		}

		return include;
		
	}

	public AST generate(String filePath, String source, String[] sourceFolders) {
		
		document = new Document();
		document.setName(filePath);
		document.setMethods(new ArrayList<MethodDeclaration>());

		IASTTranslationUnit tu = null;

		try {
			
			IScanner scanner = createScanner(filePath);
			ICPPParserExtensionConfiguration config = new ANSICPPParserExtensionConfiguration();

			ISourceCodeParser parser = new GNUCPPSourceParser(scanner,
					ParserMode.COMPLETE_PARSE, new CPPASTLogger(), config);

			if (!parser.encounteredError()) {
				tu = parser.parse();
			}
			
		} catch (Exception e) {
		}

		if (tu == null) {
			AST ast = new AST();
			ast.setDocument(document);
			return ast;
		}

		IASTPreprocessorIncludeStatement[] includes = tu.getIncludeDirectives();
		List<ImportDeclaration> importsDecls = new ArrayList<ImportDeclaration>();
		for (IASTPreprocessorIncludeStatement include : includes) {
			ImportDeclaration importDecl = new ImportDeclaration();
			importDecl.setName(parseInclude(include.getRawSignature()));
			importDecl.setStatic(false);
			importDecl.setOnDemand(false);
			importsDecls.add(importDecl);
		}
		document.setImports(importsDecls);

		CPPASTVisitor visitor = new CPPASTVisitor(this);
		tu.accept(visitor);

		document.setTypes(classes);
		document.getTypes().addAll(enumerators);

		AST ast = new AST();
		ast.setDocument(document);
		ast.setSourceCode(tu.getRawSignature());

		return ast;
	}

	public Document getDocument() {
		return this.document;
	}

	public void addType(ClassOrInterfaceDeclaration clazz) {
		currClass = clazz;

		classes.add(clazz);
	}

	public void addEnum(EnumDeclaration enumm) {
		enumerators.add(enumm);
	}

	public void addMethod(MethodDeclaration method) {
		currClass.getMethods().add(method);
	}

}
