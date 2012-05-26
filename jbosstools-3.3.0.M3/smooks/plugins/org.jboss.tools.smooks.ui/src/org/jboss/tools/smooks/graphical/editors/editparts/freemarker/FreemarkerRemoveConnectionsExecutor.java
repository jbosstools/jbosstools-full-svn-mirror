/*******************************************************************************
 * Copyright (c) 2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.graphical.editors.editparts.freemarker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jboss.tools.smooks.gef.model.AbstractSmooksGraphicalModel;
import org.jboss.tools.smooks.gef.tree.model.TreeNodeConnection;
import org.jboss.tools.smooks.gef.tree.model.TreeNodeModel;
import org.jboss.tools.smooks.graphical.editors.model.freemarker.FreemarkerTemplateConnection;
import org.jboss.tools.smooks.graphical.editors.model.freemarker.FreemarkerTemplateNodeGraphicalModel;
import org.jboss.tools.smooks.templating.template.Mapping;

/**
 * 
 * @author <a href="mailto:tom.fennelly@gmail.com">tom.fennelly@gmail.com</a>
 */
public class FreemarkerRemoveConnectionsExecutor {
	private List<TreeNodeConnection> relatedConnections = new ArrayList<TreeNodeConnection>();
	
	public List<TreeNodeConnection> removeMappingConnections(
			List<Mapping> removeMappings, AbstractSmooksGraphicalModel node) {
		if (removeMappings == null || removeMappings.isEmpty()) {
			return Collections.emptyList();
		}

		// Remove from all the children first...
		for (AbstractSmooksGraphicalModel child : node.getChildren()) {
			if (child instanceof TreeNodeModel) {
				relatedConnections.addAll(removeMappingConnections(
						removeMappings, (TreeNodeModel) child));
			}
		}

		// Now remove from this node...
		if (node.getTargetConnections() != null && !node.getTargetConnections().isEmpty()) {
			List<TreeNodeConnection> connectionsToRemove = new ArrayList<TreeNodeConnection>();
			for (TreeNodeConnection connection : node
					.getTargetConnections()) {
				Object connectionData = connection.getData();
				if (connectionData instanceof Mapping) {
					for (Mapping mapping : removeMappings) {
						if(mapping.getMappingNode() ==  ((Mapping)connectionData).getMappingNode() &&
								mapping.getSrcPath().equals(((Mapping)connectionData).getSrcPath())){
							connectionsToRemove.add(connection);
						}
					}
				}
			}
			return connectionsToRemove;
		}
		return Collections.emptyList();
	}
	
	public void execute(TreeNodeConnection connectionHandle, List<Mapping> removeMappings) {
		Object target = connectionHandle.getTargetNode();
		if (target instanceof FreemarkerTemplateNodeGraphicalModel) {
			if(removeMappings!=null){
				relatedConnections.clear();
				relatedConnections.addAll(removeMappingConnections(removeMappings, (FreemarkerTemplateNodeGraphicalModel)target));
				for (TreeNodeConnection con : relatedConnections) {
					con.disconnect();
				}
			}
		}
	}

	/**
	 * @return
	 */
	public List<TreeNodeConnection> getRelatedConnections() {
		return relatedConnections;
	}	
}
