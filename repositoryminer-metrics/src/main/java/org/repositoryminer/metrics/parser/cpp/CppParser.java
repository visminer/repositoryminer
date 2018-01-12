package org.repositoryminer.metrics.parser.cpp;

import java.util.HashMap;
import java.util.Map;

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
import org.eclipse.cdt.core.dom.parser.cpp.ANSICPPParserExtensionConfiguration;
import org.eclipse.cdt.core.dom.parser.cpp.GPPScannerExtensionConfiguration;
import org.eclipse.cdt.core.dom.parser.cpp.ICPPParserExtensionConfiguration;
import org.eclipse.cdt.core.parser.DefaultLogService;
import org.eclipse.cdt.core.parser.FileContent;
import org.eclipse.cdt.core.parser.IParserLogService;
import org.eclipse.cdt.core.parser.IScanner;
import org.eclipse.cdt.core.parser.IScannerInfo;
import org.eclipse.cdt.core.parser.IncludeFileContentProvider;
import org.eclipse.cdt.core.parser.ParserLanguage;
import org.eclipse.cdt.core.parser.ScannerInfo;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTFunctionDeclarator;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTTranslationUnit;
import org.eclipse.cdt.internal.core.parser.scanner.CPreprocessor;
import org.eclipse.core.runtime.CoreException;
import org.repositoryminer.metrics.ast.AST;
import org.repositoryminer.metrics.parser.Language;
import org.repositoryminer.metrics.parser.Parser;


public class CppParser extends Parser{
	
	private static final String[] EXTENSIONS = {"cpp","c"};
	
	public CppParser() {
		super.id = Language.CPP;
		super.extensions = EXTENSIONS;
	}

	@Override
	public AST generate(String filename, String source, String[] srcFolders) {

			AST ast = new AST();
			ast.setName(filename);
			ast.setSource(source);
			
			
			
			
					
			IASTTranslationUnit tu = null;
			FileContent f = FileContent.createForExternalFileLocation(source+"/"+filename);
			
			

			System.out.println(f.getFileLocation());
			
			ScannerInfo si = new ScannerInfo();
			
			System.out.println(ParserLanguage.CPP);
			
			GPPScannerExtensionConfiguration gp = new GPPScannerExtensionConfiguration();
			
			
			ICPPParserExtensionConfiguration config = new ANSICPPParserExtensionConfiguration();

			
			
			
			IScanner scanner = new CPreprocessor(f, si, ParserLanguage.CPP, null, gp, null);
			
			
			
		/*
			  Map definedSymbols = new HashMap();
			  String[] includePaths = new String[0];
			  IScannerInfo info = new ScannerInfo(definedSymbols, includePaths);
			  IParserLogService log = new DefaultLogService();
			 
			  IncludeFileContentProvider emptyIncludes = IncludeFileContentProvider.getEmptyFilesProvider();
			  int opts = 8;
			  
			
			 try {
				tu = GPPLanguage.getDefault().getASTTranslationUnit(f, info, emptyIncludes, null, opts, log);
			} catch (Exception e) {
				
			}
			
			 
			 IASTPreprocessorIncludeStatement[] includes = tu.getIncludeDirectives();
			    for (IASTPreprocessorIncludeStatement include : includes) {
			       System.out.println("include - " + include.getName());
			     }
		
		*/
		
		
		return null;
	}
	
	

}
