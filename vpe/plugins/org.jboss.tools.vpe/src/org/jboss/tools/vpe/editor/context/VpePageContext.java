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
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jst.jsp.core.internal.contentmodel.TaglibController;
import org.eclipse.jst.jsp.core.internal.contentmodel.tld.TLDCMDocumentManager;
import org.eclipse.jst.jsp.core.internal.contentmodel.tld.TaglibTracker;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.ide.IDE;
import org.eclipse.wst.xml.core.internal.document.ElementImpl;
import org.jboss.tools.common.kb.KbConnectorFactory;
import org.jboss.tools.common.kb.KbConnectorType;
import org.jboss.tools.common.kb.KbTldResource;
import org.jboss.tools.common.kb.wtp.JspWtpKbConnector;
import org.jboss.tools.common.kb.wtp.TLDVersionHelper;
import org.jboss.tools.common.kb.wtp.WtpKbConnector;
import org.jboss.tools.common.model.XModel;
import org.jboss.tools.common.model.project.IModelNature;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.jst.jsp.editor.IVisualContext;
import org.jboss.tools.jst.jsp.editor.TLDRegisterHelper;
import org.jboss.tools.jst.jsp.preferences.VpePreference;
import org.jboss.tools.jst.web.tld.TaglibData;
import org.jboss.tools.jst.web.tld.VpeTaglibListener;
import org.jboss.tools.jst.web.tld.VpeTaglibManager;
import org.jboss.tools.vpe.VpeDebug;
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

// vitali: Attention!!! this is refactoring intermediate version
// because of bugfixing
// http://jira.jboss.com/jira/browse/JBIDE-788 and
// http://jira.jboss.com/jira/browse/JBIDE-1457

//public class VpePageContext implements IVisualContext {
public class VpePageContext {
	
	// vitali: time solution - set connector from JSPTextEditorPageContext
	public WtpKbConnector getConnector() {
		return getVisualContext().getConnector();
	}
	IVisualContext visualContext;
	public void setVisualContext(IVisualContext visualContext) {
		this.visualContext = visualContext;
		VpeTemplateManager.getInstance().initVisualContext(visualContext);
	}
	public IVisualContext getVisualContext() {
		return visualContext;
	}
	
	
	
	private ArrayList taglibs = new ArrayList();
	private Map taglibMap = new HashMap();
	private VpeTaglibListener[] taglibListeners = new VpeTaglibListener[0];
	private BundleMap bundle;
	private Set bundleDependencySet = new HashSet();
	private VpeSourceDomBuilder sourceBuilder;
	private VpeVisualDomBuilder visualBuilder;
	private VpeEditorPart editPart;
	private List lastTaglibs = new ArrayList();
	private boolean taglibChanged = false;
	WtpKbConnector connector;	
	private nsIDOMNode currentVisualNode;
	
	public VpePageContext(BundleMap bundle, VpeEditorPart editPart) {
		this.bundle = bundle;
		this.editPart = editPart;
	}
	
	//+
	public void setSourceBuilder(VpeSourceDomBuilder sourceBuilder) {
		this.sourceBuilder = sourceBuilder;
	}
	//+
	public VpeSourceDomBuilder getSourceBuilder() {
		return sourceBuilder;
	}
	
	//+
	public VpeVisualDomBuilder getVisualBuilder(){
		return visualBuilder;
	}
	//+
	public void setVisualBuilder(VpeVisualDomBuilder visualBuilder) {
		this.visualBuilder = visualBuilder;
	}
	
	// vitali: delete this function with the class - use VpeController->getVisualBuilder()
	public VpeDomMapping getDomMapping ()  {
		return visualBuilder.getDomMapping();
	}
	
	// vitali: delete this function with the class
	public void clearAll() {
		taglibs.clear();
		taglibMap.clear();
		bundleDependencySet.clear();
		bundle.clearAll();
	}
	
	// vitali: delete this function with the class
	public void dispose() {
		bundle.dispose();
		clearAll();
	}
	
	// vitali: delete this function with the class - from all places
	//-
	public void setTaglib(int id, String newUri, String newPrefix, boolean ns) {
		for (int i = 0; i < taglibs.size(); i++) {
			TaglibData taglib = (TaglibData)taglibs.get(i);
			if (taglib.getId() == id) {
				if (newUri != null && newPrefix != null) {
					if (!newUri.equals(taglib.getUri()) || !newPrefix.equals(taglib.getPrefix())) {
						taglibs.set(i, new TaglibData(id, newUri, newPrefix, ns));
						rebuildTaglibMap();
					}
				} else {
					taglibs.remove(i);
					rebuildTaglibMap();
				}
				return;
			}
		}
		if (newUri != null && newPrefix != null) {
			taglibs.add(new TaglibData(id, newUri, newPrefix, ns));
			rebuildTaglibMap();
		}
	}

