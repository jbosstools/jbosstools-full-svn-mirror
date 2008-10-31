package org.jboss.tools.vpe.editor.template;

import java.util.List;

import junit.framework.TestCase;

public class VpeTemplateManagerTest extends TestCase {

	public void testSetAnyTemplates() {
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
	}
	
	public void testGetAnyTemplates() {
		List<VpeAnyData> templates = VpeTemplateManager.getInstance().getAnyTemplates();
		assertEquals(4, templates.size());
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
