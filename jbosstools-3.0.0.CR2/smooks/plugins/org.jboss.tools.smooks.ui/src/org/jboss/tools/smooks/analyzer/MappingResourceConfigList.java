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
 *         Date : Sep 10, 2008
 */
public class MappingResourceConfigList {
	private List<MappingModel> mappingModelList = new ArrayList<MappingModel>();
	private List<ResourceConfigType> relationgResourceConfigList = new ArrayList<ResourceConfigType>();

	public List<MappingModel> getMappingModelList() {
		return mappingModelList;
	}

	public void setMappingModelList(List<MappingModel> mappingModelList) {
		this.mappingModelList = mappingModelList;
	}

	public List<ResourceConfigType> getGraphRenderResourceConfigList() {
		return relationgResourceConfigList;
	}

	public void addResourceConfig(ResourceConfigType resourceConfig) {
		if (!this.getGraphRenderResourceConfigList().contains(resourceConfig))
			this.getGraphRenderResourceConfigList().add(resourceConfig);
	}

	public void removeResourceConfig(ResourceConfigType resourceConfig) {
		this.getGraphRenderResourceConfigList().remove(resourceConfig);
	}

	public void setRelationgResourceConfigList(
			List<ResourceConfigType> relationgResourceConfigList) {
		this.relationgResourceConfigList = relationgResourceConfigList;
	}

	public static MappingResourceConfigList createEmptyList() {
		MappingResourceConfigList list = new MappingResourceConfigList();
		list.setMappingModelList(new ArrayList());
		return list;
	}
}
