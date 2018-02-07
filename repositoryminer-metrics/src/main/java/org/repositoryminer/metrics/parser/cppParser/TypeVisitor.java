package org.repositoryminer.metrics.parser.cppParser;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.Expression;
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

	
}