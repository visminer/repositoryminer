package org.repositoryminer.metrics.parser.cpp;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.cdt.core.dom.IName;
import org.eclipse.cdt.core.dom.ast.ASTVisitor;
import org.eclipse.cdt.core.dom.ast.DOMException;
import org.eclipse.cdt.core.dom.ast.ExpansionOverlapsBoundaryException;
import org.eclipse.cdt.core.dom.ast.IASTCompositeTypeSpecifier;
import org.eclipse.cdt.core.dom.ast.IASTDeclSpecifier;
import org.eclipse.cdt.core.dom.ast.IASTDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTDeclarator;
import org.eclipse.cdt.core.dom.ast.IASTEnumerationSpecifier.IASTEnumerator;
import org.eclipse.cdt.core.dom.ast.IASTExpression;
import org.eclipse.cdt.core.dom.ast.IASTFunctionDefinition;
import org.eclipse.cdt.core.dom.ast.IASTName;
import org.eclipse.cdt.core.dom.ast.IASTPreprocessorIncludeStatement;
import org.eclipse.cdt.core.dom.ast.IASTStatement;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.core.dom.ast.IScope;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPASTCatchHandler;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPASTParameterDeclaration;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTBreakStatement;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTCaseStatement;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTCompositeTypeSpecifier;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTCompoundStatement;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTConditionalExpression;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTContinueStatement;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTDeclarator;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTDefaultStatement;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTDoStatement;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTEnumerationSpecifier;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTExpressionStatement;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTForStatement;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTFunctionDeclarator;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTFunctionDefinition;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTIfStatement;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTNamedTypeSpecifier;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTSimpleDeclSpecifier;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTSimpleDeclaration;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTSwitchStatement;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTTryBlockStatement;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTVisibilityLabel;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTWhileStatement;
import org.repositoryminer.metrics.ast.AbstractClass;
import org.repositoryminer.metrics.ast.AbstractEnum;
import org.repositoryminer.metrics.ast.AbstractEnumConstant;
import org.repositoryminer.metrics.ast.AbstractField;
import org.repositoryminer.metrics.ast.AbstractImport;
import org.repositoryminer.metrics.ast.AbstractMethod;
import org.repositoryminer.metrics.ast.AbstractParameter;
import org.repositoryminer.metrics.ast.AbstractStatement;
import org.repositoryminer.metrics.ast.AbstractType;
import org.repositoryminer.metrics.ast.NodeType;

public class HeaderVisitor extends ASTVisitor {
	
	private List<AbstractMethod> methods = new ArrayList<AbstractMethod>();
	private List<AbstractImport> imports = new ArrayList<AbstractImport>();
	private List<AbstractType>  types = new ArrayList<AbstractType>();
	private AbstractClass classe;
	private AbstractMethod currmethod = new AbstractMethod();	
	

	
	
	
	public HeaderVisitor() {
		super();
		this.shouldVisitNames = true;		
	}
	
	
	
	public void extractImports(IASTTranslationUnit tu) {
	
		for (IASTPreprocessorIncludeStatement include : tu.getIncludeDirectives()) {
			getImports().add(new AbstractImport(include.getName().getRawSignature(),false,false ));
		}
	}
	
	public List<AbstractMethod> getMethods() {
		return methods;
	}

	public void setMethods(List<AbstractMethod> methods) {
		this.methods = methods;
	}
	
		
	public List<AbstractImport> getImports(){
		return this.imports;
	}
	
	public void setTypes(List<AbstractType> types) {
		this.types = types;
	}
	
	public List<AbstractType> getTypes(){
		return this.types;
	}
	
	

	public AbstractMethod generateMethod(CPPASTFunctionDeclarator declarator, IASTDeclSpecifier specifier,String body) {
		
		
		AbstractMethod method = new AbstractMethod();
		String  methodname = declarator.getName().toString();
		
		if(declarator.getName().toString().indexOf("::") > -1) {
			methodname = declarator.getName().toString().split("::")[1];
		}
		
		
		
		
		List<String> modifiers = new ArrayList<String>();
		
		if(declarator.isConst())
			modifiers.add("const");
		if(declarator.isFinal())
			modifiers.add("final");
		if(declarator.isOverride())
			modifiers.add("override");
		if(declarator.isMutable())
			modifiers.add("mutable");
		if(declarator.isPureVirtual())
			modifiers.add("purevirtual");
		if(declarator.isVolatile())
			modifiers.add("volatile");

		method.setName(methodname);
		method.setReturnType(specifier.toString());
		method.setStartPosition( declarator.getOffset());
		
		
		
		
		
		if(body.indexOf('{') > 0 ) {
			
			method.setBody(body);	
			Pattern pattern = Pattern.compile("(\r\n)|(\n)|(\r)");
			
			int lines = 1;
			Matcher matcher = pattern.matcher(body.substring( body.indexOf('{') , body.lastIndexOf('}') ));
			while (matcher.find()) {
				lines++;
			}
			
			method.setLength( lines );
			
			
		}else {
			method.setBody("");
			method.setLength(0);
		}
		
		
		//System.out.println( method.getBody() );
		//System.out.println("=================================");
		
		method.setEndPosition(declarator.getOffset() + declarator.getLength());
		
		method.setConstructor(  (classe != null) && ( classe.getName() != null) && (classe.getName().equals(method.getName() ) )  );
		method.setModifiers(modifiers);
		
		List<AbstractParameter> parameters = new ArrayList< AbstractParameter>();
		ICPPASTParameterDeclaration[] FuncParam = declarator.getParameters();
		
		
		for (ICPPASTParameterDeclaration parameter : FuncParam) {
			
			AbstractParameter param = new AbstractParameter(parameter.getDeclSpecifier().toString(),parameter.getDeclarator().getName().toString());
			parameters.add(param);
		}
		
		method.setParameters(parameters);
		method.setStatements(new ArrayList<AbstractStatement>());
				
		return method;
		
	}
	
	
	
