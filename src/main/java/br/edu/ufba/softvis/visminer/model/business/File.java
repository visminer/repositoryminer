package br.edu.ufba.softvis.visminer.model.business;

import java.util.List;

/**
 * @author Felipe Gustavo de Souza Gomes (felipegustavo1000@gmail.com)
 * @version 0.9
 * User friendly file bean class.
 * This class will be used for user interface.
 */

public class File {

  private int id;
  private String path;
  private String uid;
  private FileState fileState;
  private List<SoftwareUnit> softwareUnits;

  public File(){}

  /**
   * @param id
   * @param path
   * @param uid
   */
  public File(int id, String path, String uid) {
    super();
    this.id = id;
    this.path = path;
    this.uid = uid;
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
   * @return the path
   */
  public String getPath() {
    return path;
  }

  /**
   * @param path the path to set
   */
  public void setPath(String path) {
    this.path = path;
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
   * @return the fileState
   */
  public FileState getFileState() {
    return fileState;
  }

  /**
   * @param fileState the fileState to set
   */
  public void setFileState(FileState fileState) {
    this.fileState = fileState;
  }

  /**
   * @return the softwareUnits
   */
  public List<SoftwareUnit> getSoftwareUnits() {
    return softwareUnits;
  }

  /**
   * @param softwareUnits the softwareUnits to set
   */
  public void setSoftwareUnits(List<SoftwareUnit> softwareUnits) {
    this.softwareUnits = softwareUnits;
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
    File other = (File) obj;
    if (id != other.id)
      return false;
    return true;
  }


}