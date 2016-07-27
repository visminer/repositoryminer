package org.repositoryminer.mining;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

import org.repositoryminer.codesmell.commit.ICommitCodeSmell;
import org.repositoryminer.codesmell.tag.ITagCodeSmell;
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
	
	private List<Parser> parsers;
	private List<ICommitMetric> commitMetrics;
	private List<ICommitCodeSmell> commitCodeSmells;
	private List<ITechnicalDebt> technicalDebts;
	private List<TimeFrameType> timeFrames;
	private List<ITagCodeSmell> tagCodeSmells;
	
	private IProgressListener progressListener;
	
	// TODO : To implement
	// private boolean allowTextFiles;
	// private List<String> allowedExtensions;

	public RepositoryMiner() {}
	
	public RepositoryMiner(String path, String name, String description, SCMType scm) {
		super();
		
		this.path = path;
		this.name = name;
		this.description = description;
		this.scm = scm;
	}
	
	public RepositoryMiner setParsers(Parser... parsers) {
		this.parsers = Arrays.asList(parsers);
		
		return this;
	}

	public RepositoryMiner setCommitMetrics(ICommitMetric... commitMetrics) {
		this.commitMetrics = Arrays.asList(commitMetrics);
		
		return this;
	}

	public RepositoryMiner setCommitCodeSmells(ICommitCodeSmell... commitCodeSmells) {
		this.commitCodeSmells = Arrays.asList(commitCodeSmells);
		
		return this;
	}

	public RepositoryMiner setTechnicalDebts(ITechnicalDebt... technicalDebts) {
		this.technicalDebts = Arrays.asList(technicalDebts);
		
		return this;
	}

	public RepositoryMiner setTimeFrames(TimeFrameType... timeFrames) {
		this.timeFrames = Arrays.asList(timeFrames);
		
		return this;
	}

	public RepositoryMiner setTagCodeSmells(ITagCodeSmell... tagCodeSmells) {
		this.tagCodeSmells = Arrays.asList(tagCodeSmells);
		
		return this;
	}

	public String getPath() {
		return path;
	}

	public RepositoryMiner setPath(String path) {
		this.path = path;
		
		return this;
	}

	public String getName() {
		return name;
	}

	public RepositoryMiner setName(String name) {
		this.name = name;
		
		return this;
	}

	public String getDescription() {
		return description;
	}

	public RepositoryMiner setDescription(String description) {
		this.description = description;
		
		return this;
	}

	public SCMType getScm() {
		return scm;
	}

	public RepositoryMiner setScm(SCMType scm) {
		this.scm = scm;
		
		return this;
	}

	public String getCharset() {
		return charset;
	}

	public RepositoryMiner setCharset(String charset) {
		this.charset = charset;
		
		return this;
	}

	public int getBinaryThreshold() {
		return binaryThreshold;
	}

	public RepositoryMiner setBinaryThreshold(int binaryThreshold) {
		this.binaryThreshold = binaryThreshold;
		
		return this;
	}

	public List<Parser> getParsers() {
		return parsers;
	}

	public RepositoryMiner setParsers(List<Parser> parsers) {
		this.parsers = parsers;
		
		return this;
	}

	public List<ICommitMetric> getCommitMetrics() {
		return commitMetrics;
	}

	public RepositoryMiner setCommitMetrics(List<ICommitMetric> commitMetrics) {
		this.commitMetrics = commitMetrics;
		
		return this;
	}

	public List<ICommitCodeSmell> getCommitCodeSmells() {
		return commitCodeSmells;
	}

	public RepositoryMiner setCommitCodeSmells(List<ICommitCodeSmell> commitCodeSmells) {
		this.commitCodeSmells = commitCodeSmells;
		
		return this;
	}

	public List<ITechnicalDebt> getTechnicalDebts() {
		return technicalDebts;
	}

	public RepositoryMiner setTechnicalDebts(List<ITechnicalDebt> technicalDebts) {
		this.technicalDebts = technicalDebts;
		
		return this;
	}

	public List<TimeFrameType> getTimeFrames() {
		return timeFrames;
	}

	public RepositoryMiner setTimeFrames(List<TimeFrameType> timeFrames) {
		this.timeFrames = timeFrames;
		
		return this;
	}

	public List<ITagCodeSmell> getTagCodeSmells() {
		return tagCodeSmells;
	}

	public RepositoryMiner setTagCodeSmells(List<ITagCodeSmell> tagCodeSmells) {
		this.tagCodeSmells = tagCodeSmells;
		
		return this;
	}
	
	public RepositoryMiner setProgressListener(IProgressListener progressListener) {
		this.progressListener = progressListener;
		
		return this;
	}
	
	public IProgressListener getProgressListener() {
		return progressListener;
	}
	
	public void mine() throws UnsupportedEncodingException {
		MiningProcessor processor = new MiningProcessor();
		
		if (progressListener != null) {
			progressListener.initMining(name);
			processor.mine(this);
			progressListener.endOfMining();
		} else {
			processor.mine(this);
		}
	}

}