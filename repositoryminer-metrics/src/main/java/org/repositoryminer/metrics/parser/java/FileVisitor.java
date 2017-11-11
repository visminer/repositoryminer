package org.repositoryminer.metrics.parser.java;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.repositoryminer.metrics.ast.AbstractAnnotation;
import org.repositoryminer.metrics.ast.AbstractClass;
import org.repositoryminer.metrics.ast.AbstractEnum;
import org.repositoryminer.metrics.ast.AbstractImport;
import org.repositoryminer.metrics.ast.AbstractType;

public class FileVisitor extends ASTVisitor {

	private String packageName;
	private List<AbstractImport> imports = new ArrayList<>();
	private List<AbstractType> types = new ArrayList<>();

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public List<AbstractImport> getImports() {
		return imports;
	}

	public void setImports(List<AbstractImport> imports) {
		this.imports = imports;
	}

	public List<AbstractType> getTypes() {
		return types;
	}

	public void setTypes(List<AbstractType> types) {
		this.types = types;
	}

	@Override
	public boolean visit(PackageDeclaration node) {
		packageName = node.getName().getFullyQualifiedName();
		return true;
	}

	@Override
	public boolean visit(ImportDeclaration node) {
		imports.add(new AbstractImport(
				node.getName().getFullyQualifiedName(), 
				node.isStatic(), node.isOnDemand()));
		return true;
	}

	@Override
	public boolean visit(TypeDeclaration node) {
		AbstractClass clazz = new AbstractClass();
		if (node.getSuperclassType() != null) {
			ITypeBinding bind = node.getSuperclassType().resolveBinding();
			clazz.setSuperClass(bind.getQualifiedName());
		}

		clazz.setInterface(node.isInterface());
		if (packageName != null) {
			clazz.setName(packageName+'.'+node.getName().getFullyQualifiedName());
		} else {
			clazz.setName(node.getName().getFullyQualifiedName());
		}

		TypeVisitor visitor = new TypeVisitor();
		node.accept(visitor);

		clazz.setMethods(visitor.getMethods());
		clazz.setStartPosition(node.getStartPosition());
		clazz.setEndPosition(node.getStartPosition() + node.getLength() - 1);
		clazz.setFields(visitor.getFields());
		
		types.add(clazz);
		return true;
	}

	@Override
	public boolean visit(EnumDeclaration node) {
		AbstractEnum absEnum = new AbstractEnum();
		
		if (packageName != null) {
			absEnum.setName(packageName+'.'+node.getName().getFullyQualifiedName());
		} else {
			absEnum.setName(node.getName().getFullyQualifiedName());
		}

		TypeVisitor visitor = new TypeVisitor();
		node.accept(visitor);
		
		absEnum.setMethods(visitor.getMethods());
		absEnum.setFields(visitor.getFields());
		absEnum.setStartPosition(node.getStartPosition());
		absEnum.setEndPosition(node.getStartPosition() + node.getLength() - 1);
		absEnum.setConstants(visitor.getEnumConstants());
		
		types.add(absEnum);
		return true;
	}

	@Override
	public boolean visit(AnnotationTypeDeclaration node) {
		AbstractAnnotation absAnnotation = new AbstractAnnotation();
		
		if (packageName != null) {
			absAnnotation.setName(packageName+'.'+node.getName().getFullyQualifiedName());
		} else {
			absAnnotation.setName(node.getName().getFullyQualifiedName());
		}

		TypeVisitor visitor = new TypeVisitor();
		node.accept(visitor);
		
		absAnnotation.setMethods(visitor.getMethods());
		absAnnotation.setFields(visitor.getFields());
		absAnnotation.setStartPosition(node.getStartPosition());
		absAnnotation.setEndPosition(node.getStartPosition() + node.getLength() - 1);
		absAnnotation.setMembers(visitor.getAnnotationMembers());
		
		types.add(absAnnotation);
		return true;
	}

}