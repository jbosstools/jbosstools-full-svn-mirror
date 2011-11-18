package org.jboss.tools.vpe.html.test.jbide;

import org.eclipse.core.resources.IFile;
import org.jboss.tools.jst.jsp.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.vpe.base.test.TestUtil;
import org.jboss.tools.vpe.base.test.VpeTest;
import org.jboss.tools.vpe.html.test.HtmlAllTests;

public class TestNPEinPreviewJbide10178 extends VpeTest {

	private static final String TEST_PAGE_NAME="jbide10178/cssInHtml.html"; //$NON-NLS-1$
	private static final String TEST_PAGE_NAME2="jbide10178/veryLongCssString.html"; //$NON-NLS-1$
	
	public TestNPEinPreviewJbide10178(String name) {
		super(name);
	}

	public void testNPEinPreviewWhileDecodingUrl() throws Throwable {
		setException(null);
		IFile ifile = (IFile) TestUtil.getComponentPath(
				TEST_PAGE_NAME, HtmlAllTests.IMPORT_PROJECT_NAME);
		JSPMultiPageEditor jspMultiPageEditor = openEditor(ifile);
		jspMultiPageEditor.pageChange(jspMultiPageEditor.getPreviewIndex());
		TestUtil.waitForJobs();
		/*
		 * Check error log for exception.
		 */
		if (getException() != null) {
			throw getException();
		}
	}
	
	public void testNPEinPreviewWhileDecodingUrlInLongCssString() throws Throwable {
		setException(null);
		IFile ifile = (IFile) TestUtil.getComponentPath(
				TEST_PAGE_NAME2, HtmlAllTests.IMPORT_PROJECT_NAME);
		JSPMultiPageEditor jspMultiPageEditor = openEditor(ifile);
		jspMultiPageEditor.pageChange(jspMultiPageEditor.getPreviewIndex());
		TestUtil.waitForJobs();
		/*
		 * Check error log for exception.
		 */
		if (getException() != null) {
			throw getException();
		}
	}
}
