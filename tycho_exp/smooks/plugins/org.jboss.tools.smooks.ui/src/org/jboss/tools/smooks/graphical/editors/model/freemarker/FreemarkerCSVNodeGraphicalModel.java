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

import java.util.List;

import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.jboss.tools.smooks.configuration.SmooksConstants;
import org.jboss.tools.smooks.configuration.editors.xml.AbstractXMLObject;
import org.jboss.tools.smooks.editor.ISmooksModelProvider;
import org.jboss.tools.smooks.gef.model.AbstractSmooksGraphicalModel;
import org.jboss.tools.smooks.gef.tree.model.TreeNodeConnection;
import org.jboss.tools.smooks.gef.tree.model.TreeNodeModel;
import org.jboss.tools.smooks.model.freemarker.Freemarker;
import org.jboss.tools.smooks.model.freemarker.Template;
import org.jboss.tools.smooks10.model.smooks.util.SmooksModelUtils;

/**
 * @author Dart
 * 
 */
public class FreemarkerCSVNodeGraphicalModel extends TreeNodeModel {
	protected IEditingDomainProvider domainProvider = null;

	protected FreemarkerCSVContentGenerator generator;

	public FreemarkerCSVNodeGraphicalModel(Object data, ITreeContentProvider contentProvider,
			ILabelProvider labelProvider, IEditingDomainProvider domainProvider) {
		super(data, contentProvider, labelProvider);
		this.domainProvider = domainProvider;
		generator = new FreemarkerCSVContentGenerator();
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.tools.smooks.gef.tree.model.TreeNodeModel#hasChildren()
	 */
	@Override
	public boolean hasChildren() {
		CSVNodeModel node = (CSVNodeModel) getData();
		if (node.isRecord())
			return true;
		return false;
	}

	/**
	 * 
	 * @param name
	 */
	public void setName(String name) {
		AbstractXMLObject node = (AbstractXMLObject) getData();
		String oldName = node.getName();
		if (!oldName.equals(name)) {
			node.setName(name);
			fireVisualChanged();
			changeFreemarkerContents();
		}
	}
	
	

	/* (non-Javadoc)
	 * @see org.jboss.tools.smooks.gef.model.AbstractSmooksGraphicalModel#addChild(org.jboss.tools.smooks.gef.model.AbstractSmooksGraphicalModel)
	 */
	@Override
	public void addChild(AbstractSmooksGraphicalModel node) {
		super.addChild(node);
		changeFreemarkerContents();
	}
	
	protected void changeFreemarkerContents(){
		AbstractSmooksGraphicalModel parent1 = this;
		while(!(parent1 instanceof FreemarkerTemplateGraphicalModel)){
			parent1 = parent1.getParent();
		}
		Object data = parent1.getData();
		data = AdapterFactoryEditingDomain.unwrap(data);
		Template template = null;
		if(data instanceof Freemarker){
			template = ((Freemarker)data).getTemplate();
		}
		if(template == null) return;
		String content = null;
		try {
			Object parent = this.getParent();
			if (parent instanceof FreemarkerTemplateGraphicalModel) {
				content = generator.generateCSVContents(this);
			}
			if (parent instanceof FreemarkerCSVNodeGraphicalModel) {
				content = generator.generateCSVContents((FreemarkerCSVNodeGraphicalModel) parent);
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
		String version = SmooksConstants.VERSION_1_1;
		if(this.domainProvider instanceof ISmooksModelProvider){
			version = ((ISmooksModelProvider)domainProvider).getSmooksGraphicsExt().getPlatformVersion();
		}
		if (SmooksConstants.VERSION_1_1.equals(version)) {
			// CDATA
			SmooksModelUtils.setCDATAToSmooksType(domainProvider.getEditingDomain(), template, content);
		}
		
		if (SmooksConstants.VERSION_1_2.equals(version)) {
			// Comment
			SmooksModelUtils.setCommentToSmooksType(domainProvider.getEditingDomain(), template, content);
		}
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.smooks.gef.model.AbstractSmooksGraphicalModel#addTargetConnection(org.jboss.tools.smooks.gef.tree.model.TreeNodeConnection)
	 */
	@Override
	public void addTargetConnection(TreeNodeConnection connection) {
		// TODO Auto-generated method stub
		super.addTargetConnection(connection);
		changeFreemarkerContents();
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.smooks.gef.model.AbstractSmooksGraphicalModel#removeTargetConnection(org.jboss.tools.smooks.gef.tree.model.TreeNodeConnection)
	 */
	@Override
	public void removeTargetConnection(TreeNodeConnection connection) {
		// TODO Auto-generated method stub
		super.removeTargetConnection(connection);
		changeFreemarkerContents();
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
		return new FreemarkerCSVNodeGraphicalModel(model, contentProvider, labelProvider, this.domainProvider);
	}

}
