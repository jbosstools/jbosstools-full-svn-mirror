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

import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.jboss.tools.smooks.gef.model.AbstractSmooksGraphicalModel;
import org.jboss.tools.smooks.gef.tree.model.TreeNodeModel;
import org.jboss.tools.smooks.graphical.editors.model.AbstractResourceConfigGraphModel;

/**
 * @author Dart
 * 
 */
public class FreemarkerTemplateGraphicalModel extends AbstractResourceConfigGraphModel {

	public static final int TYPE_CSV = 1;

	public static final int TYPE_EDI = 2;

	public static final int TYPE_XML = 3;

	private int templateType = TYPE_CSV;

	public FreemarkerTemplateGraphicalModel(Object data, ITreeContentProvider contentProvider,
			ILabelProvider labelProvider, IEditingDomainProvider domainProvider) {
		super(data, contentProvider, labelProvider, domainProvider);
		// TODO Auto-generated constructor stub
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

	/* (non-Javadoc)
	 * @see org.jboss.tools.smooks.gef.tree.model.TreeNodeModel#getChildren()
	 */
	@Override
	public List<AbstractSmooksGraphicalModel> getChildren() {
		return this.getChildrenWithoutDynamic();
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.smooks.gef.model.AbstractSmooksGraphicalModel#addChild(org.jboss.tools.smooks.gef.model.AbstractSmooksGraphicalModel)
	 */
	@Override
	public void addChild(AbstractSmooksGraphicalModel node) {
		super.addChild(node);
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.smooks.gef.model.AbstractSmooksGraphicalModel#addChild(int, org.jboss.tools.smooks.gef.model.AbstractSmooksGraphicalModel)
	 */
	@Override
	public void addChild(int index, AbstractSmooksGraphicalModel node) {
		super.addChild(index, node);
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
