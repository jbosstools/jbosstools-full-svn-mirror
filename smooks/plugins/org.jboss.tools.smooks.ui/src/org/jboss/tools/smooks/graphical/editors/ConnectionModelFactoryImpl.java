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
package org.jboss.tools.smooks.graphical.editors;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.jboss.tools.smooks.configuration.editors.IXMLStructuredObject;
import org.jboss.tools.smooks.gef.common.RootModel;
import org.jboss.tools.smooks.gef.model.AbstractSmooksGraphicalModel;
import org.jboss.tools.smooks.gef.tree.model.TreeNodeConnection;
import org.jboss.tools.smooks.graphical.editors.editparts.SmooksGraphUtil;

/**
 * @author Dart
 * 
 */
public class ConnectionModelFactoryImpl implements ConnectionModelFactory {

	public TreeNodeConnection createBeanIDReferenceConnection(EObject rootModel , RootModel root,
			AbstractSmooksGraphicalModel model) {
		return null;
	}

	public boolean hasBeanIDReferenceConnection(AbstractSmooksGraphicalModel model) {
		return false;
	}

	public boolean hasSelectorConnection(AbstractSmooksGraphicalModel model) {
		Object data = model.getData();
		data = AdapterFactoryEditingDomain.unwrap(data);
		if (data instanceof EObject) {
			return (SmooksGraphUtil.getSelectorFeature((EObject) data) != null);
		}
		return false;
	}

	public List<TreeNodeConnection> createSelectorConnection(List<Object> inputDataList, RootModel root,
			AbstractSmooksGraphicalModel model) {
		Object data = model.getData();
		data = AdapterFactoryEditingDomain.unwrap(data);
		List<TreeNodeConnection> connections = new ArrayList<TreeNodeConnection>();
		if (data != null && data instanceof EObject) {
			EStructuralFeature feature = SmooksGraphUtil.getSelectorFeature((EObject) data);
			Object sd = ((EObject) data).eGet(feature);
			if (sd != null) {
				String selector = sd.toString();
				if (inputDataList != null) {
					for (Iterator<?> iterator = inputDataList.iterator(); iterator.hasNext();) {
						Object obj = (Object) iterator.next();
						if (obj instanceof IXMLStructuredObject) {
							AbstractSmooksGraphicalModel sourceGraphModel = SmooksGraphUtil.findInputGraphModel(
									selector, (IXMLStructuredObject) obj, root);
							if (sourceGraphModel != null) {
								boolean canCreate = true;
								List<TreeNodeConnection> tcs = model.getTargetConnections();
								for (Iterator<?> iterator2 = tcs.iterator(); iterator2.hasNext();) {
									TreeNodeConnection treeNodeConnection = (TreeNodeConnection) iterator2.next();
									if (treeNodeConnection.getSourceNode() == sourceGraphModel) {
										canCreate = false;
									}
								}
								if (!canCreate) {
									break;
								}
								TreeNodeConnection connection = new TreeNodeConnection(sourceGraphModel, model);
								sourceGraphModel.getSourceConnections().add(connection);
								sourceGraphModel.fireConnectionChanged();
								model.getTargetConnections().add(connection);
								model.fireConnectionChanged();
								connections.add(connection);
							}
						}
					}
				}
			}
		}
		return connections;
	}
}
