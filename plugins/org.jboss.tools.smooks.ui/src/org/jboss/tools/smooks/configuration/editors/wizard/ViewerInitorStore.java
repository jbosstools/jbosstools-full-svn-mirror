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
package org.jboss.tools.smooks.configuration.editors.wizard;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.jboss.tools.smooks.configuration.editors.javabean.JavabeanContentProvider;
import org.jboss.tools.smooks.configuration.editors.javabean.JavabeanlabelProvider;
import org.jboss.tools.smooks.configuration.editors.javabean.JavabeanStrucutredDataWizard;
import org.jboss.tools.smooks10.model.smooks.util.SmooksModelUtils;

/**
 * @author Dart Peng
 * @Date Aug 5, 2008
 */
public class ViewerInitorStore {
	protected static ViewerInitorStore instance = null;

	protected HashMap<String, IViewerInitor> initorMap = null;

	protected ViewerInitorStore() {

	}

	public Collection<IViewerInitor> getViewerInitorCollection() {
		Map<String, IViewerInitor> map = this.getInitorMap();
		if (map != null) {
			return map.values();
		}
		return null;
	}

	public IStructuredDataSelectionWizard getStructuredDataCreationWizard(String typeID) {
		if (this.getInitorMap() == null)
			return null;
		IViewerInitor initor = this.getInitorMap().get(typeID);
		if (initor == null)
			return null;
		return initor.getStructuredDataLoadWizard();
	}

	public ILabelProvider getLabelProvider(String typeID) {
		if (this.getInitorMap() == null)
			return null;
		IViewerInitor initor = this.getInitorMap().get(typeID);
		if (initor == null)
			return null;
		return initor.getLabelProvider();
	}

	public ITreeContentProvider getTreeCotentProvider(String typeID) {
		if (this.getInitorMap() == null)
			return null;
		IViewerInitor initor = this.getInitorMap().get(typeID);
		if (initor == null)
			return null;
		return initor.getTreeContentProvider();
	}

	public synchronized static ViewerInitorStore getInstance() {
		if (instance == null) {
			instance = new ViewerInitorStore();
		}
		return instance;
	}

	/**
	 * @return the initorMap
	 */
	protected HashMap<String, IViewerInitor> getInitorMap() {
		if (initorMap == null) {
			initorMap = createNewInitorMap();
		}
		return initorMap;
	}

	// protected ILabelProvider createLabelProvider(BaseViewerInitor initor,
	// IConfigurationElement element) {
	// try {
	// initor
	// .setLabelProvider();
	// } catch (CoreException e) {
	// // ignore
	// }
	// return initor.getLabelProvider();
	// }
	//
	// protected ILabelProvider createTreeContentProvider(BaseViewerInitor
	// initor,
	// IConfigurationElement element) {
	// try {
	// initor
	// .setTreeContentProvider();
	// } catch (CoreException e) {// ignore
	// }
	// return initor.getLabelProvider();
	// }
	//
	// protected ILabelProvider createCreationWizard(BaseViewerInitor initor,
	// IConfigurationElement element) {
	// try {
	// initor
	// .setStructuredDataLoadWizard);
	// } catch (CoreException e) {// ignore
	// }
	// return initor.getLabelProvider();
	// }

	protected HashMap<String, IViewerInitor> createNewInitorMap() {
		HashMap<String, IViewerInitor> map = new HashMap<String, IViewerInitor>();
		BaseViewerInitor javabeanViewerInitor = new BaseViewerInitor();
		String name = "Java";
		String description = "Java class";
		String iconPath = null;
		String typeID = SmooksModelUtils.INPUT_TYPE_JAVA;

		javabeanViewerInitor.setName(name);
		javabeanViewerInitor.setDescription(description);
		javabeanViewerInitor.setWizardIconPath(iconPath);
		javabeanViewerInitor.setTypeID(typeID);
		javabeanViewerInitor.setLabelProvider(new JavabeanlabelProvider());
		javabeanViewerInitor.setTreeContentProvider(new JavabeanContentProvider());
		javabeanViewerInitor.setStructuredDataLoadWizard(new JavabeanStrucutredDataWizard());
		map.put(typeID, javabeanViewerInitor);
		return map;
	}
}
