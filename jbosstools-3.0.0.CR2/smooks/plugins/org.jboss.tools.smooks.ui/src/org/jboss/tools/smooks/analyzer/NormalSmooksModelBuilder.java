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

import org.jboss.tools.smooks.model.ResourceConfigType;
import org.jboss.tools.smooks.model.ResourceType;
import org.jboss.tools.smooks.model.SmooksResourceListType;
import org.jboss.tools.smooks.model.util.SmooksModelConstants;

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
		NormalSmooksModelPackage modelPackage = new NormalSmooksModelPackage(
				list);
		if (list != null) {
			List resourceConfigList = list.getAbstractResourceConfig();
			for (Iterator iterator = resourceConfigList.iterator(); iterator
					.hasNext();) {
				Object resourceConfig = iterator.next();
				if (resourceConfig instanceof ResourceConfigType) {
					this.processResouceConfig((ResourceConfigType)resourceConfig, modelPackage);
				}
			}
			if (resourceConfigList.isEmpty()) {

			}
		}
		return modelPackage;
	}

	protected void processResouceConfig(ResourceConfigType config,
			NormalSmooksModelPackage modelPackage) {
		// if (isBeanPopulator(config)) {
		// modelPackage.getBeanPopulatorResourceConfigList().add(config);
		// }
		// if (isDateConfig(config)) {
		// modelPackage.getDateResourceConfigList().add(config);
		// }
		// if (isSmooksTransformType(config)) {
		// modelPackage.setSmooksTransformTypeResourceConfig(config);
		// }
	}

	public static boolean isSmooksTransformType(ResourceConfigType config) {
		if (config == null)
			return false;
		String selector = config.getSelector();
		if (selector != null) {
			return SmooksModelConstants.GLOBAL_PARAMETERS.equals(selector
					.trim());
		}
		return false;
	}

	public static boolean isDateConfig(ResourceConfigType config) {
		if (config == null)
			return false;
		ResourceType resource = config.getResource();
		if (resource != null) {
			String r = resource.getStringValue();
			if (r != null) {
				for (int i = 0; i < SmooksModelConstants.DECODER_CLASSES.length; i++) {
					String decoder = SmooksModelConstants.DECODER_CLASSES[i];
					if (r.trim().equals(decoder)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static boolean isBeanPopulator(ResourceConfigType config) {
		if (config == null)
			return false;
		ResourceType resource = config.getResource();
		if (resource != null) {
			String resourceString = resource.getStringValue();
			if (resourceString != null)
				return SmooksModelConstants.BEAN_POPULATOR
						.equals(resourceString.trim());
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
