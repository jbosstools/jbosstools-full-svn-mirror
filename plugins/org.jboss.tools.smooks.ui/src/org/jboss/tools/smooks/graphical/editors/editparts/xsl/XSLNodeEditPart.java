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
package org.jboss.tools.smooks.graphical.editors.editparts.xsl;

import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;
import org.dom4j.dom.DOMDocumentFactory;
import org.dom4j.tree.DefaultElement;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.editpolicies.FlowLayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.gef.requests.GroupRequest;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.jboss.tools.smooks.configuration.SmooksConfigurationActivator;
import org.jboss.tools.smooks.configuration.editors.GraphicsConstants;
import org.jboss.tools.smooks.configuration.editors.xml.AbstractXMLObject;
import org.jboss.tools.smooks.configuration.editors.xml.TagPropertyObject;
import org.jboss.tools.smooks.configuration.editors.xml.XSLModelAnalyzer;
import org.jboss.tools.smooks.configuration.editors.xml.XSLTagObject;
import org.jboss.tools.smooks.gef.model.AbstractSmooksGraphicalModel;
import org.jboss.tools.smooks.gef.tree.editparts.TreeNodeEditPart;
import org.jboss.tools.smooks.gef.tree.figures.TreeNodeFigure;
import org.jboss.tools.smooks.graphical.actions.xsltemplate.XSLConstants;
import org.jboss.tools.smooks.graphical.editors.commands.AddSmooksGraphicalModelCommand;
import org.jboss.tools.smooks.graphical.editors.commands.ChangeXSLNodeNameCommand;
import org.jboss.tools.smooks.graphical.editors.commands.DeleteXSLNodeCommand;
import org.jboss.tools.smooks.graphical.editors.model.xsl.XSLNodeGraphicalModel;

/**
 * @author Dart
 * 
 */
public class XSLNodeEditPart extends TreeNodeEditPart {

