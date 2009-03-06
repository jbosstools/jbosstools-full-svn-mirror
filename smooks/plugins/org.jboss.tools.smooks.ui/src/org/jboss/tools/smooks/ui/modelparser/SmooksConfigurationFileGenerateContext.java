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
package org.jboss.tools.smooks.ui.modelparser;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.smooks.graphical.GraphInformations;
import org.jboss.tools.smooks.graphical.Param;
import org.jboss.tools.smooks.model.SmooksResourceListType;
import org.jboss.tools.smooks.ui.editors.SmooksGraphicalFormPage;
import org.jboss.tools.smooks.ui.gef.model.GraphRootModel;

/**
 * @author Dart Peng
 * @Date Aug 18, 2008
 */
public class SmooksConfigurationFileGenerateContext {

	protected String sourceDataTypeID = null;
	protected String targetDataTypeID = null;
	protected GraphRootModel dataMappingRootModel;
	protected SmooksResourceListType smooksResourceListModel;
	protected List generatorResourceList = new ArrayList();
	private Properties properties = new Properties();
	protected IFile smooksConfigFile = null;
	protected EditingDomain domain;
	protected SmooksGraphicalFormPage smooksGraphcalPage = null;
	protected EditDomain gefDomain;
	protected GraphInformations graphInformations = null;
	
	public GraphInformations getGraphInformations() {
		return graphInformations;
	}

	public void setGraphInformations(GraphInformations graphInformations) {
		this.graphInformations = graphInformations;
	}

	public SmooksGraphicalFormPage getSmooksGraphcalPage() {
		return smooksGraphcalPage;
	}

	public void setSmooksGraphcalPage(SmooksGraphicalFormPage smooksGraphcalPage) {
		this.smooksGraphcalPage = smooksGraphcalPage;
	}

	public EditDomain getGefDomain() {
		return gefDomain;
	}

	public void setGefDomain(EditDomain gefDomain) {
		this.gefDomain = gefDomain;
	}

	protected ITreeContentProvider sourceViewerProvider;
	
	protected ITreeContentProvider targetViewerProvider;
	
	protected LabelProvider sourceViewerLabelProvider;
	
	protected LabelProvider targetViewerLabelProvider;

	public LabelProvider getSourceViewerLabelProvider() {
		return sourceViewerLabelProvider;
	}

	public void setSourceViewerLabelProvider(LabelProvider sourceViewerLabelProvider) {
		this.sourceViewerLabelProvider = sourceViewerLabelProvider;
	}

	public LabelProvider getTargetViewerLabelProvider() {
		return targetViewerLabelProvider;
	}

	public void setTargetViewerLabelProvider(LabelProvider targetViewerLabelProvider) {
		this.targetViewerLabelProvider = targetViewerLabelProvider;
	}

	public ITreeContentProvider getSourceViewerProvider() {
		return sourceViewerProvider;
	}

	public void setSourceViewerContentProvider(ITreeContentProvider sourceViewerProvider) {
		this.sourceViewerProvider = sourceViewerProvider;
	}

	public ITreeContentProvider getTargetViewerProvider() {
		return targetViewerProvider;
	}

	public void setTargetViewerContentProvider(ITreeContentProvider targetViewerProvider) {
		this.targetViewerProvider = targetViewerProvider;
	}

	private Shell shell;

	protected String smooksType = null;

	/**
	 * @return the sourceDataTypeID
	 */
	public String getSourceDataTypeID() {
		return sourceDataTypeID;
	}

	/**
	 * @param sourceDataTypeID
	 *            the sourceDataTypeID to set
	 */
	public void setSourceDataTypeID(String sourceDataTypeID) {
		this.sourceDataTypeID = sourceDataTypeID;
	}

	/**
	 * @return the targetDataTypeID
	 */
	public String getTargetDataTypeID() {
		return targetDataTypeID;
	}

	/**
	 * @param targetDataTypeID
	 *            the targetDataTypeID to set
	 */
	public void setTargetDataTypeID(String targetDataTypeID) {
		this.targetDataTypeID = targetDataTypeID;
	}

	/**
	 * @return the dataMappingRootModel
	 */
	public GraphRootModel getGraphicalRootModel() {
		return dataMappingRootModel;
	}

	/**
	 * @param dataMappingRootModel
	 *            the dataMappingRootModel to set
	 */
	public void setDataMappingRootModel(GraphRootModel dataMappingRootModel) {
		this.dataMappingRootModel = dataMappingRootModel;
	}

	/**
	 * @return the smooksType
	 */
	public String getSmooksType() {
		return smooksType;
	}

	/**
	 * @param smooksType
	 *            the smooksType to set
	 */
	public void setSmooksType(String smooksType) {
		this.smooksType = smooksType;
	}

	public SmooksResourceListType getSmooksResourceListModel() {
		return smooksResourceListModel;
	}

	public void setSmooksResourceListModel(
			SmooksResourceListType smooksResourceListModel) {
		this.smooksResourceListModel = smooksResourceListModel;
	}

	public void addParam(String name, String value) {

	}

	public void addParam(Param param) {

	}

	public EditingDomain getDomain() {
		return domain;
	}

	public void setDomain(EditingDomain domain) {
		this.domain = domain;
	}

	public List getGeneratorResourceList() {
		return generatorResourceList;
	}

	public void setGeneratorResourceList(List generatorResourceList) {
		this.generatorResourceList = generatorResourceList;
	}

	public void putProperty(String paramName, String paramValue) {
		properties.setProperty(paramName, paramValue);
	}

	public String getProperty(String paramName) {
		return properties.getProperty(paramName);
	}

	public void cleanProperties() {
		properties.clear();
	}

	public IFile getSmooksConfigFile() {
		return smooksConfigFile;
	}

	public void setSmooksConfigFile(IFile smooksConfigFile) {
		this.smooksConfigFile = smooksConfigFile;
	}

	public Properties getProperties() {
		return properties;
	}

	public void addProperties(Properties properties) {
		if (properties == null)
			return;
		Enumeration keys = properties.keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			this.properties.put(key, properties.get(key));
		}
	}

	public Shell getShell() {
		return shell;
	}

	public void setShell(Shell shell) {
		this.shell = shell;
	}

}
