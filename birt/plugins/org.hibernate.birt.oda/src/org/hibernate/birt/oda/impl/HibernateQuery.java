/*******************************************************************************
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 ******************************************************************************/
package org.hibernate.birt.oda.impl;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.datatools.connectivity.oda.IParameterMetaData;
import org.eclipse.datatools.connectivity.oda.IResultSet;
import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.datatools.connectivity.oda.IQuery;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.SortSpec;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.type.Type;

/**
 * Implementation class of IQuery for an ODA runtime driver.
 * 
 *  @author snjeza
 */
public class HibernateQuery implements IQuery
{
	private Integer _maxRows;
	private HibernateConnection connection;
	private HibernateResultSetMetaData resultSetMetaData;
	private String queryText;
	
	public HibernateQuery(HibernateConnection connection) {
		this.connection = connection;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#prepare(java.lang.String)
	 */
	public void prepare(String queryText) throws OdaException {
		this.queryText = queryText;
		List arColsType = new ArrayList();
		List arCols = new ArrayList();
		List arColClass = new ArrayList();
		String[] props = null;
		Session session = null;
		try {
			session = connection.getSessionFactory().openSession();
			Query query = session.createQuery(queryText);
			int maxRows = connection.getMaxRows();
			if (maxRows > 0) {
				query.setFirstResult(0);
				query.setMaxResults(maxRows);
			}
			_maxRows = maxRows;
			Type[] qryReturnTypes = query.getReturnTypes();
			if (qryReturnTypes.length > 0 && qryReturnTypes[0].isEntityType()) {
				for (int j = 0; j < qryReturnTypes.length; j++) {
					String clsName = qryReturnTypes[j].getName();
					props = getHibernateProp(clsName);
					for (int x = 0; x < props.length; x++) {
						String propType = getHibernatePropTypes(clsName,
								props[x]);
						if (DataTypes.isValidType(propType)) {
							arColsType.add(propType);
							arCols.add(props[x]);
							arColClass.add(clsName);
						} else {
							//throw new OdaException("Data Type is Not Valid");
							arColsType.add(DataTypes.UNKNOWN);
							arCols.add(props[x]);
							arColClass.add("java.lang.String");
						}
					}
				}
			} else {
				props = extractColumns(query.getQueryString());
				for (int t = 0; t < qryReturnTypes.length; t++) {
					if (DataTypes.isValidType(qryReturnTypes[t].getName())) {
						arColsType.add(qryReturnTypes[t].getName());
						arCols.add(props[t]);
					} else {
						throw new OdaException("'"
								+ qryReturnTypes[t].getName()
								+ "' is not a valid type.");
					}
				}

			}
			String[] arLabels = (String[]) arCols.toArray(new String[arCols
					.size()]);
			for (int j = 0; j < arLabels.length; j++) {
				arLabels[j] = arLabels[j].replace('.', ':');
			}

			this.resultSetMetaData = new HibernateResultSetMetaData(arLabels,
					(String[]) arColsType
							.toArray(new String[arColsType.size()]), arLabels,
					(String[]) arColClass
							.toArray(new String[arColClass.size()]));
		} catch (Exception e) {
			throw new OdaException(e.getLocalizedMessage());
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	private static String[] extractColumns(final String query) {
        int fromPosition = query.toLowerCase().indexOf("from");
        int selectPosition = query.toLowerCase().indexOf("select");
        if (selectPosition >= 0) {
            String columns = query.substring(selectPosition + 6, fromPosition);
            StringTokenizer st = new StringTokenizer(columns, ",");
            List columnList = new ArrayList();
            while (st.hasMoreTokens()) {
                columnList.add(st.nextToken().trim());
            }
            return (String[]) columnList.toArray(new String[0]);
        } else {
            return null;
        }
    }
	
	private  String[] getHibernateProp(String className){
        SessionFactory sf = connection.getSessionFactory();
        String[] properties = sf.getClassMetadata(className).getPropertyNames();
        return( properties);
    }    
    
    private String getHibernatePropTypes(String className, String property){
    	SessionFactory sf = connection.getSessionFactory();
        Type hibClassProps = sf.getClassMetadata(className).getPropertyType(property);
        return(hibClassProps.getName());

    }  
	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setAppContext(java.lang.Object)
	 */
	public void setAppContext( Object context ) throws OdaException
	{
	    // do nothing; assumes no support for pass-through context
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#close()
	 */
	public void close() throws OdaException
	{
        // TODO Auto-generated method stub
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#getMetaData()
	 */
	public IResultSetMetaData getMetaData() throws OdaException
	{
		return this.resultSetMetaData;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#executeQuery()
	 */
	public IResultSet executeQuery() throws OdaException
	{
        try {
            return new HibernateResultSet(this);
        } catch (Exception e) {
            throw new OdaException(e.getLocalizedMessage());
        } 
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setProperty(java.lang.String, java.lang.String)
	 */
	public void setProperty( String name, String value ) throws OdaException
	{
		// do nothing; assumes no data set query property
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setMaxRows(int)
	 */
	public void setMaxRows( int max ) throws OdaException
	{
	    _maxRows = max;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#getMaxRows()
	 */
	public int getMaxRows() throws OdaException
	{
		return _maxRows;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#clearInParameters()
	 */
	public void clearInParameters() throws OdaException
	{
        // TODO Auto-generated method stub
		// only applies to input parameter
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setInt(java.lang.String, int)
	 */
	public void setInt( String parameterName, int value ) throws OdaException
	{
        // TODO Auto-generated method stub
		// only applies to named input parameter
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setInt(int, int)
	 */
	public void setInt( int parameterId, int value ) throws OdaException
	{
        // TODO Auto-generated method stub
		// only applies to input parameter
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setDouble(java.lang.String, double)
	 */
	public void setDouble( String parameterName, double value ) throws OdaException
	{
        // TODO Auto-generated method stub
		// only applies to named input parameter
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setDouble(int, double)
	 */
	public void setDouble( int parameterId, double value ) throws OdaException
	{
        // TODO Auto-generated method stub
		// only applies to input parameter
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setBigDecimal(java.lang.String, java.math.BigDecimal)
	 */
	public void setBigDecimal( String parameterName, BigDecimal value ) throws OdaException
	{
        // TODO Auto-generated method stub
		// only applies to named input parameter
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setBigDecimal(int, java.math.BigDecimal)
	 */
	public void setBigDecimal( int parameterId, BigDecimal value ) throws OdaException
	{
        // TODO Auto-generated method stub
		// only applies to input parameter
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setString(java.lang.String, java.lang.String)
	 */
	public void setString( String parameterName, String value ) throws OdaException
	{
        // TODO Auto-generated method stub
		// only applies to named input parameter
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setString(int, java.lang.String)
	 */
	public void setString( int parameterId, String value ) throws OdaException
	{
        // TODO Auto-generated method stub
		// only applies to input parameter
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setDate(java.lang.String, java.sql.Date)
	 */
	public void setDate( String parameterName, Date value ) throws OdaException
	{
        // TODO Auto-generated method stub
		// only applies to named input parameter
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setDate(int, java.sql.Date)
	 */
	public void setDate( int parameterId, Date value ) throws OdaException
	{
        // TODO Auto-generated method stub
		// only applies to input parameter
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setTime(java.lang.String, java.sql.Time)
	 */
	public void setTime( String parameterName, Time value ) throws OdaException
	{
        // TODO Auto-generated method stub
		// only applies to named input parameter
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setTime(int, java.sql.Time)
	 */
	public void setTime( int parameterId, Time value ) throws OdaException
	{
        // TODO Auto-generated method stub
		// only applies to input parameter
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setTimestamp(java.lang.String, java.sql.Timestamp)
	 */
	public void setTimestamp( String parameterName, Timestamp value ) throws OdaException
	{
        // TODO Auto-generated method stub
		// only applies to named input parameter
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setTimestamp(int, java.sql.Timestamp)
	 */
	public void setTimestamp( int parameterId, Timestamp value ) throws OdaException
	{
        // TODO Auto-generated method stub
		// only applies to input parameter
	}

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IQuery#setBoolean(java.lang.String, boolean)
     */
    public void setBoolean( String parameterName, boolean value )
            throws OdaException
    {
        // TODO Auto-generated method stub
        // only applies to named input parameter
    }

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IQuery#setBoolean(int, boolean)
     */
    public void setBoolean( int parameterId, boolean value )
            throws OdaException
    {
        // TODO Auto-generated method stub       
        // only applies to input parameter
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IQuery#setNull(java.lang.String)
     */
    public void setNull( String parameterName ) throws OdaException
    {
        // TODO Auto-generated method stub
        // only applies to named input parameter
    }

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IQuery#setNull(int)
     */
    public void setNull( int parameterId ) throws OdaException
    {
        // TODO Auto-generated method stub
        // only applies to input parameter
    }

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#findInParameter(java.lang.String)
	 */
	public int findInParameter( String parameterName ) throws OdaException
	{
        // TODO Auto-generated method stub
		// only applies to named input parameter
		return 0;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#getParameterMetaData()
	 */
	public IParameterMetaData getParameterMetaData() throws OdaException
	{
        /* TODO Auto-generated method stub
         * Replace with implementation to return an instance 
         * based on this prepared query.
         */
		return new HibernateParameterMetaData();
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#setSortSpec(org.eclipse.datatools.connectivity.oda.SortSpec)
	 */
	public void setSortSpec( SortSpec sortBy ) throws OdaException
	{
        // TODO Auto-generated method stub
		// only applies to sorting, assumes not supported
        throw new UnsupportedOperationException();
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IQuery#getSortSpec()
	 */
	public SortSpec getSortSpec() throws OdaException
	{
        // TODO Auto-generated method stub
		// only applies to sorting
		return null;
	}

	public HibernateConnection getConnection() {
		return connection;
	}

	public String getQueryText() {
		return queryText;
	}
    
}
