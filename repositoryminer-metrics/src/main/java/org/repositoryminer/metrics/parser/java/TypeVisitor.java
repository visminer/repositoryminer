package org.repositoryminer.metrics.parser.java;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.repositoryminer.metrics.ast.AbstractAnnotationMember;
import org.repositoryminer.metrics.ast.AbstractEnumConstant;
import org.repositoryminer.metrics.ast.AbstractField;
import org.repositoryminer.metrics.ast.AbstractMethod;
import org.repositoryminer.metrics.ast.AbstractParameter;
import org.repositoryminer.metrics.ast.AbstractStatement;

public class TypeVisitor extends ASTVisitor {

	private List<AbstractMethod> methods = new ArrayList<>();
	private List<AbstractAnnotationMember> annotationMembers = new ArrayList<>();
	private List<AbstractField> fields = new ArrayList<>();
	private List<AbstractEnumConstant> enumConstants = new ArrayList<>();

	public List<AbstractMethod> getMethods() {
		return methods;
	}

	public void setMethods(List<AbstractMethod> methods) {
		this.methods = methods;
	}

	public List<AbstractAnnotationMember> getAnnotationMembers() {
		return annotationMembers;
	}

	public void setAnnotationMembers(List<AbstractAnnotationMember> annotationMembers) {
		this.annotationMembers = annotationMembers;
	}

	public List<AbstractField> getFields() {
		return fields;
	}

	public void setFields(List<AbstractField> fields) {
		this.fields = fields;
	}

	public List<AbstractEnumConstant> getEnumConstants() {
		return enumConstants;
	}

	public void setEnumConstants(List<AbstractEnumConstant> enumConstants) {
		this.enumConstants = enumConstants;
	}

	@Override
	public boolean visit(AnnotationTypeMemberDeclaration node) {
		AbstractAnnotationMember annoMember = new AbstractAnnotationMember(
				node.getType().resolveBinding().getQualifiedName(), node.getName().getIdentifier());
		annoMember.setDefaultExpression(node.getDefault() != null ? node.getDefault().toString() : null);

		annotationMembers.add(annoMember);
		return true;
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean visit(EnumConstantDeclaration node) {
		AbstractEnumConstant constant = new AbstractEnumConstant(node.getName().getIdentifier());

		List<String> expressions = new ArrayList<>();
		for (Expression exp : (List<Expression>) node.arguments()) {
			expressions.add(exp.toString());
		}
		constant.setArguments(expressions);

		enumConstants.add(constant);
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean visit(FieldDeclaration node) {
		List<AbstractField> fields2 = new ArrayList<>();
		ITypeBinding bind = node.getType().resolveBinding();
		
		String type = "";
		boolean primitive = false;
		
		if (bind != null) {
			type = bind.getQualifiedName();
			primitive = bind.isPrimitive();
		}
		
		List<String> modifiers = new ArrayList<String>();
		for (Object modifier : node.modifiers()) {
			modifiers.add(modifier.toString());
		}

		boolean builtIn = type.startsWith("java.") || type.startsWith("javax.") ? true : false;
		
		for (VariableDeclarationFragment vdf : (List<VariableDeclarationFragment>) node.fragments()) {
			fields2.add(new AbstractField(vdf.getName().getIdentifier(), type, modifiers, primitive, builtIn));
		}

		fields.addAll(fields2);
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean visit(MethodDeclaration node) {
		AbstractMethod method = new AbstractMethod();
		method.setConstructor(node.isConstructor());
		method.setVarargs(node.isVarargs());

		StringBuilder builder = null;
		builder = new StringBuilder();
		builder.append(node.getName().getIdentifier()).append("(");

		List<AbstractParameter> params = new ArrayList<>();
		for (SingleVariableDeclaration var : (List<SingleVariableDeclaration>) node.parameters()) {
			IVariableBinding varBind = var.resolveBinding();
			AbstractParameter param = new AbstractParameter(varBind.getType().getQualifiedName(), varBind.getName());
			params.add(param);
			builder.append(param.getType() + ",");
		}

		if (builder.substring(builder.length() - 1).equals(",")) {
			builder.replace(builder.length() - 1, builder.length(), ")");
		} else {
			builder.append(")");
		}

		method.setName(builder.toString());
		method.setParameters(params);
		verifyAccessorMethod(method);

		List<String> throwsList = new ArrayList<String>();
		List<Type> types = node.thrownExceptionTypes();
		for (Type type : types) {
			throwsList.add(type.toString());
		}
		method.setThrownsExceptions(throwsList);

		List<String> modifiers = new ArrayList<String>();
		for (Object modifier : node.modifiers()) {
			modifiers.add(modifier.toString());
		}
		method.setModifiers(modifiers);

		
		if (node.getBody() != null) {
			MethodVisitor visitor = new MethodVisitor();
			node.getBody().accept(visitor);
			method.setMaxDepth(visitor.getMaxDepth());
			method.setStatements(visitor.getStatements());
		} else {
			method.setStatements(new ArrayList<AbstractStatement>());
		}

		if (node.getReturnType2() != null) {
			ITypeBinding bind = node.getReturnType2().resolveBinding();
			if (bind != null)
			method.setReturnType(bind.getQualifiedName());
		}

		method.setStartPosition(node.getStartPosition());
		method.setEndPosition(node.getStartPosition() + node.getLength() - 1);
		
		methods.add(method);
		return true;
	}

	public void verifyAccessorMethod(AbstractMethod method) {
		String name = method.getName();
		String field = null;
		
		if ((name.startsWith("get") || name.startsWith("set")) && name.length() > 3) {
			field = name.substring(3);
		} else if (name.startsWith("is") && name.length() > 2) {
			field = name.substring(2);
		} else {
			return;
		}

		char c[] = field.toCharArray();
		c[0] = Character.toLowerCase(c[0]);
		String field2 = new String(c);

		for (AbstractField fd : fields) {
			if (fd.getName().equals(field) || fd.getName().equals(field2)) {
				method.setAccessor(true);
				method.setAccessoredField(fd.getName());
				break;
			}
		}
	}
	
}