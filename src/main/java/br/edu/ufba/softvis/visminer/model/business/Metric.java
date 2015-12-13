package br.edu.ufba.softvis.visminer.model.business;

import br.edu.ufba.softvis.visminer.constant.MetricType;
import br.edu.ufba.softvis.visminer.constant.MetricUid;

/**
 * @author Felipe Gustavo de Souza Gomes (felipegustavo1000@gmail.com)
 * @version 0.9
 * User friendly metric bean class.
 * This class will be used for user interface.
 */

public class Metric {

  private int id;
  private String acronym;
  private String description;
  private String name;
  private MetricUid uid;
  private MetricType type;

  public Metric(){}


  /**
   * @param id
   * @param acronym
   * @param description
   * @param name
   * @param uid
   * @param type
   */
  public Metric(int id, String acronym, String description, String name,
      MetricUid uid, MetricType type) {
    super();
    this.id = id;
    this.acronym = acronym;
    this.description = description;
    this.name = name;
    this.uid = uid;
    this.type = type;
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
   * @return the acronym
   */
  public String getAcronym() {
    return acronym;
  }


  /**
   * @param acronym the acronym to set
   */
  public void setAcronym(String acronym) {
    this.acronym = acronym;
  }


  /**
   * @return the description
   */
  public String getDescription() {
    return description;
  }


  /**
   * @param description the description to set
   */
  public void setDescription(String description) {
    this.description = description;
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
  public MetricUid getUid() {
    return uid;
  }


  /**
   * @param uid the uid to set
   */
  public void setUid(MetricUid uid) {
    this.uid = uid;
  }


  /**
   * @return the type
   */
  public MetricType getType() {
    return type;
  }


  /**
   * @param type the type to set
   */
  public void setType(MetricType type) {
    this.type = type;
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
    Metric other = (Metric) obj;
    if (id != other.id)
      return false;
    return true;
  }

}