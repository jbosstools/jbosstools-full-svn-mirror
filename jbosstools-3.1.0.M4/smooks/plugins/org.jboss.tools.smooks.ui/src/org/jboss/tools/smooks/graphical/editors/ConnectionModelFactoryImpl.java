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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.jboss.tools.smooks.configuration.editors.IXMLStructuredObject;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.gef.common.RootModel;
import org.jboss.tools.smooks.gef.model.AbstractSmooksGraphicalModel;
import org.jboss.tools.smooks.gef.tree.model.TreeNodeConnection;
import org.jboss.tools.smooks.graphical.editors.editparts.SmooksGraphUtil;

/**
 * @author Dart
 * 
 */
public class ConnectionModelFactoryImpl implements ConnectionModelFactory {

	public Collection<TreeNodeConnection> createBeanIDReferenceConnection(EObject rootModel, RootModel root,
			AbstractSmooksGraphicalModel model) {
		Object mm = model.getData();
		mm = AdapterFactoryEditingDomain.unwrap(mm);
		if (mm == null && !(mm instanceof EObject))
			return null;
		EObject cmodel = (EObject) mm;
		EStructuralFeature beanIDRefFeature = SmooksUIUtils.getBeanIDRefFeature(cmodel);
		EStructuralFeature beanIDFeature = SmooksUIUtils.getBeanIDFeature(cmodel);
		List<TreeNodeConnection> connections = new ArrayList<TreeNodeConnection>();
		if (beanIDRefFeature != null) {
			Object data = cmodel.eGet(beanIDRefFeature);
			if (data != null) {
				String refID = data.toString();
				Collection<EObject> modelList = SmooksUIUtils.getBeanIdModelList(rootModel);
				for (Iterator<?> iterator = modelList.iterator(); iterator.hasNext();) {
					EObject eObject = (EObject) iterator.next();
					EStructuralFeature idfeature = SmooksUIUtils.getBeanIDFeature(eObject);
					if (idfeature == null)
						continue;
					Object iddata = eObject.eGet(idfeature);
					if (iddata == null)
						continue;
					if (refID.equals(iddata)) {
						AbstractSmooksGraphicalModel targetGraphModel = SmooksGraphUtil.findSmooksGraphModel(root,
								eObject);
						if (targetGraphModel != null) {
							List<TreeNodeConnection> tcs = targetGraphModel.getTargetConnections();
							boolean createNewConnection = true;
							for (Iterator<?> iterator2 = tcs.iterator(); iterator2.hasNext();) {
								TreeNodeConnection treeNodeConnection = (TreeNodeConnection) iterator2.next();
								if (treeNodeConnection.getSourceNode() == model) {
									// it means that the connection has been
									// exist
									createNewConnection = false;
									break;
								}
							}
							if (!createNewConnection)
								continue;
							// create new connection;
							AbstractSmooksGraphicalModel target = SmooksGraphUtil.findSmooksGraphModel(root, eObject);
							TreeNodeConnection connection = new TreeNodeConnection(model, target);
							model.getSourceConnections().add(connection);
							model.fireConnectionChanged();
							target.getTargetConnections().add(connection);
							target.fireConnectionChanged();
							connections.add(connection);
						}
					}
				}
			}
		}

		if (beanIDFeature != null) {
			Object data = cmodel.eGet(beanIDFeature);
			if (data != null) {
				String beanid = data.toString();
				Collection<EObject> beanIdRefModelList = SmooksUIUtils.getBeanIdRefModelList(rootModel);
				for (Iterator<?> iterator = beanIdRefModelList.iterator(); iterator.hasNext();) {
					EObject eObject = (EObject) iterator.next();
					EStructuralFeature idRefFeature = SmooksUIUtils.getBeanIDRefFeature(eObject);
					if (idRefFeature == null)
						continue;
					Object idRefData = eObject.eGet(idRefFeature);
					if (idRefData == null)
						continue;
					if (beanid.equals(idRefData)) {
						AbstractSmooksGraphicalModel sourceGraphModel = SmooksGraphUtil.findSmooksGraphModel(root,
								eObject);
						List<TreeNodeConnection> scs = sourceGraphModel.getSourceConnections();
						boolean createNewConnection = true;
						for (Iterator<?> iterator2 = scs.iterator(); iterator2.hasNext();) {
							TreeNodeConnection treeNodeConnection = (TreeNodeConnection) iterator2.next();
							if (treeNodeConnection.getTargetNode() == model) {
								// it means that the connection has been exist
								createNewConnection = false;
								break;
							}
						}
						if (!createNewConnection)
							continue;
						// create new connection;
						AbstractSmooksGraphicalModel source = SmooksGraphUtil.findSmooksGraphModel(root, eObject);
						TreeNodeConnection connection = new TreeNodeConnection(source, model);
						source.getSourceConnections().add(connection);
						model.getTargetConnections().add(connection);
						source.fireConnectionChanged();
						model.fireConnectionChanged();
						connections.add(connection);
					}
				}
			}
		}

		if (connections.isEmpty())
			return null;
		return connections;
	}

	public boolean hasBeanIDConnection(AbstractSmooksGraphicalModel model) {
		Object data = model.getData();
		data = AdapterFactoryEditingDomain.unwrap(data);
		if (data instanceof EObject) {
			if (!(SmooksUIUtils.getBeanIDRefFeature((EObject) data) != null)
					|| !(SmooksUIUtils.getBeanIDFeature((EObject) data) != null)) {
				return true;
			}
		}
		return false;
	}

	public boolean hasSelectorConnection(AbstractSmooksGraphicalModel model) {
		Object data = model.getData();
		data = AdapterFactoryEditingDomain.unwrap(data);
		if (data instanceof EObject) {
			return (SmooksUIUtils.getSelectorFeature((EObject) data) != null);
		}
		return false;
	}

	public Collection<TreeNodeConnection> createSelectorConnection(List<Object> inputDataList, RootModel root,
			AbstractSmooksGraphicalModel model) {
		Object data = model.getData();
		data = AdapterFactoryEditingDomain.unwrap(data);
		List<TreeNodeConnection> connections = new ArrayList<TreeNodeConnection>();
		if (data != null && data instanceof EObject) {
			EStructuralFeature feature = SmooksUIUtils.getSelectorFeature((EObject) data);
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
