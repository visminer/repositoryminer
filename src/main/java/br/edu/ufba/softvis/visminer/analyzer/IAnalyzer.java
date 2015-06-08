package br.edu.ufba.softvis.visminer.analyzer;

public interface IAnalyzer<T> {

	public T persist(Object...objects);
	public T update(Object...objects);
	
}
