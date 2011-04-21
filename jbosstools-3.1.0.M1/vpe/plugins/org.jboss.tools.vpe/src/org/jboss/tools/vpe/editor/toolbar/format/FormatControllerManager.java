/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.vpe.editor.toolbar.format;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.eclipse.core.runtime.Status;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.wst.sse.ui.internal.StructuredTextViewer;
import org.eclipse.wst.xml.core.internal.document.ElementImpl;
import org.eclipse.wst.xml.core.internal.document.TextImpl;
import org.w3c.dom.Node;

import org.jboss.tools.common.reporting.ProblemReportingHelper;
import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.VpeController;
import org.jboss.tools.vpe.editor.mapping.VpeDomMapping;
import org.jboss.tools.vpe.editor.mapping.VpeElementMapping;
import org.jboss.tools.vpe.editor.mapping.VpeNodeMapping;
import org.jboss.tools.vpe.editor.selection.VpeSelectedNodeInfo;
import org.jboss.tools.vpe.editor.selection.VpeSourceSelection;
import org.jboss.tools.vpe.editor.selection.VpeSourceSelectionBuilder;
import org.jboss.tools.vpe.editor.template.VpeTemplate;
import org.jboss.tools.vpe.editor.template.textformating.FormatData;
import org.jboss.tools.vpe.editor.template.textformating.TextFormatingData;
import org.jboss.tools.vpe.editor.toolbar.format.handler.FormatHandler;
import org.jboss.tools.vpe.editor.toolbar.format.handler.HandlerFactory;
import org.jboss.tools.vpe.editor.toolbar.format.handler.IFormatHandler;

/**
 * This manager set format toolbar items to enabled/disabled status after selection changing.
 * @author Igels
 */
public class FormatControllerManager {

	private VpeController vpeController = null;
	private List formatControllers = new ArrayList();
	private HandlerFactory handlerFactory;

	private FormatData currentFormatData;
	private Node currentSelectedNode;
	private Node currentSelectedElement; // Parent node If currentSelectedNode is Text.
	private VpeSelectedNodeInfo currentSelectedNodeInfo;
	private boolean controllerNotifedSelectionChange = false;
	private String currentSelectedTagValue;
	private boolean ignoreSelectionChanges = false;

	/**
	 * Constructor
	 */
	public FormatControllerManager() {
		handlerFactory = new HandlerFactory(this);
	}

	/**
	 * @param vpeController
	 */
	public void setVpeController(VpeController vpeController) {
		this.vpeController = vpeController;
	}

	/**
	 * Notify selection changed.
	 * Set format toolbar items to enabled/disabled status.
	 */
	public void selectionChanged() {
		List cleanNodesList = getCleanSelectedNodesList();
		if(cleanNodesList.size()==1) {
			VpeSelectedNodeInfo newNodeInfo = (VpeSelectedNodeInfo)cleanNodesList.get(0);
			if(newNodeInfo!=null) {
				 Node newNode = newNodeInfo.getNode();
				 if(currentSelectedNode == newNode && newNode.getNodeType() == Node.ELEMENT_NODE) {
					 ElementImpl elementImpl = (ElementImpl)newNode;
					 int startOffset = elementImpl.getStartOffset();
					 int startEndOffset = elementImpl.getStartEndOffset();
					 StructuredTextViewer viewer = getVpeController().getPageContext().getSourceBuilder().getStructuredTextViewer();
					 try {
						String newSelectedTagValue = viewer.getDocument().get(startOffset, startEndOffset-startOffset);
						if(currentSelectedTagValue!=null && currentSelectedTagValue.equals(newSelectedTagValue)) {
							return;
						}
						currentSelectedTagValue = newSelectedTagValue;
					 } catch (BadLocationException e) {
						 VpePlugin.getPluginLog().logError(e);
					 }
				 } else {
					 currentSelectedTagValue = null;
				 }
			}
			currentSelectedNodeInfo = newNodeInfo;
			currentSelectedNode = newNodeInfo.getNode();
			currentSelectedElement = currentSelectedNode;
			TextFormatingData textFormatingData = getFormatTemplateForSelectedNode();
			if(textFormatingData!=null) {
				selectionChanged(textFormatingData);
			} else {
				disableAllFormatControllers();
			}
		} else {
			disableAllFormatControllers();
			currentSelectedTagValue = null;
			currentSelectedNodeInfo = null;
			currentSelectedNode = null;
			currentSelectedElement = currentSelectedNode; 
		}
	}

