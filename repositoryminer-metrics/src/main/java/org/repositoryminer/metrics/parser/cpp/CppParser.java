package org.repositoryminer.metrics.parser.cpp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.File;
import java.io.PrintStream;

import org.eclipse.cdt.core.dom.ast.ASTVisitor;
import org.eclipse.cdt.core.dom.ast.DOMException;
import org.eclipse.cdt.core.dom.ast.ExpansionOverlapsBoundaryException;
import org.eclipse.cdt.core.dom.ast.IASTAttribute;
import org.eclipse.cdt.core.dom.ast.IASTDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTDeclarator;
import org.eclipse.cdt.core.dom.ast.IASTFileLocation;
import org.eclipse.cdt.core.dom.ast.IASTFunctionDefinition;
import org.eclipse.cdt.core.dom.ast.IASTName;
import org.eclipse.cdt.core.dom.ast.IASTNode;
import org.eclipse.cdt.core.dom.ast.IASTPreprocessorIncludeStatement;
import org.eclipse.cdt.core.dom.ast.IASTSimpleDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTStatement;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.core.dom.ast.IASTTypeId;
import org.eclipse.cdt.core.dom.ast.IScope;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPASTFunctionDeclarator;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPASTVisibilityLabel;
import org.eclipse.cdt.core.dom.ast.gnu.cpp.GPPLanguage;
import org.eclipse.cdt.core.dom.parser.ISourceCodeParser;
import org.eclipse.cdt.core.dom.parser.cpp.ANSICPPParserExtensionConfiguration;
import org.eclipse.cdt.core.dom.parser.cpp.GPPScannerExtensionConfiguration;
import org.eclipse.cdt.core.dom.parser.cpp.ICPPParserExtensionConfiguration;
import org.eclipse.cdt.core.index.IIndex;
import org.eclipse.cdt.core.model.ILanguage;
import org.eclipse.cdt.core.parser.DefaultLogService;
import org.eclipse.cdt.core.parser.FileContent;
import org.eclipse.cdt.core.parser.IParserLogService;
import org.eclipse.cdt.core.parser.IScanner;
import org.eclipse.cdt.core.parser.IScannerInfo;
import org.eclipse.cdt.core.parser.IncludeFileContentProvider;
import org.eclipse.cdt.core.parser.ParserLanguage;
import org.eclipse.cdt.core.parser.ParserMode;
import org.eclipse.cdt.core.parser.ScannerInfo;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTFunctionDeclarator;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTTranslationUnit;
import org.eclipse.cdt.internal.core.dom.parser.cpp.GNUCPPSourceParser;
import org.eclipse.cdt.internal.core.model.Include;
import org.eclipse.cdt.internal.core.model.TranslationUnit;
import org.eclipse.cdt.internal.core.parser.scanner.CPreprocessor;
import org.eclipse.core.runtime.CoreException;
import org.repositoryminer.metrics.ast.AST;
import org.repositoryminer.metrics.ast.AbstractImport;
import org.repositoryminer.metrics.parser.Language;
import org.repositoryminer.metrics.parser.Parser;


public class CppParser extends Parser{
	
	private static final String[] EXTENSIONS = {"cpp","c"};
	
	public CppParser() {
		super.id = Language.CPP;
		super.extensions = EXTENSIONS;
	}
	
	public IASTTranslationUnit generateTu(String filename,String source) {
		
		
		IASTTranslationUnit tu = null;
		FileContent file = FileContent.createForExternalFileLocation(source+"/"+filename);
		
					
		System.out.println(file.getFileLocation());
		ICPPParserExtensionConfiguration config = new ANSICPPParserExtensionConfiguration();
		IScanner scanner = new CPreprocessor(file, new ScannerInfo(), ParserLanguage.CPP, null, new GPPScannerExtensionConfiguration(), null);
		
		
		
		ISourceCodeParser parser = new GNUCPPSourceParser(scanner,ParserMode.COMPLETE_PARSE, null, config);

		Map<String, String> macroDefinitions = new HashMap<>();
        String[] includeSearchPaths = new String[0];
        
        IScannerInfo si = new ScannerInfo(macroDefinitions, includeSearchPaths);
        IncludeFileContentProvider ifcp = IncludeFileContentProvider.getEmptyFilesProvider();
        
        IIndex idx = null;
        int options = ILanguage.OPTION_PARSE_INACTIVE_CODE;
        
        IParserLogService log = new DefaultLogService();
		
        try {
        	// GCCLanguage é para C
			tu = GPPLanguage.getDefault().getASTTranslationUnit(file, si, ifcp, idx, options, log);
			
        }catch(Exception e) {
        	
        }
        
        return tu;
		
	}

	@Override
	public AST generate(String filename, String source, String[] srcFolders) {

			AST ast = new AST();
			ast.setName(filename);
			ast.setSource(source);
			
			
			
			
			IASTTranslationUnit tu = generateTu(filename, source);
			CppVisitor visitor = new CppVisitor();
			
	        
	        for (IASTPreprocessorIncludeStatement include : tu.getIncludeDirectives()) {
	        	
	        	 String namefile = include.getName().getRawSignature().toString();
	        	 File f = new File(source+"/"+namefile);
	        	if(include.getName().getRawSignature().toString().endsWith(".h")  && f.exists() ) {
	        		
	        			
	        		IASTTranslationUnit tu2 = generateTu(namefile, source);
	        		CppVisitor visitor2 = new CppVisitor();
	        		tu2.accept(visitor2);
	        		visitor.setTypes(visitor2.getTypes());
	        		visitor.setMethods(visitor2.getMethods());
	        		
	        	}
				
			}
	        
	        
	        
	       
	      
			
			visitor.extractImports(tu);
			
			
			tu.accept(visitor);
			
	        
			ast.setImports(visitor.getImports());
			ast.setTypes(visitor.getTypes());
			ast.setMethods(visitor.getMethods());
			
			
			
			
			
			
			return ast;
			
	
	}
	
	

}
