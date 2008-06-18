/*************************************************************************************
 * Copyright (c) 2008 JBoss, a division of Red Hat and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     JBoss, a division of Red Hat - Initial implementation.
 ************************************************************************************/
package org.jboss.tools.birt.oda;

import java.util.Iterator;
import java.util.List;

import org.eclipse.datatools.connectivity.oda.OdaException;
import org.jboss.tools.birt.oda.impl.HibernateOdaQuery;
import org.jboss.tools.birt.oda.impl.HibernateResultSetMetaData;

/**
 * 
 * @author snjeza
 * 
 */
public interface IOdaFactory {

	public static final String MAX_ROWS = "maxRows";
	public static final String CONFIGURATION = "configuration";
	public static final String ORG_HIBERNATE_ECLIPSE_BUNDLE_ID = "org.hibernate.eclipse";
	
	void close();
	boolean isOpen();
	HibernateResultSetMetaData prepare(String queryText) throws OdaException;
	void setMaxRows(int max);
	int getMaxRows();
	void executeQuery(HibernateOdaQuery query) throws OdaException;
	Iterator getIterator();
	List getResult();
	Object getResult(int rstcol) throws OdaException;
	void next();
}
