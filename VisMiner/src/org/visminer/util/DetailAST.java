package org.visminer.util;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

/**
 * The utility class to create AST from source code
 * 
 * @author felipe
 */
public class DetailAST {

    private CompilationUnit root = null;
    
    private String source;
    
    /**
     * Create a AST from a file
     *
     * @param file the file
     */
    public void parserFromFile(File file){
        
        try {        
            byte[] bytes = FileUtils.readFileToByteArray(file);
            String source = new String(bytes, "UTF-8");
            this.source = source;
            setRoot(source);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(DetailAST.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DetailAST.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    /**
     * Create a AST from a String
     * 
     * @param source the source
     */
    public void parserFromString(String source){
        this.source = source;
        setRoot(source);
    }
    
    /**
     * @param source the new root
     */
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
     * @return the AST parent's node
     */
    public CompilationUnit getRoot(){

    	return this.root;
        
    }
    
}