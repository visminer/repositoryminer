package org.repositoryminer.mining;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.repositoryminer.codemetric.direct.IDirectCodeMetric;
import org.repositoryminer.codemetric.indirect.IIndirectCodeMetric;
import org.repositoryminer.codesmell.direct.IDirectCodeSmell;
import org.repositoryminer.codesmell.indirect.IIndirectCodeSmell;
import org.repositoryminer.listener.mining.IMiningListener;
import org.repositoryminer.listener.mining.NullMiningListener;
import org.repositoryminer.listener.postmining.IPostMiningListener;
import org.repositoryminer.listener.postmining.NullPostMiningListener;
import org.repositoryminer.mining.local.IncrementalMiningProcessor;
import org.repositoryminer.mining.local.MiningProcessor;
import org.repositoryminer.model.Repository;
import org.repositoryminer.parser.IParser;
import org.repositoryminer.persistence.handler.RepositoryDocumentHandler;
import org.repositoryminer.postmining.IPostMiningTask;
import org.repositoryminer.postmining.PostMiningProcessor;
import org.repositoryminer.scm.ReferenceType;
import org.repositoryminer.scm.SCMType;

/**
 * <h1>A facade for the repository miner API</h1>
 * <p>
 * This is the entry point to configure the parameters that enable the mining of
 * software projects. We can divide the parameters (attributes) into two sets:
 * <p>
 * <ul>
 * <li>Mandatory parameters -> project's path, project's name, project's
 * description, {@link org.repositoryminer.scm.SCMType}.
 * <li>Optional parameters -> lists of metrics (to be calculated), list of
 * codesmells (to be detected) and others
 * </ul>
 * <p>
 * Prior to activating the mining ({@link #mine()}) it must be attributed
 * instances to needed parameters. The responsibility for creating such
 * instances lies with caller applications. In this case, it is mandatory that
 * callers make sure that necessary resources are present/provided, <i>e.g.</i>,
 * the .git directory if a GIT project is intended to be mined. RepositoryMiner
 * does <b>NOT</b> perform any checking on the parameters before mining
 * projects.
 * <p>
 * It may be important to provide an instance of
 * {@link org.repositoryminer.listener.IProgressListener} so that callers will
 * be notified about the progress of the mining. The
 * {@link #setProgressListener(IProgressListener)} method can be used to inject
 * a listener.
 * <p>
 */
public class RepositoryMiner {

	private String path;
	private String name;
	private String description;
	private SCMType scm = SCMType.GIT;
	private String charset = "UTF-8";

	private List<IParser> parsers;

	private List<IDirectCodeMetric> directCodeMetrics;
	private List<IIndirectCodeMetric> indirectCodeMetrics;

	private List<IDirectCodeSmell> directCodeSmells;
	private List<IIndirectCodeSmell> indirectCodeSmell;

	private List<Entry<String, ReferenceType>> references;
	private List<String> snapshots;

	private IMiningListener miningListener = new NullMiningListener();
	private IPostMiningListener postMiningListener = new NullPostMiningListener();
	private List<IPostMiningTask> postMiningTasks;

	public boolean addParser(IParser parser) {
		for (IParser p : getParsers()) {
			if (p.getLanguage() == parser.getLanguage()) {
				return false;
			}
		}

		parsers.add(parser);
		return true;
	}

	public boolean addDirectCodeMetric(IDirectCodeMetric codeMetric) {
		for (IDirectCodeMetric metric : getDirectCodeMetrics()) {
			if (metric.getId() == codeMetric.getId()) {
				return false;
			}
		}

		directCodeMetrics.add(codeMetric);
		return true;
	}

	public boolean addIndirectCodeMetric(IIndirectCodeMetric codeMetric) {
		for (IIndirectCodeMetric metric : getIndirectCodeMetrics()) {
			if (metric.getId() == codeMetric.getId()) {
				return false;
			}
		}

		indirectCodeMetrics.add(codeMetric);
		return true;
	}

	public boolean addDirectCodeSmell(IDirectCodeSmell codeSmell) {
		for (IDirectCodeSmell smell : getDirectCodeSmells()) {
			if (smell.getId() == codeSmell.getId()) {
				return false;
			}
		}

		directCodeSmells.add(codeSmell);
		return true;
	}

	public boolean addIndirectCodeSmell(IIndirectCodeSmell codeSmell) {
		for (IIndirectCodeSmell smell : getIndirectCodeSmell()) {
			if (smell.getId() == codeSmell.getId()) {
				return false;
			}
		}

		indirectCodeSmell.add(codeSmell);
		return true;
	}

	public boolean addReference(String name, ReferenceType type) {
		Entry<String, ReferenceType> entry = new AbstractMap.SimpleEntry<String, ReferenceType>(name, type);
		if (getReferences().contains(entry)) {
			return false;
		}

		references.add(entry);
		return true;
	}

	public boolean addSnapshot(String snapshot) {
		if (getSnapshots().contains(snapshot)) {
			return false;
		}

		snapshots.add(snapshot);
		return true;
	}

	public boolean addPostTaskMining(IPostMiningTask task) {
		for (IPostMiningTask task2 : getPostMiningTasks()) {
			if (task2.getTaskName().equals(task.getTaskName())) {
				return false;
			}
		}
		postMiningTasks.add(task);
		return true;
	}

