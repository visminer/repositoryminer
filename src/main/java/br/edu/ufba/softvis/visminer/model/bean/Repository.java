package br.edu.ufba.softvis.visminer.model.bean;

import br.edu.ufba.softvis.visminer.constant.WebRepositoryType;
import br.edu.ufba.softvis.visminer.constant.RepositoryType;

/**
 * @author Felipe Gustavo de Souza Gomes (felipegustavo1000@gmail.com)
 * @version 0.9
 * 
 * Simple repository bean.
 * This bean is used to simplify interaction between some parts, avoiding coupling and doing smaller core codes.
 */

public class Repository {

	private int id;
	private String description;
	private String name;
	private String path;
	private String remoteUrl;
	private RepositoryType type;
	private WebRepositoryType serviceType;
	private String uid;
	
	public Repository(){}

	/**
	 * @param id
	 * @param description
	 * @param name
	 * @param path
	 * @param remoteUrl
	 * @param type
	 * @param serviceType
	 * @param uid
	 */
	public Repository(int id, String description, String name, String path,
			String remoteUrl, RepositoryType type,
			WebRepositoryType serviceType, String uid) {
		super();
		this.id = id;
		this.description = description;
		this.name = name;
		this.path = path;
		this.remoteUrl = remoteUrl;
		this.type = type;
		this.serviceType = serviceType;
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
	 * @return the remoteUrl
	 */
	public String getRemoteUrl() {
		return remoteUrl;
	}

	/**
	 * @param remoteUrl the remoteUrl to set
	 */
	public void setRemoteUrl(String remoteUrl) {
		this.remoteUrl = remoteUrl;
	}

	/**
	 * @return the type
	 */
	public RepositoryType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(RepositoryType type) {
		this.type = type;
	}

	/**
	 * @return the serviceType
	 */
	public WebRepositoryType getServiceType() {
		return serviceType;
	}

	/**
	 * @param serviceType the serviceType to set
	 */
	public void setServiceType(WebRepositoryType serviceType) {
		this.serviceType = serviceType;
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
	
}