package br.edu.ufba.softvis.visminer.retriever;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.edu.ufba.softvis.visminer.constant.MetricType;
import br.edu.ufba.softvis.visminer.constant.MetricUid;
import br.edu.ufba.softvis.visminer.model.business.SoftwareUnit;
import br.edu.ufba.softvis.visminer.model.database.CommitDB;
import br.edu.ufba.softvis.visminer.model.database.MetricValueDB;
import br.edu.ufba.softvis.visminer.model.database.SoftwareUnitDB;
import br.edu.ufba.softvis.visminer.persistence.dao.CommitDAO;
import br.edu.ufba.softvis.visminer.persistence.dao.MetricValueDAO;
import br.edu.ufba.softvis.visminer.persistence.dao.SoftwareUnitDAO;
import br.edu.ufba.softvis.visminer.persistence.impl.CommitDAOImpl;
import br.edu.ufba.softvis.visminer.persistence.impl.MetricValueDAOImpl;
import br.edu.ufba.softvis.visminer.persistence.impl.SoftwareUnitDAOImpl;

public class SoftwareUnitRetriever extends Retriever {

  public SoftwareUnit findByRepository(int repositoryId, int commitId) {

    SoftwareUnitDAO suDao = new SoftwareUnitDAOImpl();
    CommitDAO commitDao = new CommitDAOImpl();

    super.createEntityManager();
    super.shareEntityManager(suDao);
    super.shareEntityManager(commitDao);

    List<SoftwareUnitDB> softUnitsDb = suDao.findByRepository(repositoryId, 
        commitId);
    CommitDB commitDb = commitDao.find(commitId);

    List<SoftwareUnit> softwareUnits = SoftwareUnitDB.toBusiness(
        softUnitsDb);
    List<Integer> commitsId = commitDao.findIdsUntilDate(repositoryId,
        commitDb.getDate());

    List<Integer> softUnitIds = new ArrayList<Integer>();
    for(SoftwareUnit su : softwareUnits){
      softUnitIds.add(su.getId());
    }

    setMetricValues(softwareUnits, commitsId, softUnitIds);
    processListToTree(softwareUnits);
    return softwareUnits.get(0);

  }

  ///// HELPERS ///////

  // sets metrics values for all software units
  private void setMetricValues(List<SoftwareUnit> softUnits, 
      List<Integer> commitsId, List<Integer> softUnitIds){

    MetricValueDAO metricValDao = new MetricValueDAOImpl();
    super.shareEntityManager(metricValDao);

    /*
     * used to control how many software units(su) received metrics 
     * values, when gets to 0 all su has values.
     */
    int control = softUnits.size();

    List<MetricValueDB> metricVals = metricValDao.
        findByCommitAndTypeAndSoftwareUnits(
            commitsId.get(commitsId.size()-1),
            MetricType.SNAPSHOT.getId(),
            softUnitIds);

    setMetricValuesIntoSoftwareUnits(metricVals, softUnits);

    while(control != 0 && !commitsId.isEmpty()){
      metricVals = metricValDao.findByCommitAndTypeAndSoftwareUnits(
          commitsId.get(commitsId.size()-1),
          MetricType.COMMIT.getId(),
          softUnitIds);

      control -= setMetricValuesIntoSoftwareUnits(metricVals, softUnits);
      commitsId.remove(commitsId.size()-1);
    }

  }

  private int setMetricValuesIntoSoftwareUnits(
      List<MetricValueDB> metricVals, List<SoftwareUnit> softUnits){

    Set<Integer> idSet = new HashSet<Integer>();
    for(MetricValueDB mv : metricVals){

      int suId = mv.getSoftwareUnitXCommit().getId().getSoftwareUnitId();
      MetricUid mId = mv.getMetric().getId();

      SoftwareUnit suTemp = new SoftwareUnit(suId);
      int index = softUnits.indexOf(suTemp);

      suTemp = softUnits.get(index);
      suTemp.getMetricValues().put(mId, mv.getValue());
      idSet.add(suId);

    }

    return idSet.size();

  }

  // creates a tree according software unit hierarchy  
  private void processListToTree(List<SoftwareUnit> softwareUnits) {

    int i = 0;
    while (i < softwareUnits.size()) {
      SoftwareUnit elem = softwareUnits.get(i);
      if (elem.getParent() == null) {
        i++;
      } else {
        elem = softwareUnits.remove(i);
        setElementInTree(softwareUnits, elem);
      }
    }

  }

  /* puts an element in a position of the tree according software
	   unit hirearchy */
  private void setElementInTree(List<SoftwareUnit> list, SoftwareUnit elem) {

    if (list == null) {
      return;
    }

    if (list.contains(elem.getParent())) {
      int index = list.indexOf(elem.getParent());
      SoftwareUnit parent = list.get(index);
      elem.setParent(parent);
      parent.getChildren().add(elem);
    } else {
      for (int i = 0; i < list.size(); i++) {
        setElementInTree(list.get(i).getChildren(), elem);
      }
    }

  }

}