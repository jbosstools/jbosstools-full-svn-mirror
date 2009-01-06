/**
 * 
 */
package org.jboss.tools.smooks.test.java2java;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 * @author Dart
 * 
 */
public class AbstractJavaBeanModel {
	
	private String stringProperty;
	private Date dateProperty;
	private Boolean booleanProperty;
	private Integer integerProperty;
	private Long longProperty;
	private Double doubleProperty;
	private Float floatProperty;
	private BigDecimal bigDecimalProperty;
	private BigInteger bigIntegerProperty;
	// ignore list
	private List testList;
	public List getTestList() {
		return testList;
	}
	public void setTestList(List testList) {
		this.testList = testList;
	}
	
	public String getStringProperty() {
		return stringProperty;
	}
	public void setStringProperty(String stringProperty) {
		this.stringProperty = stringProperty;
	}
	public Date getDateProperty() {
		return dateProperty;
	}
	public void setDateProperty(Date dateProperty) {
		this.dateProperty = dateProperty;
	}
	public Boolean getBooleanProperty() {
		return booleanProperty;
	}
	public void setBooleanProperty(Boolean booleanProperty) {
		this.booleanProperty = booleanProperty;
	}
	public Integer getIntegerProperty() {
		return integerProperty;
	}
	public void setIntegerProperty(Integer integerProperty) {
		this.integerProperty = integerProperty;
	}
	public Long getLongProperty() {
		return longProperty;
	}
	public void setLongProperty(Long longProperty) {
		this.longProperty = longProperty;
	}
	public Double getDoubleProperty() {
		return doubleProperty;
	}
	public void setDoubleProperty(Double doubleProperty) {
		this.doubleProperty = doubleProperty;
	}
	public Float getFloatProperty() {
		return floatProperty;
	}
	public void setFloatProperty(Float floatProperty) {
		this.floatProperty = floatProperty;
	}
	public BigDecimal getBigDecimalProperty() {
		return bigDecimalProperty;
	}
	public void setBigDecimalProperty(BigDecimal bigDecimalProperty) {
		this.bigDecimalProperty = bigDecimalProperty;
	}
	public BigInteger getBigIntegerProperty() {
		return bigIntegerProperty;
	}
	public void setBigIntegerProperty(BigInteger bigIntegerProperty) {
		this.bigIntegerProperty = bigIntegerProperty;
	}
}
