import java.beans.Statement;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.cdt.core.dom.ast.ASTVisitor;
import org.eclipse.cdt.core.dom.ast.DOMException;
import org.eclipse.cdt.core.dom.ast.EScopeKind;
import org.eclipse.cdt.core.dom.ast.ExpansionOverlapsBoundaryException;
import org.eclipse.cdt.core.dom.ast.IASTAttribute;
import org.eclipse.cdt.core.dom.ast.IASTDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTDeclarator;
import org.eclipse.cdt.core.dom.ast.IASTFunctionDefinition;
import org.eclipse.cdt.core.dom.ast.IASTName;
import org.eclipse.cdt.core.dom.ast.IASTNode;
import org.eclipse.cdt.core.dom.ast.IASTExpression;
import org.eclipse.cdt.core.dom.ast.IASTInitializerClause;
import org.eclipse.cdt.core.dom.ast.IASTPreprocessorIncludeStatement;
import org.eclipse.cdt.core.dom.ast.IASTSimpleDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTStatement;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.core.dom.ast.IASTTypeId;
import org.eclipse.cdt.core.dom.ast.IASTForStatement;
import org.eclipse.cdt.core.dom.ast.IASTDoStatement;
import org.eclipse.cdt.core.dom.ast.IASTIfStatement;
import org.eclipse.cdt.core.dom.ast.IASTSwitchStatement;
import org.eclipse.cdt.core.dom.ast.IASTCaseStatement;
import org.eclipse.cdt.core.dom.ast.IASTWhileStatement;
import org.eclipse.cdt.core.dom.ast.IASTBreakStatement;
import org.eclipse.cdt.core.dom.ast.IASTGotoStatement;
import org.eclipse.cdt.core.dom.ast.IASTProblemStatement;
import org.eclipse.cdt.core.dom.ast.IASTReturnStatement;
import org.eclipse.cdt.core.dom.ast.IASTConditionalExpression;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPASTNamespaceDefinition;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPASTTryBlockStatement;
import org.eclipse.cdt.core.dom.ast.IASTContinueStatement;
import org.eclipse.cdt.core.dom.ast.IASTDeclaration;
import org.eclipse.cdt.core.dom.ast.IBinding;
import org.eclipse.cdt.core.dom.ast.IASTParameterDeclaration;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPASTRangeBasedForStatement;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTBinaryExpression;
import org.eclipse.cdt.core.dom.ast.IASTExpressionStatement;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPASTCatchHandler;
import org.eclipse.cdt.core.dom.ast.IASTDeclSpecifier;
import org.eclipse.cdt.core.dom.ast.IASTAttributeOwner;
import org.eclipse.cdt.core.dom.ast.IScope;
import org.eclipse.cdt.core.dom.ast.IType;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPASTFunctionDeclarator;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPASTVisibilityLabel;
import org.eclipse.cdt.core.dom.ast.gnu.cpp.GPPLanguage;
import org.eclipse.cdt.core.parser.DefaultLogService;
import org.eclipse.cdt.core.parser.FileContent;
import org.eclipse.cdt.core.parser.IParserLogService;
import org.eclipse.cdt.core.parser.IScannerInfo;
import org.eclipse.cdt.core.parser.IncludeFileContentProvider;
import org.eclipse.cdt.core.parser.ScannerInfo;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTFunctionDeclarator;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTTranslationUnit;

		public class ParserExample
		{
				public static void main(String[] args)
				throws Exception
				{
					FileContent fileContent = FileContent.createForExternalFileLocation("C:/Users/Danniel/Desktop/velha2.cpp");
 
					Map definedSymbols = new HashMap();
					String[] includePaths = new String[0];
					IScannerInfo info = new ScannerInfo(definedSymbols, includePaths);
					IParserLogService log = new DefaultLogService();
	 
					IncludeFileContentProvider emptyIncludes = IncludeFileContentProvider.getEmptyFilesProvider();
					
					int opts = 8;
					IASTTranslationUnit translationUnit = GPPLanguage.getDefault().getASTTranslationUnit(fileContent, info, emptyIncludes, null, opts, log);

					IASTPreprocessorIncludeStatement[] includes = translationUnit.getIncludeDirectives();
					for (IASTPreprocessorIncludeStatement include : includes) {
					System.out.println("include - " + include.getName());
				
				}
System.out.println("-----------------------------------------------------");
System.out.println("-----------------------------------------------------");
System.out.println("Here Begins!!!!!!!!!!!!!");
System.out.println("-----------------------------------------------------");
System.out.println("-----------------------------------------------------");
  
					//printTree(translationUnit, 1);  //imprime a árvore
				


				ASTVisitor visitor = new ASTVisitor()   //cria um novo visitante tipo ASTVisitor para visitar nós da árvore
				{
					
					
					public int visit(IASTParameterDeclaration pD) {
						
						IASTDeclarator de = pD.getDeclarator();
						IASTDeclSpecifier ds = pD.getDeclSpecifier();
						
						System.out.println("     AMOMOMSOAMOMAOMAOSMOAMOM" +de.getRawSignature() + ds.getRawSignature());
						
						return 3;
						
					}
					
					public int visit(IASTTranslationUnit tu) {
						
						System.out.println (tu.getContainingFilename());
						
						return 3;
					}
					
					public int visit(ICPPASTNamespaceDefinition name) {
						
						IASTName m = name.getName();
				
						IBinding bind = m.getBinding();
						
						System.out.println(bind.toString() + m.getRawSignature());
						
						return 3; 
					}
					
					
					public int visit(IASTName name) 
					{
						if ((name.getParent() instanceof CPPASTFunctionDeclarator)) {
							
							
							IBinding bind = name.resolveBinding();
							String type = null;
							
							if (bind == null) {
								System.out.println("Bind not solve to "+name.getClass().getName());
								return 3;
							}
							
							try {
								if (bind.getScope().getKind() == EScopeKind.eClassType ) {
									type = bind.getClass().getTypeName();
								}
							} catch (DOMException e) {
								e.printStackTrace();
							}
						}
							
							System.out.println("IASTName: " + name.getClass().getSimpleName() + "(" + name.getRawSignature() + ") - > parent: " + name.getParent().getClass().getSimpleName());
							System.out.println("-- isVisible: " + ParserExample.isVisible(name));

						return 3;
					}

					public int visit(IASTDeclaration declaration)
					{
						
						if ((declaration instanceof IASTSimpleDeclaration)) {
							
							IASTSimpleDeclaration ast = (IASTSimpleDeclaration)declaration;
							String type = null;
							
								IASTDeclarator[] declarators = ast.getDeclarators();
								for (IASTDeclarator iastDeclarator : declarators) {
							
									}
					
								IASTAttribute[] attributes = ast.getAttributes();
								for (IASTAttribute iastAttribute : attributes) {
								
								}

						}		
	 
					if ((declaration instanceof IASTFunctionDefinition)) {
						IASTFunctionDefinition ast = (IASTFunctionDefinition)declaration;
						
					
						IScope scope = ast.getScope();
						String type = null;
						String name = scope.getScopeName().toString();
						IBinding[] bind = scope.find(name); 
						
						if (bind!=null) {
							type = bind.getClass().getTypeName();
						}
						
					
					}
	 
					return 3;
			}

				public int visit(IASTTypeId typeId)
				{
					
					
					
					System.out.println("typeId: " + typeId.getRawSignature());
					return 3;
				}
				
				
				

				public int visit(IASTStatement statement)
				{
				//	statements.add(new AbstractStatement(statement.getSyntax().toString() , null));
					if ((statement instanceof IASTForStatement)) {
						
						
						IASTStatement init = ((IASTForStatement) statement).getInitializerStatement();
						IASTExpression condExpression = ((IASTForStatement) statement).getConditionExpression();
						IASTExpression iterExpression = ((IASTForStatement) statement).getIterationExpression();
							
						String expressionn = init.getRawSignature()+condExpression.getRawSignature()+";"+iterExpression.getRawSignature();
						
							System.out.println("(" + expressionn +")");
			
			
					}
					
					if ((statement instanceof IASTDoStatement)) {
						
						IASTExpression expression = ((IASTDoStatement) statement).getCondition();
						System.out.println("DO    " + expression.getRawSignature());
					}
					
					if ((statement instanceof IASTIfStatement)) {
						
						IASTExpression expression = ((IASTIfStatement) statement).getConditionExpression();
						
						
						System.out.println("If (" + expression.getRawSignature() + ")");
					}
					
					if ((statement instanceof IASTWhileStatement)) {
						
						IASTExpression expression = ((IASTWhileStatement) statement).getCondition();
		
						
							System.out.println("While" + expression.getExpressionType() + " (" + expression.getRawSignature() + ")");
						
					}
					
					if ((statement instanceof IASTSwitchStatement)) {
						
						IASTExpression expression = ((IASTSwitchStatement) statement).getControllerExpression();
						IASTAttribute[] teste = statement.getAttributes();
						for (IASTAttribute iastAttribute : teste) {
							System.out.println("iastAttribute > " + iastAttribute);
						}
						System.out.println("Switch (" + expression.getRawSignature() + ")");
					}
					
					if ((statement instanceof IASTCaseStatement)) {
						
						IASTExpression expression = ((IASTCaseStatement) statement).getExpression();
						System.out.println("Case (" + expression.getRawSignature() + ")");
					}
					
					if ((statement instanceof IASTBreakStatement)) {
						

						System.out.println(statement.getRawSignature());
					}
					
					if ((statement instanceof IASTExpressionStatement)) {
						
						System.out.println("    expression (" + statement.getRawSignature() + ")");
					}

					if ((statement instanceof IASTGotoStatement)) {
						
						System.out.println("  Go To  hell (" + statement.getRawSignature() + ")");
					}


					if ((statement instanceof IASTReturnStatement)) {
	
						System.out.println(" return  " );
					}
					
					if ((statement instanceof IASTContinueStatement)) {
						
						System.out.println(" continue  " );
					}
					

					if ((statement instanceof ICPPASTCatchHandler)) {
						
						IASTDeclaration m = ((ICPPASTCatchHandler) statement).getDeclaration();
						System.out.println(" Cath    "+m.getRawSignature() + "  <- ->    ");
					}
				
					if ((statement instanceof ICPPASTTryBlockStatement)) {
						
						String teste = statement.getRawSignature();
							
							System.out.println("try    " + teste);
						
						System.out.println(statement.getRawSignature());
					}
					
						if ((statement instanceof ICPPASTRangeBasedForStatement)) {
						
						IASTDeclaration m = ((ICPPASTRangeBasedForStatement) statement).getDeclaration();
						System.out.println("   "+m.getRawSignature() + "  <- ->    ");
					}
					
					return 3;
				}
				
				public int visit(IASTExpression expression){
					
					
					if ((expression instanceof CPPASTBinaryExpression)) {
			
						System.out.println ("bin expression  ->  " + expression.getRawSignature());
						
					}
					
						String m = expression.getRawSignature();
					

		
					return 3;
				}
				
				/*public int visit(IASTStatement statement);
				{ 
					statements.add(new AbstractStatement(statement.getClass().getSimpleName(), statement.getRawSignature()));
					return 3;
				}*/
				
			
				
				public int visit(IASTAttribute attribute)
				{
				
					
					
					System.out.println("attribute: " + attribute.getRawSignature());
					return 3;
				}
				
				
				
				
			};

				visitor.shouldVisitNames = true;
				visitor.shouldVisitDeclarations = false;
				visitor.shouldVisitDeclarators = true;
				visitor.shouldVisitAttributes = true;
				visitor.shouldVisitStatements = true;
				visitor.shouldVisitTypeIds = true;
				visitor.shouldVisitExpressions = true;

				translationUnit.accept(visitor);
			}


				private static void printTree(IASTNode node, int index) {	//imprime árvore
					IASTNode[] children = node.getChildren();   //pega o filho o nó

					boolean printContents = true;

					if ((node instanceof CPPASTTranslationUnit)) {
						printContents = false;
					}

					String offset = "";   
					try {
						offset = node.getSyntax() != null ? " (offset: " + node.getFileLocation().getNodeOffset() + "," + node.getFileLocation().getNodeLength() + ")" : "";
						printContents = node.getFileLocation().getNodeLength() < 30;
					} catch (ExpansionOverlapsBoundaryException e) {
						e.printStackTrace();
					} catch (UnsupportedOperationException e) {
						offset = "UnsupportedOperationException";
					}
 
				System.out.println(String.format(new StringBuilder("%1$").append(index * 2).append("s").toString(), new Object[] { "-" }) + node.getClass().getSimpleName() + offset + " -> " + (printContents ? node.getRawSignature().replaceAll("\n", " \\ ") : node.getRawSignature().subSequence(0, 5)));
	 
				for (IASTNode iastNode : children) //pra cada filho
					printTree(iastNode, index + 1);  //imprimir filho, index +1
				}

				public static boolean isVisible(IASTNode current)
				{
					IASTNode declator = current.getParent().getParent();
					IASTNode[] children = declator.getChildren();

					for (IASTNode iastNode : children) {
						if ((iastNode instanceof ICPPASTVisibilityLabel)) {
							return 1 == ((ICPPASTVisibilityLabel)iastNode).getVisibility();
						}
					}

					return false;
				}
		}
