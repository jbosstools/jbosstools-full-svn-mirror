/*******************************************************************************
  * Copyright (c) 2008-2009 Red Hat, Inc.
  * Distributed under license by Red Hat, Inc. All rights reserved.
  * This program is made available under the terms of the
  * Eclipse Public License v1.0 which accompanies this distribution,
  * and is available at http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributor:
  *     Red Hat, Inc. - initial API and implementation
  ******************************************************************************/
package org.jboss.tools.hibernate.jpt.core.internal.context;

import java.io.File;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jpt.core.context.persistence.Persistence;
import org.eclipse.jpt.core.internal.context.persistence.AbstractPersistenceUnit;
import org.eclipse.jpt.core.resource.persistence.XmlPersistenceUnit;
import org.eclipse.wst.validation.internal.core.Message;
import org.eclipse.wst.validation.internal.provisional.core.IMessage;
import org.eclipse.wst.validation.internal.provisional.core.IReporter;
import org.jboss.tools.hibernate.jpt.core.internal.HibernateJptPlugin;
import org.jboss.tools.hibernate.jpt.core.internal.context.basic.BasicHibernateProperties;
import org.jboss.tools.hibernate.jpt.core.internal.context.basic.Hibernate;
import org.jboss.tools.hibernate.jpt.core.internal.context.basic.HibernatePersistenceUnitProperties;
import org.jboss.tools.hibernate.jpt.core.internal.context.persistence.HibernatePersistenceXmlContextNodeFactory;

/**
 * @author Dmitry Geraskov
 *
 */
public class HibernatePersistenceUnit extends AbstractPersistenceUnit 
	implements Messages, Hibernate {
	
	private HibernatePersistenceUnitProperties hibernateProperties;

	/**
	 * @param parent
	 * @param persistenceUnit
	 */
	public HibernatePersistenceUnit(Persistence parent,
			XmlPersistenceUnit persistenceUnit) {
		super(parent, persistenceUnit);
	}
	
	@Override
	public HibernatePersistenceXmlContextNodeFactory getContextNodeFactory() {
		return (HibernatePersistenceXmlContextNodeFactory) super.getContextNodeFactory();
	}
	
	@Override
	protected void initializeProperties() {
		super.initializeProperties();
		this.hibernateProperties = this.getContextNodeFactory().buildHibernatePersistenceUnitProperties(this);
	}
	
	@Override
	public void propertyRemoved(String propertyName) {
		super.propertyRemoved(propertyName);
		this.hibernateProperties.propertyRemoved(propertyName);
	}
	
	@Override
	public void propertyValueChanged(String propertyName, String newValue) {
		super.propertyValueChanged(propertyName, newValue);
		this.hibernateProperties.propertyValueChanged(propertyName, newValue);
	}

	// ******** Behavior *********
	public HibernatePersistenceUnitProperties getHibernatePersistenceUnitProperties() {
		return this.hibernateProperties;
	}

	// ********** Validation ***********************************************
	@Override
	public void validate(List<IMessage> messages, IReporter reporter) {
		super.validate(messages, reporter);
		validateHibernateConfigurationFileExists(messages, reporter);
	}

	protected void validateHibernateConfigurationFileExists(List<IMessage> messages, IReporter reporter) {
		String configFile = hibernateProperties.getConfigurationFile();
		if (configFile != null && configFile.length() > 0){
			IPath path = new Path(configFile);
				
			if (new File(path.toOSString()).exists()) return;
			
			try {
				IJavaProject jp = getJpaProject().getJavaProject();
				IPackageFragmentRoot[] pfrs = jp.getPackageFragmentRoots();
				for (int i = 0; i < pfrs.length; i++) {
					if (pfrs[i].isArchive()) continue;
					if (((IContainer)pfrs[i].getResource()).findMember(path) != null){
						return;
					}
				}
			} catch (JavaModelException e) {
				HibernateJptPlugin.logException(e);
			}

		    IResource res= ResourcesPlugin.getWorkspace().getRoot().findMember(path);
		    if (res != null) {
		        int resType= res.getType();
		        if (resType != IResource.FILE) {
		        	Property prop = getProperty(BasicHibernateProperties.HIBERNATE_CONFIG_FILE);
	            	IMessage message = new LocalMessage(IMessage.HIGH_SEVERITY, 
	            			NOT_A_FILE, new String[]{configFile}, getResource());
	            	message.setLineNo(prop.getValidationTextRange().getLineNumber());
	            	messages.add(message);					
		        }
		    } else {
		    	Property prop = getProperty(BasicHibernateProperties.HIBERNATE_CONFIG_FILE);
	        	IMessage message = new LocalMessage(IMessage.HIGH_SEVERITY, 
            			CONFIG_FILE_NOT_FOUND, new String[]{configFile}, getResource());
	        	message.setLineNo(prop.getValidationTextRange().getLineNumber());
            	messages.add(message);	
		    }
		}
	}

	/**
	 * Hack class needed to make JPA/Validation API pick up our classloader instead of its own.
	 * 
	 * @author max
	 *
	 */
	static public class LocalMessage extends Message {

		public LocalMessage(int severity, String message,
				String[] strings, Object resource) {
			super(Messages.class.getName(), severity, message, strings, resource);
		}
	}
	
}
