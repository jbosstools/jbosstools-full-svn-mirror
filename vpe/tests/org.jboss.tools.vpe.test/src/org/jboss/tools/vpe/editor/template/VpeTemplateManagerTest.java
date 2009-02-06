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

import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;

/**
 * Test for Template Manager
 * @author mareshkau
 */
public class VpeTemplateManagerTest extends TestCase {

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
	
}
