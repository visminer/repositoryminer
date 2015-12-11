package br.edu.ufba.softvis.visminer.model.business;

import java.util.List;
import java.util.Map;

import br.edu.ufba.softvis.visminer.constant.MetricUid;
import br.edu.ufba.softvis.visminer.constant.SoftwareUnitType;

/**
 * User friendly software unit bean class.
 * This class will be used for user interface.
 */

public class SoftwareUnit {

  private int id;
  private String name;
  private String uid;
  private SoftwareUnitType type;
  private SoftwareUnit parent;
  private List<SoftwareUnit> children;
  private Map<MetricUid, String> metricValues;
  private File file;

  public SoftwareUnit(){}

  /**
   * @param id
   * @param name
   * @param fullName
   * @param uid
   * @param type
   */
  public SoftwareUnit(int id, String name, String uid,
      SoftwareUnitType type) {
    super();
    this.id = id;
    this.name = name;
    this.uid = uid;
    this.type = type;
  }

  public SoftwareUnit(int id) {
    super();
    this.id = id;
  }

  /**
   * @return the id
   */
  public int getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return the uid
   */
  public String getUid() {
    return uid;
  }

  /**
   * @param uid the uid to set
   */
  public void setUid(String uid) {
    this.uid = uid;
  }

  /**
   * @return the type
   */
  public SoftwareUnitType getType() {
    return type;
  }

  /**
   * @param type the type to set
   */
  public void setType(SoftwareUnitType type) {
    this.type = type;
  }

  /**
   * @return the parent
   */
  public SoftwareUnit getParent() {
    return parent;
  }

  /**
   * @param parentUnit the parent to set
   */
  public void setParent(SoftwareUnit parent) {
    this.parent = parent;
  }

  /**
   * @return the children
   */
  public List<SoftwareUnit> getChildren() {
    return children;
  }

  /**
   * @param children the children to set
   */
  public void setChildren(List<SoftwareUnit> children) {
    this.children = children;
  }

  /**
   * @return the metricValues
   */
  public Map<MetricUid, String> getMetricValues() {
    return metricValues;
  }

  /**
   * @param metricValues the metricValues to set
   */
  public void setMetricValues(Map<MetricUid, String> metricValues) {
    this.metricValues = metricValues;
  }

  /**
   * @return the file
   */
  public File getFile() {
    return file;
  }

  /**
   * @param file the file to set
   */
  public void setFile(File file) {
    this.file = file;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + id;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    SoftwareUnit other = (SoftwareUnit) obj;
    if (id != other.id)
      return false;
    return true;
  }

}