	private void selectionChanged(TextFormatingData textFormatingData) {
		if(ignoreSelectionChanges) {
			return;
		}
		for(int i=0; i<formatControllers.size(); i++) {
			IFormatController controller = (IFormatController)formatControllers.get(i);
			FormatData[] formatDatas = textFormatingData.getFormatDatas(controller.getType());
			boolean enabled = false;
			for(int j=0; j<formatDatas.length; j++) {
				FormatData formatData = formatDatas[j];
				currentFormatData = formatData;
				IFormatHandler handler = handlerFactory.createHandler(formatData);
				if(handler!=null) {
					if(handler instanceof FormatHandler) {
						FormatHandler formatHandler = (FormatHandler)handler;
						formatHandler.setFormatController(controller);
					}
					if(handler instanceof IFormatItemSelector) {
						IFormatItemSelector itemHandler = (IFormatItemSelector)handler;
						if(handler.formatIsAllowable()) {
							itemHandler.setToolbarItemEnabled(true);
							enabled = true;
							break;
						}
					} else {
						if(handler.formatIsAllowable()) {
							controller.setToolbarItemEnabled(true);
							enabled = true;
							break;
						}
					}
				} else {
					boolean allowable = formatData.getFormatAttributes().length > 0;
					if(allowable) {
						controller.setToolbarItemEnabled(true);
						enabled = true;
						break;
					}
				}
			}
			if(!enabled) {
				controller.setToolbarItemEnabled(false);
			}
		}
	}

	private TextFormatingData getParentFormatingDataForTextNode(VpeSelectedNodeInfo selectedNodeInfo) {
		Node selectedNode = selectedNodeInfo.getNode();
		if(selectedNode instanceof TextImpl) {
//				int startOffset = selectedNodeInfo.getStartOffset()>selectedNodeInfo.getEndOffset()?selectedNodeInfo.getEndOffset():selectedNodeInfo.getStartOffset();
//				int endOffset = selectedNodeInfo.getStartOffset()>selectedNodeInfo.getEndOffset()?selectedNodeInfo.getStartOffset():selectedNodeInfo.getEndOffset();
//
//				IndexedRegion region = (IndexedRegion)selectedNode;
//				String wholeValue = vpeController.getSourceEditor().getTextViewer().getDocument().get(region.getStartOffset(), region.getEndOffset()-region.getStartOffset());
//
//				String selectedValue = wholeValue.substring(startOffset, endOffset);
//				// if selected text is not whole text node return null.
//				if((startOffset==endOffset) || (wholeValue.trim().equals(selectedValue.trim()))) {
//					Node parentNode = selectedNode.getParentNode();
//					TextFormatingData data = getFormatTemplateForTag(parentNode);
//					if(data!=null) {
//						currentSelectedNode = parentNode;
//					}
//					return data;
//				}
				currentSelectedElement = selectedNode.getParentNode();
				return getFormatTemplateForTag(selectedNode.getParentNode());
		}

		return null;
	}

	/**
	 * @return
	 */
	public TextFormatingData getFormatTemplateForSelectedNode() {
		if(currentSelectedNodeInfo==null) {
			return null;
		}
		TextFormatingData textFormatingData = getFormatTemplateForTag(currentSelectedNodeInfo.getNode());
		if(textFormatingData!=null) {
			currentSelectedNode = currentSelectedNodeInfo.getNode();
			currentSelectedElement = currentSelectedNode;
			return textFormatingData;
		} else {
			// Try to take text formating data if selected node is text
			textFormatingData = getParentFormatingDataForTextNode(currentSelectedNodeInfo);
			if(textFormatingData!=null) {
				return textFormatingData;
			}
		}
		return null;
	}