	private Font xslLabelFont = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.gef.tree.editparts.AbstractTreeEditPart#activate()
	 */
	@Override
	public void activate() {
		super.activate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.gef.tree.editparts.TreeNodeEditPart#canDirectEdit
	 * ()
	 */
	@Override
	protected boolean canDirectEdit() {
		Object data = ((AbstractSmooksGraphicalModel) getModel()).getData();
		if (data instanceof AbstractXMLObject) {
			return !XSLModelAnalyzer.isXSLTagObject((AbstractXMLObject) data);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.gef.tree.editparts.TreeNodeEditPart#
	 * createDirectEditCommand(org.eclipse.gef.requests.DirectEditRequest)
	 */
	@Override
	protected Command createDirectEditCommand(DirectEditRequest request) {
		Object value = request.getCellEditor().getValue();
		if (value != null && value instanceof String) {
			ChangeXSLNodeNameCommand command = new ChangeXSLNodeNameCommand((XSLNodeGraphicalModel) getModel(), value);
			return command;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.gef.tree.editparts.TreeNodeEditPart#createEditPolicies
	 * ()
	 */
	@Override
	protected void createEditPolicies() {
		super.createEditPolicies();
		this.installEditPolicy(EditPolicy.COMPONENT_ROLE, new ComponentEditPolicy() {

			@Override
			protected Command getDeleteCommand(GroupRequest request) {
				List<?> editParts = request.getEditParts();
				for (Iterator<?> iterator = editParts.iterator(); iterator.hasNext();) {
					EditPart editPart = (EditPart) iterator.next();
					AbstractSmooksGraphicalModel graphModel = (AbstractSmooksGraphicalModel) editPart.getModel();
					Object data = graphModel.getData();
					data = AdapterFactoryEditingDomain.unwrap(data);
					if (data instanceof XSLTagObject) {
						DeleteXSLNodeCommand command = new DeleteXSLNodeCommand(editPart);
						return command;
					}
					if (data instanceof TagPropertyObject) {
						DeleteXSLNodeCommand command = new DeleteXSLNodeCommand(editPart);
						return command;
					}
				}
				return null;
			}
		});

		this.installEditPolicy(EditPolicy.LAYOUT_ROLE, new FlowLayoutEditPolicy() {

			@Override
			protected Command getCreateCommand(CreateRequest request) {
				Object model = request.getNewObject();
				Object graphModel = getHost().getModel();
				AbstractXMLObject parentNode = (AbstractXMLObject) ((XSLNodeGraphicalModel) graphModel).getData();
				if (parentNode instanceof TagPropertyObject)
					return null;
				if (graphModel instanceof XSLNodeGraphicalModel) {
					ILabelProvider provider = ((XSLNodeGraphicalModel) graphModel).getLabelProvider();
					ITreeContentProvider provider1 = ((XSLNodeGraphicalModel) graphModel).getContentProvider();
					IEditingDomainProvider provider2 = ((XSLNodeGraphicalModel) graphModel).getDomainProvider();
					XSLNodeGraphicalModel childGraphModel = new XSLNodeGraphicalModel(model, provider1, provider,
							provider2);
					if (model instanceof XSLTagObject) {
						String name = ((XSLTagObject) model).getName();
						String namespace = ((XSLTagObject) model).getNamespaceURI();
						String namespaceprefix = ((XSLTagObject) model).getNameSpacePrefix();
						if (XSLModelAnalyzer.isXSLTagObject((XSLTagObject) parentNode)
								&& XSLModelAnalyzer.isXSLTagObject((XSLTagObject) model)) {
							if (!canCreateAddCommand(name, parentNode.getName())) {
								return null;
							}
						}
						Element element = new DefaultElement(new QName(name, new Namespace(namespaceprefix, namespace)));
						((XSLTagObject) model).setReferenceElement(element);
					}
					if (model instanceof TagPropertyObject) {
						String name = ((TagPropertyObject) model).getName();
						String namespace = ((TagPropertyObject) model).getNamespaceURI();
						String namespaceprefix = ((TagPropertyObject) model).getNameSpacePrefix();
						if (XSLModelAnalyzer.isXSLTagObject((XSLTagObject) parentNode)) {
							return null;
						}
						Element parentElement = ((XSLTagObject) parentNode).getReferenceElement();
						Attribute element = DOMDocumentFactory.getInstance().createAttribute(parentElement,
								new QName(name, new Namespace(namespaceprefix, namespace)), "");
						((TagPropertyObject) model).setReferenceAttibute(element);

					}
					AddSmooksGraphicalModelCommand command = new AddSmooksGraphicalModelCommand((AbstractSmooksGraphicalModel) graphModel,
							childGraphModel);
					return command;
				}
				return null;
			}

			@Override
			protected Command createMoveChildCommand(EditPart child, EditPart after) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			protected Command createAddCommand(EditPart child, EditPart after) {
				return null;
			}

			@Override
			protected EditPolicy createChildEditPolicy(EditPart child) {
				return null;
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.gef.editpolicies.FlowLayoutEditPolicy#isHorizontal()
			 */
			@Override
			protected boolean isHorizontal() {
				return false;
			}
		});
	}

	private boolean canCreateAddCommand(String nodeName, String parentNodeName) {
		if (XSLConstants.STYLESHEET.equals(parentNodeName)) {
			if (XSLConstants.ATTRIBUTE.equals(nodeName)) {
				return true;
			}
			if (XSLConstants.ATTRIBUTE_SET.equals(nodeName)) {
				return true;
			}
			if (XSLConstants.DECIMAL_FORMAT.equals(nodeName)) {
				return true;
			}
			if (XSLConstants.IMPORT.equals(nodeName)) {
				return true;
			}
			if (XSLConstants.INCLUDE.equals(nodeName)) {
				return true;
			}
			if (XSLConstants.KEY.equals(nodeName)) {
				return true;
			}
			if (XSLConstants.NAMESPACE_ALIAS.equals(nodeName)) {
				return true;
			}
			if (XSLConstants.OUTPUT.equals(nodeName)) {
				return true;
			}
			if (XSLConstants.PARAM.equals(nodeName)) {
				return true;
			}
			if (XSLConstants.PRESERVE_SPACE.equals(nodeName)) {
				return true;
			}
			if (XSLConstants.TEMPLATE.equals(nodeName)) {
				return true;
			}
			if (XSLConstants.VARIABLE.equals(nodeName)) {
				return true;
			}
		}
		if (XSLConstants.TEMPLATE.equals(parentNodeName)) {
			if (XSLConstants.APPLY_TEMPLATES.equals(nodeName)) {
				return true;
			}
			if (XSLConstants.CHOOSE.equals(nodeName)) {
				return true;
			}
			if (XSLConstants.CALL_TEMPLATE.equals(nodeName)) {
				return true;
			}
			if (XSLConstants.COMMENT.equals(nodeName)) {
				return true;
			}
			if (XSLConstants.COPY.equals(nodeName)) {
				return true;
			}
			if (XSLConstants.COPY_OF.equals(nodeName)) {
				return true;
			}
			if (XSLConstants.ELEMENT.equals(nodeName)) {
				return true;
			}
			if (XSLConstants.FALLBACK.equals(nodeName)) {
				return true;
			}
			if (XSLConstants.FOR_EACH.equals(nodeName)) {
				return true;
			}
			if (XSLConstants.IF.equals(nodeName)) {
				return true;
			}
			if (XSLConstants.MESSAGE.equals(nodeName)) {
				return true;
			}
			if (XSLConstants.NUMBER.equals(nodeName)) {
				return true;
			}
			if (XSLConstants.TEXT.equals(nodeName)) {
				return true;
			}
			if (XSLConstants.VALUE_OF.equals(nodeName)) {
				return true;
			}
			if (XSLConstants.PROCESSING_INSTRUCTION.equals(nodeName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return the xslLabelFont
	 */
	public Font getXslLabelFont() {
		if (xslLabelFont == null) {
			IFigure figure = getFigure();

			FontData fd = new FontData();
			fd.setStyle(SWT.BOLD);
			if (figure != null) {
				FontData[] fds = figure.getFont().getFontData();
				if (fds != null && fds.length > 0) {
					fd.setHeight(fds[0].getHeight());
					fd.setName(fds[0].getName());
					fd.setLocale(fds[0].getLocale());
				}
			}
			xslLabelFont = new Font(null, fd);
		}
		return xslLabelFont;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.gef.tree.editparts.AbstractTreeEditPart#deactivate
	 * ()
	 */
	@Override
	public void deactivate() {
		super.deactivate();
		if (xslLabelFont != null) {
			xslLabelFont.dispose();
		}
	}

	@Override
	protected void refreshVisuals() {
		AbstractSmooksGraphicalModel model = (AbstractSmooksGraphicalModel) getModel();
		TreeNodeFigure figure = (TreeNodeFigure) getFigure();
		Object data = model.getData();
		if (data instanceof XSLTagObject) {
			if (XSLModelAnalyzer.isXSLTagObject((XSLTagObject) data)) {
				figure.getLabel().setForegroundColor(ColorConstants.blue);
				figure.getLabel().setFont(getXslLabelFont());
			}
		}
		super.refreshVisuals();
		if (data instanceof XSLTagObject) {
			if (((XSLTagObject) data).isTemplateElement()) {
				figure.setLabelImage(SmooksConfigurationActivator.getDefault().getImageRegistry().get(
						GraphicsConstants.IMAGE_XSL_TEMPLATE));
				figure.repaint();
			}

			if (((XSLTagObject) data).isApplyTemplatesElement()) {
				figure.setLabelImage(SmooksConfigurationActivator.getDefault().getImageRegistry().get(
						GraphicsConstants.IMAGE_XSL_TEMPLATE_APPLY));
				figure.repaint();
			}

			if (((XSLTagObject) data).isChoiceElement()) {
				figure.setLabelImage(SmooksConfigurationActivator.getDefault().getImageRegistry().get(
						GraphicsConstants.IMAGE_XSL_CHOICE));
				figure.repaint();
			}

			if (((XSLTagObject) data).isSortElement()) {
				figure.setLabelImage(SmooksConfigurationActivator.getDefault().getImageRegistry().get(
						GraphicsConstants.IMAGE_XSL_SORT));
				figure.repaint();
			}

			if (((XSLTagObject) data).isForeachElement()) {
				figure.setLabelImage(SmooksConfigurationActivator.getDefault().getImageRegistry().get(
						GraphicsConstants.IMAGE_XSL_FOREACH));
				figure.repaint();
			}
			if (((XSLTagObject) data).isIfElement()) {
				figure.setLabelImage(SmooksConfigurationActivator.getDefault().getImageRegistry().get(
						GraphicsConstants.IMAGE_XSL_IF));
				figure.repaint();
			}

			if (((XSLTagObject) data).isStyleSheetElement()) {
				figure.setLabelImage(SmooksConfigurationActivator.getDefault().getImageRegistry().get(
						GraphicsConstants.IMAGE_XSL_STYLESHEET));
				figure.repaint();
			}
		}
	}

}
