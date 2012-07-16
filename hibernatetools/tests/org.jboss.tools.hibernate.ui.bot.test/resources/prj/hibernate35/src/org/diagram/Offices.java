package org.diagram;

// Generated Jul 16, 2012 4:06:14 PM by Hibernate Tools 4.0.0

/**
 * Offices generated by hbm2java
 */
public class Offices implements java.io.Serializable {

	private String officecode;
	private String addressline1;
	private String addressline2;
	private String city;
	private String country;
	private String phone;
	private String postalcode;
	private String state;
	private String territory;

	public Offices() {
	}

	public Offices(String officecode, String addressline1, String city,
			String country, String phone, String postalcode, String territory) {
		this.officecode = officecode;
		this.addressline1 = addressline1;
		this.city = city;
		this.country = country;
		this.phone = phone;
		this.postalcode = postalcode;
		this.territory = territory;
	}

	public Offices(String officecode, String addressline1, String addressline2,
			String city, String country, String phone, String postalcode,
			String state, String territory) {
		this.officecode = officecode;
		this.addressline1 = addressline1;
		this.addressline2 = addressline2;
		this.city = city;
		this.country = country;
		this.phone = phone;
		this.postalcode = postalcode;
		this.state = state;
		this.territory = territory;
	}

	public String getOfficecode() {
		return this.officecode;
	}

	public void setOfficecode(String officecode) {
		this.officecode = officecode;
	}

	public String getAddressline1() {
		return this.addressline1;
	}

	public void setAddressline1(String addressline1) {
		this.addressline1 = addressline1;
	}

	public String getAddressline2() {
		return this.addressline2;
	}

	public void setAddressline2(String addressline2) {
		this.addressline2 = addressline2;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPostalcode() {
		return this.postalcode;
	}

	public void setPostalcode(String postalcode) {
		this.postalcode = postalcode;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getTerritory() {
		return this.territory;
	}

	public void setTerritory(String territory) {
		this.territory = territory;
	}

}
