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
import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.wst.xsl.core.XSLCore;
import org.jboss.tools.smooks.configuration.editors.IXMLStructuredObject;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.configuration.editors.xml.AbstractXMLObject;
import org.jboss.tools.smooks.configuration.editors.xml.TagObject;
import org.jboss.tools.smooks.configuration.editors.xml.TagPropertyObject;
import org.jboss.tools.smooks.configuration.editors.xml.XMLUtils;
import org.jboss.tools.smooks.configuration.editors.xml.XSLModelAnalyzer;
import org.jboss.tools.smooks.configuration.editors.xml.XSLTagObject;
import org.jboss.tools.smooks.gef.model.AbstractSmooksGraphicalModel;
import org.jboss.tools.smooks.gef.tree.model.TreeNodeConnection;
import org.jboss.tools.smooks.gef.tree.model.TreeNodeModel;
import org.jboss.tools.smooks.graphical.editors.model.InputDataTreeNodeModel;
import org.jboss.tools.smooks.model.xsl.Template;
import org.jboss.tools.smooks.model.xsl.Xsl;
import org.jboss.tools.smooks10.model.smooks.util.SmooksModelUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author Dart
 * 
 */
public class XSLNodeGraphicalModel extends TreeNodeModel {

	protected IEditingDomainProvider domainProvider = null;

	public XSLNodeGraphicalModel(Object data, ITreeContentProvider contentProvider, ILabelProvider labelProvider,
			IEditingDomainProvider domainProvider) {
		super(data, contentProvider, labelProvider);
		this.domainProvider = domainProvider;
	}

	/**
	 * @return the domainProvider
	 */
	public IEditingDomainProvider getDomainProvider() {
		return domainProvider;
	}

