/*******************************************************************************
 * Copyright (c) 2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.hibernate.console.ext;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.osgi.util.NLS;
import org.hibernate.console.HibernateConsoleRuntimeException;
import org.hibernate.util.ReflectHelper;

/**
 * @author Dmitry Geraskov
 *
 */
public class HibernateExtensionDefinition {
	
	public static final String CLASSNAME = "classname"; //$NON-NLS-1$
	
	public static final String HIBERNATE_VERSION = "version"; //$NON-NLS-1$
	
	private final String classname;
	
	private final String hibernateVersion;
	
	private IConfigurationElement element;
	
	public HibernateExtensionDefinition(IConfigurationElement element) {
		this(element.getAttribute( CLASSNAME ),
			    element.getAttribute( HIBERNATE_VERSION ));
		this.element = element;
	}

	private HibernateExtensionDefinition(String classname, String hibernateVersion) {
		this.classname = classname;
		this.hibernateVersion = hibernateVersion;
	}
	
	//TODO do we need to create new instance every time?
	public HibernateExtension createHibernateExtensionInstance() {
		HibernateExtension hiberanteExtension = null;

		   try {
			  // hiberanteExtension = (HibernateExtension) ReflectHelper.classForName( classname ).newInstance();
			   hiberanteExtension = (HibernateExtension) element.createExecutableExtension(CLASSNAME);
		   }
		  /* catch (InstantiationException e) {
			   throw new HibernateConsoleRuntimeException(NLS.bind(
					   "Problem while creating hibernate extension instance {0}", classname));
		   }
		   catch (IllegalAccessException e) {
			   throw new HibernateConsoleRuntimeException(NLS.bind(
					   "Problem while creating hibernate extension instance {0}", classname));	}
		   catch (ClassNotFoundException e) {
			   throw new HibernateConsoleRuntimeException(NLS.bind(
					   "Problem while creating hibernate extension instance {0}", classname));
		   } */catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		   return hiberanteExtension;
		}

	/**
	 * @return the hibernateVersion
	 */
	public String getHibernateVersion() {
		return hibernateVersion;
	}
}
