package org.repositoryminer.mining;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.repositoryminer.codesmell.clazz.IClassCodeSmell;
import org.repositoryminer.codesmell.project.IProjectCodeSmell;
import org.repositoryminer.listener.IMiningListener;
import org.repositoryminer.listener.IPostMiningListener;
import org.repositoryminer.metric.clazz.IClassMetric;
import org.repositoryminer.model.Repository;
import org.repositoryminer.parser.Parser;
import org.repositoryminer.postprocessing.IPostMiningTask;
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
	private SCMType scm;
	private String charset = "UTF-8";

	private List<Parser> parsers = new ArrayList<Parser>();
	private List<IClassMetric> classMetrics = new ArrayList<IClassMetric>();
	private List<IClassCodeSmell> classCodeSmells = new ArrayList<IClassCodeSmell>();
	private List<IProjectCodeSmell> projectCodeSmells = new ArrayList<IProjectCodeSmell>();
	private List<IPostMiningTask> postMiningTasks = new ArrayList<IPostMiningTask>();
	private List<String> references = new ArrayList<String>();

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
	 * 
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

	/**
	 * Adds a class metric without duplicates
	 * 
	 * @param classMetric
	 * @return true if the metric was added and false otherwise
	 */
	public boolean addClassMetric(IClassMetric classMetric) {
		for (IClassMetric c : this.classMetrics) {
			if (c.getId().equals(classMetric.getId()))
				return false;
		}
		this.classMetrics.add(classMetric);
		return true;
	}

	/**
	 * Adds a class code smell without duplicates
	 * 
	 * @param classCodeSmell
	 * @return true if the code smell was added and false otherwise
	 */
	public boolean addClassCodeSmell(IClassCodeSmell classCodeSmell) {
		for (IClassCodeSmell c : this.classCodeSmells) {
			if (c.getId().equals(classCodeSmell.getId()))
				return false;
		}
		this.classCodeSmells.add(classCodeSmell);
		return true;
	}

	/**
	 * Adds a project code smell without duplicates
	 * 
	 * @param projectCodeSmell
	 * @return true if the code smell was added and false otherwise
	 */
	public boolean addProjectCodeSmell(IProjectCodeSmell projectCodeSmell) {
		for (IProjectCodeSmell c : this.projectCodeSmells) {
			if (c.getId().equals(projectCodeSmell.getId()))
				return false;
		}
		this.projectCodeSmells.add(projectCodeSmell);
		return true;
	}

	/**
	 * Adds a parser without duplicates
	 * 
	 * @param parser
	 * @return true if the parser was added and false otherwise
	 */
	public boolean addParser(Parser parser) {
		for (Parser p : this.parsers) {
			if (p.getLanguage().equals(parser.getLanguage()))
				return false;
		}
		this.parsers.add(parser);
		return true;
	}

	/**
	 * Adds a post mining task without duplicates
	 * 
	 * @param postMiningTask
	 * @return true if the task was added and false otherwise
	 */
	public boolean addPostMiningTask(IPostMiningTask postMiningTask) {
		for (IPostMiningTask p : this.postMiningTasks) {
			if (p.getTaskName().equals(postMiningTask.getTaskName()))
				return false;
		}
		this.postMiningTasks.add(postMiningTask);
		return true;
	}

	/**
	 * Adds a reference without duplicates
	 * 
	 * @param reference
	 * @return true if the reference was added and false otherwise
	 */
	public boolean addReference(String reference) {
		if (this.references.contains(reference)) {
			return false;
		}
		this.references.add(reference);
		return true;
	}

	public boolean hasClassMetrics() {
		return !classMetrics.isEmpty();
	}

	public boolean hasClassCodeSmells() {
		return !classCodeSmells.isEmpty();
	}

	public boolean hasProjectsCodeSmells() {
		return !projectCodeSmells.isEmpty();
	}

	public boolean hasPostMiningTasks() {
		return !postMiningTasks.isEmpty();
	}

	/**
	 * @return True if calculation (metrics) and detections (smells/debts)
	 *         should be performed in commits and False otherwise
	 */
	public boolean shouldProcessCommits() {
		return hasClassCodeSmells() || hasClassMetrics();
	}

	/**
	 * @return True if calculation (metrics) and detections (smells/debts)
	 *         should be performed in references and False otherwise
	 */
	public boolean shouldProcessReferences() {
		return hasProjectsCodeSmells();
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

	public List<IClassCodeSmell> getClassCodeSmells() {
		return classCodeSmells;
	}

	public List<IProjectCodeSmell> getProjectCodeSmells() {
		return projectCodeSmells;
	}

	public List<IPostMiningTask> getPostMiningTasks() {
		return postMiningTasks;
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

	public List<String> getReferences() {
		return references;
	}

}