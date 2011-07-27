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
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.wst.xml.core.internal.document.ElementImpl;
import org.jboss.tools.common.resref.core.ResourceReference;
import org.jboss.tools.jst.jsp.bundle.BundleMap;
import org.jboss.tools.jst.jsp.editor.IVisualContext;
import org.jboss.tools.jst.jsp.preferences.VpePreference;
import org.jboss.tools.jst.web.tld.TaglibData;
import org.jboss.tools.vpe.editor.VpeEditorPart;
import org.jboss.tools.vpe.editor.VpeIncludeInfo;
import org.jboss.tools.vpe.editor.VpeSourceDomBuilder;
import org.jboss.tools.vpe.editor.VpeVisualDomBuilder;
import org.jboss.tools.vpe.editor.mapping.VpeDomMapping;
import org.jboss.tools.vpe.editor.util.ElService;
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
@SuppressWarnings("restriction")
public class VpePageContext implements IVisualContext {
	
	public static final String CUSTOM_ELEMENTS_ATTRS = "customElementsAttributes"; //$NON-NLS-1$
	public static final String CURRENT_VISUAL_NODE = "currentVisualNode"; //$NON-NLS-1$
	public static final String RES_REFERENCES = "resourceReferences"; //$NON-NLS-1$
	
	private BundleMap bundle;
	private VpeSourceDomBuilder sourceBuilder;
	private VpeVisualDomBuilder visualBuilder;
	private VpeEditorPart editPart;
	protected Map<String, Object> values = new HashMap<String, Object>();

	public VpePageContext(BundleMap bundle, VpeEditorPart editPart) {
		this.bundle = bundle;
		this.editPart = editPart;
	}

	public VpeSourceDomBuilder getSourceBuilder() {
		return sourceBuilder;
	}

	public static boolean isAbsolutePosition() {
		if ("yes".equals(VpePreference.USE_ABSOLUTE_POSITION.getValue())) { //$NON-NLS-1$
			return true;
		}
		return false;
	}

	/**
	 * Adds attribute to element attribute map
	 * 
	 * @param key
	 * @param value
	 */
	public void addAttributeInCustomElementsMap(String key, String value) {
		getCustomElementsAttributes().put(key, value);
	}

	/**
	 * Removes attribute from custom attribute map
	 * 
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
		// TODO we need to provide loading of kb here
	}

	public VpeVisualDomBuilder getVisualBuilder() {
		return visualBuilder;
	}

	public void setVisualDomBuilder(VpeVisualDomBuilder visualBuilder) {
		this.visualBuilder = visualBuilder;
	}

	public void refreshResReferences() {
		if (visualBuilder != null ) {
			final VpeIncludeInfo vii = visualBuilder.getCurrentIncludeInfo();
			if (vii != null && (vii.getStorage() instanceof IFile)) {
				final IFile file = (IFile)vii.getStorage();
				ResourceReference[] rr = ElService.getAllResources(file);
				rr = ElService.sortReferencesByScope2(rr);
				putValue(RES_REFERENCES, rr);
			}
		}
	}

	public VpeDomMapping getDomMapping() {
		return visualBuilder.getDomMapping();
	}

	public void clearAll() {
		bundle.clearAll();
		clearValues();
	}

	public void dispose() {
		bundle.dispose();
		clearAll();
		editPart = null;
		sourceBuilder = null;
		visualBuilder = null;
	}

	public BundleMap getBundle() {
		return bundle;
	}

	public void refreshBundleValues() {
		if (getVisualBuilder() == null) {
			return;
		}
		List<Node> nodes = getVisualBuilder().getSourceNodes();
		// Iterator iterator = bundleDependencySet.iterator();
		for (Node node : nodes) {
			if (node instanceof Element) {
				Element sourceElement = (Element) node;
				visualBuilder.refreshBundleValues(sourceElement);
			}
		}
	}

	/**
	 * Checks for URI for source node was registred on page
	 * 
	 * @param sourceNode
	 * @return true - if uri was registred false- if uri doesn't was registered
	 */
	public boolean isCorrectNS(Node sourceNode) {
		String sourcePrefix = sourceNode.getPrefix();
		if (sourcePrefix == null || ((ElementImpl) sourceNode).isJSPTag()) {
			return true;
		}
		List<TaglibData> taglibs = XmlUtil.getTaglibsForNode(sourceNode, this);
		TaglibData sourceNodeTaglib = XmlUtil.getTaglibForPrefix(sourcePrefix, taglibs);
		if (sourceNodeTaglib != null) {
			return true;
		}
		return false;
	}

	/**
	 * Returns
	 * 
	 * @param sourceNode
	 * @return
	 */
	public String getSourceTaglibUri(Node sourceNode) {
		String sourcePrefix = sourceNode.getPrefix();
		if (sourcePrefix == null || ((ElementImpl) sourceNode).isJSPTag()) {
			return null;
		}
		List<TaglibData> taglibs = XmlUtil.getTaglibsForNode(sourceNode, this);
		TaglibData sourceNodeTaglib = XmlUtil.getTaglibForPrefix(sourcePrefix, taglibs);
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
			file = ((IFileEditorInput) input).getFile();
		}
		ResourceReference[] resourceReferences = new ResourceReference[0];
		if (file != null) {
			resourceReferences = TaglibReferenceList.getInstance().getAllResources(file);
		}
		// added by Max Areshkau Fix for JBIDE-2065
		List<TaglibData> taglibData = new ArrayList<TaglibData>();
		for (ResourceReference resourceReference : resourceReferences) {
			taglibData.add(new TaglibData(0, resourceReference.getLocation(), resourceReference.getProperties()));
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
		return (nsIDOMNode)getValue(CURRENT_VISUAL_NODE);
	}

	/**
	 * @param currentVisualNode
	 *            the currentVisualNode to set
	 */
	public void setCurrentVisualNode(nsIDOMNode currentVisualNode) {
		putValue(CURRENT_VISUAL_NODE, currentVisualNode);
	}

	/**
	 * Processes display events to prevent eclipse froze
	 */
	public static void processDisplayEvents() {
		Display display = Display.getCurrent();
		if (display == null) {
			display = Display.getDefault();
		}
		if (display != null) {
			while (display.readAndDispatch()) {
			}
		}
	}

	/**
	 * @return the customElementsAttributes
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getCustomElementsAttributes() {
		Map<String, String> res = null;
		Object obj = values.get(CUSTOM_ELEMENTS_ATTRS);
		if (obj == null || !(obj instanceof HashMap)) {
			res = new HashMap<String, String>();
			values.put(CUSTOM_ELEMENTS_ATTRS, res);
		} else {
			res = (Map<String, String>)obj;
		}
		return res;
	}
	
	/**
	 * @param key
	 * @param val
	 * @return false in case of element replacement
	 */
	public boolean putValue(String key, Object val) {
		final boolean res = !values.containsKey(key);
		values.put(key, val);
		return res;
	}

	public Object removeValue(String key) {
		return values.remove(key);
	}

	public Object getValue(String key) {
		return values.get(key);
	}

	public void clearValues() {
		values.clear();
	}

	public String[] getValueNames() {
		return values.keySet().toArray(new String[0]);
	}

}
