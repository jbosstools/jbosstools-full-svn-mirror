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
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.ide.IDE;
import org.eclipse.wst.xml.core.internal.document.ElementImpl;
import org.jboss.tools.common.kb.KbConnectorFactory;
import org.jboss.tools.common.kb.KbConnectorType;
import org.jboss.tools.common.kb.wtp.WtpKbConnector;
import org.jboss.tools.jst.jsp.editor.IVisualContext;
import org.jboss.tools.jst.jsp.preferences.VpePreference;
import org.jboss.tools.jst.web.tld.TaglibData;
import org.jboss.tools.jst.web.tld.VpeTaglibListener;
import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.VpeEditorPart;
import org.jboss.tools.vpe.editor.VpeSourceDomBuilder;
import org.jboss.tools.vpe.editor.VpeVisualDomBuilder;
import org.jboss.tools.vpe.editor.bundle.BundleMap;
import org.jboss.tools.vpe.editor.css.AbsoluteFolderReferenceList;
import org.jboss.tools.vpe.editor.css.CSSReferenceList;
import org.jboss.tools.vpe.editor.css.RelativeFolderReferenceList;
import org.jboss.tools.vpe.editor.css.ResourceReference;
import org.jboss.tools.vpe.editor.css.TaglibReferenceList;
import org.jboss.tools.vpe.editor.mapping.VpeDomMapping;
import org.jboss.tools.vpe.editor.template.VpeCreationData;
import org.jboss.tools.vpe.editor.template.VpeTemplateManager;
import org.jboss.tools.vpe.editor.util.FileUtil;
import org.jboss.tools.vpe.editor.util.XmlUtil;
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
	WtpKbConnector connector;
	private nsIDOMNode currentVisualNode;
	
	//Added by Max Areshkau to increase perfomance of VPE JBIDE-675
	private Map<Node,VpeCreationData> vpeCash;
	
	public VpePageContext(VpeTemplateManager templateManager, BundleMap bundle, VpeEditorPart editPart) {
		this.bundle = bundle;
		this.editPart = editPart;
	}
	
	public VpeSourceDomBuilder getSourceBuilder(){
		return sourceBuilder;
	}
	
	public boolean isAbsolutePosition(){
		if("yes".equals(VpePreference.USE_ABSOLUTE_POSITION.getValue()))return true;
		else return false;
	}
	
	public void setSourceDomBuilder(VpeSourceDomBuilder sourceBuilder) {
		this.sourceBuilder = sourceBuilder;
		refreshConnector();
	}
	
	public void refreshConnector() {
		try {
			IDocument document = sourceBuilder.getStructuredTextViewer().getDocument();
			connector = (WtpKbConnector)KbConnectorFactory.getIntstance().createConnector(KbConnectorType.JSP_WTP_KB_CONNECTOR, document);
		} catch (Exception e) {
			VpePlugin.getPluginLog().logError(e);
		}
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
		//clean a cash nodes
		clearVpeCash();
	}
	
	public void dispose() {
		for (Iterator iterator = connectorDocuments.iterator(); iterator.hasNext();) {
			IDocument document = (IDocument) iterator.next();
			KbConnectorFactory.getIntstance().removeConnector(KbConnectorType.JSP_WTP_KB_CONNECTOR, document);
		}
		connectorDocuments.clear();
		connectorDocuments = null;
		bundle.dispose();
		clearAll();
		editPart=null;
		connector=null;
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

	public void openIncludeFile(String file) {
		IEditorInput input = editPart.getEditorInput();
		IWorkbenchPage workbenchPage = VpePlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
		try{
		    IFile f = FileUtil.getFile(input, file);
		    if (f != null) {
				IDE.openEditor(workbenchPage, f, true);
		    }
		}catch(Exception ex){
			VpePlugin.reportProblem(ex);
		}
	}

	public List<TaglibData> getIncludeTaglibs() {
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

	public ResourceReference[] getIncludeCss() {
		IEditorInput input = getEditPart().getEditorInput();
		IFile file = null;
		if (input instanceof IFileEditorInput) {
	        file = ((IFileEditorInput)input).getFile();
	    }
		ResourceReference[] resourceReferences = new ResourceReference[0];
		if (file != null) {
		    resourceReferences = CSSReferenceList.getInstance().getAllResources(file);
		}
		return resourceReferences;
	}

	public void installIncludeElements() {
//		ResourceReference[] list = getIncludeTaglibs();
//		for (int i = 0; i < list.length; i++) {
//			ResourceReference reference = list[i];
////			setTaglib(i, reference.getLocation(), reference.getProperties(), false);
//		}

		ResourceReference[] list = getIncludeCss();
		for (int i = 0; i < list.length; i++) {
			visualBuilder.addLinkNodeToHead(list[i].getLocation(), "yes");
		}
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

	
	private void fireTaglibChanged(VpeTaglibListener taglibListener, List newTaglibs, List delTaglibs) {
		Iterator iter = delTaglibs.iterator();
		while (iter.hasNext()) {
			TaglibData taglib = (TaglibData)iter.next();
			taglibListener.removeTaglib(taglib.getUri(), taglib.getPrefix());
		}
		iter = newTaglibs.iterator();
		while (iter.hasNext()) {
			TaglibData taglib = (TaglibData)iter.next();
			taglibListener.addTaglib(taglib.getUri(), taglib.getPrefix());
		}
	}
	
	public WtpKbConnector getConnector() {
		return connector;
	}

	public void addTaglibListener(VpeTaglibListener listener) {
		// TODO Auto-generated method stub
		
	}

	public List<TaglibData> getTagLibs(Node sourceNode) {
		
		return XmlUtil.getTaglibsForNode(sourceNode, this);
	}

	public void removeTaglibListener(VpeTaglibListener listener) {
		// TODO Auto-generated method stub
		
	}

	public void setReferenceNode(Node node) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @return the currentVisualNode
	 */
	public nsIDOMNode getCurrentVisualNode() {
		return currentVisualNode;
	}

	/**
	 * @param currentVisualNode the currentVisualNode to set
	 */
	public void setCurrentVisualNode(nsIDOMNode currentVisualNode) {
		this.currentVisualNode = currentVisualNode;
	}

	/**
	 * Removes  information about source node from vpe cash
	 * @param sourceNode
	 */
	public void removeNodeFromVpeCash(Node sourceNode) {
		
		getVpeCash().remove(sourceNode);
		Node parentNode = sourceNode.getParentNode();
		//we should on change remove also parent nodes because information for
		//this nodes doen't actual when we change child
		while(parentNode!=null) {
			getVpeCash().remove(parentNode);
			parentNode=parentNode.getParentNode();
		}
	}
	/**
	 * Clears all information in cash
	 */
	public void clearVpeCash() {
		
		getVpeCash().clear();
	}
	/**
	 * Checs is creation data exist in cash
	 * @param sourceNode
	 * @return true - if date exist 
	 * 		   false -otherwise
	 */
	public boolean isCreationDataExistInCash(Node sourceNode){
		
//		Iterator<Node> keys =	getVpeCash().keySet().iterator();
//
//		//Map.get() doesn't work correctly for this situation	
////			while(keys.hasNext()){
////				Node key= keys.next();
////				if(sourceNode.isEqu alNode(key)) {
////					return true;
////				}
////			}
////			return false;
		return getVpeCash().containsKey(sourceNode);
	}
	/**
	 * Inserts creation data into cash
	 * @param sourceNode
	 * @param creationData
	 */
	public void addCreationDataToCash(Node sourceNode,VpeCreationData creationData) {
		//TODO Max Areshkau JBIDE-675 Adds data to cash, think about cloning creationData
		getVpeCash().put(sourceNode, creationData);
	}
	/**
	 * Looks creates data in cash
	 * @param sourceNode
	 * @return returns creation data
	 */
	public VpeCreationData getVpeCreationDataFromCash(Node sourceNode) {
		
		return getVpeCash().get(sourceNode);
	}
	/**
	 * Return vpe Cash
	 * @return the vpeCash
	 */
	private Map<Node, VpeCreationData> getVpeCash() {
		
		if(vpeCash ==null) {
			
			vpeCash = new HashMap<Node, VpeCreationData>();	
		}
		return vpeCash;
	}

}