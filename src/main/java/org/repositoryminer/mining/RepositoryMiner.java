package org.repositoryminer.mining;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.repositoryminer.codesmell.clazz.IClassCodeSmell;
import org.repositoryminer.codesmell.project.IProjectCodeSmell;
import org.repositoryminer.listener.IMiningListener;
import org.repositoryminer.listener.IPostMiningListener;
import org.repositoryminer.metric.clazz.IClassMetric;
import org.repositoryminer.model.Reference;
import org.repositoryminer.model.Repository;
import org.repositoryminer.parser.Parser;
import org.repositoryminer.postprocessing.IPostMiningTask;
import org.repositoryminer.scm.SCMType;
import org.repositoryminer.technicaldebt.ITechnicalDebt;

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
	private SCMType scm;
	private String charset = "UTF-8";

	private List<Parser> parsers;
	private List<IClassMetric> classMetrics;
	private List<IClassCodeSmell> classCodeSmells;
	private List<ITechnicalDebt> technicalDebts;
	private List<IProjectCodeSmell> projectCodeSmells;
	private List<IPostMiningTask> postMiningTasks;
	private Map<Reference, TimeFrameType[]> references;

	private IMiningListener miningListener;
	private IPostMiningListener postMiningListener;

	/**
	 * Use this void constructor if parameters are going to be set later
	 * <p>
	 * Otherwise, the non-void constructor (
	 * {@link #RepositoryMiner(String, String, String, SCMType)}) can be used if
	 * mandatory parameters are known
	 */
	public RepositoryMiner() {
	}

	/**
	 * Use this non-void constructor if mandatory parameters are known
	 * <p>
	 * Otherwise, the void constructor ({@link #RepositoryMiner()} can be used
	 * if mandatory parameters are not known
	 * 
	 * @param path
	 *            path to versioned project to be mined
	 * @param name
	 *            user-friendly name of the project
	 * @param description
	 *            extra descriptive information about the project
	 * @param scm
	 *            the SCM type ({@link org.repositoryminer.scm.SCMType}) of the
	 *            mined project
	 */
	public RepositoryMiner(String path, String name, String description, SCMType scm) {
		super();
		this.path = path;
		this.name = name;
		this.description = description;
		this.scm = scm;
	}

	/**
	 * It activates the mining of a project
	 * <p>
	 * One must make sure that all needed parameters are set prior to calling
	 * this method
	 * <p>
	 * @return instance of repository after mining and post-mining are performed
	 * @throws IOException
	 */
	public Repository mine() throws IOException {
		MiningProcessor processor = new MiningProcessor();
		PostMiningProcessor postProcessor = new PostMiningProcessor();
		if (miningListener != null) {
			miningListener.initMining(name);
		}

		return postProcessor.executeTasks(processor.mine(this), this);
	}

	public boolean hasClassMetrics() {
		return (classMetrics != null && !classMetrics.isEmpty());
	}

	public boolean hasTechnicalDebts() {
		return (technicalDebts != null && !technicalDebts.isEmpty());
	}

	public boolean hasClassCodeSmells() {
		return (classCodeSmells != null && !classCodeSmells.isEmpty());
	}

	public boolean hasProjectsCodeSmells() {
		return (projectCodeSmells != null && !projectCodeSmells.isEmpty());
	}

	public boolean hasPostMiningTasks() {
		return (postMiningTasks != null && !postMiningTasks.isEmpty());
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

	public List<Parser> getParsers() {
		return parsers;
	}

	public RepositoryMiner setParsers(List<Parser> parsers) {
		this.parsers = parsers;
		return this;
	}

	public List<IClassMetric> getClassMetrics() {
		return classMetrics;
	}

	public RepositoryMiner setClassMetrics(List<IClassMetric> classMetrics) {
		this.classMetrics = classMetrics;
		return this;
	}

	public List<IClassCodeSmell> getClassCodeSmells() {
		return classCodeSmells;
	}

	public RepositoryMiner setClassCodeSmells(List<IClassCodeSmell> classCodeSmells) {
		this.classCodeSmells = classCodeSmells;
		return this;
	}

	public List<ITechnicalDebt> getTechnicalDebts() {
		return technicalDebts;
	}

	public RepositoryMiner setTechnicalDebts(List<ITechnicalDebt> technicalDebts) {
		this.technicalDebts = technicalDebts;
		return this;
	}

	public List<IProjectCodeSmell> getProjectCodeSmells() {
		return projectCodeSmells;
	}

	public RepositoryMiner setProjectCodeSmells(List<IProjectCodeSmell> projectCodeSmells) {
		this.projectCodeSmells = projectCodeSmells;
		return this;
	}

	public List<IPostMiningTask> getPostMiningTasks() {
		return postMiningTasks;
	}

	public RepositoryMiner setPostMiningTasks(List<IPostMiningTask> postMiningTasks) {
		this.postMiningTasks = postMiningTasks;
		return this;
	}

	public RepositoryMiner setMiningListener(IMiningListener listener) {
		this.miningListener = listener;
		return this;
	}

	public IMiningListener getMiningListener() {
		return miningListener;
	}

	public RepositoryMiner setPostMiningListener(IPostMiningListener listener) {
		this.postMiningListener = listener;
		return this;
	}

	public IPostMiningListener getPostMiningListener() {
		return postMiningListener;
	}

	public RepositoryMiner setReferences(Map<Reference, TimeFrameType[]> references) {
		this.references = references;
		return this;
	}

	public Map<Reference, TimeFrameType[]> getReferences() {
		return references;
	}

}