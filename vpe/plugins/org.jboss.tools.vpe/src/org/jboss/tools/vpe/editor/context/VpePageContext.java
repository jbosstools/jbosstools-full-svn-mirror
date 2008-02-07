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

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.ide.IDE;
import org.jboss.tools.common.kb.KbConnectorFactory;
import org.jboss.tools.common.kb.KbConnectorType;
import org.jboss.tools.common.kb.wtp.WtpKbConnector;
import org.jboss.tools.jst.jsp.editor.IVisualContext;
import org.jboss.tools.jst.jsp.preferences.VpePreference;
import org.jboss.tools.jst.web.tld.TaglibData;
import org.jboss.tools.jst.web.tld.VpeTaglibListener;
import org.jboss.tools.jst.web.tld.VpeTaglibManager;
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
import org.jboss.tools.vpe.editor.template.VpeTemplateManager;
import org.jboss.tools.vpe.editor.util.FileUtil;
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
		bundleDependencySet.add(sourceNode);
	}
	
	public void removeBundleDependency(Element sourceNode) {
		bundleDependencySet.remove(sourceNode);
	}
	
	public void refreshBundleValues() {
		Iterator iterator = bundleDependencySet.iterator();
		while (iterator.hasNext()) {
			Element sourceElement = (Element) iterator.next();
			visualBuilder.refreshBundleValues(sourceElement);
		}
	}
	
//	public boolean isCorrectNS(Node sourceNode) {
//		String sourcePrefix = sourceNode.getPrefix();
//		if (sourcePrefix == null || ((ElementImpl)sourceNode).isJSPTag()) {
//			return true;
//		}
//		for (int i = 0; i < taglibs.size(); i++) {
//			TaglibData taglib = (TaglibData)taglibs.get(i);
//			if (sourcePrefix.equals(taglib.getPrefix())) {
//				return true;
//			}
//		}
//		return false;
//	}
	
//	public String getSourceTaglibUri(Node sourceNode) {
//		String sourcePrefix = sourceNode.getPrefix();
//		if (sourcePrefix == null || ((ElementImpl)sourceNode).isJSPTag()) {
//			return null;
//		}
//		for (int i = 0; i < taglibs.size(); i++) {
//			TaglibData taglib = (TaglibData)taglibs.get(i);
//			if (sourcePrefix.equals(taglib.getPrefix())) {
//				return taglib.getUri();
//			}
//		}
//		return null;
//	}

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

	public ResourceReference[] getIncludeTaglibs() {
		IEditorInput input = getEditPart().getEditorInput();
		IFile file = null;
		if (input instanceof IFileEditorInput) {
	        file = ((IFileEditorInput)input).getFile();
	    }
		ResourceReference[] resourceReferences = new ResourceReference[0];
		if (file != null) {
		    resourceReferences = TaglibReferenceList.getInstance().getAllResources(file);
		}
	    return resourceReferences;
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
		ResourceReference[] list = getIncludeTaglibs();
		for (int i = 0; i < list.length; i++) {
			ResourceReference reference = list[i];
//			setTaglib(i, reference.getLocation(), reference.getProperties(), false);
		}

		list = getIncludeCss();
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

//	public List<TaglibData> getTagLibs() {
//		List<TaglibData> clone = new ArrayList<TaglibData>();
//		Iterator iter = taglibs.iterator();
//		while (iter.hasNext()) {
//			TaglibData taglib = (TaglibData)iter.next();
//			if (!taglib.inList(clone)) {
//				clone.add(taglib);
//			}
//		}
//		return clone;
//	}
	
//	private boolean  buildTaglibsDifferences(List newTaglibs, List delTaglibs) {
//		Iterator lastIter = lastTaglibs.iterator();
//		while (lastIter.hasNext()) {
//			TaglibData oldTaglib = (TaglibData)lastIter.next();
//			Iterator newIter = newTaglibs.iterator();
//			while (newIter.hasNext()) {
//				if (oldTaglib.isEquals((TaglibData)newIter.next())) {
//					newIter.remove();
//					oldTaglib = null;
//					break;
//				}
//			}
//			if (oldTaglib != null) {
//				delTaglibs.add(oldTaglib);
//			}
//		}
//		return newTaglibs.size() > 0 || delTaglibs.size() > 0;
//	}
	
//	public void fireTaglibsChanged() {
//		List newTaglibs = getTagLibs();
//		List delTaglibs = new ArrayList();
//		if (buildTaglibsDifferences(newTaglibs, delTaglibs)) {
//			if (VpeDebug.PRINT_SOURCE_MUTATION_EVENT) {
//				System.out.println(">>> TaglibsChanged");
//			}
//			for (int i = 0; i < taglibListeners.length; i++) {
//				taglibListeners[i].taglibPrefixChanged(null);
//				fireTaglibChanged(taglibListeners[i], newTaglibs, delTaglibs);
//			}
//			lastTaglibs = getTagLibs();
//		}
//		taglibChanged = false;
//	}
	
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
	
//	public boolean isTaglibChanged() {
//		if (!taglibChanged) return false;
//		List newTaglibs = getTagLibs();
//		List delTaglibs = new ArrayList();
//		return buildTaglibsDifferences(newTaglibs, delTaglibs);
//	}
	
	public WtpKbConnector getConnector() {
		return connector;
	}

	public void addTaglibListener(VpeTaglibListener listener) {
		// TODO Auto-generated method stub
		
	}

	public List<TaglibData> getTagLibs() {
		// TODO Auto-generated method stub
		return null;
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

//	boolean registerTaglibs(WtpKbConnector wtpKbConnector, VpeTaglibManager taglibManager, IDocument document) {
//		if(wtpKbConnector == null) return false;
//		TLDCMDocumentManager manager = TaglibController.getTLDCMDocumentManager(document);
//		if(taglibManager != null) {
//			List list = taglibManager.getTagLibs();
//			if(list != null) {
//				Iterator it = list.iterator();
//				while(it.hasNext()) {
//					TaglibData data = (TaglibData)it.next();
//					IEditorInput ei = editPart.getEditorInput();
//					TLDRegisterHelper.registerTld(data, (JspWtpKbConnector)wtpKbConnector, document, ei);
//				}
//				return true;
//			}
//		}
//		if(manager != null) {
//			List list = manager.getTaglibTrackers();
//			for (int i = 0; i < list.size(); i++) {
//				TaglibTracker tracker = (TaglibTracker)list.get(i);
//				if(tracker == null) continue;
//				String version = TLDVersionHelper.getTldVersion(tracker);
//				KbTldResource resource = new KbTldResource(tracker.getURI(), "", tracker.getPrefix(), version);
//				wtpKbConnector.registerResource(resource);
//			}
//			return true;
//		}
//		return false;
//	}
	
//	public nsIDOMNode getCurrentVisualNode() {
//		return currentVisualNode;
//	}
//	
//	public void setCurrentVisualNode(nsIDOMNode currentVisualNode) {
//		this.currentVisualNode = currentVisualNode;
//	}

}