	//+
	public String getTemplateTaglibPrefix(String sourceTaglibPrefix) {
		// vitali TODO: this is wrong temporary way - get rid of it 
		return (String) taglibMap.get(sourceTaglibPrefix);
		// vitali TODO: this is right way to use - for refactoring
		//return getVisualContext().getTemplateTaglibPrefix(sourceTaglibPrefix);
	}
	
	//-
	private void rebuildTaglibMap() {
		/**/
		// vitali TODO: this is wrong temporary way - get rid of it 
		taglibMap.clear();
		VpeTemplateManager templateManager = VpeTemplateManager.getInstance();
		Set prefixSet = new HashSet();
		for (int i = 0; i < taglibs.size(); i++) {
			TaglibData taglib = (TaglibData)taglibs.get(i);
			String prefix = taglib.getPrefix();
			if (!prefixSet.contains(prefix)) {
				String templatePrefix = templateManager.getTemplateTaglibPrefix(taglib.getUri());
				if (templatePrefix != null) {
					taglibMap.put(prefix, templatePrefix);
				}
				prefixSet.add(prefix);
			}
		}
		taglibChanged = true;
		/** /
		try {
			registerTaglibs(connector, this, getSourceBuilder().getStructuredTextViewer().getDocument());
		} catch (Exception e) {
			VpePlugin.getPluginLog().logError(e);
		}
		/**/
	}

	/** /
	// vitali: parent
	public void addTaglibListener(VpeTaglibListener listener) {
		if (listener != null) {
			VpeTaglibListener[] newTaglibListeners = new VpeTaglibListener[taglibListeners.length + 1];
			System.arraycopy(taglibListeners, 0, newTaglibListeners, 0, taglibListeners.length);
			taglibListeners = newTaglibListeners;
			taglibListeners[taglibListeners.length - 1] = listener;
		}
	}
	
	// vitali: parent
	public void removeTaglibListener(VpeTaglibListener listener) {
		if (listener == null || taglibListeners.length == 0) return;
		int index = -1;
		for (int i = 0; i < taglibListeners.length; i++) {
			if (listener == taglibListeners[i]){
				index = i;
				break;
			}
		}
		if (index == -1) return;
		if (taglibListeners.length == 1) {
			taglibListeners = new VpeTaglibListener[0];
			return;
		}
		VpeTaglibListener[] newTaglibListeners = new VpeTaglibListener[taglibListeners.length - 1];
		System.arraycopy(taglibListeners, 0, newTaglibListeners, 0, index);
		System.arraycopy(taglibListeners, index + 1, newTaglibListeners, index, taglibListeners.length - index - 1);
		taglibListeners = newTaglibListeners;
	}
	/**/
	
	// vitali: delete this function with the class - save and getBundle() from other place
	// for examle visualBuilder
	public BundleMap getBundle() {
		return bundle;
	}

	// vitali: delete this function with the class - save and getBundle() from other place
	// for examle visualBuilder
	public void addBundleDependency(Element sourceNode) {
		bundleDependencySet.add(sourceNode);
	}
	
	// vitali: delete this function with the class - save and getBundle() from other place
	// for examle visualBuilder
	public void removeBundleDependency(Element sourceNode) {
		bundleDependencySet.remove(sourceNode);
	}
	
	/** /
	// vitali: parent
	public void refreshBundleValues() {
		Iterator iterator = bundleDependencySet.iterator();
		while (iterator.hasNext()) {
			Element sourceElement = (Element) iterator.next();
			visualBuilder.refreshBundleValues(sourceElement);
		}
	}
	/**/
	
	// vitali: put this function in to JSPTextEditorPageContext
	// VpeController ask JSPTextEditorPageContext via sourceEditor
	public boolean isCorrectNS(Node sourceNode) {
		String sourcePrefix = sourceNode.getPrefix();
		if (sourcePrefix == null || ((ElementImpl)sourceNode).isJSPTag()) {
			return true;
		}
		for (int i = 0; i < taglibs.size(); i++) {
			TaglibData taglib = (TaglibData)taglibs.get(i);
			if (sourcePrefix.equals(taglib.getPrefix())) {
				return true;
			}
		}
		return false;
	}
	
	// vitali: 2 places - RichFacesDataDefinitionListTemplate & VpeController
	// should implement this function & get taglibs from sourceEditor->JSPTextEditorPageContext
	public String getSourceTaglibUri(Node sourceNode) {
		String sourcePrefix = sourceNode.getPrefix();
		if (sourcePrefix == null || ((ElementImpl)sourceNode).isJSPTag()) {
			return null;
		}
		for (int i = 0; i < taglibs.size(); i++) {
			TaglibData taglib = (TaglibData)taglibs.get(i);
			if (sourcePrefix.equals(taglib.getPrefix())) {
				return taglib.getUri();
			}
		}
		return null;
	}

	// vitali: 24 places - !!! use other way to get VpeEditorPart
	public VpeEditorPart getEditPart() {
		return editPart;
	}

