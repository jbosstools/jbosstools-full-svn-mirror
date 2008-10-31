/*******************************************************************************
  * Copyright (c) 2007-2008 Red Hat, Inc.
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

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jpt.core.context.persistence.Persistence;
import org.eclipse.jpt.core.context.persistence.Property;
import org.eclipse.jpt.core.internal.context.persistence.GenericPersistenceUnit;
import org.eclipse.jpt.core.resource.persistence.XmlPersistenceUnit;
import org.eclipse.wst.validation.internal.core.Message;
import org.eclipse.wst.validation.internal.provisional.core.IMessage;
import org.jboss.tools.hibernate.jpt.core.internal.context.basic.BasicHibernateProperties;

/**
 * @author Dmitry Geraskov
 *
 */
public class HibernatePersistenceUnit extends GenericPersistenceUnit 
	implements Messages{
	
	private HibernateProperties hibernateProperties;

	/**
	 * @param parent
	 * @param persistenceUnit
	 */
	public HibernatePersistenceUnit(Persistence parent,
			XmlPersistenceUnit persistenceUnit) {
		super(parent, persistenceUnit);
	}
	
	protected void initialize(XmlPersistenceUnit xmlPersistenceUnit) {
		super.initialize(xmlPersistenceUnit);
		this.hibernateProperties = new HibernateJpaProperties(this);
	}

	// ******** Behavior *********
	public BasicHibernateProperties getBasicProperties() {
		return this.hibernateProperties.getBasicHibernate();
	}

	// ********** Validation ***********************************************	
	@Override
	public void addToMessages(List<IMessage> messages) {
		super.addToMessages(messages);
		addFileNotExistsMessages(messages);
	}	
	
	protected void addFileNotExistsMessages(List<IMessage> messages) {
		String configFile = getBasicProperties().getConfigurationFile();
		if (configFile != null && configFile.length() > 0){
			IPath path = new Path(configFile);
				
			if (new File(path.toOSString()).exists()) return;


		    IResource res= ResourcesPlugin.getWorkspace().getRoot().findMember(path);
		    if (res != null) {
		        int resType= res.getType();
		        if (resType != IResource.FILE) {
		        	Property prop = getProperty(BasicHibernateProperties.HIBERNATE_CONFIG_FILE);
	            	IMessage message = new HibernateMessage(IMessage.HIGH_SEVERITY, 
	            			NOT_A_FILE, new String[]{configFile}, getResource());
	            	message.setLineNo(prop.getValidationTextRange().getLineNumber());
	            	messages.add(message);					
		        }
		    } else {
		    	Property prop = getProperty(BasicHibernateProperties.HIBERNATE_CONFIG_FILE);
	        	IMessage message = new HibernateMessage(IMessage.HIGH_SEVERITY, 
            			FILE_NOT_FOUND, new String[]{configFile}, getResource());
	        	message.setLineNo(prop.getValidationTextRange().getLineNumber());
            	messages.add(message);	
		    }
		}
	}
}

/*Fixes the problem with class loader*/
class HibernateMessage extends Message {
	
	/**
	 * @param aSeverity
	 * @param anId
	 * @param aParams
	 * @param aTargetObject
	 */
	public HibernateMessage(int aSeverity, String anId, String[] aParams, Object aTargetObject) {
		super(Messages.class.getName(), aSeverity, anId,  aParams, aTargetObject);
	}
	
}
