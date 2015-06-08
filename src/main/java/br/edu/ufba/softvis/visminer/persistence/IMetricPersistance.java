package br.edu.ufba.softvis.visminer.persistence;

import br.edu.ufba.softvis.visminer.model.bean.SoftwareUnit;

public interface IMetricPersistance {

	public void save(SoftwareUnit unit, Integer value);

}
