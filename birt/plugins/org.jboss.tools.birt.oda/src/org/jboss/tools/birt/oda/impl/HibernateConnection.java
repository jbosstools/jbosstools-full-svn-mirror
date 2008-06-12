/*******************************************************************************
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 ******************************************************************************/
package org.jboss.tools.birt.oda.impl;

import java.util.Map;
import java.util.Properties;

import org.eclipse.core.runtime.Platform;
import org.eclipse.datatools.connectivity.oda.IConnection;
import org.eclipse.datatools.connectivity.oda.IDataSetMetaData;
import org.eclipse.datatools.connectivity.oda.IQuery;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.jboss.tools.birt.oda.IOdaSessionFactory;
import org.osgi.framework.Bundle;

/**
 * Implementation class of IConnection for an ODA runtime driver.
 * 
 * @author snjeza
 */
public class HibernateConnection implements IConnection
{
	private IOdaSessionFactory odaSessionFactory;
	private Map appContext;
    
	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#open(java.util.Properties)
	 */
	public void open( Properties connProperties ) throws OdaException
	{
		Bundle bundle = Platform.getBundle(IOdaSessionFactory.ORG_HIBERNATE_ECLIPSE_BUNDLE_ID);
		if (bundle != null) {
			odaSessionFactory = new ConsoleConfigurationOdaSessionFactory(connProperties);
		} else {
			//parentClassLoader = appContext.get(key);
			// FIXME
			odaSessionFactory = new ServerOdaSessionFactory(connProperties);
		}
 	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#setAppContext(java.lang.Object)
	 */
	public void setAppContext( Object context ) throws OdaException
	{
	    if (!(context instanceof Map)) {
	    	throw new OdaException("Invalid AppContext");
	    }
	    this.appContext = (Map) context;
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#close()
	 */
	public void close() throws OdaException
	{
        odaSessionFactory.close();
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#isOpen()
	 */
	public boolean isOpen() throws OdaException
	{
		return odaSessionFactory != null && odaSessionFactory.isOpen();
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
		return new HibernateOdaQuery(this);
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

	/*public SessionFactory getSessionFactory() {
		return odaSessionFactory.getSessionFactory();
	}*/
	
	public IOdaSessionFactory getOdaSessionFactory() {
		return odaSessionFactory;
	}
    
}
