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

import org.eclipse.emf.edit.domain.EditingDomain;
import org.jboss.tools.smooks.model.ResourceConfigType;
import org.jboss.tools.smooks.model.SmooksResourceListType;

/**
 * @author Dart Peng<br>
 *         Date : Sep 11, 2008
 */
public class ResourceConfigEraser {
	public void cleanMappingResourceConfig(SmooksResourceListType list,
			MappingResourceConfigList mappingResourceConfigList,
			EditingDomain domain) {
		if (mappingResourceConfigList != null) {
			List<ResourceConfigType> resourceConfigList = mappingResourceConfigList
					.getGraphRenderResourceConfigList();
			for (Iterator iterator = resourceConfigList.iterator(); iterator
					.hasNext();) {
				ResourceConfigType resourceConfigType = (ResourceConfigType) iterator
						.next();
				list.getAbstractResourceConfig().remove(resourceConfigType);
			}
		}
	}
}
