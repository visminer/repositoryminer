package org.repositoryminer.mining;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

import org.repositoryminer.codesmell.commit.ICommitCodeSmell;
import org.repositoryminer.codesmell.tag.ITagCodeSmell;
import org.repositoryminer.listener.IMiningListener;
import org.repositoryminer.listener.IProgressListener;
import org.repositoryminer.metric.ICommitMetric;
import org.repositoryminer.parser.Parser;
import org.repositoryminer.scm.SCMType;
import org.repositoryminer.technicaldebt.ITechnicalDebt;

public class RepositoryMiner {

	private String path;
	private String name;
	private String description;
	private SCMType scm;
	private String charset = "UTF-8";
	private int binaryThreshold = 2048;
	
	private IMiningListener miningListener;
	private IProgressListener progressListener;
	
	private List<Parser> parsers;
	private List<ICommitMetric> commitMetrics;
	private List<ICommitCodeSmell> commitCodeSmells;
	private List<ITechnicalDebt> technicalDebts;
	private List<TimeFrameType> timeFrames;
	private List<ITagCodeSmell> tagCodeSmells;

	public RepositoryMiner() {
	}

	public RepositoryMiner(String path, String name, String description, SCMType scm) {
		super();
		this.path = path;
		this.name = name;
		this.description = description;
		this.scm = scm;
	}

	// TODO : To implement
	// private boolean allowTextFiles;
	// private List<String> allowedExtensions;
	public void mine() throws UnsupportedEncodingException {
		MiningProcessor processor = new MiningProcessor(this);
		processor.setProgressListener(progressListener).
				setMiningListener(miningListener).
				mine();
	}
	
	public RepositoryMiner setMiningListener(IMiningListener listener) {
		miningListener =listener;
		
		return this;
	}
	
	public RepositoryMiner setProgressListener(IProgressListener listener) {
		progressListener = listener;
		
		return this;
	}
	
	public void setParsers(Parser... parsers) {
		this.parsers = Arrays.asList(parsers);
	}

	public void setCommitMetrics(ICommitMetric... commitMetrics) {
		this.commitMetrics = Arrays.asList(commitMetrics);
	}

	public void setCommitCodeSmells(ICommitCodeSmell... commitCodeSmells) {
		this.commitCodeSmells = Arrays.asList(commitCodeSmells);
	}

	public void setTechnicalDebts(ITechnicalDebt... technicalDebts) {
		this.technicalDebts = Arrays.asList(technicalDebts);
	}

	public void setTimeFrames(TimeFrameType... timeFrames) {
		this.timeFrames = Arrays.asList(timeFrames);
	}

	public void setTagCodeSmells(ITagCodeSmell tagCodeSmells) {
		this.tagCodeSmells = Arrays.asList(tagCodeSmells);
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
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
	 * @param name the name to set
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
	 * @param description the description to set
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
	 * @param scm the scm to set
	 */
	public void setScm(SCMType scm) {
		this.scm = scm;
	}

	/**
	 * @return the charset
	 */
	public String getCharset() {
		return charset;
	}

	/**
	 * @param charset the charset to set
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
	 * @param binaryThreshold the binaryThreshold to set
	 */
	public void setBinaryThreshold(int binaryThreshold) {
		this.binaryThreshold = binaryThreshold;
	}

	/**
	 * @return the parsers
	 */
	public List<Parser> getParsers() {
		return parsers;
	}

	/**
	 * @param parsers the parsers to set
	 */
	public void setParsers(List<Parser> parsers) {
		this.parsers = parsers;
	}

	/**
	 * @return the commitMetrics
	 */
	public List<ICommitMetric> getCommitMetrics() {
		return commitMetrics;
	}

	/**
	 * @param commitMetrics the commitMetrics to set
	 */
	public void setCommitMetrics(List<ICommitMetric> commitMetrics) {
		this.commitMetrics = commitMetrics;
	}

	/**
	 * @return the commitCodeSmells
	 */
	public List<ICommitCodeSmell> getCommitCodeSmells() {
		return commitCodeSmells;
	}

	/**
	 * @param commitCodeSmells the commitCodeSmells to set
	 */
	public void setCommitCodeSmells(List<ICommitCodeSmell> commitCodeSmells) {
		this.commitCodeSmells = commitCodeSmells;
	}

	/**
	 * @return the technicalDebts
	 */
	public List<ITechnicalDebt> getTechnicalDebts() {
		return technicalDebts;
	}

	/**
	 * @param technicalDebts the technicalDebts to set
	 */
	public void setTechnicalDebts(List<ITechnicalDebt> technicalDebts) {
		this.technicalDebts = technicalDebts;
	}

	/**
	 * @return the timeFrames
	 */
	public List<TimeFrameType> getTimeFrames() {
		return timeFrames;
	}

	/**
	 * @param timeFrames the timeFrames to set
	 */
	public void setTimeFrames(List<TimeFrameType> timeFrames) {
		this.timeFrames = timeFrames;
	}

	/**
	 * @return the tagCodeSmells
	 */
	public List<ITagCodeSmell> getTagCodeSmells() {
		return tagCodeSmells;
	}

	/**
	 * @param tagCodeSmells the tagCodeSmells to set
	 */
	public void setTagCodeSmells(List<ITagCodeSmell> tagCodeSmells) {
		this.tagCodeSmells = tagCodeSmells;
	}

}