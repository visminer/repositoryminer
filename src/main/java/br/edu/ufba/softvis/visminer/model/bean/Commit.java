package br.edu.ufba.softvis.visminer.model.bean;

import java.util.Date;

/**
 * @author Felipe Gustavo de Souza Gomes (felipegustavo1000@gmail.com)
 * @version 0.9
 * 
 * Simple commit bean.
 * This bean is used to simplify interaction between some parts, avoiding coupling and doing smaller core codes.
 */

public class Commit {

	private int id;
	private Date date;
	private String message;
	private String name;
	
	public Commit(){}

	/**
	 * @param id
	 * @param date
	 * @param message
	 * @param name
	 */
	public Commit(int id, Date date, String message, String name) {
		super();
		this.id = id;
		this.date = date;
		this.message = message;
		this.name = name;
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
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
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

}