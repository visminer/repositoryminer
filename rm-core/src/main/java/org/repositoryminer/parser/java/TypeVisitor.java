package org.repositoryminer.parser.java;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.repositoryminer.ast.AbstractAnnotationMember;
import org.repositoryminer.ast.AbstractEnumConstant;
import org.repositoryminer.ast.AbstractField;
import org.repositoryminer.ast.AbstractMethod;
import org.repositoryminer.ast.AbstractParameter;

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
		String type = node.getType().resolveBinding().getQualifiedName();

		List<String> modifiers = new ArrayList<String>();
		for (Object modifier : node.modifiers()) {
			modifiers.add(modifier.toString());
		}

		for (VariableDeclarationFragment vdf : (List<VariableDeclarationFragment>) node.fragments()) {
			fields2.add(new AbstractField(vdf.getName().getIdentifier(), type, modifiers));
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

		MethodVisitor visitor = new MethodVisitor();
		node.getBody().accept(visitor);
		
		method.setMaxDepth(visitor.getMaxDepth());
		method.setStatements(visitor.getStatements());

		if (node.getReturnType2() != null) {
			method.setReturnType(node.getReturnType2().resolveBinding().getQualifiedName());
		}

		method.setStartPosition(node.getStartPosition());
		method.setEndPosition(node.getStartPosition() + node.getLength() - 1);
		
		methods.add(method);
		return true;
	}

}