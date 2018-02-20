package org.repositoryminer.metrics.parser.cpp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

import org.eclipse.cdt.core.dom.ast.IASTPreprocessorIncludeStatement;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
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
import org.eclipse.cdt.internal.core.dom.parser.cpp.GNUCPPSourceParser;
import org.eclipse.cdt.internal.core.parser.scanner.CPreprocessor;
import org.repositoryminer.metrics.ast.AST;
import org.repositoryminer.metrics.ast.AbstractType;
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
		FileContent file = FileContent.create(filename, source.toCharArray());
		
					
		//System.out.println(file.getFileLocation());
		
		
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

	@SuppressWarnings("resource")
	@Override
	public AST generate(String filename, String source, String[] srcFolders) {

			AST ast = new AST();
			ast.setName(filename);
			ast.setSource(source);
			ast.setLanguage("cpp");
			
			IASTTranslationUnit tu = generateTu(filename, source);
			CppVisitor visitor = new CppVisitor();
			visitor.setHeaders(new ArrayList<AbstractType>());
			
				
			//String folder = filename.substring(0,filename.lastIndexOf("/"));
       	 
	        for (IASTPreprocessorIncludeStatement include : tu.getIncludeDirectives()) {
	        	
	        	
	        	for(String folder: this.getSourceFolders()) {
					//System.out.println(folder);
			       	
				
		
		        	 String namefile = folder+"/"+include.getName().getRawSignature().toString();
	
		        	 File f = new File(namefile);
		        	
		        	 
		        	 if(include.getName().getRawSignature().toString().endsWith(".h")  && f.exists() ) {
		        		 //System.out.println("----->"+namefile);
		 	        	
		        	   
		        		 String includesource = "";
		        	   try {
		        		  includesource = new Scanner(f).useDelimiter("\\Z").next();
		        	   } catch (FileNotFoundException e) {
		        		   e.printStackTrace();
		        	   }
		        			
		        		IASTTranslationUnit tu2 = generateTu(namefile, includesource);
		        		HeaderVisitor visitorHeader = new HeaderVisitor();
		        		tu2.accept(visitorHeader);
		        		//ast.setSource( includesource+"\r\n"+ast.getSource() );
		        		for(AbstractType type: visitorHeader.getTypes()) {
		        			visitor.getHeaders().add(type);
		        		}	
		        		
		        		break;
		        	}
	        	 
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
