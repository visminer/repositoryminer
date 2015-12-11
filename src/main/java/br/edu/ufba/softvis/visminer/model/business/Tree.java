package br.edu.ufba.softvis.visminer.model.business;

import java.util.Date;

import br.edu.ufba.softvis.visminer.constant.TreeType;

/**
 * @author Felipe Gustavo de Souza Gomes (felipegustavo1000@gmail.com)
 * @version 0.9
 * User friendly tree bean class.
 * This class will be used for user interface.
 */

public class Tree {

  private int id;
  private Date lastUpdate;
  private String name;
  private String fullName;
  private TreeType type;

  public Tree(){}

  /**
   * @param id
   * @param lastUpdate
   * @param name
   * @param fullName
   * @param type
   */
  public Tree(int id, Date lastUpdate, String name, String fullName,
      TreeType type) {
    super();
    this.id = id;
    this.lastUpdate = lastUpdate;
    this.name = name;
    this.fullName = fullName;
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
   * @return the lastUpdate
   */
  public Date getLastUpdate() {
    return lastUpdate;
  }

  /**
   * @param lastUpdate the lastUpdate to set
   */
  public void setLastUpdate(Date lastUpdate) {
    this.lastUpdate = lastUpdate;
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
   * @return the fullName
   */
  public String getFullName() {
    return fullName;
  }

  /**
   * @param fullName the fullName to set
   */
  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  /**
   * @return the type
   */
  public TreeType getType() {
    return type;
  }

  /**
   * @param type the type to set
   */
  public void setType(TreeType type) {
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
    Tree other = (Tree) obj;
    if (id != other.id)
      return false;
    return true;
  }

  @Override
  public String toString() {
    return name;
  }	

}