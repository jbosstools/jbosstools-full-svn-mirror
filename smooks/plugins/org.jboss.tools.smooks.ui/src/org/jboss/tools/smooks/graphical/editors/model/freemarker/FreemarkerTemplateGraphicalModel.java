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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.swt.graphics.Image;
import org.jboss.tools.smooks.configuration.SmooksConfigurationActivator;
import org.jboss.tools.smooks.configuration.editors.GraphicsConstants;
import org.jboss.tools.smooks.editor.ISmooksModelProvider;
import org.jboss.tools.smooks.gef.model.AbstractSmooksGraphicalModel;
import org.jboss.tools.smooks.gef.tree.model.TreeNodeModel;
import org.jboss.tools.smooks.graphical.editors.model.AbstractResourceConfigGraphModel;
import org.jboss.tools.smooks.model.freemarker.Freemarker;
import org.jboss.tools.smooks10.model.smooks.util.SmooksModelUtils;

/**
 * @author Dart
 * 
 */
public class FreemarkerTemplateGraphicalModel extends AbstractResourceConfigGraphModel {

	public static final int TYPE_CSV = 1;

	public static final int TYPE_EDI = 2;

	public static final int TYPE_XML = 3;

	public static final int TYPE_XSD = 4;

	private ISmooksModelProvider smooksModelProvider;

	private int templateType = TYPE_CSV;

	private boolean firstLoadChildren = true;

	public FreemarkerTemplateGraphicalModel(Object data, ITreeContentProvider contentProvider,
			ILabelProvider labelProvider, ISmooksModelProvider domainProvider) {
		super(data, contentProvider, labelProvider, domainProvider);
		this.smooksModelProvider = domainProvider;
	}

	private void initChildrenNodes() {
		Freemarker freemarker = (Freemarker) getData();
		// Template template = freemarker.getTemplate();
		if (freemarker != null) {
			CSVNodeModel recordModel = new CSVNodeModel();
			recordModel.setName(Messages.FreemarkerTemplateGraphicalModel_CSV_Record_Name);
			recordModel.setRecord(true);
			FreemarkerCSVNodeGraphicalModel recordGraphNode = new FreemarkerCSVNodeGraphicalModel(recordModel,
					contentProvider, labelProvider, smooksModelProvider);
			this.getChildrenWithoutDynamic().add(recordGraphNode);
			recordGraphNode.setParent(this);
			String[] fields = SmooksModelUtils.getFreemarkerCSVFileds(freemarker);
			String type = SmooksModelUtils.getTemplateType(freemarker);
			if (SmooksModelUtils.FREEMARKER_TEMPLATE_TYPE_CSV.equals(type)) {
				if (fields != null) {
					List<FreemarkerCSVNodeGraphicalModel> fieldsGraphNodeList = new ArrayList<FreemarkerCSVNodeGraphicalModel>();
					for (int i = 0; i < fields.length; i++) {
						String field = fields[i];
						CSVNodeModel fieldNode = new CSVNodeModel();
						fieldNode.setName(field);
						FreemarkerCSVNodeGraphicalModel fieldGraphNode = new FreemarkerCSVNodeGraphicalModel(fieldNode,
								contentProvider, labelProvider, smooksModelProvider);
						recordGraphNode.getChildrenWithoutDynamic().add(fieldGraphNode);
						fieldGraphNode.setParent(recordGraphNode);
						fieldsGraphNodeList.add(fieldGraphNode);
					}
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.graphical.editors.model.
	 * AbstractResourceConfigGraphModel#createChildModel(java.lang.Object,
	 * org.eclipse.jface.viewers.ITreeContentProvider,
	 * org.eclipse.jface.viewers.ILabelProvider)
	 */
	@Override
	protected TreeNodeModel createChildModel(Object model, ITreeContentProvider contentProvider,
			ILabelProvider labelProvider) {
		if (getTemplateType() == TYPE_CSV) {
			return new FreemarkerCSVNodeGraphicalModel(model, contentProvider, labelProvider, domainProvider);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.tools.smooks.gef.tree.model.TreeNodeModel#getImage()
	 */
	@Override
	public Image getImage() {
		ImageRegistry registry = SmooksConfigurationActivator.getDefault().getImageRegistry();
		if (getTemplateType() == TYPE_CSV) {
			return registry.get(GraphicsConstants.IMAGE_CSV_FILE);
		}
		if (getTemplateType() == TYPE_XML) {
			return registry.get(GraphicsConstants.IMAGE_XML_FILE);
		}
		return registry.get(GraphicsConstants.IMAGE_UNKNOWN_OBJ);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.tools.smooks.gef.tree.model.TreeNodeModel#getText()
	 */
	@Override
	public String getText() {
		if (getTemplateType() == TYPE_CSV) {
			return Messages.FreemarkerTemplateGraphicalModel_CSV_Template_Name;
		}
		return super.getText();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.tools.smooks.gef.tree.model.TreeNodeModel#getChildren()
	 */
	@Override
	public List<AbstractSmooksGraphicalModel> getChildren() {
		if (firstLoadChildren) {
			try {
				initChildrenNodes();
			} catch (Exception e) {
				e.printStackTrace();
			}
			firstLoadChildren = false;
		}
		return this.getChildrenWithoutDynamic();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.gef.model.AbstractSmooksGraphicalModel#addChild
	 * (org.jboss.tools.smooks.gef.model.AbstractSmooksGraphicalModel)
	 */
	@Override
	public void addChild(AbstractSmooksGraphicalModel node) {
		super.addChild(node);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.gef.model.AbstractSmooksGraphicalModel#addChild
	 * (int, org.jboss.tools.smooks.gef.model.AbstractSmooksGraphicalModel)
	 */
	@Override
	public void addChild(int index, AbstractSmooksGraphicalModel node) {
		super.addChild(index, node);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.gef.tree.model.TreeNodeModel#canLinkWithSource
	 * (java.lang.Object)
	 */
	@Override
	public boolean canLinkWithSource(Object model) {
		return false;
		// return super.canLinkWithSource(model);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.gef.tree.model.TreeNodeModel#canLinkWithTarget
	 * (java.lang.Object)
	 */
	@Override
	public boolean canLinkWithTarget(Object model) {
		return false;
		// return super.canLinkWithTarget(model);
	}

	/**
	 * @return the templateType
	 */
	public int getTemplateType() {
		return templateType;
	}

	/**
	 * @param templateType
	 *            the templateType to set
	 */
	public void setTemplateType(int templateType) {
		this.templateType = templateType;
	}

}
