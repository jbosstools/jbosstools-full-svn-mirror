package org.jboss.tools.jst.jsp.test.ca;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.FindReplaceDocumentAdapter;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.jboss.tools.common.test.util.TestProjectProvider;
import org.jboss.tools.jst.jsp.contentassist.AutoContentAssistantProposal;
import org.jboss.tools.jst.jsp.test.TestUtil;
import org.jboss.tools.test.util.JobUtils;

public class JsfJspJbide2437Test extends ContentAssistantTestCase {
	TestProjectProvider provider = null;
	
	boolean makeCopy = false;
	private static final String PROJECT_NAME = "JsfJbide1704Test";
	private static final String PAGE_NAME = "/WebContent/pages/greeting1.xhtml";
	
	public static Test suite() {
		return new TestSuite(JsfJspJbide2437Test.class);
	}

	public void setUp() throws Exception {
		provider = new TestProjectProvider("org.jboss.tools.jst.jsp.test", null, PROJECT_NAME, makeCopy); 
		project = provider.getProject();
	}

	protected void tearDown() throws Exception {
		provider.dispose();
	}

	public void testJsfJspJbide2437 () throws BadLocationException {
		openEditor(PAGE_NAME);
		JobUtils.waitForIdle();
		ICompletionProposal[] result= null;
		String errorMessage = null;
		
		IRegion reg = new FindReplaceDocumentAdapter(document).find(0, "/templates/common.xhtml", true, true, false, false);
		
		IContentAssistProcessor p= TestUtil.getProcessor(viewer, reg.getOffset(), contentAssistant);
		assertNotNull("Cannot obtain IContentAssistProcessor instance",p);
		result= p.computeCompletionProposals(viewer, reg.getOffset());
		
		boolean bELProposalsFound = false;
		boolean bTemplatePathProposalsFound = false;
		for (int k = 0; result != null && k < result.length &&
				(!bELProposalsFound || !bTemplatePathProposalsFound); k++) {
			if (result[k] instanceof AutoContentAssistantProposal) {
				AutoContentAssistantProposal proposal = (AutoContentAssistantProposal)result[k];
				
				// Test the display string for the proposals - it has to shown the thmplate path beginning or EL-expression beginning
				// because the CA is started the calculation from the very beginning of the attribute value.
				String dispString = proposal.getDisplayString();
				
				assertFalse("The CA proposal returned NULL display string.", (dispString == null));
				
				if (dispString.startsWith("#{") || dispString.startsWith("${")) {
					bELProposalsFound = true;
				} else if (dispString.indexOf("/") != -1) {
					bTemplatePathProposalsFound = true;
				}
			}
		}
			
		// There should be proposals for template paths 
		assertTrue("Content Assistant peturned no proposals for template paths.", bTemplatePathProposalsFound);
		// There should be proposals for EL-expresions
		
		// JBIDE-4341: JstJspAllTests/testJsfJspJbide1813Test failing with missing applicationScope in completions
		// Due to the current EL-prompting rules there is no EL-proposals allowed if there are no EL-starting char 
		// sequence in the text. So, The following code line is commented out. This is the subject to rollback in the future.
		//
//		assertTrue("Content Assistant peturned no proposals for EL.", bELProposalsFound);
		
		closeEditor();
	}

}
