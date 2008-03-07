/******************************************************************************* 
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.editor.mapping;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Sergey Dzmitrovich
 * 
 * keep information about element
 */
public class VpeElementData {

	/**
	 * list of attribute data
	 * 
	 * keep information about output attributes
	 */
	private List<VpeAttributeData> attributesData;

	/**
	 * get attributes data
	 * 
	 * @return
	 */
	public List<VpeAttributeData> getAttributesData() {
		return attributesData;
	}

	/**
	 * set attributes data
	 * 
	 * @param attributesData
	 */
	public void setAttributesData(List<VpeAttributeData> attributesData) {
		this.attributesData = attributesData;
	}

	/**
	 * add <code>VpeAttributeData</code>
	 * @param attributeData
	 */
	public void addAttributeData(VpeAttributeData attributeData) {

		if (attributesData == null)
			attributesData = new ArrayList<VpeAttributeData>();

		attributesData.add(attributeData);

	}

}
