package org.pregen.hsqldb;

// Generated Mar 30, 2012 12:35:44 PM by Hibernate Tools 3.4.0.CR1

/**
 * Kunde generated by hbm2java
 */
public class Kunde implements java.io.Serializable {

	private long id;
	private String art;

	public Kunde() {
	}

	public Kunde(long id, String art) {
		this.id = id;
		this.art = art;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getArt() {
		return this.art;
	}

	public void setArt(String art) {
		this.art = art;
	}

}
