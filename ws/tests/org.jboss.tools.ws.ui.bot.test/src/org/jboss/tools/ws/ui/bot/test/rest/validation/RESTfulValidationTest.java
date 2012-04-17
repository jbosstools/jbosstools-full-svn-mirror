 /*******************************************************************************
  * Copyright (c) 2007-2011 Red Hat, Inc.
  * Distributed under license by Red Hat, Inc. All rights reserved.
  * This program is made available under the terms of the
  * Eclipse Public License v1.0 which accompanies this distribution,
  * and is available at http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributor:
  *     Red Hat, Inc. - initial API and implementation
  ******************************************************************************/

package org.jboss.tools.ws.ui.bot.test.rest.validation;

import org.jboss.tools.ws.ui.bot.test.rest.RESTfulTestBase;
import org.jboss.tools.ws.ui.bot.test.rest.explorer.RESTfulExplorerTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test operates on JAX-RS validation
 * @author jjankovi
 *
 */
public class RESTfulValidationTest extends RESTfulTestBase {
	
	private final String GET_METHOD_PATH = "/{id}";
	private final String CORRECT_PATH_PARAM = "id";
	private final String BAD_PATH_PARAM = "customerId";
	
	@Before
	public void setup() {
		
	}
	
	@After
	public void cleanup() {
		projectExplorer.deleteAllProjects();
	}
	
	@Test
	public void testCorrectValueValidation() {
		
		setWsProjectName("restValidation1");
		prepareRestProject();
		
		assertTrue(restfulHelper.getRESTValidationErrors(getWsProjectName()).length == 0);
	}
	
	@Test
	public void testBadValueValidation() {
		
		setWsProjectName("restValidation2");
		prepareRestProject();
		
		assertTrue("" + restfulHelper.getRESTValidationErrors(getWsProjectName()).length, 
				restfulHelper.getRESTValidationErrors(getWsProjectName()).length == 1);
	}
	
	@Test
	public void testCorrectToBadValueValidation() {
		
		setWsProjectName("restValidation1");
		prepareRestProject();
		
		packageExplorer.openFile(getWsProjectName(), "src", 
				getWsPackage(), getWsName() + ".java").toTextEditor();
		resourceHelper.copyResourceToClass(bot.editorByTitle(getWsName() + ".java"), 
				   RESTfulExplorerTest.class.getResourceAsStream(SIMPLE_REST_WS_RESOURCE), 
				   false, getWsPackage(), getWsName(), GET_METHOD_PATH, BAD_PATH_PARAM);
		
		assertTrue("" + restfulHelper.getRESTValidationErrors(getWsProjectName()).length, 
				restfulHelper.getRESTValidationErrors(getWsProjectName()).length == 1);
	}
	
	@Test
	public void testBadToCorrectValueValidation() {
		setWsProjectName("restValidation2");
		prepareRestProject();
		
		packageExplorer.openFile(getWsProjectName(), "src", 
				getWsPackage(), getWsName() + ".java").toTextEditor();
		resourceHelper.copyResourceToClass(bot.editorByTitle(getWsName() + ".java"), 
				   RESTfulExplorerTest.class.getResourceAsStream(SIMPLE_REST_WS_RESOURCE), 
				   false, getWsPackage(), getWsName(), GET_METHOD_PATH, CORRECT_PATH_PARAM);
		
		assertTrue("" + restfulHelper.getRESTValidationErrors(getWsProjectName()).length, 
				restfulHelper.getRESTValidationErrors(getWsProjectName()).length == 0);
	}
	
	@Test
	public void testJAX_RS_Validator() {
		
		restfulHelper.disableRESTValidation();
		
		setWsProjectName("restValidation2");
		prepareRestProject();
		
		assertTrue("" + restfulHelper.getRESTValidationErrors(getWsProjectName()).length, 
				restfulHelper.getRESTValidationErrors(getWsProjectName()).length == 0);
		
		restfulHelper.enableRESTValidation();
		
		assertTrue("" + restfulHelper.getRESTValidationErrors(getWsProjectName()).length, 
				restfulHelper. getRESTValidationErrors(getWsProjectName()).length == 1);
	}

}