	// vitali: this is not right place to the function - put in into editPart - VpeEditorPart
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

	// vitali: public - can be protected!
	protected ResourceReference[] getIncludeTaglibs() {
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

	// vitali: public - can be protected!
	protected ResourceReference[] getIncludeCss() {
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

	// vitali: 1 place - VpeVisualDomBuilder - put this into VpeVisualDomBuilder
	// and getIncludeTaglibs and getIncludeCss
	public void installIncludeElements() {
		ResourceReference[] list = getIncludeTaglibs();
		for (int i = 0; i < list.length; i++) {
			ResourceReference reference = list[i];
			setTaglib(i, reference.getLocation(), reference.getProperties(), false);
		}

		list = getIncludeCss();
		for (int i = 0; i < list.length; i++) {
			visualBuilder.addLinkNodeToHead(list[i].getLocation(), "yes");
		}
	}

	/** /
	// vitali: parent
	public List<TaglibData> getTagLibs() {
		List<TaglibData> clone = new ArrayList<TaglibData>();
		Iterator iter = taglibs.iterator();
		while (iter.hasNext()) {
			TaglibData taglib = (TaglibData)iter.next();
			if (!taglib.inList(clone)) {
				clone.add(taglib);
			}
		}
		return clone;
	}
	/**/
	
	// vitali: public
	private boolean  buildTaglibsDifferences(List newTaglibs, List delTaglibs) {
		Iterator lastIter = lastTaglibs.iterator();
		while (lastIter.hasNext()) {
			TaglibData oldTaglib = (TaglibData)lastIter.next();
			Iterator newIter = newTaglibs.iterator();
			while (newIter.hasNext()) {
				if (oldTaglib.isEquals((TaglibData)newIter.next())) {
					newIter.remove();
					oldTaglib = null;
					break;
				}
			}
			if (oldTaglib != null) {
				delTaglibs.add(oldTaglib);
			}
		}
		return newTaglibs.size() > 0 || delTaglibs.size() > 0;
	}
	
	/** /
	// vitali: 5 places - all in VpeController - 
	public void fireTaglibsChanged() {
		List newTaglibs = getTagLibs();
		List delTaglibs = new ArrayList();
		if (buildTaglibsDifferences(newTaglibs, delTaglibs)) {
			if (VpeDebug.PRINT_SOURCE_MUTATION_EVENT) {
				System.out.println(">>> TaglibsChanged");
			}
			for (int i = 0; i < taglibListeners.length; i++) {
				taglibListeners[i].taglibPrefixChanged(null);
				fireTaglibChanged(taglibListeners[i], newTaglibs, delTaglibs);
			}
			lastTaglibs = getTagLibs();
		}
		taglibChanged = false;
	}
	/**/
	
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
	
	// vitali: 1 place VpeController - 
	public boolean isTaglibChanged() {
		if (!taglibChanged) return false;
		//vitali
		List newTaglibs = null;//getTagLibs();
		List delTaglibs = new ArrayList();
		return buildTaglibsDifferences(newTaglibs, delTaglibs);
	}
	
	/** /
	// vitali: parent
	public WtpKbConnector getConnector() {
		return connector;
	}
	/**/

	boolean registerTaglibs(WtpKbConnector wtpKbConnector, VpeTaglibManager taglibManager, IDocument document) {
		if(wtpKbConnector == null) return false;
		TLDCMDocumentManager manager = TaglibController.getTLDCMDocumentManager(document);
		if(taglibManager != null) {
			List list = taglibManager.getTagLibs();
			if(list != null) {
				Iterator it = list.iterator();
				while(it.hasNext()) {
					TaglibData data = (TaglibData)it.next();
					IEditorInput ei = editPart.getEditorInput();
					TLDRegisterHelper.registerTld(data, (JspWtpKbConnector)wtpKbConnector, document, ei);
				}
				return true;
			}
		}
		if(manager != null) {
			List list = manager.getTaglibTrackers();
			for (int i = 0; i < list.size(); i++) {
				TaglibTracker tracker = (TaglibTracker)list.get(i);
				if(tracker == null) continue;
				String version = TLDVersionHelper.getTldVersion(tracker);
				KbTldResource resource = new KbTldResource(tracker.getURI(), "", tracker.getPrefix(), version);
				wtpKbConnector.registerResource(resource);
			}
			return true;
		}
		return false;
	}
	
	// vitali: 2 places - VpeDataTableColumnCreator & VpeDataTableCreator
	public nsIDOMNode getCurrentVisualNode() {
		return currentVisualNode;
	}
	
	// vitali: 8 places in VpePreviewDomBuilder & VpeVisualDomBuilder
	// use - setCurrentVisualNode(XXX)
	//      - getCurrentVisualNode()
	// then - setCurrentVisualNode(null)
	// just use other way to send current node as parameter
	public void setCurrentVisualNode(nsIDOMNode currentVisualNode) {
		this.currentVisualNode = currentVisualNode;
	}

}
