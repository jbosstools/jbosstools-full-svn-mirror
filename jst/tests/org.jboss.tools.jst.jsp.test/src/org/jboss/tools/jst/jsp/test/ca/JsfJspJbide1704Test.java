package org.jboss.tools.jst.jsp.test.ca;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.FindReplaceDocumentAdapter;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocument;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocumentRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegionList;
import org.eclipse.wst.xml.core.internal.regions.DOMRegionContext;
import org.jboss.tools.common.test.util.TestProjectProvider;
import org.jboss.tools.jst.jsp.contentassist.AutoContentAssistantProposal;
import org.jboss.tools.jst.jsp.test.TestUtil;
import org.jboss.tools.test.util.JobUtils;

public class JsfJspJbide1704Test extends ContentAssistantTestCase {
	TestProjectProvider provider = null;
	
	boolean makeCopy = false;
	private static final String PROJECT_NAME = "JsfJbide1704Test";
	private static final String PAGE_NAME = "/WebContent/pages/greeting";
	
	public static Test suite() {
		return new TestSuite(JsfJspJbide1704Test.class);
	}

	public void setUp() throws Exception {
		provider = new TestProjectProvider("org.jboss.tools.jst.jsp.test", null, PROJECT_NAME, makeCopy); 
		project = provider.getProject();
	}

	protected void tearDown() throws Exception {
		if(provider != null) {
			provider.dispose();
		}
	}

	public void testJspJbide1704 () {
		assertTrue("Test project \"" + PROJECT_NAME + "\" is not loaded", (project != null));
		doTestJsfJspJbide1704(PAGE_NAME + ".jsp");
	}
	
	public void testXhtmlJbide1704 () {
		assertTrue("Test project \"" + PROJECT_NAME + "\" is not loaded", (project != null));
		doTestJsfJspJbide1704(PAGE_NAME + ".xhtml");
	}
	
	private void doTestJsfJspJbide1704(String pageName) {

		openEditor(pageName);
		
		try {
			
			ICompletionProposal[] result= null;
			final IRegion reg = new FindReplaceDocumentAdapter(document).find(0,
					" var=\"msg\"", true, true, false, false);
			String errorMessage = null;

			final IContentAssistProcessor p= TestUtil.getProcessor(viewer, reg.getOffset(), contentAssistant);
			if (p != null) {
				result= p.computeCompletionProposals(viewer, reg.getOffset());
			}
			for (int k = 0; result != null && k < result.length; k++) {
				// There should not be a proposal of type Red.Proposal in the result
				assertFalse("Content Assistant peturned proposals of type (" + result[k].getClass().getName() + ").", (result[k] instanceof AutoContentAssistantProposal));
			}
	
		} catch (BadLocationException e) {
			fail(e.getMessage());
		}
		
		closeEditor();
	}

}
