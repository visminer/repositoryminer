package org.repositoryminer.scm;

import java.util.Arrays;
import java.util.List;

import org.repositoryminer.codesmell.ICodeSmell;
import org.repositoryminer.metric.IMetric;
import org.repositoryminer.model.SCMType;
import org.repositoryminer.parser.IParser;
import org.repositoryminer.technicaldebt.ITechnicalDebt;

public class SCMRepository {

	private String path;
	private String name;
	private String description;
	private SCMType scm;
	private int commitThreshold = 3000;
	private String charset = "UTF-8";
	private int binaryThreshold = 2048;
	private List<IParser> parsers;
	private List<IMetric> metrics;
	private List<ICodeSmell> codeSmells;
	private List<ITechnicalDebt> technicalDebts;
	// TODO : Should I use this?
	//private boolean allowTextFiles;
	//private List<String> allowedExtensions;

	public SCMRepository() {
	}

	public SCMRepository(String path, String name, String description, SCMType scm) {
		super();
		this.path = path;
		this.name = name;
		this.description = description;
		this.scm = scm;
	}

	public SCMRepository setParsers(IParser... parsers){
		this.parsers = Arrays.asList(parsers);
		return this;
	}
	public SCMRepository setMetrics(IMetric... metrics){
		this.metrics = Arrays.asList(metrics);
		return this;
	}
	
	public SCMRepository setCodeSmells(ICodeSmell... codeSmells){
		this.codeSmells = Arrays.asList(codeSmells);
		return this;
	}
	
	public SCMRepository setTechnicalDebts(ITechnicalDebt... technicalDebts) {
		this.technicalDebts = Arrays.asList(technicalDebts);
		return this;
	}
	
	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path
	 *            the path to set
	 */
	public SCMRepository setPath(String path) {
		this.path = path;
		return this;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public SCMRepository setName(String name) {
		this.name = name;
		return this;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public SCMRepository setDescription(String description) {
		this.description = description;
		return this;
	}

	/**
	 * @return the scm
	 */
	public SCMType getScm() {
		return scm;
	}

	/**
	 * @param scm
	 *            the scm to set
	 */
	public SCMRepository setScm(SCMType scm) {
		this.scm = scm;
		return this;
	}

	/**
	 * @return the commitThreshold
	 */
	public int getCommitThreshold() {
		return commitThreshold;
	}

	/**
	 * @param commitThreshold
	 *            the commitThreshold to set
	 */
	public SCMRepository setCommitThreshold(int commitThreshold) {
		this.commitThreshold = commitThreshold;
		return this;
	}

	/**
	 * @return the charset
	 */
	public String getCharset() {
		return charset;
	}

	/**
	 * @param charset
	 *            the charset to set
	 */
	public SCMRepository setCharset(String charset) {
		this.charset = charset;
		return this;
	}

	/**
	 * @return the binaryThreshold
	 */
	public int getBinaryThreshold() {
		return binaryThreshold;
	}

	/**
	 * @param binaryThreshold
	 *            the binaryThreshold to set
	 */
	public SCMRepository setBinaryThreshold(int binaryThreshold) {
		this.binaryThreshold = binaryThreshold;
		return this;
	}

	/**
	 * @return the parsers
	 */
	public List<IParser> getParsers() {
		return parsers;
	}

	/**
	 * @param parsers
	 *            the parsers to set
	 */
	public SCMRepository setParsers(List<IParser> parsers) {
		this.parsers = parsers;
		return this;
	}

	/**
	 * @return the metrics
	 */
	public List<IMetric> getMetrics() {
		return metrics;
	}

	/**
	 * @param metrics
	 *            the metrics to set
	 */
	public SCMRepository setMetrics(List<IMetric> metrics) {
		this.metrics = metrics;
		return this;
	}

	/**
	 * @return the codeSmells
	 */
	public List<ICodeSmell> getCodeSmells() {
		return codeSmells;
	}

	/**
	 * @param codeSmells
	 *            the antiPatterns to set
	 */
	public SCMRepository setCodeSmells(List<ICodeSmell> codeSmells) {
		this.codeSmells = codeSmells;
		return this;
	}
	
	/**
	 * @return the technicalDebts
	 */
	public List<ITechnicalDebt> getTechnicalDebts() {
		return technicalDebts;
	}

	/**
	 * @param technicalDebts
	 *            the technicalDebts to set
	 */
	public SCMRepository setTechnicalDebts(List<ITechnicalDebt> technicalDebts) {
		this.technicalDebts = technicalDebts;
		return this;
	}
	
}