	/**
	 * Compare two methods (Adaptado de um Método desenvolvido por @author Luis Paulo
	 * 
	 * @param method1
	 * @param method2
	 * @return true if they have the same name && return type && parameters
	 */
	private boolean areEqual(AbstractMethod method1, AbstractMethod method2) {
		boolean are = (method1.getName().equals(method2.getName())
				&& (method1.getReturnType().equals(method1.getReturnType())) 
				&& (method1.getParameters().size() == method2.getParameters().size()));

		if (are) {
			for (int i = 0; (are) && (i < method1.getParameters().size()); i++) {
				are = (are
						&& (method1.getParameters().get(i).getName().equals(method2.getParameters().get(i).getName())) 
						&& (method1.getParameters().get(i).getType().equals(method2.getParameters().get(i).getType())));
			}
		}

		return are;
	}
	
	@Override
	public int visit(IASTName name){
		
			
		if((name.getParent() instanceof CPPASTCompositeTypeSpecifier)) {
			
			CPPASTCompositeTypeSpecifier typeSpe = (CPPASTCompositeTypeSpecifier) name.getParent(); 
			
			if(typeSpe.getName().toString().isEmpty())
				return 3;
				
			classe = new AbstractClass();
			classe.setName(typeSpe.getName().toString());
			classe.setInterface(typeSpe.isVirtual());
			classe.setMethods(new ArrayList<AbstractMethod>());
			classe.setFields(new ArrayList<AbstractField>());
			classe.setBody(typeSpe.getRawSignature());
			
			
			 //System.out.println("==========@@@@@@@@@@@@@@@@@@@@@@@@@@@@@=======================");
			  //System.out.println(classe.getName());
			 // System.out.println(classe.getBody());
			
			
			
			if(typeSpe.getKey() == IASTCompositeTypeSpecifier.k_struct){
				classe.setNodeType(NodeType.STRUCT);
			}
			
			
			IASTDeclaration[] items = typeSpe.getMembers();
		    String visibility = "";
			 
			 for (IASTDeclaration item : items) {
					
					if (item instanceof CPPASTSimpleDeclaration) {
						
						CPPASTSimpleDeclaration simpDecl = (CPPASTSimpleDeclaration) item;
						
						String body = simpDecl.getRawSignature();
						
						//System.out.println(body);
						//System.out.println("8888888888888888");
						
						if(simpDecl.getDeclarators().length > 0) {
			
							IASTDeclarator declarator = simpDecl.getDeclarators()[0];
							IASTDeclSpecifier specifier = simpDecl.getDeclSpecifier();
							
							if(declarator instanceof CPPASTFunctionDeclarator ) {
								
								CPPASTFunctionDeclarator funcDcl = (CPPASTFunctionDeclarator) declarator;
								currmethod = generateMethod(funcDcl, specifier, body);
								
								if(!visibility.equals(""))
									currmethod.getModifiers().add(visibility);
								
								classe.getMethods().add( currmethod );
															
							}else if(declarator instanceof CPPASTDeclarator && (((specifier instanceof CPPASTSimpleDeclSpecifier))
									|| (specifier instanceof CPPASTNamedTypeSpecifier))) {
								
							
								AbstractField field = new AbstractField();
								field.setName(declarator.getName().toString());
								field.setType(specifier.toString());
								field.setModifiers(new ArrayList<String>());
								
								
								if(!visibility.equals(""))
									field.getModifiers().add(visibility);
																
								classe.getFields().add(field);
								
							}
						}
						
						
					}else if(item instanceof CPPASTVisibilityLabel) {
						
						CPPASTVisibilityLabel visi = (CPPASTVisibilityLabel) item;
						switch( visi.getVisibility()) {
						case 1:
							visibility = "Public";
						break;
						case 2:
							visibility = "Protected";
						break;
						case 3:
							visibility = "Private";
						break;		
						
						}

					}else if(item instanceof CPPASTFunctionDefinition) {
						
						CPPASTFunctionDefinition funcDef = (CPPASTFunctionDefinition) item;
						IASTDeclarator declarator =funcDef.getDeclarator();
						IASTDeclSpecifier specifier = funcDef.getDeclSpecifier();
						
												
						
						if(declarator instanceof CPPASTFunctionDeclarator ) {
							
							CPPASTFunctionDeclarator funcDcl = (CPPASTFunctionDeclarator) declarator;
							currmethod = generateMethod(funcDcl, specifier,funcDef.getRawSignature().toString());
							
							if(!visibility.equals(""))
								currmethod.getModifiers().add(visibility);
							
							
							classe.getMethods().add( currmethod );
														
						}
							
					}
					
			 }
			
			 types.add(classe);
			
		}else if(name.getParent() instanceof CPPASTEnumerationSpecifier) {
			
			CPPASTEnumerationSpecifier enumSpec = (CPPASTEnumerationSpecifier) name.getParent();
			AbstractEnum enumm = new AbstractEnum();
			enumm.setName(enumSpec.getName().toString());
			List<AbstractEnumConstant> constants = new ArrayList<AbstractEnumConstant>();

			IASTEnumerator[] enumerators = enumSpec.getEnumerators();
			for (IASTEnumerator enumerator : enumerators) {
				AbstractEnumConstant constant = new AbstractEnumConstant();
				constant.setName(enumerator.getName().toString());
				constants.add(constant);
			}
			
			enumm.setConstants(constants);

			types.add(enumm);
		}

		return PROCESS_CONTINUE;
	}
	
	
	
}
