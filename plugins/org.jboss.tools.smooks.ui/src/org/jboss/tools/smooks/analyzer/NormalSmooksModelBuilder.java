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

import java.util.Iterator;
import java.util.List;

import org.milyn.xsd.smooks.ResourceConfigType;
import org.milyn.xsd.smooks.ResourceType;
import org.milyn.xsd.smooks.SmooksResourceListType;

/**
 * @author Dart Peng<br>
 *         Date : Sep 11, 2008
 */
public class NormalSmooksModelBuilder {

	private static NormalSmooksModelBuilder instance = null;

	private NormalSmooksModelBuilder() {

	}

	public NormalSmooksModelPackage buildNormalSmooksModelPackage(
			SmooksResourceListType list) {
		NormalSmooksModelPackage modelPackage = new NormalSmooksModelPackage();
		if (list != null) {
			List resourceConfigList = list.getAbstractResourceConfig();
			for (Iterator iterator = resourceConfigList.iterator(); iterator
					.hasNext();) {
				ResourceConfigType resourceConfig = (ResourceConfigType) iterator
						.next();
				this.processResouceConfig(resourceConfig, modelPackage);
			}
		}
		return modelPackage;
	}

	protected void processResouceConfig(ResourceConfigType config,
			NormalSmooksModelPackage modelPackage) {
		if (isBeanPopulator(config)) {
			modelPackage.getBeanPopulatorResourceConfigList().add(config);
		}
		if (isDateConfig(config)) {
			modelPackage.getDateResourceConfigList().add(config);
		}
		if (isSmooksTransformType(config)) {
			modelPackage.setSmooksTransformTypeResourceConfig(config);
		}
	}

	protected boolean isSmooksTransformType(ResourceConfigType config) {
		return false;
	}

	protected boolean isDateConfig(ResourceConfigType config) {
		return false;
	}

	protected boolean isBeanPopulator(ResourceConfigType config) {
		ResourceType resource = config.getResource();
		if (resource != null) {
			String resourceString = resource.getValue();
			return NormalSmooksModelPackage.RESOURCE_CLASS_BEAN_POPULATOR
					.equals(resourceString);
		}
		return false;
	}

	/**
	 * @return the instace
	 */
	public synchronized static NormalSmooksModelBuilder getInstance() {
		if (instance == null) {
			instance = new NormalSmooksModelBuilder();
		}
		return instance;
	}
}
