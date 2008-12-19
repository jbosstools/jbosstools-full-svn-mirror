/*******************************************************************************
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 ******************************************************************************/
package org.jboss.tools.birt.oda.ui.impl;

import java.util.Properties;

import org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSourceWizardPage;
import org.eclipse.swt.widgets.Composite;
/**
 * Hibernate wizard page
 * 
 * @author snjeza
 **/
public class HibernateDataSourceWizardPage extends DataSourceWizardPage {

	private HibernateSelectionPageHelper helper;
	private Properties folderProperties;

	public HibernateDataSourceWizardPage(String pageName) {
		super(pageName);
	}

	public Properties collectCustomProperties() {
		if( helper != null ) 
            return helper.collectCustomProperties( folderProperties );

        return ( folderProperties != null ) ?
                    folderProperties : new Properties();
	}

	public void createPageCustomControl(Composite parent) {
		if( helper == null )
            helper = new HibernateSelectionPageHelper( this );
        helper.createCustomControl( parent );
        helper.initCustomControl( folderProperties ); 
        this.setPingButtonVisible( false );
	}

	public void setInitialProperties(Properties dataSourceProps) {
		folderProperties = dataSourceProps;
        if( helper == null )
            return;
        helper.initCustomControl( folderProperties );  
	}

	
}
