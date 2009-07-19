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
import java.util.List;

import org.eclipse.datatools.connectivity.oda.IParameterMetaData;
import org.eclipse.datatools.connectivity.oda.IQuery;
import org.eclipse.datatools.connectivity.oda.IResultSet;
import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.SortSpec;
import org.hibernate.Session;

/**
 * Implementation class of IQuery for an ODA runtime driver.
 * 
 * @author snjeza
 */
public class HibernateOdaQuery implements IQuery {
	private HibernateConnection connection;
	private HibernateResultSetMetaData resultSetMetaData;
	private HibernateParameterMetaData parameterMetaData = new HibernateParameterMetaData();
	private Session session;

	public HibernateOdaQuery(HibernateConnection connection) {
		this.connection = connection;
		this.session = connection.getSession();
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IQuery#prepare(java.lang.String)
	 */
	public void prepare(String queryText) throws OdaException {
		this.resultSetMetaData = getConnection().getOdaSessionFactory()
				.prepare(queryText,session);
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IQuery#setAppContext(java.lang
	 * .Object)
	 */
	public void setAppContext(Object context) throws OdaException {
		// do nothing; assumes no support for pass-through context
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#close()
	 */
	public void close() throws OdaException {
		// TODO Auto-generated method stub
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#getMetaData()
	 */
	public IResultSetMetaData getMetaData() throws OdaException {
		return this.resultSetMetaData;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#executeQuery()
	 */
	public IResultSet executeQuery() throws OdaException {
		try {
			return new HibernateResultSet(this);
		} catch (Exception e) {
			throw new OdaException(e.getLocalizedMessage());
		}
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IQuery#setProperty(java.lang.String
	 * , java.lang.String)
	 */
	public void setProperty(String name, String value) throws OdaException {
		// do nothing; assumes no data set query property
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setMaxRows(int)
	 */
	public void setMaxRows(int max) throws OdaException {
		getConnection().getOdaSessionFactory().setMaxRows(max);
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#getMaxRows()
	 */
	public int getMaxRows() throws OdaException {
		return getConnection().getOdaSessionFactory().getMaxRows();
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#clearInParameters()
	 */
	public void clearInParameters() throws OdaException {
		parameterMetaData.getParameters().clear();
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IQuery#setInt(java.lang.String,
	 * int)
	 */
	public void setInt(String parameterName, int value) throws OdaException {
		setParameter(parameterName, Parameter.IntegerType, value);
	}
	
	private void setParameter(String parameterName,int type,Object value) {
		Parameter parameter = getParameter(parameterName);
		if (parameter == null) {
			parameter = new Parameter(type, parameterName,
					value);
			parameterMetaData.getParameters().add(parameter);
		} else {
			parameter.setValue(value);
			parameter.setType(type);
		}
	}

	private Parameter getParameter(String parameterName) {
		if (parameterName == null)
			return null;
		List<Parameter> parameters = parameterMetaData.getParameters();
		for (Parameter parameter : parameters) {
			if (parameterName.equals(parameter.getName())) {
				return parameter;
			}
		}
		return null;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setInt(int, int)
	 */
	public void setInt(int parameterId, int value) throws OdaException {
		setParameter(parameterId, Parameter.IntegerType, value);
	}
	
	private void setParameter(int parameterId,int type,Object value) {
		List<Parameter> parameters = parameterMetaData.getParameters();
		String parameterName = "parameter" + parameterId; //$NON-NLS-1$
		if (parameters.size() < parameterId) {
			Parameter parameter = new Parameter(type, parameterName,
					value);
			parameterMetaData.getParameters().add(parameter);
		} else {
			Parameter parameter = parameterMetaData.getParameters().get(parameterId-1);
			parameter.setValue(value);
			parameter.setType(type);
		}
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IQuery#setDouble(java.lang.String,
	 * double)
	 */
	public void setDouble(String parameterName, double value)
			throws OdaException {
		setParameter(parameterName, Parameter.DoubleType, new Double(value));
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setDouble(int, double)
	 */
	public void setDouble(int parameterId, double value) throws OdaException {
		setParameter(parameterId, Parameter.DoubleType, new Double(value));
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IQuery#setBigDecimal(java.lang
	 * .String, java.math.BigDecimal)
	 */
	public void setBigDecimal(String parameterName, BigDecimal value)
			throws OdaException {
		setParameter(parameterName, Parameter.BigDecimalType, value);
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setBigDecimal(int,
	 * java.math.BigDecimal)
	 */
	public void setBigDecimal(int parameterId, BigDecimal value)
			throws OdaException {
		setParameter(parameterId, Parameter.BigDecimalType, value);
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IQuery#setString(java.lang.String,
	 * java.lang.String)
	 */
	public void setString(String parameterName, String value)
			throws OdaException {
		setParameter(parameterName, Parameter.StringType, value);
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setString(int,
	 * java.lang.String)
	 */
	public void setString(int parameterId, String value) throws OdaException {
		setParameter(parameterId, Parameter.StringType, value);
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IQuery#setDate(java.lang.String,
	 * java.sql.Date)
	 */
	public void setDate(String parameterName, Date value) throws OdaException {
		setParameter(parameterName, Parameter.DateType, value);
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setDate(int,
	 * java.sql.Date)
	 */
	public void setDate(int parameterId, Date value) throws OdaException {
		setParameter(parameterId, Parameter.DateType, value);
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IQuery#setTime(java.lang.String,
	 * java.sql.Time)
	 */
	public void setTime(String parameterName, Time value) throws OdaException {
		setParameter(parameterName, Parameter.TimeType, value);
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setTime(int,
	 * java.sql.Time)
	 */
	public void setTime(int parameterId, Time value) throws OdaException {
		setParameter(parameterId, Parameter.TimeType, value);
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IQuery#setTimestamp(java.lang.
	 * String, java.sql.Timestamp)
	 */
	public void setTimestamp(String parameterName, Timestamp value)
			throws OdaException {
		setParameter(parameterName, Parameter.TimestampType, value);
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setTimestamp(int,
	 * java.sql.Timestamp)
	 */
	public void setTimestamp(int parameterId, Timestamp value)
			throws OdaException {
		setParameter(parameterId, Parameter.TimestampType, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IQuery#setBoolean(java.lang.String
	 * , boolean)
	 */
	public void setBoolean(String parameterName, boolean value)
			throws OdaException {
		setParameter(parameterName, Parameter.BooleanType, new Boolean(value));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setBoolean(int,
	 * boolean)
	 */
	public void setBoolean(int parameterId, boolean value) throws OdaException {
		setParameter(parameterId, Parameter.BooleanType, new Boolean(value));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IQuery#setNull(java.lang.String)
	 */
	public void setNull(String parameterName) throws OdaException {
		Parameter parameter = getParameter(parameterName);
		if (parameter != null) {
			parameter.setValue(null);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setNull(int)
	 */
	public void setNull(int parameterId) throws OdaException {
		Parameter parameter = parameterMetaData.getParameters().get(parameterId);
		if (parameter != null) {
			parameter.setValue(null);
		}
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IQuery#findInParameter(java.lang
	 * .String)
	 */
	public int findInParameter(String parameterName) throws OdaException {
		if (parameterName == null)
			return 0;
		for(Parameter parameter:parameterMetaData.getParameters()) {
			if (parameterName.equals(parameter.getName()))
				return parameterMetaData.getParameters().indexOf(parameter);
		}
		return 0;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#getParameterMetaData()
	 */
	public IParameterMetaData getParameterMetaData() throws OdaException {
		return parameterMetaData;
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IQuery#setSortSpec(org.eclipse
	 * .datatools.connectivity.oda.SortSpec)
	 */
	public void setSortSpec(SortSpec sortBy) throws OdaException {
		// TODO Auto-generated method stub
		// only applies to sorting, assumes not supported
		throw new UnsupportedOperationException();
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#getSortSpec()
	 */
	public SortSpec getSortSpec() throws OdaException {
		// TODO Auto-generated method stub
		// only applies to sorting
		return null;
	}

	public HibernateConnection getConnection() {
		return connection;
	}

}
