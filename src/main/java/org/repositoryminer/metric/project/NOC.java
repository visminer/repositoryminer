package org.repositoryminer.metric.project;

import java.util.List;

import org.bson.Document;
import org.repositoryminer.metric.MetricId;
import org.repositoryminer.parser.IParser;

public class NOC implements IProjectClass {

	@Override
	public void calculate(List<IParser> parsers, String repositoryPath, String charset, Document document) {

		
		
	}

	@Override
	public MetricId getId() {
		return MetricId.NOC;
	}

}
