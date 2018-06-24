package org.repositoryminer.metrics.ast;

import java.util.List;

import org.repositoryminer.metrics.parser.Language;

/**
 * This class represents the file and is also the Abstract Syntax Tree root
 * node.
 */
public class AST {

	private String filename;
	private String source;
	private Language language;
	private List<AbstractType> types;
	private List<AbstractImport> imports;
	private String packageDeclaration;

	public String getFileName() {
		return filename;
	}

	public void setFileName(String filename) {
		this.filename = filename;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	public List<AbstractType> getTypes() {
		return types;
	}

	public void setTypes(List<AbstractType> types) {
		this.types = types;
	}

	public List<AbstractImport> getImports() {
		return imports;
	}

	public void setImports(List<AbstractImport> imports) {
		this.imports = imports;
	}

	public String getPackageDeclaration() {
		return packageDeclaration;
	}

	public void setPackageDeclaration(String packageDeclaration) {
		this.packageDeclaration = packageDeclaration;
	}

}