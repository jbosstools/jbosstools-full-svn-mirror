/*******************************************************************************
 * Copyright (c) 2008 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.ui;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.jboss.tools.smooks.utils.SmooksExtensionPointConstants;

/**
 * @author Dart Peng
 * @Date Aug 6, 2008
 */
public class BaseViewerInitor implements IViewerInitor {
	protected String typeID = null;
	protected String name = "nonamed";
	protected String wizardIconPath = null;
	IConfigurationElement configurationElement = null;
	protected String description;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the treeContentProvider
	 */
	public ITreeContentProvider getTreeContentProvider() {
		try {
			return (ITreeContentProvider) configurationElement
					.createExecutableExtension(SmooksExtensionPointConstants.EXTENTION_POINT_ATTRIBUTE_CONTENT_PROVIDER);
		} catch (CoreException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @param treeContentProvider
	 *            the treeContentProvider to set
	 */

	/**
	 * @return the structuredDataLoadWizard
	 */
	public IStructuredDataCreationWizard getStructuredDataLoadWizard() {
		try {
			return ((IStructuredDataCreationWizard) configurationElement
					.createExecutableExtension(SmooksExtensionPointConstants.EXTENTION_POINT_ATTRIBUTE_CREATION_WIZARD));
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return null;
	}


	/**
	 * @return the typeID
	 */
	public String getTypeID() {
		return typeID;
	}

	/**
	 * @param typeID
	 *            the typeID to set
	 */
	public void setTypeID(String typeID) {
		this.typeID = typeID;
	}

	/**
	 * @return the labelProvider
	 */
	public ILabelProvider getLabelProvider() {
		try {
			return (ILabelProvider) configurationElement
			.createExecutableExtension(SmooksExtensionPointConstants.EXTENTION_POINT_ATTRIBUTE_LABEL_PROVIDER);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}


	/**
	 * @return the wizardIconPath
	 */
	public String getWizardIconPath() {
		return wizardIconPath;
	}

	/**
	 * @param wizardIconPath
	 *            the wizardIconPath to set
	 */
	public void setWizardIconPath(String wizardIconPath) {
		this.wizardIconPath = wizardIconPath;
	}

	public IConfigurationElement getConfigurationElement() {
		return configurationElement;
	}

	public void setConfigurationElement(
			IConfigurationElement configurationElement) {
		this.configurationElement = configurationElement;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
