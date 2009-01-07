/*
 * JBoss, Home of Professional Open Source
 * Copyright 2006, JBoss Inc., and others contributors as indicated 
 * by the @authors tag. All rights reserved. 
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors. 
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 * 
 * (C) 2005-2006,
 * @author JBoss Inc.
 */

package org.jboss.tools.smooks.test.xml2java.dvdstore;

import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.io.Serializable;

/**
 * @author <a href="mailto:tom.fennelly@jboss.com">tom.fennelly@jboss.com</a>
 */
public class OrderHeader implements Serializable {
	
	// <Order orderId="1" orderDate="Wed Nov 15 13:45:28 EST 2006" statusCode="0" netAmount="59.97" totalAmount="64.92" tax="4.95">
	private String orderId;
	private Calendar orderDate;
	private int statusCode;
	private double netAmount;
	private double totalAmount;
	private double tax;
	
	/**
	 * @return Returns the netAmount.
	 */
	public double getNetAmount() {
		return netAmount;
	}
	/**
	 * @param netAmount The netAmount to set.
	 */
	public void setNetAmount(double netAmount) {
		this.netAmount = netAmount;
	}
	/**
	 * @return Returns the orderDate.
	 */
	public Calendar getOrderDate() {
		return orderDate;
	}
	/**
	 * @param orderDate The orderDate to set.
	 */
	public void setOrderDate(Calendar orderDate) {
		this.orderDate = orderDate;
	}
	/**
	 * @return Returns the orderId.
	 */
	public String getOrderId() {
		return orderId;
	}
	/**
	 * @param orderId The orderId to set.
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	/**
	 * @return Returns the statusCode.
	 */
	public int getStatusCode() {
		return statusCode;
	}
	/**
	 * @param statusCode The statusCode to set.
	 */
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	/**
	 * @return Returns the tax.
	 */
	public double getTax() {
		return tax;
	}
	/**
	 * @param tax The tax to set.
	 */
	public void setTax(double tax) {
		this.tax = tax;
	}
	/**
	 * @return Returns the totalAmount.
	 */
	public double getTotalAmount() {
		return totalAmount;
	}
	/**
	 * @param totalAmount The totalAmount to set.
	 */
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

    private SimpleDateFormat dateFormat = new SimpleDateFormat("d-MMM-yyyy");

    /* (non-Javadoc)
      * @see java.lang.Object#toString()
      */
	@Override
	public String toString() {
		return orderId + ", " + (orderDate == null?null:dateFormat.format(orderDate.getTime())) + ", " + statusCode + ", " + netAmount + ", " + totalAmount + ", " + tax + ", ";
	}
}
