

package org.jboss.tools.seam.ui.test.jbide;


import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.wst.sse.ui.StructuredTextViewerConfiguration;
import org.jboss.tools.common.test.util.TestProjectProvider;
import org.jboss.tools.jst.jsp.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.jst.jsp.jspeditor.JSPTextEditor;
import org.jboss.tools.jst.jsp.test.ca.ContentAssistantTestCase;
import org.jboss.tools.vpe.ui.test.TestUtil;


/**
 * The Class JBide2227TestCase.
 */
public class JBide2227TestCase extends ContentAssistantTestCase {

    /** The Constant IMPORT_PROJECT_NAME. */
    private static final String IMPORT_PROJECT_NAME = "TestSeamELContentAssist";

    /** The Constant PAGE_1. */
    private static final String PAGE_1 = "/WebContent/jbide2227/withEl.xhtml";

    /** The Constant CA_NAME. */
    private static final String CA_NAME = "org.eclipse.wst.html.HTML_DEFAULT";

    /** The provider. */
    private TestProjectProvider provider;

    /** The project. */
    private IProject project;

    /** The make copy. */
    private boolean makeCopy;

    /** The Constant PAGE_2. */
    private static final String PAGE_2 = "/WebContent/jbide2227/withoutEl.xhtml";


    /**
     * The Constructor.
     */
    public JBide2227TestCase() {
        super();
    }

    /**
     * Suite.
     * 
     * @return the test
     */
    public static Test suite() {
        return new TestSuite(JBide2227TestCase.class);
    }

    /**
     * Sets the up.
     * 
     * @throws Exception the exception
     */
    public void setUp() throws Exception {
        provider = new TestProjectProvider("org.jboss.tools.seam.ui.test", null, IMPORT_PROJECT_NAME, makeCopy);
        project = provider.getProject();
        Throwable exception = null;
        try {
            project.refreshLocal(IResource.DEPTH_INFINITE, null);
        } catch (Exception x) {
            exception = x;
            x.printStackTrace();
        }
        assertNull("An exception caught: " + (exception != null ? exception.getMessage() : ""), exception);
    }

    /**
     * Tear down.
     * 
     * @throws Exception the exception
     */
    protected void tearDown() throws Exception {
        if (provider != null) {
            provider.dispose();
        }
    }

    /**
     * Test content assist with el.
     * 
     * @throws Throwable the throwable
     */
    public void testContentAssistWithEl() throws Throwable {
        // wait
        TestUtil.waitForJobs();
        // set exception
        setException(null);
        // Tests CA

        check(CA_NAME, PAGE_1, 576, 114);

        // check exception
        if (getException() != null) {

            throw getException();
        }
    }

    /**
     * Base checkof CA.
     * 
     * @param testPagePath the test page path
     * @param position the position
     * @param caName the ca name
     * @param numberOfProposals the number of proposals
     * 
     * @throws CoreException the core exception
     */
    protected void check(String caName, String testPagePath, int position, int numberOfProposals) throws CoreException {
        // get test page path
        IFile file = project.getFile(testPagePath);
        assertNotNull("Could not open specified file " + file.getFullPath(), file);

        IEditorInput input = new FileEditorInput(file);

        assertNotNull("Editor input is null", input);

        // open and get editor
        JSPMultiPageEditor part = openEditor(input);

        // sets cursor position
        part.getSourceEditor().getTextViewer().getTextWidget().setCaretOffset(position);
        TestUtil.waitForJobs();
        TestUtil.delay(2000);
        SourceViewerConfiguration sourceViewerConfiguration = ((JSPTextEditor) part.getSourceEditor())
                .getSourceViewerConfigurationForTest();
        // errase errors which can be on start of editor(for example xuklunner
        // not found)
        setException(null);
        StructuredTextViewerConfiguration stvc = (StructuredTextViewerConfiguration) sourceViewerConfiguration;
        IContentAssistant iContentAssistant = stvc.getContentAssistant((ISourceViewer) part.getSourceEditor().getAdapter(
                ISourceViewer.class));
        assertNotNull(iContentAssistant);
        IContentAssistProcessor iContentAssistProcessor = iContentAssistant.getContentAssistProcessor(caName);
        assertNotNull(iContentAssistProcessor);
        ICompletionProposal[] results = iContentAssistProcessor
                .computeCompletionProposals(part.getSourceEditor().getTextViewer(), position);
        assertNotNull(results);
        assertEquals(numberOfProposals, results.length);

        closeEditors();
        TestUtil.delay(1000L);
    }

    /**
     * Test content assist without el.
     * 
     * @throws Throwable the throwable
     */
    public void testContentAssistWithoutEl() throws Throwable {
        TestUtil.waitForJobs();

        setException(null);

        check(CA_NAME, PAGE_2, 580, 11);
    }

}