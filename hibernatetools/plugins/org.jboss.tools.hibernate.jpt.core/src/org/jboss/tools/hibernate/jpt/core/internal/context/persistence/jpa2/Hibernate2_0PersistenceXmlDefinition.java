/*******************************************************************************
  * Copyright (c) 2010 Red Hat, Inc.
  * Distributed under license by Red Hat, Inc. All rights reserved.
  * This program is made available under the terms of the
  * Eclipse Public License v1.0 which accompanies this distribution,
  * and is available at http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributor:
  *     Red Hat, Inc. - initial API and implementation
  ******************************************************************************/
package org.jboss.tools.hibernate.jpt.core.internal.context.persistence.jpa2;

import org.eclipse.emf.ecore.EFactory;
import org.eclipse.jpt.core.JpaResourceType;
import org.eclipse.jpt.core.JptCorePlugin;
import org.eclipse.jpt.core.context.persistence.PersistenceXmlContextNodeFactory;
import org.eclipse.jpt.core.context.persistence.PersistenceXmlDefinition;
import org.eclipse.jpt.core.internal.context.persistence.AbstractPersistenceXmlDefinition;
import org.eclipse.jpt.core.resource.persistence.PersistenceFactory;

/**
 * @author Dmitry Geraskov
 *
 */
public class Hibernate2_0PersistenceXmlDefinition extends
		AbstractPersistenceXmlDefinition {

	// singleton
	private static final PersistenceXmlDefinition INSTANCE = 
			new Hibernate2_0PersistenceXmlDefinition();
	
	
	/**
	 * Return the singleton
	 */
	public static PersistenceXmlDefinition instance() {
		return INSTANCE;
	}
	
	
	/**
	 * Enforce singleton usage
	 */
	private Hibernate2_0PersistenceXmlDefinition() {
		super();
	}
	
	
	public EFactory getResourceNodeFactory() {
		return PersistenceFactory.eINSTANCE;
	}
	
	@Override
	protected PersistenceXmlContextNodeFactory buildContextNodeFactory() {
		return new Hibernate2_0PersistenceXmlContextNodeFactory();
	}
	
	public JpaResourceType getResourceType() {
		return JptCorePlugin.PERSISTENCE_XML_1_0_RESOURCE_TYPE;
	}

}
