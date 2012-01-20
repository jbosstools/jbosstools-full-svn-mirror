/******************************************************************************* 
 * Copyright (c) 2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.ui.bot.test.editor;

public class VerificationOfNameSpacesTest extends VPEEditorTestCase{

	private static String testText = "<jsp:root\n" + //$NON-NLS-1$
			"xmlns:jsp=\"http://java.sun.com/JSP/Page\n" + //$NON-NLS-1$
			"xmlns:public=\"http://www.jspcentral.com/tags\"\n" + //$NON-NLS-1$
			"version=\"1.2\">\n" + //$NON-NLS-1$
			"...\n" + //$NON-NLS-1$
			"</jsp:root>"; //$NON-NLS-1$
	
	public void testVerificationOfNameSpaces() throws Throwable{
		
		//Test open page
		
		openPage();
		
		setEditor(bot.editorByTitle(TEST_PAGE).toTextEditor());
		setEditorText(getEditor().getText());
		
		//Test clear source
		
		getEditor().setFocus();
		bot.menu("Edit").menu("Select All").click(); //$NON-NLS-1$ //$NON-NLS-2$
		delay();
		bot.menu("Edit").menu("Delete").click(); //$NON-NLS-1$ //$NON-NLS-2$
		
		//Test insert test text
		
		getEditor().setText(testText);
		getEditor().save();
		waitForBlockingJobsAcomplished(VISUAL_UPDATE);
		performContentTestByDocument("VerificationOfNameSpaces.xml", bot.multiPageEditorByTitle(TEST_PAGE)); //$NON-NLS-1$
	
	}
	
}
