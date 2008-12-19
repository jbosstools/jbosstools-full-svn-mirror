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
//		if (SmooksModelUtils.isBeanPopulatorResource(type)) {
//			BeanPopulatorWarrper p = new BeanPopulatorWarrper(type);
//			return p;
//		}
		if(SmooksModelUtils.isDateTypeSelector(type)){
			DateTypeWarrper warrper = new DateTypeWarrper(type);
			return warrper;
		}
		if(SmooksModelUtils.isFilePathResourceConfig(type) || SmooksModelUtils.isInnerFileContents(type)){
			DocumentSelectionWarrper warrper = new DocumentSelectionWarrper(type);
			return warrper;
		}
		return new NormalResourceConfigWarpper(type);
	}

}
