package br.edu.ufba.softvis.visminer.ast;

public class FieldDeclaration {

  private int id;
  private String name;
  private String type;
  private String modifier;

  public int getId() {
    return id;
  }
  public void setId(int id) {
    this.id = id;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }
  public String getModifier() {
    return modifier;
  }
  public void setModifier(String modifier) {
    this.modifier = modifier;
  }


}