	/**
	 * @param domainProvider
	 *            the domainProvider to set
	 */
	public void setDomainProvider(IEditingDomainProvider domainProvider) {
		this.domainProvider = domainProvider;
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
		// TODO Auto-generated method stub
		return new XSLNodeGraphicalModel(model, contentProvider, labelProvider, this.domainProvider);
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
		List<TreeNodeConnection> targetConnections = this.getTargetConnections();
		for (Iterator<?> iterator = targetConnections.iterator(); iterator.hasNext();) {
			TreeNodeConnection treeNodeConnection = (TreeNodeConnection) iterator.next();
			if (treeNodeConnection.getSourceNode() == model) {
				return false;
			}
		}
		return true;
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
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.gef.model.AbstractSmooksGraphicalModel#
	 * addSourceConnection
	 * (org.jboss.tools.smooks.gef.tree.model.TreeNodeConnection)
	 */
	@Override
	public void addSourceConnection(TreeNodeConnection connection) {
		super.addSourceConnection(connection);
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
		Object data = getData();

		if (data instanceof TagPropertyObject) {
			((TagPropertyObject) data).setValue(null);
		}

		if (data instanceof XSLTagObject) {
			if (XSLModelAnalyzer.isXSLTagObject((XSLTagObject) data)) {
				if (((XSLTagObject) data).isForeachElement() || ((XSLTagObject) data).isSortElement()) {
					((XSLTagObject) data).removeSelectProperty();
				}
				if (((XSLTagObject) data).isTemplateElement()) {
					((XSLTagObject) data).removeMatchProperty();
					this.fireVisualChanged();
				}
			} else {
				Object sourceModel = connection.getSourceNode();
				AbstractXMLObject xmlObject = null;
				if (sourceModel instanceof InputDataTreeNodeModel) {
					xmlObject = (AbstractXMLObject) ((InputDataTreeNodeModel) sourceModel).getData();
				}
				if (xmlObject != null) {
					List<?> relatedTags = ((XSLTagObject) data).getRelatedIgnoreXSLTagObjects();
					TagObject relatedTag = null;
					for (Iterator<?> iterator = relatedTags.iterator(); iterator.hasNext();) {
						Object object = (Object) iterator.next();
						if (object instanceof XSLTagObject) {
							if (((XSLTagObject) object).isValueOfElement()) {
								String select = ((XSLTagObject) object).getSelectValue();
								IXMLStructuredObject node = SmooksUIUtils.localXMLNodeWithPath(select, SmooksUIUtils
										.getRootParent(xmlObject));
								if (node == xmlObject) {
									Element parentElement = ((XSLTagObject) data).getReferenceElement();
									Element thisElement = ((XSLTagObject) object).getReferenceElement();
									parentElement.removeChild(thisElement);
									((XSLTagObject) data).removeChildTag((XSLTagObject) object);
									relatedTag = (XSLTagObject) object;
									break;
								}
							}
						}
					}
					if (relatedTag != null) {
						((XSLTagObject) data).getRelatedIgnoreXSLTagObjects().remove(relatedTag);
					}
				}
			}
		}
		super.removeTargetConnection(connection);
		setXSLContents(getXSLContents());

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
		AbstractSmooksGraphicalModel sourceModel = connection.getSourceNode();
		Object source = sourceModel.getData();
		String selectorString = null;
		if (source instanceof IXMLStructuredObject) {
			selectorString = SmooksUIUtils.generateFullPath((IXMLStructuredObject) source, "/");
		}

		Object data = getData();

		if (data instanceof TagPropertyObject) {
			((TagPropertyObject) data).setValue("{" + selectorString + "}");
		}

		if (data instanceof XSLTagObject) {
			if (XSLModelAnalyzer.isXSLTagObject((XSLTagObject) data)) {
				if (((XSLTagObject) data).isForeachElement() || ((XSLTagObject) data).isSortElement()) {
					((XSLTagObject) data).setSelectValue(selectorString);
				}
				if (((XSLTagObject) data).isTemplateElement()) {
					((XSLTagObject) data).setMatchValue(selectorString);
					this.fireVisualChanged();
				}
			} else {
				XSLTagObject relateTag = null;
				if (relateTag == null) {
					relateTag = new XSLTagObject();
					Element element = ((XSLTagObject) data).getReferenceElement();
					Document doc = element.getOwnerDocument();
					Element rootElement = doc.getDocumentElement();
					String namespace = rootElement.getNamespaceURI();
					if (namespace == null) {
						namespace = XSLModelAnalyzer.XSL_NAME_SPACE;
					}

					Element newElement = doc.createElementNS(namespace, "value-of");
					element.appendChild(newElement);
					relateTag.setName("value-of");
					relateTag.setReferenceElement(newElement);
					((XSLTagObject) data).getRelatedIgnoreXSLTagObjects().add(relateTag);
				}
				relateTag.setSelectValue(selectorString);
			}
		}

		super.addTargetConnection(connection);
		setXSLContents(getXSLContents());

	}

	public static String getXSLContents(Element rootElement) {
		if (rootElement != null) {

			ByteArrayOutputStream stream = null;
			try {
				stream = new ByteArrayOutputStream();
				XMLUtils.outDOMNode(rootElement, stream);
				String string = new String(stream.toByteArray());
				return string;
			} catch (Exception e1) {

			} finally {
				try {
					if (stream != null)
						stream.close();
				} catch (Throwable t) {
				}
			}
		}
		return null;
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
		Object data = this.getData();
		Object childModel = node.getData();
		if (data instanceof XSLTagObject) {
			if (childModel instanceof XSLTagObject) {
				((XSLTagObject) data).removeChildTag((XSLTagObject) childModel);

			}
			if (childModel instanceof TagPropertyObject) {
				((XSLTagObject) data).removeProperty((TagPropertyObject) childModel);
			}
		}
		super.removeChild(node);
		setXSLContents(getXSLContents());
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
		Object data = this.getData();
		Object childModel = node.getData();
		if (data instanceof XSLTagObject) {
			if (childModel instanceof XSLTagObject) {
				((XSLTagObject) data).addChildTag((XSLTagObject) childModel);

			}
			if (childModel instanceof TagPropertyObject) {
				((XSLTagObject) data).addProperty((TagPropertyObject) childModel);
			}
		}
		super.addChild(node);

		setXSLContents(getXSLContents());
	}

	@Override
	public void moveChild(int index, AbstractSmooksGraphicalModel node) {
		try {
			AbstractXMLObject xmlNode = (AbstractXMLObject) node.getData();
			AbstractXMLObject thisNode = (AbstractXMLObject) this.getData();
			Element element = thisNode.getReferenceElement();
			// if (xmlNode instanceof TagObject) {
			// Element childElement = xmlNode.getReferenceElement();
			// if (element..indexOf(childElement) != -1) {
			// element.elements().remove(childElement);
			// int size = element.attributes().size();
			// int realIndex = index;
			// if (!XSLModelAnalyzer.isXSLTagObject(thisNode)) {
			// realIndex = index - size;
			// }
			// element.elements().add(realIndex, childElement);
			// }
			// }
			// if (xmlNode instanceof TagPropertyObject) {
			// Attribute attribute = ((TagPropertyObject)
			// xmlNode).getReferenceAttibute();
			// if (element.attributes().indexOf(attribute) != -1) {
			// int size = element.elements().size();
			// int realIndex = index - size;
			// element.attributes().remove(attribute);
			// element.attributes().add(realIndex, attribute);
			// }
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.moveChild(index, node);
		this.setXSLContents(getXSLContents());
	}

	protected String getXSLContents() {
		Object data = getData();
		Element rootElement = null;
		Element parentElement = null;
		if (data instanceof TagPropertyObject) {
			parentElement = ((TagPropertyObject) data).getReferenceAttibute().getOwnerElement();
		}
		if (data instanceof XSLTagObject) {
			parentElement = ((XSLTagObject) data).getReferenceElement();
		}
		if (parentElement != null) {
			Document d = parentElement.getOwnerDocument();
			rootElement = d.getDocumentElement();
		}
		return getXSLContents(rootElement);
	}

	protected void setXSLContents(String contents) {
		Template template = getXSLTemplate();
		EditingDomain domain = this.domainProvider.getEditingDomain();
		if (template != null) {
			String filePath = SmooksModelUtils.getAnyTypeText(template);
			if (filePath != null)
				filePath = filePath.trim();
			if (filePath != null && !"".equals(filePath)) {
				IFile file = SmooksUIUtils.getFile(filePath, SmooksUIUtils.getResource(template).getProject());
				if (file != null && XSLCore.isXSLFile(file) && file.exists()) {
					try {
						if (contents == null)
							contents = "";
						file.setContents(new ByteArrayInputStream(contents.getBytes()), IResource.FORCE, null);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else {
				SmooksModelUtils.setCDATAToSmooksType(domain, template, contents);
			}
		}
	}

	protected Template getXSLTemplate() {
		AbstractSmooksGraphicalModel parent = this;
		while (parent != null) {
			if (parent instanceof XSLTemplateGraphicalModel) {
				break;
			}
			parent = parent.getParent();
		}

		if (parent != null && parent instanceof XSLTemplateGraphicalModel) {
			Object data = ((XSLTemplateGraphicalModel) parent).getData();
			if (data instanceof Xsl) {
				return ((Xsl) data).getTemplate();
			}
		}

		return null;
	}

	public void setName(String name, String namespaceURI, String namespacePrefix) {
		AbstractXMLObject obj = (AbstractXMLObject) getData();
		String oldName = obj.getName();
		if (oldName == null) {
			oldName = "";
		}
		if (oldName.equals(name)) {
			return;
		}
		obj.setName(name);
		setXSLContents(getXSLContents());
		if (support != null) {
			support.firePropertyChange(PRO_TEXT_CHANGED, oldName, name);
		}
	}
}
