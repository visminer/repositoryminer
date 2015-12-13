package br.edu.ufba.softvis.visminer.ast;

import br.edu.ufba.softvis.visminer.constant.SoftwareUnitType;

public class TypeDeclaration {

  private int id;
  private String name;
  private SoftwareUnitType type;

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

}