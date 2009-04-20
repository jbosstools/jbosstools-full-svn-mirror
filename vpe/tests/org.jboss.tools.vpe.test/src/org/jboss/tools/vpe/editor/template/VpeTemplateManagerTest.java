/*******************************************************************************
 * Copyright (c) 2007-2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.editor.template;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.core.runtime.IPath;
import org.jboss.tools.vpe.test.VpeTestPlugin;

/**
 * Test for Template Manager
 * @author mareshkau
 */
public class VpeTemplateManagerTest extends TestCase {
	private static final String MANDATORY_TEMPLATE_FILE_ENTRY
			= "<vpe:templates"; 
	private List<VpeAnyData> oldTemplates;
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();		
		oldTemplates = VpeTemplateManager.getInstance().getAnyTemplates();
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		VpeTemplateManager.getInstance().setAnyTemplates(oldTemplates);
		super.tearDown();	
	}

	public void testSetAnyTemplates() {
		List<VpeAnyData> clearList = Collections.emptyList();
		VpeTemplateManager.getInstance().setAnyTemplates(clearList);
		VpeAnyData data = new VpeAnyData(
				"tag-name1", //$NON-NLS-1$
				"tag-value", //$NON-NLS-1$
				"color:red" //$NON-NLS-1$
				);
		
		VpeTemplateManager.getInstance().setAnyTemplate(data);
		
		data = new VpeAnyData(
				"tag-name2", //$NON-NLS-1$
				"tag-value", //$NON-NLS-1$
				"color:white" //$NON-NLS-1$
				);
		
		VpeTemplateManager.getInstance().setAnyTemplate(data);
		List<VpeAnyData> templates = VpeTemplateManager.getInstance().getAnyTemplates();
		assertEquals(2, templates.size());
	}
	
	public void testReload() {
		VpeTemplateManager.getInstance().reload();
	}
	
	public void testGetDefTemplate() {
		VpeTemplateManager.getInstance().setDefTemplate(null);
		VpeTemplate template = VpeTemplateManager.getInstance().getDefTemplate();
		assertNotNull("TemplateManager.getDefTemplate() cannot return null",template); //$NON-NLS-1$
	}
	
	/**
	 * Tests {@link VpeTemplateManager#getAutoTemplates()}
	 */
	public void testGetAutoTemplates() {
		final String workspacePath = VpeTestPlugin.getDefault()
				.getStateLocation().makeAbsolute().removeLastSegments(1)
				.toPortableString();
		try {
			final IPath autoTemplate1 = VpeTemplateManager
					.getAutoTemplates().makeAbsolute();
			
			// check if the file is in the workspace
			assertTrue(
					autoTemplate1.toPortableString().startsWith(workspacePath));
			
			// ensure the file is deleted
			assertTrue("Cannot delete user's templates file.", //$NON-NLS-1$
					autoTemplate1.toFile().delete());

			final IPath autoTemplate2 = VpeTemplateManager
					.getAutoTemplates().makeAbsolute();

			// check if the new file has the same path as the old one
			assertEquals("The path of the user's templates file" //$NON-NLS-1$
						+ "is changed.", //$NON-NLS-1$ 
					autoTemplate1, autoTemplate2);
			
			final BufferedReader in = new BufferedReader(
					new FileReader(autoTemplate2.toFile()));
			boolean hasMandatoryEntry = false;
			String line;
			while (!hasMandatoryEntry 
					&& (line = in.readLine()) != null) {
				if (line.contains(MANDATORY_TEMPLATE_FILE_ENTRY)) {
					hasMandatoryEntry = true;
				}
			}
			// check if the file contains MANDATORY_TEMPLATE_FILE_ENTRY
			assertTrue(
					"File '" + autoTemplate2.toPortableString() //$NON-NLS-1$
						+ "' does not contain string '" //$NON-NLS-1$
						+ MANDATORY_TEMPLATE_FILE_ENTRY + "'.", //$NON-NLS-1$
					hasMandatoryEntry);

			in.close();
			
			// delete the file on exit and check if it is deleted
			assertTrue("Cannot delete user's templates file.", //$NON-NLS-1$
					autoTemplate2.toFile().delete());
		} catch (IOException e) {
			fail(e.toString());
		}
	}
}
