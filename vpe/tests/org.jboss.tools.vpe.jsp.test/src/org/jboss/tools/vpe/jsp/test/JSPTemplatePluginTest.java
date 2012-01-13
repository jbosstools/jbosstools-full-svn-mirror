package org.jboss.tools.vpe.jsp.test;

import org.jboss.tools.vpe.jsp.JspTemplatePluign;

import junit.framework.TestCase;

public class JSPTemplatePluginTest extends TestCase {
	
	public void testJspTemplatePluginActivator () {
		assertNotNull(JspTemplatePluign.getDefault());
	}

}
