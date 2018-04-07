package org.repositoryminer.metrics.parser.cppParser;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.cdt.core.dom.ast.ASTVisitor;
import org.eclipse.cdt.core.dom.ast.IASTAttribute;
import org.eclipse.cdt.core.dom.ast.IASTDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTDeclarator;
import org.eclipse.cdt.core.dom.ast.IASTFunctionDefinition;
import org.eclipse.cdt.core.dom.ast.IASTParameterDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTSimpleDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTDeclSpecifier;
import org.eclipse.cdt.core.dom.ast.IScope;
import org.eclipse.cdt.core.dom.ast.IBinding;

import org.repositoryminer.metrics.ast.AbstractDeclaration;
import org.repositoryminer.metrics.ast.AbstractAttribute;
import org.repositoryminer.metrics.ast.AbstractFunction;
import org.repositoryminer.metrics.ast.AbstractType;

public class FileVisitor extends ASTVisitor {

	
	private List<AbstractType> types = new ArrayList<>();
	private List<AbstractDeclaration> declarations = new ArrayList<>();
	private List<AbstractAttribute> attributes = new ArrayList<>();
	private List<AbstractFunction> function = new ArrayList<>();

	public List<AbstractType> getTypes() {
		return types;
	}

	public void setTypes(List<AbstractType> types) {
		this.types = types;
	}
	
	public List<AbstractDeclaration> getDeclaration() {
		return declarations;
	}

	public void setDeclaration(List<AbstractDeclaration> declarations) {
		this.declarations = declarations;
	}
	
	
				public int visit(IASTDeclaration declaration){
						
					if ((declaration instanceof IASTSimpleDeclaration)) {
							
						IASTSimpleDeclaration ast = (IASTSimpleDeclaration)declaration;
						String type = null;
							
						IASTDeclarator[] declarators = ast.getDeclarators();
						for (IASTDeclarator iastDeclarator : declarators) {
							declarations.add(new AbstractDeclaration(declaration.getParent(),declarators.toString()));
							}
					
						IASTAttribute[] attribute = ast.getAttributes();
						for (IASTAttribute iastAttribute : attribute) {
							attributes.add(new AbstractAttribute(declaration.getParent(),attribute.toString()));
							
						}

					}		
	 
					if ((declaration instanceof IASTFunctionDefinition)) {
					
						IASTFunctionDefinition ast = (IASTFunctionDefinition)declaration;
						IScope scope = ast.getScope();
						String type = null;
						String name = declaration.getRawSignature();
						IBinding[] bind = scope.find(name); 
						
						if (bind!=null) {
							type = bind.getClass().toString();
						}
						
						function.add(new AbstractFunction(declaration.getParent(), scope.toString(), type));
						
					}
	 
					return 3;
				}
				
				
					public int visit(IASTParameterDeclaration pD){
						
						IASTDeclarator de = pD.getDeclarator();
						IASTDeclSpecifier ds = pD.getDeclSpecifier();
						
						declarations.add(new AbstractDeclaration(pD.getParent(),de.getRawSignature()+"  "+ds.getRawSignature()));	
						return 3;
						
					}

	
				
	}