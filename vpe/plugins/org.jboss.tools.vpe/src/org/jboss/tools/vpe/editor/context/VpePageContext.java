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
package org.jboss.tools.vpe.editor.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.wst.xml.core.internal.document.ElementImpl;
import org.jboss.tools.common.resref.core.ResourceReference;
import org.jboss.tools.jst.jsp.editor.IVisualContext;
import org.jboss.tools.jst.jsp.preferences.VpePreference;
import org.jboss.tools.jst.web.tld.TaglibData;
import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.VpeEditorPart;
import org.jboss.tools.vpe.editor.VpeSourceDomBuilder;
import org.jboss.tools.vpe.editor.VpeVisualDomBuilder;
import org.jboss.tools.vpe.editor.bundle.BundleMap;
import org.jboss.tools.vpe.editor.mapping.VpeDomMapping;
import org.jboss.tools.vpe.editor.util.XmlUtil;
import org.jboss.tools.vpe.resref.core.AbsoluteFolderReferenceList;
import org.jboss.tools.vpe.resref.core.RelativeFolderReferenceList;
import org.jboss.tools.vpe.resref.core.TaglibReferenceList;
import org.mozilla.interfaces.nsIDOMNode;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Contains the information on edited page.
 */

public class VpePageContext implements IVisualContext {
	private Set connectorDocuments = new HashSet();
	private BundleMap bundle;
	private Set bundleDependencySet = new HashSet();
	private VpeSourceDomBuilder sourceBuilder;
	private VpeVisualDomBuilder visualBuilder;
	private VpeEditorPart editPart;
	private nsIDOMNode currentVisualNode;
	/**
	 * Created to store custom element attributes and use it in time of
	 * rendering
	 * @author mareshkau
	 */
	private Map<String,String> customElementsAttributes;
	
	
	public VpePageContext(BundleMap bundle, VpeEditorPart editPart) {
		this.bundle = bundle;
		this.editPart = editPart;
	}
	
	public VpeSourceDomBuilder getSourceBuilder(){
		return sourceBuilder;
	}
	
	public boolean isAbsolutePosition(){
		if("yes".equals(VpePreference.USE_ABSOLUTE_POSITION.getValue()))return true; //$NON-NLS-1$
		else return false;
	}
	
	/**
	 * Adds attribute to element attribute map
	 * @param key
	 * @param value
	 */
	public void addAttributeInCustomElementsMap(String key, String value){
		getCustomElementsAttributes().put(key, value);
	}
	/**
	 * Removes attribute from custom attribute map
	 * @param sourceBuilder
	 */
	public void clearCustomElementAttributesMap() {
		getCustomElementsAttributes().clear();
	}

	public void setSourceDomBuilder(VpeSourceDomBuilder sourceBuilder) {
		this.sourceBuilder = sourceBuilder;
		refreshConnector();
	}
	
	public void refreshConnector() {
		//TODO we need to provide loading of kb here
	}
	
	public VpeVisualDomBuilder getVisualBuilder(){
		return visualBuilder;
	}
	
	public void setVisualDomBuilder(VpeVisualDomBuilder visualBuilder) {
		this.visualBuilder = visualBuilder;
	}
	
	public VpeDomMapping getDomMapping ()  {
		return visualBuilder.getDomMapping();
	}
	
	public void clearAll() {
		bundleDependencySet.clear();
		bundle.clearAll();
	}
	
	public void dispose() {
		for (Iterator iterator = connectorDocuments.iterator(); iterator.hasNext();) {
			IDocument document = (IDocument) iterator.next();
		}
		connectorDocuments.clear();
		connectorDocuments = null;
		bundle.dispose();
		clearAll();
		editPart=null;
		sourceBuilder=null;
		visualBuilder=null;
	}
	
	public BundleMap getBundle() {
		return bundle;
	}

	public void addBundleDependency(Element sourceNode) {
		//bundleDependencySet.add(sourceNode);
	}
	
	public void removeBundleDependency(Element sourceNode) {
		bundleDependencySet.remove(sourceNode);
	}
	
