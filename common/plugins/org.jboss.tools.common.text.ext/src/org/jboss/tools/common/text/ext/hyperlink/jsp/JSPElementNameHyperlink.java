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
package org.jboss.tools.common.text.ext.hyperlink.jsp;

import java.text.MessageFormat;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IRegion;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.jboss.tools.common.model.XModel;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.util.FindObjectHelper;
import org.jboss.tools.common.text.ext.ExtensionsPlugin;
import org.jboss.tools.common.text.ext.hyperlink.AbstractHyperlink;
import org.jboss.tools.common.text.ext.hyperlink.xpl.Messages;
import org.jboss.tools.common.text.ext.util.StructuredModelWrapper;
import org.jboss.tools.common.text.ext.util.TaglibManagerWrapper;
import org.jboss.tools.jst.web.tld.ITaglibMapping;
import org.jboss.tools.jst.web.tld.IWebProject;
import org.jboss.tools.jst.web.tld.WebProjectFactory;
import org.w3c.dom.Document;

/**
 * @author Jeremy
 */
public class JSPElementNameHyperlink extends AbstractHyperlink {

	/** 
	 * @see com.ibm.sse.editor.AbstractHyperlink#doHyperlink(org.eclipse.jface.text.IRegion)
	 */
	protected void doHyperlink(IRegion region) {
		XModelObject object = getFilename(region);
		if(object != null)  {
			FindObjectHelper.findModelObject(object, FindObjectHelper.IN_EDITOR_ONLY);
		} else {
			openFileFailed();
		}
	}
	
	protected final String JAR_FILE_PROTOCOL = "jar:file:/";//$NON-NLS-1$
	
    /*
     * @see com.ibm.sse.editor.hyperlink.AbstractHyperlink#openFileInEditor(java.lang.String)
     */
    protected void openFileInEditor(String fileString) {
        
        if (fileString.startsWith(JAR_FILE_PROTOCOL)) {
			fileString = fileString.substring(JAR_FILE_PROTOCOL.length());
        
			IEditorInput jarEditorInput = createEditorInput(fileString);
	        IEditorPart part = openFileInEditor(jarEditorInput,  fileString);
	        if (part == null) openFileFailed();
		} else {
			super.openFileInEditor(fileString);    
		}
    }

	private XModelObject getFilename(IRegion region) {
		XModel xModel = getXModel();
		if (xModel == null) return null;

		StructuredModelWrapper smw = new StructuredModelWrapper();
		try {
			smw.init(getDocument());
			Document xmlDocument = smw.getDocument();
			if (xmlDocument == null) return null;
			
			String nodePrefix = getTagPrefix(region);
			if (nodePrefix == null) 
				return null;

			TaglibManagerWrapper tmw = new TaglibManagerWrapper();
			tmw.init(getDocument(), region.getOffset());
			if(!tmw.exists()) return null;
			String uri = tmw.getUri(nodePrefix);

			if (uri == null) return null;
			
			IWebProject wp = WebProjectFactory.instance.getWebProject(xModel);
			if (wp == null) return null;
			
			ITaglibMapping tm = wp.getTaglibMapping();
			if (tm == null) return null;
			tm.invalidate();
			return tm.getTaglibObject(uri);
		} finally {
			smw.dispose();
		}
	}
	
	private String getTagPrefix(IRegion region) {
		if (region == null)
			return null;
		
		String nodeName;
		try {
			nodeName = getDocument().get(region.getOffset(), region.getLength());
		} catch (BadLocationException e) {
			// Ignore
			return null;
		}
		if (nodeName.indexOf(":") != -1) {
			String nodePrefix = nodeName.substring(0, nodeName.indexOf(":"));
			if (nodePrefix != null && nodePrefix.length() > 0) 		
				return nodePrefix;
		}
		return null;
	}
	
	IRegion fLastRegion = null;
	/** 
	 * @see com.ibm.sse.editor.AbstractHyperlink#doGetHyperlinkRegion(int)
	 */
	protected IRegion doGetHyperlinkRegion(int offset) {
		fLastRegion = JSPElementNameHyperlinkPartitioner.getRegion(getDocument(), offset);
		return fLastRegion;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see IHyperlink#getHyperlinkText()
	 */
	public String getHyperlinkText() {
		String tagPrefix = getTagPrefix(fLastRegion);
		if (tagPrefix == null)
			return  MessageFormat.format(Messages.OpenA, Messages.TagLibrary);
		
		return MessageFormat.format(Messages.OpenTagLibraryForPrefix, tagPrefix);
	}

}
