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
package org.jboss.tools.vpe.editor.mapping;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.xulrunner.editor.XulRunnerEditor;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNode;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class VpeDomMapping {
	private Map<Node, VpeNodeMapping> sourceMap = new HashMap<Node, VpeNodeMapping>();
	private Map<nsIDOMNode, VpeNodeMapping> visualMap = new HashMap<nsIDOMNode, VpeNodeMapping>();
	private VpePageContext pageContext;
	
	public VpeDomMapping(VpePageContext pageContext) {
		this.pageContext = pageContext;
	}

	public void mapNodes(VpeNodeMapping nodeMapping) {
		sourceMap.put(nodeMapping.getSourceNode(), nodeMapping);
		if (nodeMapping.getVisualNode() != null) {
			visualMap.put(nodeMapping.getVisualNode(), nodeMapping);
			if (nodeMapping instanceof VpeElementMapping) {
				if (((VpeElementMapping)nodeMapping).getBorder() != null) {
					visualMap.put(((VpeElementMapping)nodeMapping).getBorder(), nodeMapping);
				}
			}
		}
	}
	
	public void clear(nsIDOMNode except) {
		Set<Map.Entry<nsIDOMNode, VpeNodeMapping>> entrySet = visualMap.entrySet();
		Iterator<Map.Entry<nsIDOMNode, VpeNodeMapping>> iter = entrySet.iterator();
		while (iter.hasNext()) {
			Map.Entry<nsIDOMNode, VpeNodeMapping> entry = iter.next();
			nsIDOMNode visualNode = entry.getKey();
			if (!visualNode.equals(except)) {
				iter.remove();
			}
		}
		sourceMap.clear();
	}
	
	public VpeNodeMapping getNodeMapping(Node node) {
		return getNodeMappingAtSourceNode(node);
	}

	public VpeNodeMapping getNodeMapping(nsIDOMNode node) {
		return getNodeMappingAtVisualNode(node);
	}
	
	public VpeNodeMapping getNodeMappingAtSourceNode(Node sourceNode) {
		if (sourceNode != null) {
			return sourceMap.get(sourceNode);
		}
		
		return null;
	}
	
	public VpeNodeMapping getNodeMappingAtVisualNode(nsIDOMNode visualNode) {

		
		Iterator<Map.Entry<nsIDOMNode, VpeNodeMapping>> iter = visualMap.entrySet().iterator();
	//Map.get() doesn't work correctly for this situation	
		while(iter.hasNext()){
			Map.Entry<nsIDOMNode,VpeNodeMapping> element = iter.next();
			nsIDOMNode key = element.getKey();
			if(visualNode!=null&&visualNode.equals(key)) {
				
				return element.getValue();
			}

		}
//		
//		if (visualNode != null) {
//			return visualMap.get(visualNode);
//		}
		
		return null;
	}
	
	public nsIDOMNode getVisualNode(Node sourceNode) {
		VpeNodeMapping nodeMapping = getNodeMapping(sourceNode);
		if (nodeMapping != null) {
			return nodeMapping.getVisualNode();
		}
		
		return null;
	}
	
	public Node getSourceNode(nsIDOMNode visualNode) {
		VpeNodeMapping nodeMapping = getNodeMapping(visualNode);
		if (nodeMapping != null) {
			return nodeMapping.getSourceNode();
		}
		
		return null;
	}
	
	public VpeNodeMapping getNearNodeMapping(Node node) {
		return getNearNodeMappingAtSourceNode(node);
	}
	
	public VpeNodeMapping getNearNodeMapping(nsIDOMNode node) {
		return getNearNodeMappingAtVisualNode(node);
	}

	
	public VpeNodeMapping getNearNodeMappingAtSourceNode(Node sourceNode) {
		VpeNodeMapping nodeMapping = getNodeMappingAtSourceNode(sourceNode);
		
		if(nodeMapping!=null){
			
			if(sourceNode!=null && nodeMapping != null) {			
				
				nsIDOMNode nearVisualNode = nodeMapping.getVisualNode();
				if(nearVisualNode instanceof nsIDOMElement){	
					
					nsIDOMElement visualElement = (nsIDOMElement) nearVisualNode;
					visualElement.removeAttribute(XulRunnerEditor.VPEFLASHERCOLORATTRIBUTE);
				}
			}
		}
		while (sourceNode != null && nodeMapping == null) {
			sourceNode = sourceNode.getParentNode();
			nodeMapping = getNodeMappingAtSourceNode(sourceNode);
			
			if(sourceNode!=null && nodeMapping != null) {			
				nsIDOMNode nearVisualNode = nodeMapping.getVisualNode();
				if(nearVisualNode instanceof nsIDOMElement){	
					nsIDOMElement visualElement = (nsIDOMElement) nearVisualNode;
					visualElement.setAttribute(XulRunnerEditor.VPEFLASHERCOLORATTRIBUTE, 
							XulRunnerEditor.flasherHiddentElementColor);
				}
			} 
		}
		return nodeMapping;
	}

	public VpeNodeMapping getNearNodeMappingAtVisualNode(nsIDOMNode visualNode) {
		VpeNodeMapping nodeMapping = getNodeMappingAtVisualNode(visualNode);
		while (visualNode != null && nodeMapping == null) {
			visualNode = visualNode.getParentNode();
			nodeMapping = getNodeMappingAtVisualNode(visualNode);
		}
		return nodeMapping;
	}
	
	public VpeNodeMapping getNearParentMapping(Node sourceNode) {
		VpeNodeMapping nodeMapping = null;
		if (sourceNode.getNodeType() == Node.ELEMENT_NODE) {
			nodeMapping = getNearNodeMapping(sourceNode);
		} else if (sourceNode.getNodeType() == Node.TEXT_NODE) {
			sourceNode = sourceNode.getParentNode();
			nodeMapping = getNodeMapping(sourceNode);
			while (sourceNode != null && sourceNode.getNodeType() != Node.DOCUMENT_NODE && nodeMapping == null) {
				sourceNode = sourceNode.getParentNode();
				nodeMapping = getNodeMapping(sourceNode);
			}
		}
		return nodeMapping;
	}

	public VpeNodeMapping getParentMapping(Node sourceNode) {
		VpeNodeMapping nodeMapping = null;
		sourceNode = sourceNode.getParentNode();
		nodeMapping = getNodeMapping(sourceNode);
		while (sourceNode != null && sourceNode.getNodeType() != Node.DOCUMENT_NODE && nodeMapping == null) {
			sourceNode = sourceNode.getParentNode();
			nodeMapping = getNodeMapping(sourceNode);
		}
		return nodeMapping;
	}

	public VpeElementMapping getNearElementMapping(Node node) {
		return getNearElementMappingAtSourceNode(node);
	}

	public VpeElementMapping getNearElementMapping(nsIDOMNode node) {
		return getNearElementMappingAtVisualNode(node);
	}
	
	public VpeElementMapping getNearElementMappingAtSourceNode(Node sourceNode) {
		VpeNodeMapping nodeMapping = getNearNodeMappingAtSourceNode(sourceNode);
		if (nodeMapping != null) {
		
//			switch (nodeMapping.getType()) {
//			case VpeNodeMapping.TEXT_MAPPING:
//				return getNearElementMappingAtSourceNode(nodeMapping.getSourceNode().getParentNode());
//			case VpeNodeMapping.ELEMENT_MAPPING:
//				return (VpeElementMapping)nodeMapping;
//			}
			if(nodeMapping instanceof VpeElementMapping) {
				return (VpeElementMapping)nodeMapping;
			} else {
				return getNearElementMappingAtSourceNode(nodeMapping.getSourceNode().getParentNode());
			}
		}
		return null;
	}

	public VpeElementMapping getNearElementMappingAtVisualNode(nsIDOMNode visualNode) {
		VpeNodeMapping nodeMapping = getNearNodeMappingAtVisualNode(visualNode);
		if (nodeMapping != null) {
//			switch (nodeMapping.getType()) {
//			case VpeNodeMapping.TEXT_MAPPING:
//				return getNearElementMappingAtSourceNode(nodeMapping.getSourceNode().getParentNode());
//			case VpeNodeMapping.ELEMENT_MAPPING:
//				return (VpeElementMapping)nodeMapping;
//			}
			if(nodeMapping instanceof VpeElementMapping) {
				return (VpeElementMapping)nodeMapping;
			} else {
				return getNearElementMappingAtSourceNode(nodeMapping.getSourceNode().getParentNode());
			}
		}
		return null;
	}
	
	public nsIDOMNode getNearVisualNode_(Node sourceNode) {
		VpeNodeMapping nodeMapping = getNearNodeMapping(sourceNode);
		if (nodeMapping != null) {
			return nodeMapping.getVisualNode();
		}
		return null;
	}
	
	public nsIDOMNode getNearVisualNode(Node sourceNode) {
		if (sourceNode == null) return null;
		VpeNodeMapping nodeMapping = getNearNodeMappingAtSourceNode(sourceNode);
		if (nodeMapping != null) {
			if (nodeMapping.getVisualNode() == null) {
	
				return getNearVisualNode(sourceNode.getParentNode());
			} else {
				
				return nodeMapping.getVisualNode();
			}
		}
		return null;
	}
	
	public Node getNearSourceNode(nsIDOMNode visualNode) {
		VpeNodeMapping nodeMapping = getNearNodeMapping(visualNode);
		if (nodeMapping != null) {
			return nodeMapping.getSourceNode();
		}
		return null;
	}
	
	public nsIDOMNode remove(Node sourceNode) {
		nsIDOMNode visualNode = getVisualNode(sourceNode);
//		if (visualNode != null) {
			removeImpl(sourceNode);
//		}
		return visualNode;
	}
	
	public void removeChildren(Node sourceNode) {
		NodeList sourceChildren = sourceNode.getChildNodes();
		if (sourceChildren != null) {
			int len = sourceChildren.getLength();
			for (int i = 0; i < len; i++) {
				removeImpl(sourceChildren.item(i));
			}
		}
	}
	
	private VpeNodeMapping removeImpl(Node sourceNode) {
		nsIDOMNode visualNode = null;
		VpeNodeMapping nodeMapping = (VpeNodeMapping)sourceMap.remove(sourceNode);
		if (nodeMapping != null) {
			visualNode = nodeMapping.getVisualNode();
			if (visualNode != null) {
				visualMap.remove(visualNode);
			}
			if (nodeMapping instanceof VpeElementMapping) { 
				VpeElementMapping elementMapping = (VpeElementMapping)nodeMapping;
//				Map xmlnsMap = elementMapping.getXmlnsMap();
//				if (xmlnsMap != null) {
//					for (Iterator iter = xmlnsMap.values().iterator(); iter.hasNext();) {
//						pageContext.setTaglib(((Integer)iter.next()).intValue(), null, null, true);
//					}
//					elementMapping.setXmlnsMap(null);
//				}
				if (elementMapping.getBorder() != null) {
					visualMap.remove(elementMapping.getBorder());
				}
				elementMapping.getTemplate().beforeRemove(pageContext, elementMapping.getSourceNode(), elementMapping.getVisualNode(), elementMapping.getData());
			}
		}
		removeChildren(sourceNode);
		return nodeMapping;
	}
	
	//for debug
	public void printMapping() {
		System.out.println("Source DOM Mapping ------------------------------------"); //$NON-NLS-1$
		Set entrySet = sourceMap.entrySet();
		Iterator iter = entrySet.iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry)iter.next();
			VpeNodeMapping nodeMapping = (VpeNodeMapping)entry.getValue(); 
			Node sourceNode = nodeMapping.getSourceNode();
			nsIDOMNode visualNode = nodeMapping.getVisualNode(); 
			System.out.println("sourceNode: " + sourceNode.getNodeName() + " (" + sourceNode.hashCode() + ")    visualNode: " + (visualNode != null ? visualNode.getNodeName() + " (" + visualNode.hashCode() + ")" : null)); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		}
		System.out.println("Visual DOM Mapping ------------------------------------"); //$NON-NLS-1$
		entrySet = visualMap.entrySet();
		iter = entrySet.iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry)iter.next();
			VpeNodeMapping nodeMapping = (VpeNodeMapping)entry.getValue(); 
			Node sourceNode = nodeMapping.getSourceNode();
			nsIDOMNode visualNode = nodeMapping.getVisualNode(); 
			System.out.println("sourceNode: " + (sourceNode != null ? sourceNode.getNodeName() + " (" + sourceNode.hashCode() + ")" : null) + "    visualNode: " + visualNode.getNodeName() + " (" + visualNode.hashCode() + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
		}
	}

	public Map<nsIDOMNode, VpeNodeMapping> getVisualMap() {
		return visualMap;
	}
	
	public Map<Node, VpeNodeMapping> getSourceMap() {
		return sourceMap;
	}
}
