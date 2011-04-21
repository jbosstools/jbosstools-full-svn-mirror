/******************************************************************************* 
 * Copyright (c) 2009 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.kb.test;

import junit.framework.TestCase;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.jboss.tools.common.el.core.resolver.ELContext;
import org.jboss.tools.common.text.TextProposal;
import org.jboss.tools.jst.web.kb.KbQuery;
import org.jboss.tools.jst.web.kb.PageContextFactory;
import org.jboss.tools.jst.web.kb.PageProcessor;
import org.jboss.tools.jst.web.kb.KbQuery.Type;
import org.jboss.tools.jst.web.kb.internal.taglib.CustomTagLibAttribute;
import org.jboss.tools.jst.web.kb.taglib.CustomTagLibManager;
import org.jboss.tools.jst.web.kb.taglib.ICustomTagLibrary;
import org.jboss.tools.test.util.JobUtils;
import org.jboss.tools.test.util.ProjectImportTestSetup;

/**
 * @author Alexey Kazakov
 */
public class WebKbTest extends TestCase {

	private IProject testProject;
	private static final String[] CUSTOM_TAG_LIB_URIS = {"http://richfaces.org/a4j", "http://richfaces.org/rich", "http://java.sun.com/jsf/core", "http://java.sun.com/jsf/html", "http://java.sun.com/jsf/facelets", "http://www.w3.org/1999/xhtml/facelets", "http://jboss.com/products/seam/taglib", "http://java.sun.com/JSP/Page", "http://struts.apache.org/tags-html", "taglibs/componentExtension.xml"};

	protected void setUp() throws Exception {
		if(testProject==null) {
			testProject = ProjectImportTestSetup.loadProject("TestKbModel");
			JobUtils.waitForIdle();
			JobUtils.delay(2000);
		}
	}

	public void testCustomTagLibs() {
		ICustomTagLibrary[] libs = CustomTagLibManager.getInstance().getLibraries();
		for (ICustomTagLibrary lib : libs) {
			boolean found = false;
			for (String uri : CUSTOM_TAG_LIB_URIS) {
				if(uri.equals(lib.getURI())) {
					found = true;
					break;
				}
			}
			assertTrue("Custom tag lib " + lib.getURI() + " is not loaded.", found);
		}
	}

	/**
	 * https://jira.jboss.org/jira/browse/JBIDE-6284
	 */
	public void testFFacet() {
		IFile file = testProject.getFile("WebContent/pages/inputUserName.jsp");
		ELContext context = PageContextFactory.createPageContext(file);
		KbQuery query = new KbQuery();
		query.setMask(true);
		query.setOffset(130);
		query.setType(Type.TAG_NAME);
		query.setPrefix("f");
		query.setUri("http://java.sun.com/jsf/core");
		query.setValue("f:facet");

		TextProposal[] proposals = PageProcessor.getInstance().getProposals(query, context);
		for (TextProposal proposal : proposals) {
			if("<f:facet name=\"\">".equals(proposal.getReplacementString())) {
				return;
			}
		}
		fail("Can't find <f:facet name=\"\"> proposal.");
	}

	public void testCustomExtensions() {
		CustomTagLibAttribute[] attributes = CustomTagLibManager.getInstance().getComponentExtensions();
		assertNotNull("Can't load component extensions.", attributes);
		assertFalse("Can't load component extensions.", attributes.length==0);
	}
}