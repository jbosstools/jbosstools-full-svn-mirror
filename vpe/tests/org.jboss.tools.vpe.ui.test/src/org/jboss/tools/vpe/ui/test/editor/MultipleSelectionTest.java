/*******************************************************************************
 * Copyright (c) 2007-2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.ui.test.editor;

import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.part.FileEditorInput;
import org.jboss.tools.jst.jsp.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.vpe.base.test.TestUtil;
import org.jboss.tools.vpe.base.test.VpeTest;
import org.jboss.tools.vpe.ui.test.VpeUiTests;

/**
 * Junit test for JBIDE-8115
 * 
 * @author mareshkau
 *
 */
public class MultipleSelectionTest extends VpeTest{
 
	private static final String TEST_CASE="WebContent/pages/selection/jbide-8115-test-case.html"; //$NON-NLS-1$
	
	public MultipleSelectionTest(String name) {
		super(name);
	}

	public void testMultipleSelectionForSimplePage() throws CoreException, IOException{
        IFile file = (IFile) TestUtil.getComponentPath(TEST_CASE,
        		VpeUiTests.IMPORT_PROJECT_NAME);
        IEditorInput input = new FileEditorInput(file);
		JSPMultiPageEditor part = openEditor(input);
		ITextViewer viewer = part.getSourceEditor().getTextViewer();
		
		int startSelectionOffcet = TestUtil.getLinePositionOffcet(viewer, 6, 1);
		int endSelectionOffcet = TestUtil.getLinePositionOffcet(viewer, 9, 4);
		viewer.setSelectedRange(startSelectionOffcet, endSelectionOffcet);
	}
}
