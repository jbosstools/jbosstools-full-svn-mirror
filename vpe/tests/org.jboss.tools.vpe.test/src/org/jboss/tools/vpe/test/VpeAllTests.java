/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.vpe.test;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.jboss.tools.jsf.vpe.jsf.test.JsfComponentTest;
import org.jboss.tools.jsf.vpe.jsf.test.JsfTestPlugin;
import org.jboss.tools.vpe.editor.preferences.VpeEditorPreferencesPageTest;
import org.jboss.tools.vpe.editor.template.VpeTemplateManagerTest;
import org.jboss.tools.vpe.ui.test.ElPreferencesTestCase;
import org.jboss.tools.vpe.ui.test.JBIDE2010Test;
import org.jboss.tools.vpe.ui.test.JBIDE2582Test;
import org.jboss.tools.vpe.ui.test.VpeTestSetup;
import org.jboss.tools.vpe.ui.test.beans.ImportBean;

/**
 * Class created for run tests for org.jboss.tools.vpe plugin.
 * 
 * @author Max Areshkau
 * 
 */

public class VpeAllTests extends TestCase{
	
	public static Test suite(){
		TestSuite suite = new TestSuite("Tests for vpe");
		// $JUnit-BEGIN$
		suite.addTestSuite(TemplateLoadingTest.class);
		suite.addTestSuite(TemplatesExpressionParsingTest.class);
		suite.addTestSuite(VpeEditorPreferencesPageTest.class);
		suite.addTestSuite(VpeTemplateManagerTest.class);
		suite.addTestSuite(ElPreferencesTestCase.class);
		suite.addTestSuite(JBIDE2010Test.class);
		suite.addTestSuite(JBIDE2582Test.class);

        List<ImportBean> projectToImport = new ArrayList<ImportBean>();
        ImportBean importBean = new ImportBean();
        importBean.setImportProjectName(JsfComponentTest.IMPORT_PROJECT_NAME);
        importBean.setImportProjectPath(JsfTestPlugin.getPluginResourcePath());
        projectToImport.add(importBean);

        return new VpeTestSetup(suite, projectToImport);
	}
}
