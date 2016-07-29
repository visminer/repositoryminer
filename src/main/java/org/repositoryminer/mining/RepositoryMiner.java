package org.repositoryminer.mining;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

import org.repositoryminer.codesmell.clazz.IClassCodeSmell;
import org.repositoryminer.codesmell.project.IProjectCodeSmell;
import org.repositoryminer.listener.IProgressListener;
import org.repositoryminer.metric.clazz.IClassMetric;
import org.repositoryminer.parser.Parser;
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
 * description, {@link org.repositoryminer.scm.SCMType} and others
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
 * Raised exceptions are:
 * <p>
 * <ul>
 * <li>UnsupportedEncodingException, if a unknown text encoding is found in
 * analyzed source-code artifact.
 * </ul>
 * It is up to the caller ignore raised exceptions and skip to next mining step
 * <p>
 */
public class RepositoryMiner {

	private String path;
	private String name;
	private String description;
	private SCMType scm;
	private String charset = "UTF-8";
	private int binaryThreshold = 2048;

	private List<Parser> parsers;
	private List<IClassMetric> classMetrics;
	private List<IClassCodeSmell> classCodeSmells;
	private List<ITechnicalDebt> technicalDebts;
	private List<TimeFrameType> timeFrames;
	private List<IProjectCodeSmell> projectCodeSmells;

	private IProgressListener progressListener;

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
	 * @throws UnsupportedEncodingException
	 *             raised if an unsupported encoding is found in a mined code
	 *             artifact.
	 */
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

	public RepositoryMiner setParsers(Parser... parsers) {
		this.parsers = Arrays.asList(parsers);
		return this;
	}

	public RepositoryMiner setClasstMetrics(IClassMetric... classMetrics) {
		this.classMetrics = Arrays.asList(classMetrics);
		return this;
	}

	public RepositoryMiner setClassCodeSmells(IClassCodeSmell... classCodeSmells) {
		this.classCodeSmells = Arrays.asList(classCodeSmells);
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

	public RepositoryMiner setProjectCodeSmells(IProjectCodeSmell... projectCodeSmells) {
		this.projectCodeSmells = Arrays.asList(projectCodeSmells);
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

	public List<TimeFrameType> getTimeFrames() {
		return timeFrames;
	}

	public RepositoryMiner setTimeFrames(List<TimeFrameType> timeFrames) {
		this.timeFrames = timeFrames;
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

	public RepositoryMiner setProgressListener(IProgressListener progressListener) {
		this.progressListener = progressListener;
		return this;
	}

	public IProgressListener getProgressListener() {
		return progressListener;
	}

}