package br.edu.ufba.softvis.visminer.persistence.dao;

import java.util.List;

import br.edu.ufba.softvis.visminer.model.database.SoftwareUnitDB;

public interface SoftwareUnitDAO extends DAO<SoftwareUnitDB, Integer>{

	public SoftwareUnitDB findByUid(String uid);

	public List<SoftwareUnitDB> findByRepository(int repositoryId);

}
