package org.jboss.tools.jst.jsp.test.ca;

import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.jboss.tools.jst.jsp.test.TestUtil;
import org.jboss.tools.test.util.JobUtils;
import org.jboss.tools.test.util.TestProjectProvider;

public class MissingKBBuilderTest extends ContentAssistantTestCase{
	TestProjectProvider provider = null;
	boolean makeCopy = false;
	private static final String PROJECT_NAME = "MissingKBBuilderTest";
	private static final String PAGE_NAME = "/WebContent/pages/inputname.xhtml";
	
	public void setUp() throws Exception {
		provider = new TestProjectProvider("org.jboss.tools.jst.jsp.test", null, PROJECT_NAME, makeCopy); 
		project = provider.getProject();
	}

	protected void tearDown() throws Exception {
		if(provider != null) {
			provider.dispose();
		}
	}
	
	public void testCAForIDTest(){
		String[] proposals = {
			"resources"
		};

		ICompletionProposal[] ps = checkProposals(PAGE_NAME, "<f:loadBundle basename=\"\" var=\"msg\" />", 24);
		System.out.println("-1->" + (ps == null ? 0 : ps.length));
		JobUtils.waitForIdle(2000);
		ps = checkProposals(PAGE_NAME, "<f:loadBundle basename=\"\" var=\"msg\" />", 24, proposals, false);
		System.out.println("-2->" + (ps == null ? 0 : ps.length));
	}

	protected ICompletionProposal[] checkProposals(String fileName, String substring, int offset){
		openEditor(fileName);

        int position = 0;
        if (substring != null) {
            String documentContent = document.get();
            position = documentContent.indexOf(substring);
        }

        ICompletionProposal[] result = null;

        IContentAssistProcessor p = TestUtil.getProcessor(viewer, position + offset, contentAssistant);
        if (p != null) {
            try {
                result = p.computeCompletionProposals(viewer, position + offset);
            } catch (Throwable x) {
                x.printStackTrace();
            }
        }

		return result;
	}

}