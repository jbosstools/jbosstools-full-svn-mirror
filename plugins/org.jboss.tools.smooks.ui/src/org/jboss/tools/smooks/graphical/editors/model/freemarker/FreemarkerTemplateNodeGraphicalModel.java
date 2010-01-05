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
package org.jboss.tools.smooks.graphical.editors.model.freemarker;

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.configuration.editors.xml.TagObject;
import org.jboss.tools.smooks.configuration.editors.xml.TagPropertyObject;
import org.jboss.tools.smooks.gef.model.AbstractSmooksGraphicalModel;
import org.jboss.tools.smooks.gef.tree.model.TreeNodeConnection;
import org.jboss.tools.smooks.gef.tree.model.TreeNodeModel;
import org.jboss.tools.smooks.graphical.editors.model.javamapping.JavaBeanGraphModel;
import org.jboss.tools.smooks.model.freemarker.Freemarker;
import org.jboss.tools.smooks.model.freemarker.Template;
import org.jboss.tools.smooks.model.javabean12.BeanType;
import org.jboss.tools.smooks.model.javabean12.ValueType;
import org.jboss.tools.smooks.templating.template.Mapping;
import org.jboss.tools.smooks.templating.template.TemplateBuilder;
import org.jboss.tools.smooks.templating.template.exception.InvalidMappingException;
import org.jboss.tools.smooks10.model.smooks.util.SmooksModelUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * @author Dart
 * 
 */
public class FreemarkerTemplateNodeGraphicalModel extends TreeNodeModel {
	protected IEditingDomainProvider domainProvider = null;

	public FreemarkerTemplateNodeGraphicalModel(Object data, ITreeContentProvider contentProvider,
			ILabelProvider labelProvider, IEditingDomainProvider domainProvider) {
		super(data, contentProvider, labelProvider);
		this.domainProvider = domainProvider;
	}

