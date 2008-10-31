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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.jboss.tools.smooks.utils.SmooksExtensionPointConstants;

/**
 * @author Dart Peng
 * @Date Aug 5, 2008
 */
public class ViewerInitorStore {
	protected static ViewerInitorStore instance = null;

	protected HashMap<String, IViewerInitor> initorMap = null;

	protected ViewerInitorStore() {

	}
	
	public Collection<IViewerInitor> getViewerInitorCollection(){
		Map<String,IViewerInitor> map = this.getInitorMap();
		if(map != null){
			return map.values();
		}
		return null;
	}
	
	public IStructuredDataCreationWizard getStructuredDataCreationWizard(String typeID) {
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

//	protected ILabelProvider createLabelProvider(BaseViewerInitor initor,
//			IConfigurationElement element) {
//		try {
//			initor
//					.setLabelProvider();
//		} catch (CoreException e) {
//			// ignore
//		}
//		return initor.getLabelProvider();
//	}
//
//	protected ILabelProvider createTreeContentProvider(BaseViewerInitor initor,
//			IConfigurationElement element) {
//		try {
//			initor
//					.setTreeContentProvider();
//		} catch (CoreException e) {// ignore
//		}
//		return initor.getLabelProvider();
//	}
//
//	protected ILabelProvider createCreationWizard(BaseViewerInitor initor,
//			IConfigurationElement element) {
//		try {
//			initor
//					.setStructuredDataLoadWizard);
//		} catch (CoreException e) {// ignore
//		}
//		return initor.getLabelProvider();
//	}

	protected HashMap<String, IViewerInitor> createNewInitorMap() {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint ep = registry
				.getExtensionPoint(SmooksExtensionPointConstants.EXTENTION_POINT_VIEWER_INITOR);
		HashMap<String, IViewerInitor> map = new HashMap<String, IViewerInitor>();
		if (ep == null)
			return null;
		IConfigurationElement[] elements = ep.getConfigurationElements();
		for (int i = 0; i < elements.length; i++) {
			IConfigurationElement element = elements[i];

			String typeID = element
					.getAttribute(SmooksExtensionPointConstants.EXTENTION_POINT_ATTRIBUTE_TYPE_ID);
			if (typeID == null) {
				continue;
			}
			String name = element.getAttribute(SmooksExtensionPointConstants.EXTENTION_POINT_ATTRIBUTE_NAME);
			
			String iconPath = element.getAttribute(SmooksExtensionPointConstants.EXTENTION_POINT_ATTRIBUTE_ICON);
			
			String description = element.getAttribute(SmooksExtensionPointConstants.EXTENTION_POINT_ATTRIBUTE_DESCRIPTION);
			BaseViewerInitor initor = new BaseViewerInitor();

			initor.setName(name);
			initor.setDescription(description);
			initor.setWizardIconPath(iconPath);
			
			initor.setTypeID(typeID);

			initor.setConfigurationElement(element);
			map.put(typeID, initor);
		}
		if (!map.isEmpty()) {
			return map;
		} else {
			map = null;
			return null;
		}
	}
}