	/**
	 * @param node
	 * @return
	 */
	public TextFormatingData getFormatTemplateForTag(Node node) {
		VpeDomMapping domMapping = vpeController.getDomMapping();
		if(node==null) {
			return null;
		}

		VpeNodeMapping nodeMapping = domMapping.getNodeMapping(node);
		if(nodeMapping instanceof VpeElementMapping) {
			VpeElementMapping elementMapping = (VpeElementMapping)nodeMapping;

			if(elementMapping==null) {
				return null;
			}
			VpeTemplate template = elementMapping.getTemplate();
			if(template!=null) {
				return template.getTextFormattingData();
			}
		} else {
			// Selected node is text.
		}
		return null;
	}

	/**
	 * @return
	 */
	public VpeSelectedNodeInfo computeSelectedNode() {
		List nodes = getCleanSelectedNodesList();
		if(nodes.size()==0) {
			return null;
		}
		return (VpeSelectedNodeInfo)nodes.get(0);
	}

	private List getCleanSelectedNodesList() {
		VpeSourceSelection selection = getSelection();
		if(selection==null) {
			return new ArrayList();
		}
		List dirtyNodesList = selection.getSelectedNodes();
		List cleanNodesList = getCleanSelectedNodesList(dirtyNodesList);
		return cleanNodesList;
	}

	private List getCleanSelectedNodesList(List dirtyNodesList) {
		ArrayList nodes = new ArrayList(dirtyNodesList.size());
		HashSet parentNodes = new HashSet();
		for(int i=0; i<dirtyNodesList.size(); i++) {
			VpeSelectedNodeInfo nodeInfo = (VpeSelectedNodeInfo)dirtyNodesList.get(i);
			Node node = nodeInfo.getNode();
			if(parentNodes.contains(node.getParentNode())) {
				// Ignore child node.
				parentNodes.add(node);
				continue;
			}
			parentNodes.add(node);
			if(node instanceof TextImpl) {
				TextImpl textNode = (TextImpl)node;
				String value = textNode.getNodeValue().trim();
				if(value==null || value.length()==0) {
					continue;
				}
			}
			nodes.add(nodeInfo);
		}
		return nodes;
	}

	private void disableAllFormatControllers() {
		for(int i=0; i<formatControllers.size(); i++) {
			((IFormatController)formatControllers.get(i)).setToolbarItemEnabled(false);
		}
	}

	/**
	 * @return current selection
	 */
	public VpeSourceSelection getSelection() {
		VpeSourceSelectionBuilder sourceSelectionBuilder = new VpeSourceSelectionBuilder(vpeController.getSourceEditor());
		return sourceSelectionBuilder.getSelection();
	}

	/**
	 * @param controller
	 */
	public void addFormatController(IFormatController controller) {
		formatControllers.add(controller);
	}

	/**
	 * @return
	 */
	public VpeController getVpeController() {
		return vpeController;
	}

	/**
	 * @return Returns the handlerFactory.
	 */
	public HandlerFactory getHandlerFactory() {
		return handlerFactory;
	}

	/**
	 * @return Returns selected node or parent of selected node if selected node is text.
	 */
	public Node getCurrentSelectedNode() {
		return currentSelectedElement;
	}

	/**
	 * @return Returns the currentFormatData.
	 */
	public FormatData getCurrentFormatData() {
		return currentFormatData;
	}

	/**
	 * @return Returns the currentSelectedNodeInfo.
	 */
	public VpeSelectedNodeInfo getCurrentSelectedNodeInfo() {
		return currentSelectedNodeInfo;
	}

	/**
	 * @return Returns the controllerNotifedSelectionChange.
	 */
	public boolean didControllerNotifySelectionChange() {
		return controllerNotifedSelectionChange;
	}

	/**
	 * @param controllerNotifedSelectionChange The controllerNotifedSelectionChange to set.
	 */
	public void setControllerNotifedSelectionChange(boolean controllerNotifedSelectionChange) {
		this.controllerNotifedSelectionChange = controllerNotifedSelectionChange;
	}

	public boolean isIgnoreSelectionChanges() {
		return ignoreSelectionChanges;
	}

	public void setIgnoreSelectionChanges(boolean ignoreSelectionChanges) {
		this.ignoreSelectionChanges = ignoreSelectionChanges;
	}
}