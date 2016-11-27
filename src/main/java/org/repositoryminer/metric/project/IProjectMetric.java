package org.repositoryminer.metric.project;

import java.util.List;

import org.bson.Document;
import org.repositoryminer.metric.MetricId;
import org.repositoryminer.parser.IParser;

/**
 * This interface defines how to implement metric calculation in project
 * level.
 */
public interface IProjectMetric {

	/**
	 * Activates the metric calculation
	 * 
	 * @param parsers
	 *            the source code parsers
	 *            ({@link org.repositoryminer.parser.IParser}}).
	 * @param repositoryPath
	 *            the repository path.
	 * @param charset
	 *            the charset.
	 * @param document
	 *            the root of mongodb document in which metrics values must be
	 *            filled.
	 */
	public void calculate(List<IParser> parsers, String repositoryPath, String charset, Document document);
	
	/**
	 * @return The metric ID
	 */
	public MetricId getId();
	
}