/*******************************************************************************
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 ******************************************************************************/

package org.jboss.tools.birt.oda.impl;

import java.util.StringTokenizer;

import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.osgi.util.NLS;
import org.jboss.tools.birt.oda.Messages;

/**
 * Implementation class of IResultSetMetaData for an ODA runtime driver.
 * 
 * @author snjeza
 */
public class HibernateResultSetMetaData implements IResultSetMetaData
{
    
	private String[] columnName = null;
	private String[] columnType = null;
	private String[] columnLabel = null;
	private String[] columnClass = null;
	//private HibernateOdaQuery query;
	
	HibernateResultSetMetaData( String[] cName, String[] cType, String[] cLabel, String[] cClass )
			throws OdaException
	{
		if ( cName == null )
			throw new OdaException( Messages.HibernateResultSetMetaData_Argument_cannot_be_null );
		this.columnName = cName;
		this.columnType = cType;
		this.columnLabel = cLabel;
		this.columnClass = cClass;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSetMetaData#getColumnCount()
	 */
	public int getColumnCount() throws OdaException
	{
        return columnName.length;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSetMetaData#getColumnName(int)
	 */
	public String getColumnName( int index ) throws OdaException
	{
		assertIndexValid( index );
        String name = this.columnName[index - 1].trim( );
        if (name.indexOf(" ") > 0) { //$NON-NLS-1$
            StringTokenizer tokenizer = new StringTokenizer(name," "); //$NON-NLS-1$
            String prettyName = null;
            while (tokenizer.hasMoreTokens()) {
                prettyName = tokenizer.nextToken();
            }
            if (prettyName != null) {
                name = prettyName;
            }
        }
		return name;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSetMetaData#getColumnLabel(int)
	 */
	public String getColumnLabel( int index ) throws OdaException
	{
		assertIndexValid( index );
		//"null" in lower case is the mark of "null value". We should not use
		// "equalsIgnoreCase"
		//here for "null" is not a keyword so that we cannot prevent user from
		// using "null" as labels of
		//certain columns.
		if ( this.columnLabel == null
				|| this.columnLabel[index - 1].equals( "null" ) ) //$NON-NLS-1$
			return this.getColumnName( index );

		return this.columnLabel[index - 1].trim( );
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSetMetaData#getColumnType(int)
	 */
	public int getColumnType( int index ) throws OdaException
	{
		assertIndexValid( index );
		//get the integer value of the data type specified
		return (this.columnType[index - 1] == null)? DataTypes.NULL : DataTypes.getType( columnType[index - 1] ) ;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSetMetaData#getColumnTypeName(int)
	 */
	public String getColumnTypeName( int index ) throws OdaException
	{
        int nativeTypeCode = getColumnType( index );
        return HibernateDriver.getNativeDataTypeName( nativeTypeCode );
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSetMetaData#getColumnDisplayLength(int)
	 */
	public int getColumnDisplayLength( int index ) throws OdaException
	{
        // TODO replace with data source specific implementation
		return 8;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSetMetaData#getPrecision(int)
	 */
	public int getPrecision( int index ) throws OdaException
	{
        // TODO Auto-generated method stub
		return -1;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSetMetaData#getScale(int)
	 */
	public int getScale( int index ) throws OdaException
	{
        // TODO Auto-generated method stub
		return -1;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IResultSetMetaData#isNullable(int)
	 */
	public int isNullable( int index ) throws OdaException
	{
        // TODO Auto-generated method stub
		return IResultSetMetaData.columnNullableUnknown;
	}
	
	/**
	 * Evaluate whether the value of an index is valid
	 *
	 * @param index
	 *            the value of an index
	 * @throws OdaException
	 *             if the value is
	 */
	private void assertIndexValid( int index ) throws OdaException
	{
		if ( index > getColumnCount( ) || index < 1 )
			throw new OdaException( NLS.bind(Messages.HibernateResultSetMetaData_Invalid_index, new Integer(index).toString()) );
	}
    
	public String getColumnClass( int index ) throws OdaException
	{
		assertIndexValid( index );
		return ( this.columnClass[index - 1] ) ;
	}	
}