	public void refreshBundleValues() {
		if (getVisualBuilder() == null) return;
		List nodes = getVisualBuilder().getSourceNodes();
		//Iterator iterator = bundleDependencySet.iterator();
		Iterator iterator = nodes.iterator();
		while (iterator.hasNext()) {
			Object object = iterator.next();
			if (object instanceof Element) {
				Element sourceElement = (Element) object;
				visualBuilder.refreshBundleValues(sourceElement);
			}
		}
	}
	
	/**
	 * Checks for URI for source node was registred on page 
	 * @param sourceNode
	 * @return true - if uri was registred
	 * 			false- if uri doesn't was registered
	 */
	public boolean isCorrectNS(Node sourceNode) {
		String sourcePrefix = sourceNode.getPrefix();
		
		if (sourcePrefix == null || ((ElementImpl)sourceNode).isJSPTag()) {
			return true;
		}
		List<TaglibData> taglibs = XmlUtil.getTaglibsForNode(sourceNode,this);
		
		TaglibData sourceNodeTaglib = XmlUtil.getTaglibForPrefix(sourcePrefix, taglibs);
		
		if(sourceNodeTaglib!=null) {
			
			return true;
			}
		return false;
	}
	/**
	 * Returns 
	 * @param sourceNode
	 * @return
	 */
	
	public String getSourceTaglibUri(Node sourceNode) {
		
		String sourcePrefix = sourceNode.getPrefix();
		if (sourcePrefix == null || ((ElementImpl) sourceNode).isJSPTag()) {
			return null;
		}

		List<TaglibData> taglibs = XmlUtil.getTaglibsForNode(sourceNode,
				this);

		TaglibData sourceNodeTaglib = XmlUtil.getTaglibForPrefix(sourcePrefix,
				taglibs);

		if (sourceNodeTaglib == null) {

			return null;
		}
		String sourceNodeUri = sourceNodeTaglib.getUri();

		return sourceNodeUri;
	}

	public VpeEditorPart getEditPart() {
		return editPart;
	}


	public List<TaglibData> getIncludeTaglibs() {
		if (getEditPart() == null) {
			return new ArrayList<TaglibData>();
		}
		IEditorInput input = getEditPart().getEditorInput();
		IFile file = null;
		if (input instanceof IFileEditorInput) {
	        file = ((IFileEditorInput)input).getFile();
	    }
		ResourceReference[] resourceReferences = new ResourceReference[0];
		if (file != null) {
		    resourceReferences = TaglibReferenceList.getInstance().getAllResources(file);
		}
		//added by Max Areshkau Fix for JBIDE-2065
		List<TaglibData> taglibData = new ArrayList<TaglibData>();
		for (ResourceReference resourceReference : resourceReferences) {
			taglibData.add(new TaglibData(0,resourceReference.getLocation(), resourceReference.getProperties()));
		}
	    return taglibData;
	}

	public ResourceReference getRuntimeRelativeFolder(IFile file) {
		ResourceReference[] list = RelativeFolderReferenceList.getInstance().getAllResources(file);
		if (list.length > 0) {
			return list[list.length - 1];
		}
		return null;
	}

	public ResourceReference getRuntimeAbsoluteFolder(IFile file) {
		ResourceReference[] list = AbsoluteFolderReferenceList.getInstance().getAllResources(file);
		if (list.length > 0) {
			return list[list.length - 1];
		}
		return null;
	}

	public List<TaglibData> getTagLibs(Node sourceNode) {
		
		return XmlUtil.getTaglibsForNode(sourceNode, this);
	}

	/**
	 * @return the currentVisualNode
	 */
	public nsIDOMNode getCurrentVisualNode() {
		return this.currentVisualNode;
	}

	/**
	 * @param currentVisualNode the currentVisualNode to set
	 */
	public void setCurrentVisualNode(nsIDOMNode currentVisualNode) {
		this.currentVisualNode = currentVisualNode;
	}

	/**
	 * Processes display events to prevent eclipse froze
	 */
	public void processDisplayEvents() {
		
		Display display=null;
		display = Display.getCurrent();
		if(display==null) {
			display=Display.getDefault();
		}
		if(display!=null) {
			while(display.readAndDispatch()){}
		}			
	}

	/**
	 * @return the customElementsAttributes
	 */
	public Map<String, String> getCustomElementsAttributes() {
		if(this.customElementsAttributes==null) {
			this.customElementsAttributes =  new HashMap<String, String>();
		}
 		return this.customElementsAttributes;
	}

}