	protected TemplateBuilder getTemplateBuilder() {
		AbstractSmooksGraphicalModel parent = this;
		while (parent != null && !(parent instanceof FreemarkerTemplateGraphicalModel)) {
			parent = parent.getParent();
		}
		if (parent instanceof FreemarkerTemplateGraphicalModel) {
			return ((FreemarkerTemplateGraphicalModel) parent).getTemplateBuilder();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.tools.smooks.gef.tree.model.TreeNodeModel#getChildren()
	 */
	@Override
	public List<AbstractSmooksGraphicalModel> getChildren() {
		return getChildrenWithoutDynamic();
	}

	/**
	 * @return the domainProvider
	 */
	public IEditingDomainProvider getDomainProvider() {
		return domainProvider;
	}

	public void changeFreemarkerContents() {
		TemplateBuilder builder = getTemplateBuilder();
		Template template = null;
		AbstractSmooksGraphicalModel templateGraph = this;
		while (templateGraph != null && !(templateGraph instanceof FreemarkerTemplateGraphicalModel)) {
			templateGraph = templateGraph.getParent();
		}
		Object data = templateGraph.getData();
		if (data instanceof Freemarker) {
			template = ((Freemarker) data).getTemplate();
		}
		if (template == null)
			return;
		if (builder != null) {
			String content = null;
			try {
				content = builder.buildTemplate();
			} catch (Exception e) {
				e.printStackTrace();
			}
			SmooksModelUtils.setCDATAToSmooksType(domainProvider.getEditingDomain(), template, content);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.gef.model.AbstractSmooksGraphicalModel#
	 * addTargetConnection
	 * (org.jboss.tools.smooks.gef.tree.model.TreeNodeConnection)
	 */
	@Override
	public void addTargetConnection(TreeNodeConnection connection) {
		super.addTargetConnection(connection);
		TemplateBuilder builder = getTemplateBuilder();
		Object obj = this.getData();
		Node node = null;
		if (obj instanceof TagObject) {
			node = ((TagObject) obj).getReferenceElement();
		}
		if (obj instanceof TagPropertyObject) {
			node = ((TagPropertyObject) obj).getReferenceAttibute();
		}
		if (builder == null || node == null)
			return;
		if (connection.getData() != null) {

		}
		try {
			if (isCollectionConnection(connection)) {
				AbstractSmooksGraphicalModel beanGraph = connection.getSourceNode();

				Object jobj = (Object) beanGraph.getData();
				jobj = AdapterFactoryEditingDomain.unwrap(jobj);

				String collectionName = null;
				List<AbstractSmooksGraphicalModel> javabeanChildren = beanGraph.getChildrenWithoutDynamic();
				for (Iterator<?> iterator = javabeanChildren.iterator(); iterator.hasNext();) {
					AbstractSmooksGraphicalModel abstractSmooksGraphicalModel = (AbstractSmooksGraphicalModel) iterator
							.next();
					Object javabean = abstractSmooksGraphicalModel.getData();
					javabean = AdapterFactoryEditingDomain.unwrap(javabean);
					if (javabean instanceof org.jboss.tools.smooks.model.javabean12.WiringType) {
						collectionName = ((org.jboss.tools.smooks.model.javabean12.WiringType) javabean).getBeanIdRef();
					}
				}
				BeanType javabeanModel = (BeanType) jobj;
				Mapping mapping = builder.addCollectionMapping(javabeanModel.getBeanId(), (Element) node,
						collectionName);
				connection.setData(mapping);
			}
			if (isMappingValueConnection(connection)) {
				JavaBeanGraphModel collectionJavaBean = getCollectionRootBeanModel(connection.getSourceNode());
				String mappingString = FreemarkerCSVContentGenerator.generateMappingString(connection.getSourceNode(),
						collectionJavaBean);
				Mapping mapping = builder.addValueMapping(mappingString, node);
				connection.setData(mapping);
			}
			changeFreemarkerContents();
		} catch (InvalidMappingException e) {
			e.printStackTrace();
		}
	}

	protected JavaBeanGraphModel getCollectionRootBeanModel(AbstractSmooksGraphicalModel sourceNode) {
		AbstractSmooksGraphicalModel parentNodeBean = sourceNode;
		while (parentNodeBean != null && !(parentNodeBean instanceof JavaBeanGraphModel)) {
			parentNodeBean = parentNodeBean.getParent();
		}
		if (parentNodeBean != null && parentNodeBean instanceof JavaBeanGraphModel) {
			// get the bean reference links
			List<TreeNodeConnection> connections = parentNodeBean.getTargetConnections();
			for (Iterator<?> iterator = connections.iterator(); iterator.hasNext();) {
				TreeNodeConnection treeNodeConnection = (TreeNodeConnection) iterator.next();
				// check if the node is "collection bean" node , if is not ,
				// check its parent node.
				AbstractSmooksGraphicalModel collectionJavaBean = treeNodeConnection.getSourceNode();
				collectionJavaBean = collectionJavaBean.getParent();
				if (collectionJavaBean instanceof JavaBeanGraphModel) {
					Object data = collectionJavaBean.getData();
					data = AdapterFactoryEditingDomain.unwrap(data);
					if (data != null && data instanceof EObject) {
						if (SmooksUIUtils.isCollectionJavaGraphModel((EObject) data)) {
							return (JavaBeanGraphModel) collectionJavaBean;
							// check if it was linked with the "many" template node
//							List<TreeNodeConnection> collectionLinks = collectionJavaBean.getSourceConnections();
//							for (Iterator<?> iterator2 = collectionLinks.iterator(); iterator2.hasNext();) {
//								TreeNodeConnection treeNodeConnection2 = (TreeNodeConnection) iterator2.next();
//								AbstractSmooksGraphicalModel templateNode = treeNodeConnection2.getTargetNode();
//								if(templateNode instanceof FreemarkerTemplateNodeGraphicalModel){
//									Object templateData = ((FreemarkerTemplateNodeGraphicalModel)templateNode).getData();
//									if(templateData instanceof IFreemarkerTemplateModel){
//										if(((IFreemarkerTemplateModel)templateData).isManyOccurs()){
//											return (JavaBeanGraphModel) collectionJavaBean;
//										}
//									}
//								}
//							}
							
						}
					}
				}
				JavaBeanGraphModel result = getCollectionRootBeanModel(collectionJavaBean);
				if (result != null)
					return result;
			}
		}
		return null;
	}

	protected boolean isCollectionConnection(TreeNodeConnection connection) {
		AbstractSmooksGraphicalModel sourceNode = connection.getSourceNode();
		AbstractSmooksGraphicalModel targetNode = connection.getTargetNode();
		Object targetData = targetNode.getData();
		Object data = sourceNode.getData();
		if (data instanceof EObject && targetData instanceof IFreemarkerTemplateModel) {
			if (SmooksUIUtils.isCollectionJavaGraphModel((EObject) data)
					&& ((IFreemarkerTemplateModel) targetData).isManyOccurs()) {
				return true;
			}
		}
		return false;
	}

	protected boolean isMappingValueConnection(TreeNodeConnection connection) {
		AbstractSmooksGraphicalModel sourceNode = connection.getSourceNode();
		Object data = sourceNode.getData();
		if (data instanceof ValueType) {
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.gef.model.AbstractSmooksGraphicalModel#
	 * removeTargetConnection
	 * (org.jboss.tools.smooks.gef.tree.model.TreeNodeConnection)
	 */
	@Override
	public void removeTargetConnection(TreeNodeConnection connection) {
		super.removeTargetConnection(connection);
		try {
			TemplateBuilder builder = getTemplateBuilder();
			Object mapping = connection.getData();
			if (builder == null || mapping == null)
				return;
			if (mapping instanceof Mapping) {
				builder.removeMapping((Mapping) mapping);
				changeFreemarkerContents();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.gef.tree.model.TreeNodeModel#createChildModel(
	 * java.lang.Object, org.eclipse.jface.viewers.ITreeContentProvider,
	 * org.eclipse.jface.viewers.ILabelProvider)
	 */
	@Override
	protected TreeNodeModel createChildModel(Object model, ITreeContentProvider contentProvider,
			ILabelProvider labelProvider) {
		return new FreemarkerTemplateNodeGraphicalModel(model, contentProvider, labelProvider, this.domainProvider);
	}

}
