/*******************************************************************************
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 ******************************************************************************/

package org.jboss.tools.birt.oda.impl;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

import org.eclipse.datatools.connectivity.oda.IBlob;
import org.eclipse.datatools.connectivity.oda.IClob;
import org.eclipse.datatools.connectivity.oda.IResultSet;
import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.osgi.util.NLS;
import org.hibernate.Session;
import org.jboss.tools.birt.oda.IOdaFactory;
import org.jboss.tools.birt.oda.Messages;

/**
 * Implementation class of IResultSet for an ODA runtime driver.
 * 
 * @author snjeza
 */
public class HibernateResultSet implements IResultSet {
	private int _maxRows;
	private HibernateOdaQuery query;
	
	private int rowNumber = -1;
	private boolean wasNull;
	private HibernateResult hibernateResult;
	
	private static DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");	 //$NON-NLS-1$

	public HibernateResultSet(HibernateOdaQuery query) throws OdaException {
		this.query = query;
		HibernateConnection connection = query.getConnection();
		IOdaFactory odaSessionFactory = connection.getOdaSessionFactory();
		Session session = connection.getSession();
		hibernateResult = odaSessionFactory.executeQuery(query,session);
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getMetaData()
	 */
	public IResultSetMetaData getMetaData() throws OdaException {
		return query.getMetaData();
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#setMaxRows(int)
	 */
	public void setMaxRows(int max) throws OdaException {
		_maxRows = max;
	}

	/**
	 * Returns the maximum number of rows that can be fetched from this result
	 * set.
	 * 
	 * @return the maximum number of rows to fetch.
	 */
	protected int getMaxRows() {
		return _maxRows;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#next()
	 */
	public boolean next() throws OdaException {
		if (getIterator().hasNext()) {
			hibernateResult.next();
			rowNumber++;
			return true;
		}
		return false;

	}

	private Iterator getIterator() {
		return hibernateResult.getIterator();
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#close()
	 */
	public void close() throws OdaException {
		hibernateResult.close();
	}

	private List getResult() {
		return hibernateResult.getResult();
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getRow()
	 */
	public int getRow() throws OdaException {
		testFetchStarted();
		return rowNumber + 1;
	}

	private Object getResult(int rstcol) throws OdaException {
		return hibernateResult.getResult(rstcol);
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getString(int)
	 */
	public String getString(int index) throws OdaException {
		String result;
		testFetchStarted();
		Object rObj = getResult(index);
		if (rObj == null) {
			result = null;
		} else {
			result = rObj.toString();
		}
		this.wasNull = (result == null);
		return result;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getString(java.lang.String)
	 */
	public String getString(String columnName) throws OdaException {
		return getString(findColumn(columnName));
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getInt(int)
	 */
	public int getInt(int index) throws OdaException {
		int result;
		testFetchStarted();
		Object rObj = getResult(index);
		if (rObj == null) {
			result = 0;
			this.wasNull = true;
		} else {
			this.wasNull = false;
			if (rObj instanceof Integer)
				result = ((Integer) rObj).intValue();
			else if (rObj instanceof Long)
				result = ((Long) rObj).intValue();
			else if (rObj instanceof Short)
				result = ((Short) rObj).intValue();
			else
				throw new RuntimeException(NLS.bind(Messages.HibernateResultSet_The_data_type_is_not_valid, rObj.getClass())); 
		}

		return result;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getInt(java.lang.String)
	 */
	public int getInt(String columnName) throws OdaException {
		return getInt(findColumn(columnName));
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getDouble(int)
	 */
	public double getDouble(int index) throws OdaException {
		double result;
		testFetchStarted();
		Object rObj = getResult(index);
		if (rObj == null) {
			result = 0;
			this.wasNull = true;
		} else {
			this.wasNull = false;
			result = ((Double) rObj).doubleValue();
		}

		return result;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getDouble(java.lang.String)
	 */
	public double getDouble(String columnName) throws OdaException {
		return getDouble(findColumn(columnName));
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getBigDecimal(int)
	 */
	public BigDecimal getBigDecimal(int index) throws OdaException {
		BigDecimal result;
		testFetchStarted();
		Object rObj = getResult(index);
		if (rObj == null) {
			result = new BigDecimal(0);
			this.wasNull = true;
		} else {
			this.wasNull = false;
			result = (BigDecimal) rObj;
		}

		return result;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getBigDecimal(java.lang.String)
	 */
	public BigDecimal getBigDecimal(String columnName) throws OdaException {
		return getBigDecimal(findColumn(columnName));
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getDate(int)
	 */
	public Date getDate(int index) throws OdaException {
		Date result = null;
		testFetchStarted();
		Object rObj = getResult(index);
		if (rObj == null) {
			this.wasNull = true;
		} else {
			this.wasNull = false;
			if (rObj instanceof String) {
				try {
					result = new Date(dateFormat.parse((String) rObj).getTime());
				} catch (ParseException e) {
					e.printStackTrace();
				}
			} else {
				result = (Date) rObj;
			}
		}

		return result;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getDate(java.lang.String)
	 */
	public Date getDate(String columnName) throws OdaException {
		return getDate(findColumn(columnName));
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getTime(int)
	 */
	public Time getTime(int index) throws OdaException {
		Time result = null;
		testFetchStarted();
		Object rObj = getResult(index);
		if (rObj == null) {
			this.wasNull = true;
		} else {
			this.wasNull = false;
			result = (Time) rObj;
		}
		return result;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getTime(java.lang.String)
	 */
	public Time getTime(String columnName) throws OdaException {
		return getTime(findColumn(columnName));
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getTimestamp(int)
	 */
	public Timestamp getTimestamp(int index) throws OdaException {
		Timestamp result = null;
		testFetchStarted();
		Object rObj = getResult(index);
		if (rObj == null) {
			this.wasNull = true;
		} else {
			this.wasNull = false;
			result = (Timestamp) rObj;
		}
		return result;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getTimestamp(java.lang.String)
	 */
	public Timestamp getTimestamp(String columnName) throws OdaException {
		return getTimestamp(findColumn(columnName));
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getBlob(int)
	 */
	public IBlob getBlob(int index) throws OdaException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getBlob(java.lang.String)
	 */
	public IBlob getBlob(String columnName) throws OdaException {
		return getBlob(findColumn(columnName));
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getClob(int)
	 */
	public IClob getClob(int index) throws OdaException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getClob(java.lang.String)
	 */
	public IClob getClob(String columnName) throws OdaException {
		return getClob(findColumn(columnName));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getBoolean(int)
	 */
	public boolean getBoolean(int index) throws OdaException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#getBoolean(java.lang.String)
	 */
	public boolean getBoolean(String columnName) throws OdaException {
		return getBoolean(findColumn(columnName));
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#wasNull()
	 */
	public boolean wasNull() throws OdaException {
		return wasNull;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSet#findColumn(java.lang.String)
	 */
	public int findColumn(String columnName) throws OdaException {
		// TODO replace with data source specific implementation

		// hard-coded for demo purpose
		int columnId = 1; // dummy column id
		if (columnName == null || columnName.length() == 0)
			return columnId;
		String lastChar = columnName.substring(columnName.length() - 1, 1);
		try {
			columnId = Integer.parseInt(lastChar);
		} catch (NumberFormatException e) {
			// ignore, use dummy column id
		}
		return columnId;
	}

	private void testFetchStarted() throws OdaException {
		if (rowNumber < 0)
			throw new OdaException(Messages.HibernateResultSet_Cursor_has_not_been_initialized);
	}

}
