package br.edu.ufba.softvis.visminer.utility;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

/**
 * @author Felipe Gustavo de Souza Gomes (felipegustavo1000@gmail.com)
 * @version 0.1
 * 
 * The utility class to create AST from source code.
 */
public class JavaAST {

    private CompilationUnit root = null;
    private String source = null;
    
    /**
     * @param source
     * Creates a AST from a String.
     */
    public void parserFromString(String source){
        this.source = source;
        setRoot(source);
    }
    
    /**
     * @param source
     * Creates a AST from a bytes.
     */
    public void partserFromBytes(byte[] bytes){
    	if(bytes != null){
    		String str = new String(bytes);
    		setRoot(str);
    	}
    }
    
    private void setRoot(String source){
        
        ASTParser parser = ASTParser.newParser(AST.JLS3);
        parser.setSource(source.toCharArray());
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        parser.setBindingsRecovery(true);
        
        root = (CompilationUnit) parser.createAST(null);
        
    }

    /**
     * @return source code used to generate the AST
     */
    public String getSource(){
        return this.source;
    }
    
    /**
     * @return the AST root node
     */
    public CompilationUnit getRoot(){
    	return this.root;
    }

}