	/**
	 * @param path
	 *            the project path
	 * @param name
	 *            the project name
	 * @param description
	 *            the project description
	 * @param scm
	 *            the project SCM ({@link org.repositoryminer.scm.SCMType})
	 */
	public RepositoryMiner(String path, String name, String description, SCMType scm) {
		this.path = path;
		this.name = name;
		this.description = description;
		this.scm = scm;
	}

	/**
	 * @param name
	 *            the project name
	 */
	public RepositoryMiner(String name) {
		this.name = name;
	}

	/**
	 * It activates the mining of a project
	 * <p>
	 * One must make sure that all needed parameters are set prior to calling
	 * this method
	 * <p>
	 * 
	 * @throws IOException
	 */
	public Repository mine() throws IOException {
		RepositoryDocumentHandler repoDocHandler = new RepositoryDocumentHandler();
		if (repoDocHandler.checkIfRepositoryExistsByName(name)) {
			IncrementalMiningProcessor processor = new IncrementalMiningProcessor();
			processor.mine(this);
		} else {
			MiningProcessor processor = new MiningProcessor();
			processor.mine(this);
		}

		if (hasPostMiningTasks()) {
			PostMiningProcessor postProcessor = new PostMiningProcessor();
			postProcessor.setListener(postMiningListener);
			postProcessor.setTasks(getPostMiningTasks());
			postProcessor.executeTasks(Repository.parseDocument(repoDocHandler.findByName(name)), this);
		}

		return Repository.parseDocument(repoDocHandler.findByName(name));
	}

	public boolean hasParsers() {
		return getParsers().size() > 0;
	}

	public boolean hasDirectCodeMetrics() {
		return getDirectCodeMetrics().size() > 0;
	}

	public boolean hasDirectCodeSmells() {
		return getDirectCodeSmells().size() > 0;
	}

	public boolean hasIndirectCodeMetrics() {
		return getIndirectCodeMetrics().size() > 0;
	}

	public boolean hasIndirectCodeSmells() {
		return getIndirectCodeSmell().size() > 0;
	}

	public boolean hasPostMiningTasks() {
		return getPostMiningTasks().size() > 0;
	}

	// getters and setters

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public SCMType getScm() {
		return scm;
	}

	public void setScm(SCMType scm) {
		this.scm = scm;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public List<IParser> getParsers() {
		if (parsers == null) {
			parsers = new ArrayList<IParser>();
		}

		return parsers;
	}

	public void setParsers(List<IParser> parsers) {
		this.parsers = parsers;
	}

	public List<IDirectCodeMetric> getDirectCodeMetrics() {
		if (directCodeMetrics == null) {
			directCodeMetrics = new ArrayList<IDirectCodeMetric>();
		}

		return directCodeMetrics;
	}

	public void setDirectCodeMetrics(List<IDirectCodeMetric> directCodeMetrics) {
		this.directCodeMetrics = directCodeMetrics;
	}

	public List<IIndirectCodeMetric> getIndirectCodeMetrics() {
		if (indirectCodeMetrics == null) {
			indirectCodeMetrics = new ArrayList<IIndirectCodeMetric>();
		}

		return indirectCodeMetrics;
	}

	public void setIndirectCodeMetrics(List<IIndirectCodeMetric> indirectCodeMetrics) {
		this.indirectCodeMetrics = indirectCodeMetrics;
	}

	public List<IDirectCodeSmell> getDirectCodeSmells() {
		if (directCodeSmells == null) {
			directCodeSmells = new ArrayList<IDirectCodeSmell>();
		}

		return directCodeSmells;
	}

	public void setDirectCodeSmells(List<IDirectCodeSmell> directCodeSmells) {
		this.directCodeSmells = directCodeSmells;
	}

	public List<IIndirectCodeSmell> getIndirectCodeSmell() {
		if (indirectCodeSmell == null) {
			indirectCodeSmell = new ArrayList<IIndirectCodeSmell>();
		}

		return indirectCodeSmell;
	}

	public void setIndirectCodeSmell(List<IIndirectCodeSmell> indirectCodeSmell) {
		this.indirectCodeSmell = indirectCodeSmell;
	}

	public List<Entry<String, ReferenceType>> getReferences() {
		if (references == null) {
			references = new ArrayList<Entry<String, ReferenceType>>();
		}

		return references;
	}

	public void setReferences(List<Entry<String, ReferenceType>> references) {
		this.references = references;
	}

	public List<String> getSnapshots() {
		if (snapshots == null) {
			snapshots = new ArrayList<String>();
		}

		return snapshots;
	}

	public void setSnapshots(List<String> snapshots) {
		this.snapshots = snapshots;
	}

	public IMiningListener getMiningListener() {
		if (miningListener == null) {
			miningListener = new NullMiningListener();
		}
		return miningListener;
	}

	public void setMiningListener(IMiningListener miningListener) {
		this.miningListener = miningListener;
	}

	public IPostMiningListener getPostMiningListener() {
		if (postMiningListener == null) {
			postMiningListener = new NullPostMiningListener();
		}
		return postMiningListener;
	}

	public void setPostMiningListener(IPostMiningListener postMiningListener) {
		this.postMiningListener = postMiningListener;
	}

	public List<IPostMiningTask> getPostMiningTasks() {
		if (postMiningTasks == null) {
			postMiningTasks = new ArrayList<IPostMiningTask>();
		}
		return postMiningTasks;
	}

	public void setPostMiningTasks(List<IPostMiningTask> postMiningTasks) {
		this.postMiningTasks = postMiningTasks;
	}

}