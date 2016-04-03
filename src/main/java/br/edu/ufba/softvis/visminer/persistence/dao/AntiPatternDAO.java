package br.edu.ufba.softvis.visminer.persistence.dao;

import java.util.List;

import br.edu.ufba.softvis.visminer.model.AntiPattern;

public interface AntiPatternDAO {
	
	public List<AntiPattern> findAll();

}
