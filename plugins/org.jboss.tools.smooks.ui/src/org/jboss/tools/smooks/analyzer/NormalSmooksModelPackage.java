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
package org.jboss.tools.smooks.analyzer;

import java.util.ArrayList;
import java.util.List;

import org.jboss.tools.smooks.model.ResourceConfigType;

/**
 * @author Dart Peng<br>
 *         Date : Sep 11, 2008
 */
public class NormalSmooksModelPackage {
	private List<ResourceConfigType> dateResourceConfigList = new ArrayList<ResourceConfigType>();

	private List<ResourceConfigType> beanPopulatorResourceConfigList = new ArrayList<ResourceConfigType>();

	private ResourceConfigType smooksTransformTypeResourceConfig;

	/**
	 * @return the dateResourceConfigList
	 */
	public List<ResourceConfigType> getDateResourceConfigList() {
		return dateResourceConfigList;
	}

	/**
	 * @param dateResourceConfigList the dateResourceConfigList to set
	 */
	public void setDateResourceConfigList(
			List<ResourceConfigType> dateResourceConfigList) {
		this.dateResourceConfigList = dateResourceConfigList;
	}

	/**
	 * @return the beanPopulatorResourceConfigList
	 */
	public List<ResourceConfigType> getBeanPopulatorResourceConfigList() {
		return beanPopulatorResourceConfigList;
	}

	/**
	 * @param beanPopulatorResourceConfigList the beanPopulatorResourceConfigList to set
	 */
	public void setBeanPopulatorResourceConfigList(
			List<ResourceConfigType> beanPopulatorResourceConfigList) {
		this.beanPopulatorResourceConfigList = beanPopulatorResourceConfigList;
	}

	/**
	 * @return the smooksTransformTypeResourceConfig
	 */
	public ResourceConfigType getSmooksTransformTypeResourceConfig() {
		return smooksTransformTypeResourceConfig;
	}

	/**
	 * @param smooksTransformTypeResourceConfig the smooksTransformTypeResourceConfig to set
	 */
	public void setSmooksTransformTypeResourceConfig(
			ResourceConfigType smooksTransformTypeResourceConfig) {
		this.smooksTransformTypeResourceConfig = smooksTransformTypeResourceConfig;
	}
	
	

}
