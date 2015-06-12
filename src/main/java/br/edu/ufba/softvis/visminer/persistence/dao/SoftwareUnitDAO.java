package br.edu.ufba.softvis.visminer.persistence.dao;

import br.edu.ufba.softvis.visminer.model.database.SoftwareUnitDB;

public interface SoftwareUnitDAO extends DAO<SoftwareUnitDB, Integer>{

	SoftwareUnitDB findByUid(String uid);

}
