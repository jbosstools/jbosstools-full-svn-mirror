package org.pregen.hsqldb;

// Generated Jul 16, 2012 4:51:41 PM by Hibernate Tools 4.0.0

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Rental generated by hbm2java
 */
public class Rental implements java.io.Serializable {

	private int rentalId;
	private Staff staff;
	private Customer customer;
	private Inventory inventory;
	private Date rentalDate;
	private Date returnDate;
	private Date lastUpdate;
	private Set payments = new HashSet(0);

	public Rental() {
	}

	public Rental(int rentalId, Staff staff, Customer customer,
			Inventory inventory, Date rentalDate, Date lastUpdate) {
		this.rentalId = rentalId;
		this.staff = staff;
		this.customer = customer;
		this.inventory = inventory;
		this.rentalDate = rentalDate;
		this.lastUpdate = lastUpdate;
	}

	public Rental(int rentalId, Staff staff, Customer customer,
			Inventory inventory, Date rentalDate, Date returnDate,
			Date lastUpdate, Set payments) {
		this.rentalId = rentalId;
		this.staff = staff;
		this.customer = customer;
		this.inventory = inventory;
		this.rentalDate = rentalDate;
		this.returnDate = returnDate;
		this.lastUpdate = lastUpdate;
		this.payments = payments;
	}

	public int getRentalId() {
		return this.rentalId;
	}

	public void setRentalId(int rentalId) {
		this.rentalId = rentalId;
	}

	public Staff getStaff() {
		return this.staff;
	}

	public void setStaff(Staff staff) {
		this.staff = staff;
	}

	public Customer getCustomer() {
		return this.customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Inventory getInventory() {
		return this.inventory;
	}

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}

	public Date getRentalDate() {
		return this.rentalDate;
	}

	public void setRentalDate(Date rentalDate) {
		this.rentalDate = rentalDate;
	}

	public Date getReturnDate() {
		return this.returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

	public Date getLastUpdate() {
		return this.lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public Set getPayments() {
		return this.payments;
	}

	public void setPayments(Set payments) {
		this.payments = payments;
	}

}
