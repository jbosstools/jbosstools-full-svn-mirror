/*******************************************************************************
 * Copyright (c) 2007-2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.jst.jsp.test.ca;

import java.util.List;

import junit.framework.TestCase;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocument;
import org.eclipse.wst.sse.ui.internal.StructuredTextViewer;
import org.jboss.tools.common.text.ext.util.Utils;
import org.jboss.tools.jst.jsp.contentassist.AutoContentAssistantProposal;
import org.jboss.tools.jst.jsp.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.jst.jsp.jspeditor.JSPTextEditor;
import org.jboss.tools.jst.jsp.test.TestUtil;
import org.jboss.tools.test.util.WorkbenchUtils;

public class ContentAssistantTestCase extends TestCase {
	protected IProject project = null;
	protected JSPMultiPageEditor jspEditor = null;
	protected JSPTextEditor jspTextEditor = null;
	protected StructuredTextViewer viewer = null;
	protected IContentAssistant contentAssistant = null;
	protected IDocument document = null;

	public void openEditor(String fileName) {
		IEditorPart editorPart = WorkbenchUtils.openEditor(project.getName()+"/"+ fileName); //$NON-NLS-1$
//		System.out.println("openEditor: " + project.getName()+"/"+ fileName);
		if (editorPart instanceof JSPMultiPageEditor)
			jspEditor = (JSPMultiPageEditor) editorPart;

		jspTextEditor = jspEditor.getJspEditor();
		viewer = jspTextEditor.getTextViewer();
		document = viewer.getDocument();
		SourceViewerConfiguration config = TestUtil
				.getSourceViewerConfiguration(jspTextEditor);
		contentAssistant = (config == null ? null : config
				.getContentAssistant(viewer));

		assertTrue(
				"Cannot get the Content Assistant instance for the editor for page \"" //$NON-NLS-1$
						+ fileName + "\"", (contentAssistant != null)); //$NON-NLS-1$

		assertTrue("The IDocument is not instance of IStructuredDocument for page \"" //$NON-NLS-1$
				+ fileName + "\"", //$NON-NLS-1$
				(document instanceof IStructuredDocument));

	}

	public ICompletionProposal[] checkProposals(String fileName, int offset, String[] proposals, boolean exactly) {
        return checkProposals(fileName, null, offset, proposals, exactly, true);
    }

	public ICompletionProposal[] checkProposals(String fileName, String substring, int offset, String[] proposals, boolean exactly) {
		return checkProposals(fileName, substring, offset, proposals, exactly, false);
	}
	public ICompletionProposal[] checkProposals(String fileName, String substring, int offset, String[] proposals, boolean exactly, boolean excludeELProposalsFromExactTest){
//		System.out.println("checkProposals >>> Enterring");
//		System.out.println("checkProposals >>> invoking openEditor() for " + fileName);
		openEditor(fileName);
//		System.out.println("checkProposals >>> openEditor() is invoked for " + fileName);

        int position = 0;
        if (substring != null) {
            String documentContent = document.get();
            position = documentContent.indexOf(substring);
        }

		List<ICompletionProposal> res = TestUtil.collectProposals(contentAssistant, viewer, position+offset);

        assertTrue("Content Assistant returned no proposals", (res != null && res.size() > 0)); //$NON-NLS-1$

        // for (int i = 0; i < result.length; i++) {
        // System.out.println("proposal - "+result[i].getDisplayString());
        // }

        ICompletionProposal[] result = res.toArray(new ICompletionProposal[res.size()]);
        int foundCounter = 0;
        for (int i = 0; i < proposals.length; i++) {
        	boolean found = compareProposal(proposals[i], result);
        	if (found)
        		foundCounter++;
            assertTrue("Proposal " + proposals[i] + " not found!", found ); //$NON-NLS-1$ //$NON-NLS-2$
        }

        if (exactly) {
        	if (excludeELProposalsFromExactTest) {
        		assertTrue("Some other proposals were found!", foundCounter == proposals.length); //$NON-NLS-1$
        	} else {
                assertTrue("Some other proposals were found!", result.length == proposals.length); //$NON-NLS-1$
        	}
        }

//        System.out.println("checkProposals <<< Exiting");
        return result;
	}

	public boolean compareProposal(String proposalName, ICompletionProposal[] proposals){
		for (int i = 0; i < proposals.length; i++) {
			if (proposals[i] instanceof AutoContentAssistantProposal) {
				AutoContentAssistantProposal ap = (AutoContentAssistantProposal)proposals[i];
				String replacementString = ap.getReplacementString().toLowerCase();
				if (replacementString.equalsIgnoreCase(proposalName)) return true;
				
				// For a tag proposal there will be not only the the tag name but all others characters like default attributes, tag ending characters and so on
				String[] replacementStringParts = replacementString.split(" "); //$NON-NLS-1$
				if (replacementStringParts != null && replacementStringParts.length > 0) {
					if (replacementStringParts[0].equalsIgnoreCase(proposalName)) return true;
				}
				
				// for an attribute proposal there will be a pare of attribute-value (i.e. attrName="attrValue")
				replacementStringParts = replacementString.split("="); //$NON-NLS-1$
				if (replacementStringParts != null && replacementStringParts.length > 0) {
					if (replacementStringParts[0].equalsIgnoreCase(proposalName)) return true;
				}
				
				// for an Unclosed EL the closing character is appended to the proposal string (i.e. person} )
				// perform case sensitive compare operation
				replacementStringParts = replacementString.split("}"); //$NON-NLS-1$
				if (replacementStringParts != null && replacementStringParts.length > 0) {
					if (replacementStringParts[0].equals(proposalName)) return true;
				}
				
				// For an attribute value proposal there will be the quote characters
				replacementString = Utils.trimQuotes(replacementString);
				if (replacementString.equalsIgnoreCase(proposalName)) return true;
			
			} else {
				if(proposals[i].getDisplayString().toLowerCase().equals(proposalName.toLowerCase())) return true;
			}
		}
		return false;
	}

	public void closeEditor() {
		if (jspEditor != null) {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getActivePage().closeEditor(jspEditor, false);
			jspEditor = null;
		}
	}

	/**
	 * @return the project
	 */
	public IProject getProject() {
		return project;
	}

	/**
	 * @param project the project to set
	 */
	public void setProject(IProject project) {
		this.project = project;
	}

	/**
	 * @return the jspEditor
	 */
	public JSPMultiPageEditor getJspEditor() {
		return jspEditor;
	}

	/**
	 * @param jspEditor the jspEditor to set
	 */
	public void setJspEditor(JSPMultiPageEditor jspEditor) {
		this.jspEditor = jspEditor;
	}

	/**
	 * @return the jspTextEditor
	 */
	public JSPTextEditor getJspTextEditor() {
		return jspTextEditor;
	}

	/**
	 * @param jspTextEditor the jspTextEditor to set
	 */
	public void setJspTextEditor(JSPTextEditor jspTextEditor) {
		this.jspTextEditor = jspTextEditor;
	}

	/**
	 * @return the viewer
	 */
	public StructuredTextViewer getViewer() {
		return viewer;
	}

	/**
	 * @param viewer the viewer to set
	 */
	public void setViewer(StructuredTextViewer viewer) {
		this.viewer = viewer;
	}

	/**
	 * @return the contentAssistant
	 */
	public IContentAssistant getContentAssistant() {
		return contentAssistant;
	}

	/**
	 * @param contentAssistant the contentAssistant to set
	 */
	public void setContentAssistant(IContentAssistant contentAssistant) {
		this.contentAssistant = contentAssistant;
	}

	/**
	 * @return the document
	 */
	public IDocument getDocument() {
		return document;
	}

	/**
	 * @param document the document to set
	 */
	public void setDocument(IDocument document) {
		this.document = document;
	}
}