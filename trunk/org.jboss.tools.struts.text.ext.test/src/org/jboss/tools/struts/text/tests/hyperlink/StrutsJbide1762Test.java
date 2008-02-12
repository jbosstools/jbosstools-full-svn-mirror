package org.jboss.tools.struts.text.tests.hyperlink;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.hyperlink.HyperlinkManager;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.jface.text.hyperlink.IHyperlinkDetector;
import org.eclipse.jface.text.hyperlink.IHyperlinkDetectorExtension2;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocument;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocumentRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegionList;
import org.eclipse.wst.sse.ui.internal.StructuredTextViewer;
import org.eclipse.wst.xml.core.internal.regions.DOMRegionContext;
import org.jboss.tools.common.model.ui.editor.EditorPartWrapper;
import org.jboss.tools.common.model.ui.texteditors.XMLTextEditorComponent;
import org.jboss.tools.common.test.util.TestProjectProvider;
import org.jboss.tools.jst.jsp.contentassist.RedHatCustomCompletionProposal;
import org.jboss.tools.jst.jsp.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.jst.jsp.jspeditor.JSPTextEditor;
import org.jboss.tools.jst.jsp.test.TestUtil;
import org.jboss.tools.test.util.xpl.EditorTestHelper;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class StrutsJbide1762Test extends TestCase {
	TestProjectProvider provider = null;
	IProject project = null;
	boolean makeCopy = false;
	private static final String PROJECT_NAME = "StrutsJbide1762Test";
	private static final String WEB_XML_NAME = "/WebContent/WEB-INF/web.xml";
	private static final String NODE_TO_FIND = "taglib-location";
	
	public static Test suite() {
		return new TestSuite(StrutsJbide1762Test.class);
	}

	public void setUp() throws Exception {
		provider = new TestProjectProvider("org.jboss.tools.struts.text.ext.test", null, PROJECT_NAME, makeCopy); 
		project = provider.getProject();
		Throwable exception = null;
		try {
			project.refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (Exception x) {
			exception = x;
			x.printStackTrace();
		}
		assertNull("An exception caught: " + (exception != null? exception.getMessage() : ""), exception);
	}

	protected void tearDown() throws Exception {
		if(provider != null) {
			provider.dispose();
		}
	}

	public void testStrutsJbide1762 () {
		try {
			EditorTestHelper.joinBackgroundActivities();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		assertTrue("Test project \"" + PROJECT_NAME + "\" is not loaded", (project != null));

		IFile webXmlFile = project.getFile(WEB_XML_NAME);

		
		assertTrue("The file \"" + WEB_XML_NAME + "\" is not found", (webXmlFile != null));
		assertTrue("The file \"" + WEB_XML_NAME + "\" is not found", (webXmlFile.exists()));

		FileEditorInput editorInput = new FileEditorInput(webXmlFile);
		Throwable exception = null;
		IEditorPart editorPart = null;
		try {
			editorPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(editorInput, "org.jboss.tools.common.model.ui.editor.EditorPartWrapper");
		} catch (PartInitException ex) {
			exception = ex;
			ex.printStackTrace();
			assertTrue("The XML Editor couldn't be initialized.", false);
		}

		EditorPartWrapper wrapperEditor = null;
		
		if (editorPart instanceof EditorPartWrapper)
			wrapperEditor = (EditorPartWrapper)editorPart;
		
		// Delay for 3 seconds so that
		// the Favorites view can be seen.
		try {
			EditorTestHelper.joinBackgroundActivities();
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue("Waiting for the jobs to complete has failed.", false);
		} 
		TestUtil.delay(3000);

//		ITextEditor textEditor = TestUtil.getActiveTextEditor(wrapperEditor);
		ITextEditor textEditor = getTextEditor(wrapperEditor);

		XMLTextEditorComponent xmlTextEditor = null;
		
		if (textEditor instanceof XMLTextEditorComponent) {
			xmlTextEditor = (XMLTextEditorComponent)textEditor;
		}
		
//		wrapperEditor.getJspEditor();
		StructuredTextViewer viewer = xmlTextEditor.getTextViewer();
		IDocument document = viewer.getDocument();
		SourceViewerConfiguration config = TestUtil.getSourceViewerConfiguration(xmlTextEditor);
		IHyperlinkDetector[] hyperlinkDetectors = (config == null ? null : config.getHyperlinkDetectors(viewer));

		assertTrue("Cannot get the Hyperlink Detectors for the editor for page \"" + WEB_XML_NAME + "\"", (hyperlinkDetectors != null));
		
		List<Region> regions = findTextRegions(document, NODE_TO_FIND);

		assertTrue("Cannot get the regions to test for the editor for page \"" + WEB_XML_NAME + "\"", (regions != null && regions.size() > 0));
		
		for (Region region : regions) {
			IHyperlink hyperlink = null;
			for (int i= 0; hyperlinkDetectors != null && i < hyperlinkDetectors.length; i++) {
				IHyperlinkDetector detector= hyperlinkDetectors[i];
				if (detector == null)
					continue;
				
				IHyperlink[] hyperlinks= detector.detectHyperlinks(viewer, region, false);
				if (hyperlinks == null)
					continue;
	
				Assert.isLegal(hyperlinks.length > 0);
	
				hyperlink = hyperlinks[0];
			}
			
			assertTrue("No Hyperlink found for the text region starting at offset" + region.getOffset() + ".", (hyperlink != null));
		}
		
		try {
			EditorTestHelper.joinBackgroundActivities();
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue("Waiting for the jobs to complete has failed.", false);
		} 

		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
		.closeEditor(editorPart, false);
	}

	protected ITextEditor getTextEditor(IEditorPart editor) {
		ITextEditor textEditor = null;
		if (editor instanceof ITextEditor)
			textEditor = (ITextEditor) editor;
		if (textEditor == null && editor != null)
			textEditor = (ITextEditor) editor.getAdapter(ITextEditor.class);
		return textEditor;
	}

	List<Region> findTextRegions(IDocument document, String nodeName) {
		List<Region> regions = new ArrayList<Region>();
		
		String documentContent = document.get();
		
		int index = 0;
		while ((index = documentContent.indexOf("<" + nodeName, index)) != -1) {
			int startNodeIndex = documentContent.indexOf(">", index);
			if (startNodeIndex == -1) {
				index = startNodeIndex;
				continue;
			}
			startNodeIndex++;
			
			int endNodeIndex = documentContent.indexOf("</" + nodeName, startNodeIndex);
			if (endNodeIndex == -1) {
				index = startNodeIndex;
				continue;
			}
			
//			System.out.println("Posting the Test Regions: " + documentContent.substring(startNodeIndex, endNodeIndex));
			for (int j = startNodeIndex; j < endNodeIndex; j++) {
				regions.add(new Region(j, 0));
			}
			index = documentContent.indexOf(">", endNodeIndex);
		}
		
		return regions;
	}

	
}
