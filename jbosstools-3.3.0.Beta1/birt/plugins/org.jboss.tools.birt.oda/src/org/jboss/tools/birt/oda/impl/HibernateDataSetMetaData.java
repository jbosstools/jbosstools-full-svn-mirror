/*******************************************************************************
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 ******************************************************************************/

package org.jboss.tools.birt.oda.impl;

import org.eclipse.datatools.connectivity.oda.IConnection;
import org.eclipse.datatools.connectivity.oda.IDataSetMetaData;
import org.eclipse.datatools.connectivity.oda.IResultSet;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.jboss.tools.birt.oda.Messages;

/**
 * Implementation class of IDataSetMetaData for an ODA runtime driver.
 * 
 * @author snjeza
 */
public class HibernateDataSetMetaData implements IDataSetMetaData
{
	private IConnection connection;
	
	HibernateDataSetMetaData( IConnection connection )
	{
		this.connection = connection;
	}
	
	/*
	 * @see org.eclipse.datatools.connectivity.oda.IDataSetMetaData#getConnection()
	 */
	public IConnection getConnection() throws OdaException
	{
		return connection;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IDataSetMetaData#getDataSourceObjects(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public IResultSet getDataSourceObjects( String catalog, String schema, String object, String version ) throws OdaException
	{
	    throw new UnsupportedOperationException();
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IDataSetMetaData#getDataSourceMajorVersion()
	 */
	public int getDataSourceMajorVersion() throws OdaException
	{
		return 1;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IDataSetMetaData#getDataSourceMinorVersion()
	 */
	public int getDataSourceMinorVersion() throws OdaException
	{
		return 0;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IDataSetMetaData#getDataSourceProductName()
	 */
	public String getDataSourceProductName() throws OdaException
	{
		return Messages.HibernateDataSetMetaData_Hibernate_Data_Source;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IDataSetMetaData#getDataSourceProductVersion()
	 */
	public String getDataSourceProductVersion() throws OdaException
	{
		return Integer.toString( getDataSourceMajorVersion() ) + "." +   //$NON-NLS-1$
			   Integer.toString( getDataSourceMinorVersion() );
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IDataSetMetaData#getSQLStateType()
	 */
	public int getSQLStateType() throws OdaException
	{
		return IDataSetMetaData.sqlStateSQL99;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IDataSetMetaData#supportsMultipleResultSets()
	 */
	public boolean supportsMultipleResultSets() throws OdaException
	{
		return false;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IDataSetMetaData#supportsMultipleOpenResults()
	 */
	public boolean supportsMultipleOpenResults() throws OdaException
	{
		return false;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IDataSetMetaData#supportsNamedResultSets()
	 */
	public boolean supportsNamedResultSets() throws OdaException
	{
		return false;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IDataSetMetaData#supportsNamedParameters()
	 */
	public boolean supportsNamedParameters() throws OdaException
	{
		return false;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IDataSetMetaData#supportsInParameters()
	 */
	public boolean supportsInParameters() throws OdaException
	{
		return true;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IDataSetMetaData#supportsOutParameters()
	 */
	public boolean supportsOutParameters() throws OdaException
	{
		return false;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IDataSetMetaData#getSortMode()
	 */
	public int getSortMode()
	{
		return IDataSetMetaData.sortModeNone;
	}
    
}
