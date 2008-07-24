package org.jboss.tools.vpe.editor.template;

import java.util.List;

import org.jboss.tools.vpe.editor.util.TemplateManagingUtil;

import junit.framework.TestCase;

public class VpeTemplateManagerTest extends TestCase {

	public void testSetAnyTemplates() {
		VpeAnyData data = new VpeAnyData(
				"tag-name1",
				"tag-value",
				"yes",
				"red",
				"green",
				"blue",
				"white",
				true
				);
		
		VpeTemplateManager.getInstance().setAnyTemplate(data);
		
		data = new VpeAnyData(
				"tag-name2",
				"tag-value",
				"yes",
				"red",
				"green",
				"blue",
				"white",
				true
				);
		
		VpeTemplateManager.getInstance().setAnyTemplate(data);
	}
	
	public void testGetAnyTemplates() {
		List<VpeAnyData> templates = VpeTemplateManager.getInstance().getAnyTemplates();
		assertEquals(2, templates.size());
	}
	
	public void testReload() {
		VpeTemplateManager.getInstance().reload();
	}
	
	public void testGetDefTemplate() {
		VpeTemplateManager.getInstance().setDefTemplate(null);
		VpeTemplate template = VpeTemplateManager.getInstance().getDefTemplate();
		assertNotNull("TemplateManager.getDefTemplate() cannot return null",template);
	}
	
}
