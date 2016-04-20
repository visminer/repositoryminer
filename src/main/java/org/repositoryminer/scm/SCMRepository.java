package org.repositoryminer.scm;

import java.util.List;

import org.repositoryminer.antipattern.IAntiPattern;
import org.repositoryminer.metric.IMetric;
import org.repositoryminer.model.SCMType;
import org.repositoryminer.parser.IParser;

public class SCMRepository {

	private String path;
	private String name;
	private String description;
	private SCMType scm;
	private int commitThreshold = 3000;
	private String charset = "UTF-8";
	private int binaryThreshold = 2048;
	private List<IParser> parser;
	private List<IMetric> metrics;
	private List<IAntiPattern> antiPatterns;
	private boolean allowTextFiles;
	private List<String> allowedExtensions;

	public SCMRepository() {
	}

	public SCMRepository(String path, String name, String description, SCMType scm) {
		super();
		this.path = path;
		this.name = name;
		this.description = description;
		this.scm = scm;
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
	public void setPath(String path) {
		this.path = path;
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
	public void setName(String name) {
		this.name = name;
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
	public void setDescription(String description) {
		this.description = description;
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
	public void setScm(SCMType scm) {
		this.scm = scm;
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
	public void setCommitThreshold(int commitThreshold) {
		this.commitThreshold = commitThreshold;
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
	public void setCharset(String charset) {
		this.charset = charset;
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
	public void setBinaryThreshold(int binaryThreshold) {
		this.binaryThreshold = binaryThreshold;
	}

	/**
	 * @return the parser
	 */
	public List<IParser> getParser() {
		return parser;
	}

	/**
	 * @param parser
	 *            the parser to set
	 */
	public void setParser(List<IParser> parser) {
		this.parser = parser;
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
	public void setMetrics(List<IMetric> metrics) {
		this.metrics = metrics;
	}

	/**
	 * @return the antiPatterns
	 */
	public List<IAntiPattern> getAntiPatterns() {
		return antiPatterns;
	}

	/**
	 * @param antiPatterns
	 *            the antiPatterns to set
	 */
	public void setAntiPatterns(List<IAntiPattern> antiPatterns) {
		this.antiPatterns = antiPatterns;
	}

	/**
	 * @return the allowTextFiles
	 */
	public boolean isAllowTextFiles() {
		return allowTextFiles;
	}

	/**
	 * @param allowTextFiles
	 *            the allowTextFiles to set
	 */
	public void setAllowTextFiles(boolean allowTextFiles) {
		this.allowTextFiles = allowTextFiles;
	}

	/**
	 * @return the allowedExtensions
	 */
	public List<String> getAllowedExtensions() {
		return allowedExtensions;
	}

	/**
	 * @param allowedExtensions
	 *            the allowedExtensions to set
	 */
	public void setAllowedExtensions(List<String> allowedExtensions) {
		this.allowedExtensions = allowedExtensions;
	}

}