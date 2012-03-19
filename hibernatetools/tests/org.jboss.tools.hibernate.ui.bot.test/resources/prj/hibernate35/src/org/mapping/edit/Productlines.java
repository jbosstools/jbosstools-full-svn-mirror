package org.mapping.edit;

// Generated Mar 7, 2012 1:42:51 PM by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;

/**
 * Productlines generated by hbm2java
 */
public class Productlines implements java.io.Serializable {

	private String productline;
	private String htmldescription;
	private String textdescription;
	private Set productses = new HashSet(0);

	public Productlines() {
	}

	public Productlines(String productline) {
		this.productline = productline;
	}

	public Productlines(String productline, String htmldescription,
			String textdescription, Set productses) {
		this.productline = productline;
		this.htmldescription = htmldescription;
		this.textdescription = textdescription;
		this.productses = productses;
	}

	public String getProductline() {
		return this.productline;
	}

	public void setProductline(String productline) {
		this.productline = productline;
	}

	public String getHtmldescription() {
		return this.htmldescription;
	}

	public void setHtmldescription(String htmldescription) {
		this.htmldescription = htmldescription;
	}

	public String getTextdescription() {
		return this.textdescription;
	}

	public void setTextdescription(String textdescription) {
		this.textdescription = textdescription;
	}

	public Set getProductses() {
		return this.productses;
	}

	public void setProductses(Set productses) {
		this.productses = productses;
	}

}
