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
import java.util.List;

import org.eclipse.emf.edit.domain.EditingDomain;
import org.jboss.tools.smooks.graphical.Param;
import org.jboss.tools.smooks.ui.gef.model.GraphRootModel;
import org.milyn.xsd.smooks.SmooksResourceListType;

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
	
	protected EditingDomain domain;
	
	protected String smooksType = null;
	/**
	 * @return the sourceDataTypeID
	 */
	public String getSourceDataTypeID() {
		return sourceDataTypeID;
	}
	/**
	 * @param sourceDataTypeID the sourceDataTypeID to set
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
	 * @param targetDataTypeID the targetDataTypeID to set
	 */
	public void setTargetDataTypeID(String targetDataTypeID) {
		this.targetDataTypeID = targetDataTypeID;
	}
	/**
	 * @return the dataMappingRootModel
	 */
	public GraphRootModel getDataMappingRootModel() {
		return dataMappingRootModel;
	}
	/**
	 * @param dataMappingRootModel the dataMappingRootModel to set
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
	 * @param smooksType the smooksType to set
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
	
	public void addParam(String name,String value){
		
	}
	
	public void addParam(Param param){
		
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
	
}
