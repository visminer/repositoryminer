package br.edu.ufba.softvis.visminer.model.database;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.edu.ufba.softvis.visminer.model.business.Label;

/**
 * The persistent class for the label database table.
 */

@Entity
@Table(name="label")
@NamedQueries({
  @NamedQuery(name="LabelDB.findByIssue", query="select l from LabelDB l where l.issue.id = :id")
})
public class LabelDB implements Serializable{

  private static final long serialVersionUID = 1L;

  @Id
  @SequenceGenerator(name="LABEL_ID_GENERATOR", sequenceName="LABEL_SEQ")
  @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LABEL_ID_GENERATOR")
  @Column(unique=true, nullable=false)
  private int id;

  @Column(name="color", nullable=false)
  private String color;

  @Column(name="name", nullable=false)
  private String name;

  @ManyToOne
  @JoinColumn(name="issue_id", nullable=false)
  private IssueDB issue;

  public LabelDB(){}

  /**
   * @param id
   * @param color
   * @param name
   * @param issue
   */
  public LabelDB(int id, String color, String name, IssueDB issue) {
    super();
    this.id = id;
    this.color = color;
    this.name = name;
    this.issue = issue;
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
   * @return the color
   */
  public String getColor() {
    return color;
  }

  /**
   * @param color the color to set
   */
  public void setColor(String color) {
    this.color = color;
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
   * @return the issue
   */
  public IssueDB getIssue() {
    return issue;
  }

  /**
   * @param issue the issue to set
   */
  public void setIssue(IssueDB issue) {
    this.issue = issue;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((name == null) ? 0 : name.hashCode());
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
    LabelDB other = (LabelDB) obj;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    return true;
  }

  public static List<Label> toBusiness(List<LabelDB> labels){

    List<Label> bizzLabels = new ArrayList<Label>();

    if(labels == null)
      return bizzLabels;

    for(LabelDB l : labels){

      Label label = new Label(l.getId(), l.getColor(), l.getName());
      bizzLabels.add(label);

    }

    return bizzLabels;

  }

}