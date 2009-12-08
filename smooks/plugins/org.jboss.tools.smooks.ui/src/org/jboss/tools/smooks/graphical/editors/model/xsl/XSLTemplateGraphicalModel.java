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
package org.jboss.tools.smooks.graphical.editors.model.xsl;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.wst.xsl.core.XSLCore;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.configuration.editors.xml.XSLTagObject;
import org.jboss.tools.smooks.gef.model.AbstractSmooksGraphicalModel;
import org.jboss.tools.smooks.gef.tree.model.TreeNodeModel;
import org.jboss.tools.smooks.graphical.editors.model.AbstractResourceConfigGraphModel;
import org.jboss.tools.smooks.model.xsl.Template;
import org.jboss.tools.smooks.model.xsl.Xsl;
import org.jboss.tools.smooks10.model.smooks.util.SmooksModelUtils;
import org.w3c.dom.Element;

/**
 * @author Dart
 * 
 */
public class XSLTemplateGraphicalModel extends AbstractResourceConfigGraphModel {

	public XSLTemplateGraphicalModel(Object data, ITreeContentProvider contentProvider, ILabelProvider labelProvider,
			IEditingDomainProvider domainProvider) {
		super(data, contentProvider, labelProvider, domainProvider);
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
		return new XSLNodeGraphicalModel(model, contentProvider, labelProvider, domainProvider);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.tools.smooks.gef.tree.model.TreeNodeModel#getChildren()
	 */
	@Override
	public List<AbstractSmooksGraphicalModel> getChildren() {
		return super.getChildren();
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
		if (node instanceof XSLNodeGraphicalModel) {
			Object data = node.getData();
			if (data instanceof XSLTagObject) {
				Element element = ((XSLTagObject) data).getReferenceElement();
				Element rootElement = element.getOwnerDocument().getDocumentElement();
				String contents = XSLNodeGraphicalModel.getXSLContents(rootElement);
				Template template = ((Xsl) getData()).getTemplate();
				String filePath = SmooksModelUtils.getAnyTypeText(template);
				if (filePath != null)
					filePath = filePath.trim();
				if (filePath != null && !"".equals(filePath)) {
					IFile file = SmooksUIUtils.getFile(filePath, SmooksUIUtils.getResource(template).getProject());
					if (file != null && XSLCore.isXSLFile(file) && file.exists()) {
						try {
							file.setContents(new ByteArrayInputStream(contents.getBytes()), IResource.FORCE, null);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} else {
					EditingDomain domain = this.domainProvider.getEditingDomain();
					SmooksModelUtils.setCDATAToSmooksType(domain, template, contents);
				}

				((XSLTemplateContentProvider) this.contentProvider).cleanBuffer();
			}
		}
		super.addChild(node);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.gef.model.AbstractSmooksGraphicalModel#removeChild
	 * (org.jboss.tools.smooks.gef.model.AbstractSmooksGraphicalModel)
	 */
	@Override
	public void removeChild(AbstractSmooksGraphicalModel node) {
		if (node instanceof XSLNodeGraphicalModel) {
			Template template = ((Xsl) getData()).getTemplate();
			String filePath = SmooksModelUtils.getAnyTypeText(template);
			if (filePath != null)
				filePath = filePath.trim();
			if (filePath != null && !"".equals(filePath)) {
				IFile file = SmooksUIUtils.getFile(filePath, SmooksUIUtils.getResource(template).getProject());
				if (file != null && XSLCore.isXSLFile(file) && file.exists()) {
					try {
						file.setContents(new ByteArrayInputStream("".getBytes()), IResource.FORCE, null);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else {
				EditingDomain domain = this.domainProvider.getEditingDomain();
				SmooksModelUtils.setCDATAToSmooksType(domain, template, null);
			}

			((XSLTemplateContentProvider) this.contentProvider).cleanBuffer();
		}
		super.removeChild(node);
	}

}
