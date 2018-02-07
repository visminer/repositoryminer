package org.repositoryminer.metrics.parser.cppParser;

import java.util.List;
import org.repositoryminer.metrics.ast.AST;
import org.repositoryminer.metrics.parser.Language;
import org.repositoryminer.metrics.parser.Parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.cdt.core.dom.ast.IASTPreprocessorIncludeStatement;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.core.dom.ast.gnu.cpp.GPPLanguage;
import org.eclipse.cdt.core.parser.DefaultLogService;
import org.eclipse.cdt.core.parser.FileContent;
import org.eclipse.cdt.core.parser.IParserLogService;
import org.eclipse.cdt.core.parser.IScannerInfo;
import org.eclipse.cdt.core.parser.IncludeFileContentProvider;
import org.eclipse.cdt.core.parser.ScannerInfo;
import org.eclipse.core.runtime.CoreException;
import org.repositoryminer.metrics.ast.AbstractInclude;

/**
 * C/C++ AST generator
 * 
 * This class has the job to create an abstract AST upon C/C++ source code.
 * 
 * The extensions accepted for this generator are:.c, .h, .cpp
 */
 
public class CppParser extends Parser {

	private static final String[] EXTENSIONS = {"c","h","cpp"};
	private List<AbstractInclude> includs = new ArrayList<>();
	
	public CppParser() {
		super.id = Language.CPP;
		super.extensions = EXTENSIONS;
	}
	
	@Override
	public AST generate(String filename, String source, String[] srcFolders) {
		AST ast = new AST();

		ast.setName(filename);
		ast.setSource(source);
		
		FileContent fileContent = FileContent.createForExternalFileLocation("C:/Users/Danniel/Desktop/velha.c");
			
			Map definedSymbols = new HashMap();
			String[] includePaths = new String[0];
			IScannerInfo info = new ScannerInfo(definedSymbols, includePaths);
			IParserLogService log = new DefaultLogService();
			IncludeFileContentProvider emptyIncludes = IncludeFileContentProvider.getEmptyFilesProvider();
					
				int opts = 8;
				IASTTranslationUnit translationUnit = null;
				try {
					translationUnit = GPPLanguage.getDefault().getASTTranslationUnit(fileContent, info, emptyIncludes, null, opts, log);
				} catch (CoreException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
		if (translationUnit==null) {
		 System.out.println("error");
		}
			
				
		FileVisitor visitor = new FileVisitor();
		translationUnit.accept(visitor);
		
		IASTPreprocessorIncludeStatement[] includes = translationUnit.getIncludeDirectives();
	    for (IASTPreprocessorIncludeStatement include : includes) {
	    	ast.setIncludes(include.getRawSignature());
		//	System.out.println("HelLo");
		}
		
		//ast.setTypes(visitor.getTypes());

		return ast;
		
		
	}

}