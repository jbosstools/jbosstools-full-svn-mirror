/*******************************************************************************
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 ******************************************************************************/
package org.jboss.tools.birt.oda.impl;

import java.util.Properties;

import org.eclipse.datatools.connectivity.oda.IConnection;
import org.eclipse.datatools.connectivity.oda.IDataSetMetaData;
import org.eclipse.datatools.connectivity.oda.IQuery;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.console.ConsoleConfiguration;
import org.hibernate.console.KnownConfigurations;

/**
 * Implementation class of IConnection for an ODA runtime driver.
 * 
 * @author snjeza
 */
public class HibernateConnection implements IConnection
{
    public static final String MAX_ROWS = "maxRows";
	public static final String CONFIGURATION = "configuration";
	private boolean isOpen = false;
    private ConsoleConfiguration consoleConfiguration;
    private SessionFactory sessionFactory;
    private Integer maxRows = -1;
    
	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#open(java.util.Properties)
	 */
	public void open( Properties connProperties ) throws OdaException
	{
        String configurationName = connProperties.getProperty(CONFIGURATION);
        ConsoleConfiguration[] configurations = KnownConfigurations.getInstance().getConfigurations();
        for (int i = 0; i < configurations.length; i++) {
			if (configurations[i].getName().equals(configurationName)) {
				consoleConfiguration=configurations[i];
				break;
			}
		}
        isOpen = (consoleConfiguration != null);
        if (isOpen) {
        	try {
				sessionFactory = consoleConfiguration.getSessionFactory();
				if (sessionFactory == null) {
					consoleConfiguration.build();
					consoleConfiguration.buildSessionFactory();
					sessionFactory = consoleConfiguration.getSessionFactory();
				}
			} catch (HibernateException e) {
				throw new OdaException(e.getLocalizedMessage());
			}
        } else {
        	throw new OdaException("Invalid configuration '" + configurationName + "'");
        }
        String maxRowString = connProperties.getProperty(MAX_ROWS);
        try {
			maxRows = new Integer(maxRowString);
		} catch (NumberFormatException e) {
			// ignore
		}
 	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#setAppContext(java.lang.Object)
	 */
	public void setAppContext( Object context ) throws OdaException
	{
	    // do nothing; assumes no support for pass-through context
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#close()
	 */
	public void close() throws OdaException
	{
        consoleConfiguration = null;
        sessionFactory = null;
	    isOpen = false;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#isOpen()
	 */
	public boolean isOpen() throws OdaException
	{
		return isOpen;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#getMetaData(java.lang.String)
	 */
	public IDataSetMetaData getMetaData( String dataSetType ) throws OdaException
	{
	    // assumes that this driver supports only one type of data set,
        // ignores the specified dataSetType
		return new HibernateDataSetMetaData( this );
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#newQuery(java.lang.String)
	 */
	public IQuery newQuery( String dataSetType ) throws OdaException
	{
        // assumes that this driver supports only one type of data set,
        // ignores the specified dataSetType
		return new HibernateQuery(this);
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#getMaxQueries()
	 */
	public int getMaxQueries() throws OdaException
	{
		return 0;	// no limit
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#commit()
	 */
	public void commit() throws OdaException
	{
	    // do nothing; assumes no transaction support needed
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#rollback()
	 */
	public void rollback() throws OdaException
	{
        // do nothing; assumes no transaction support needed
	}

	public Integer getMaxRows() {
		return maxRows;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
    
}
