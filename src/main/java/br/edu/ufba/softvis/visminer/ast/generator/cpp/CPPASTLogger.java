package br.edu.ufba.softvis.visminer.ast.generator.cpp;

import org.eclipse.cdt.core.parser.IParserLogService;

public class CPPASTLogger implements IParserLogService {

	@Override
	public boolean isTracing() {
		return true;
	}

	@Override
	public void traceLog(String log) {
		System.out.println("CPPASTLogger::traceLog: " + log);
	}
}
