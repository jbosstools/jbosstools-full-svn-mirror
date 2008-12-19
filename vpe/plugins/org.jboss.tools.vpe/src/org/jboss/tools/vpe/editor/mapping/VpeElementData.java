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
 *         keep information about element
 */
public class VpeElementData {

	/**
	 * list of node data
	 * 
	 * keep information about output nodes ( one element can contain some nodes
	 * )
	 */
	private List<NodeData> nodesData;

	/**
	 * get nodes data
	 * 
	 * @return
	 */
	public List<NodeData> getNodesData() {
		return nodesData;
	}

	/**
	 * set nodes data
	 * 
	 * @param nodesData
	 */
	public void setNodesData(List<NodeData> nodesData) {
		this.nodesData = nodesData;
	}

	/**
	 * add <code>VpeAttributeData</code>
	 * 
	 * @param attributeData
	 */
	public void addNodeData(NodeData nodeData) {

		if (nodesData == null)
			nodesData = new ArrayList<NodeData>();

		nodesData.add(nodeData);

	}

}
