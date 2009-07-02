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
import org.jboss.tools.smooks.configuration.editors.GraphicsConstants;
import org.jboss.tools.smooks.configuration.editors.csv.CSVInputDataWizard;
import org.jboss.tools.smooks.configuration.editors.javabean.JavabeanContentProvider;
import org.jboss.tools.smooks.configuration.editors.javabean.JavabeanStrucutredDataWizard;
import org.jboss.tools.smooks.configuration.editors.javabean.JavabeanlabelProvider;
import org.jboss.tools.smooks.configuration.editors.json.JsonDataWizard;
import org.jboss.tools.smooks.configuration.editors.xml.XMLStructuredDataContentProvider;
import org.jboss.tools.smooks.configuration.editors.xml.XMLStructuredDataLabelProvider;
import org.jboss.tools.smooks.configuration.editors.xml.XMLStructuredDataWizard;
import org.jboss.tools.smooks.configuration.editors.xml.XSDStructuredDataWizard;
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
		IStructuredDataSelectionWizard wizard = initor.getStructuredDataLoadWizard();
		return wizard;
		// if(wizard != null){
		// try {
		// return wizard.getClass().newInstance();
		// } catch (Throwable t){
		// t.printStackTrace();
		// return wizard;
		// }
		// }
		// return null;
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

		// for json
		BaseViewerInitor jsonViewerInitor = new BaseViewerInitor();
		String name = "Json";
		String description = "Select Json data file as the input data.";
		String iconPath = null;
		String typeID = SmooksModelUtils.INPUT_TYPE_JSON;

		jsonViewerInitor.setName(name);
		jsonViewerInitor.setDescription(description);
		jsonViewerInitor.setWizardIconPath(iconPath);
		jsonViewerInitor.setTypeID(typeID);
		jsonViewerInitor.setLabelProvider(new XMLStructuredDataLabelProvider());
		jsonViewerInitor.setTreeContentProvider(new XMLStructuredDataContentProvider());
		jsonViewerInitor.setStructuredDataLoadWizard(new JsonDataWizard());
		// jsonViewerInitor.setWizardIconPath(GraphicsConstants.IMAGE_JAVA_FILE);
		map.put(typeID, jsonViewerInitor);

		// for java
		BaseViewerInitor javabeanViewerInitor = new BaseViewerInitor();
		name = "Java";
		description = "Select a Java type(class,interface) as the input data.";
		iconPath = null;
		typeID = SmooksModelUtils.INPUT_TYPE_JAVA;

		javabeanViewerInitor.setName(name);
		javabeanViewerInitor.setDescription(description);
		javabeanViewerInitor.setWizardIconPath(iconPath);
		javabeanViewerInitor.setTypeID(typeID);
		javabeanViewerInitor.setLabelProvider(new JavabeanlabelProvider());
		javabeanViewerInitor.setTreeContentProvider(new JavabeanContentProvider());
		javabeanViewerInitor.setStructuredDataLoadWizard(new JavabeanStrucutredDataWizard());
		javabeanViewerInitor.setWizardIconPath(GraphicsConstants.IMAGE_JAVA_FILE);
		map.put(typeID, javabeanViewerInitor);

		// for XML

		BaseViewerInitor xmlViewerInitor = new BaseViewerInitor();
		name = "XML";
		description = "Select a XML file to be the input data. ";
		iconPath = null;
		typeID = SmooksModelUtils.INPUT_TYPE_XML;

		xmlViewerInitor.setName(name);
		xmlViewerInitor.setDescription(description);
		xmlViewerInitor.setWizardIconPath(iconPath);
		xmlViewerInitor.setTypeID(typeID);
		xmlViewerInitor.setLabelProvider(new XMLStructuredDataLabelProvider());
		xmlViewerInitor.setTreeContentProvider(new XMLStructuredDataContentProvider());
		xmlViewerInitor.setStructuredDataLoadWizard(new XMLStructuredDataWizard());
		xmlViewerInitor.setWizardIconPath(GraphicsConstants.IMAGE_XML_FILE);
		map.put(typeID, xmlViewerInitor);

		// for XSD

		BaseViewerInitor xsdViewerInitor = new BaseViewerInitor();
		name = "XSD/WSDL";
		description = "Select a XSD file to be the input data. Need to figure out a Root Element. ";
		iconPath = null;
		typeID = SmooksModelUtils.INPUT_TYPE_XSD;

		xsdViewerInitor.setName(name);
		xsdViewerInitor.setDescription(description);
		xsdViewerInitor.setWizardIconPath(iconPath);
		xsdViewerInitor.setTypeID(typeID);
		xsdViewerInitor.setLabelProvider(new XMLStructuredDataLabelProvider());
		xsdViewerInitor.setTreeContentProvider(new XMLStructuredDataContentProvider());
		xsdViewerInitor.setStructuredDataLoadWizard(new XSDStructuredDataWizard());
		xsdViewerInitor.setWizardIconPath(GraphicsConstants.IMAGE_XSD_FILE);
		map.put(typeID, xsdViewerInitor);

		// for Csv
		BaseViewerInitor csvViewerInitor = new BaseViewerInitor();
		name = "CSV";
		description = "Select CSV data file as the input data.";
		iconPath = null;
		typeID = SmooksModelUtils.INPUT_TYPE_CSV;

		csvViewerInitor.setName(name);
		csvViewerInitor.setDescription(description);
		csvViewerInitor.setWizardIconPath(iconPath);
		csvViewerInitor.setTypeID(typeID);
		csvViewerInitor.setLabelProvider(new XMLStructuredDataLabelProvider());
		csvViewerInitor.setTreeContentProvider(new XMLStructuredDataContentProvider());
		csvViewerInitor.setStructuredDataLoadWizard(new CSVInputDataWizard());
		// jsonViewerInitor.setWizardIconPath(GraphicsConstants.IMAGE_JAVA_FILE);
		map.put(typeID, csvViewerInitor);

		return map;
	}
}
