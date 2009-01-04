package org.jboss.tools.smooks.test.java2java;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:tom.fennelly@gmail.com">tom.fennelly@gmail.com</a>
 */
public class Order extends AbstractJavaBeanModel {
	private Header header;
	private List<OrderItem> modifyOrderItems;
	private int[] orderCounts = new int[] {};
	private Header[] headerArray = new Header[] { header };
	private int[] ages;

	public Order() {
		header = new Header();
		modifyOrderItems = new ArrayList<OrderItem>();
		modifyOrderItems.add(new OrderItem());
		modifyOrderItems.add(new OrderItem());

		modifyOrderItems.get(0).setProductId(111);
		modifyOrderItems.get(0).setQuantity(2);
		modifyOrderItems.get(0).setPrice(10.99);

		modifyOrderItems.get(1).setProductId(222);
		modifyOrderItems.get(1).setQuantity(4);
		modifyOrderItems.get(1).setPrice(25.50);
	}

	public int[] getAges() {
		return ages;
	}

	public void setAges(int[] ages) {
		this.ages = ages;
	}

	public Header[] getHeaderArray() {
		return headerArray;
	}

	public void setHeaderArray(Header[] headerArray) {
		this.headerArray = headerArray;
	}

	public int[] getOrderCounts() {
		return orderCounts;
	}

	public void setOrderCounts(int[] orderCounts) {
		this.orderCounts = orderCounts;
	}

	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public List<OrderItem> getOrderItems() {
		return modifyOrderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.modifyOrderItems = orderItems;
	}

	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append("Class: " + getClass().getName() + "\n");
		stringBuilder.append("\theader: " + header + "\n");
		stringBuilder.append("\torderItems: " + modifyOrderItems);

		return stringBuilder.toString();
	}
}