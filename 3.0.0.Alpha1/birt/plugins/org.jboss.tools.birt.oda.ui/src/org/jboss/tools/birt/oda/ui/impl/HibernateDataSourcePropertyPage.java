package org.jboss.tools.birt.oda.ui.impl;
/*******************************************************************************
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 ******************************************************************************/
import java.util.Properties;

import org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSourceEditorPage;
import org.eclipse.swt.widgets.Composite;
import org.jboss.tools.birt.oda.IOdaFactory;

/**
 * Hibernate property page
 * 
 * @author snjeza
 **/
public class HibernateDataSourcePropertyPage extends DataSourceEditorPage {

	private HibernateSelectionPageHelper helper;

	public HibernateDataSourcePropertyPage() {
		super();
	}

	public Properties collectCustomProperties(Properties dataSourceProps) {
		Properties props = dataSourceProps;
		if (dataSourceProps == null) {
			props = new Properties();
		}
		props.setProperty(IOdaFactory.CONFIGURATION, helper.getConfiguration());
		props.setProperty(IOdaFactory.MAX_ROWS, helper.getMaxRows());
		return props;
	}

	protected void createAndInitCustomControl(Composite parent,
			Properties profileProps) {
		if ( helper == null )
			helper = new HibernateSelectionPageHelper( this );

		helper.createCustomControl( parent );
		setPingButtonVisible( false );
		helper.initCustomControl( profileProps );
	}

}
