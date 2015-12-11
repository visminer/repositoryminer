package br.edu.ufba.softvis.visminer.model.database;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.edu.ufba.softvis.visminer.constant.TreeType;
import br.edu.ufba.softvis.visminer.model.business.Tree;

/**
 * @author Felipe Gustavo de Souza Gomes (felipegustavo1000@gmail.com)
 * @version 0.9 The persistent class for the tree database table.
 */
@Entity
@Table(name = "tree")
@NamedQuery(name = "TreeDB.findByRepository", query = "select t from TreeDB t where "
    + "t.repository.id = :repositoryId")
public class TreeDB implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @SequenceGenerator(name = "TREE_ID_GENERATOR", sequenceName = "TREE_SEQ")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TREE_ID_GENERATOR")
  @Column(unique = true, nullable = false)
  private int id;

  @Column(name = "full_name", nullable = false, length = 255)
  private String fullName;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "last_update", nullable = false)
  private Date lastUpdate;

  @Column(nullable = false, length = 100)
  private String name;

  @Column(nullable = false)
  private int type;

  // bi-directional many-to-many association to CommitDB
  @ManyToMany
  @JoinTable(name = "commit_reference_tree", joinColumns = { @JoinColumn(name = "tree_id", 
  nullable = false) }, inverseJoinColumns = { @JoinColumn(name = "commit_id", nullable = false) })
  private List<CommitDB> commits;

  // bi-directional many-to-one association to RepositoryDB
  @ManyToOne
  @JoinColumn(name = "repository_id", nullable = false)
  private RepositoryDB repository;

  public TreeDB() {
  }

  /**
   * @param id
   * @param fullName
   * @param lastUpdate
   * @param name
   * @param type
   */
  public TreeDB(int id, String fullName, Date lastUpdate, String name,
      TreeType type) {
    super();
    this.id = id;
    this.fullName = fullName;
    this.lastUpdate = lastUpdate;
    this.name = name;
    this.type = type != null ? type.getId() : 0;
  }

  /**
   * @return the id
   */
  public int getId() {
    return id;
  }

  /**
   * @param id
   *            the id to set
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * @return the fullName
   */
  public String getFullName() {
    return fullName;
  }

  /**
   * @param fullName
   *            the fullName to set
   */
  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  /**
   * @return the lastUpdate
   */
  public Date getLastUpdate() {
    return lastUpdate;
  }

  /**
   * @param lastUpdate
   *            the lastUpdate to set
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
   * @param name
   *            the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return the type
   */
  public TreeType getType() {
    return TreeType.parse(type);
  }

  /**
   * @param type
   *            the type to set
   */
  public void setType(TreeType type) {
    this.type = type != null ? type.getId() : 0;
  }

  /**
   * @return the commits
   */
  public List<CommitDB> getCommits() {
    return commits;
  }

  /**
   * @param commits
   *            the commits to set
   */
  public void setCommits(List<CommitDB> commits) {
    this.commits = commits;
  }

  /**
   * @return the repository
   */
  public RepositoryDB getRepository() {
    return repository;
  }

  /**
   * @param repository
   *            the repository to set
   */
  public void setRepository(RepositoryDB repository) {
    this.repository = repository;
  }

  /**
   * @return the bizz represention of Tree
   */
  public Tree toBusiness() {
    Tree tree = new Tree(this.getId(), this.getLastUpdate(),
        this.getName(), this.getFullName(), this.getType());

    return tree;
  }

  /**
   * Converts from DB beans to Bizz beans
   * 
   * @param trees
   *            collection of TreeDB instances
   * @return collection of "business" trees
   */
  public static List<Tree> toBusiness(List<TreeDB> trees) {

    List<Tree> bizzTrees = new ArrayList<Tree>();

    if(trees == null)
      return bizzTrees;

    for (TreeDB t : trees) {
      Tree tree = new Tree(t.getId(), t.getLastUpdate(), t.getName(),
          t.getFullName(), t.getType());

      bizzTrees.add(tree);
    }

    return bizzTrees;
  }

}