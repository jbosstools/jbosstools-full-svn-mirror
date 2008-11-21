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
package org.jboss.tools.smooks.ui.editors;

import org.jboss.tools.smooks.model.ResourceConfigType;
import org.jboss.tools.smooks.model.ResourceType;
import org.jboss.tools.smooks.model.util.SmooksModelConstants;
import org.jboss.tools.smooks.model.util.SmooksModelUtils;
import org.jboss.tools.smooks.ui.BeanPopulatorWarrper;
import org.jboss.tools.smooks.ui.DateTypeWarrper;
import org.jboss.tools.smooks.ui.DocumentSelectionWarrper;
import org.jboss.tools.smooks.ui.ResourceConfigWarrper;


/**
 * @author Dart Peng<br>
 *         Date : Sep 16, 2008
 */
public class ResourceConfigWarrperFactory {


	public static ResourceConfigWarrper createResourceConfigWarrper(
			ResourceConfigType type) {
		if (isBeanPopulatorResource(type)) {
			BeanPopulatorWarrper p = new BeanPopulatorWarrper(type);
			return p;
		}
		if(isDateTypeSelector(type)){
			DateTypeWarrper warrper = new DateTypeWarrper(type);
			return warrper;
		}
		if(SmooksModelUtils.isFilePathResourceConfig(type)){
			DocumentSelectionWarrper warrper = new DocumentSelectionWarrper(type);
			return warrper;
		}
		return null;
	}

	public static boolean isBeanPopulatorResource(ResourceConfigType type) {
		ResourceType resource = type.getResource();
		if (resource == null)
			return false;
		String value = resource.getValue();
		if(value != null) value = value.trim();
		if (SmooksModelConstants.BEAN_POPULATOR.equals(value)) {
			return true;
		}
		return false;
	}

	public static boolean isDateTypeSelector(ResourceConfigType type) {
		ResourceType resource = type.getResource();
		if (resource == null)
			return false;
		String value = resource.getValue();
		if(value != null) value = value.trim();
		for (int i = 0; i < SmooksModelConstants.DECODER_CLASSES.length; i++) {
			String decoderClass = SmooksModelConstants.DECODER_CLASSES[i];
			if(decoderClass.equals(value)){
				return true;
			}
		}
		return false;
	}
}
