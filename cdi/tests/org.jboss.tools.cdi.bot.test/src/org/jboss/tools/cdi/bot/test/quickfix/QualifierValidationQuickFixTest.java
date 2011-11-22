/*******************************************************************************
 * Copyright (c) 2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.cdi.bot.test.quickfix;


import org.jboss.tools.cdi.bot.test.CDIAllBotTests;
import org.jboss.tools.cdi.bot.test.annotations.CDIAnnotationsType;
import org.jboss.tools.cdi.bot.test.annotations.CDIWizardType;
import org.jboss.tools.ui.bot.ext.RequirementAwareSuite;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Test operates on quick fixes used for validation errors of CDI Qualifier component
 * 
 * @author Jaroslav Jankovic
 */

@Require(clearProjects = true, perspective = "Java EE", server = @Server(state = ServerState.NotRunning, version = "6.0", operator = ">="))
@RunWith(RequirementAwareSuite.class)
@SuiteClasses({ CDIAllBotTests.class })
public class QualifierValidationQuickFixTest extends QuickFixTestBase {
	
	@Override
	public String getProjectName() {
		return "CDIQuickFixQualifierTest";
	}
	
	// https://issues.jboss.org/browse/JBIDE-7630
	@Test
	public void testTargetAnnotation() {
		
		String className = "Qualifier1";
		
		wizard.createCDIComponent(CDIWizardType.QUALIFIER, className, getPackageName(), null);
		
		editResourceUtil.replaceInEditor("@Target({ TYPE, METHOD, PARAMETER, FIELD })", 
				"@Target({ TYPE, FIELD })");
		
		checkQuickFix(CDIAnnotationsType.TARGET, CDIWizardType.QUALIFIER);
		
		editResourceUtil.replaceInEditor("@Target({TYPE, METHOD, FIELD, PARAMETER})", "");
		
		checkQuickFix(CDIAnnotationsType.TARGET, CDIWizardType.QUALIFIER);
	}
	
	// https://issues.jboss.org/browse/JBIDE-7631
	@Test
	public void testRetentionAnnotation() {
		
		String className = "Qualifier2";

		wizard.createCDIComponent(CDIWizardType.QUALIFIER, className, getPackageName(), null);
				
		editResourceUtil.replaceInEditor("@Retention(RUNTIME)", "@Retention(CLASS)");
		
		checkQuickFix(CDIAnnotationsType.RETENTION, CDIWizardType.QUALIFIER);
		
		editResourceUtil.replaceInEditor("@Retention(RUNTIME)", "");
		
		checkQuickFix(CDIAnnotationsType.RETENTION, CDIWizardType.QUALIFIER);
		
	}
	
	// https://issues.jboss.org/browse/JBIDE-7641
	@Test
	public void testNonbindingAnnotation() {
	
		String className = "Qualifier3";
		
		wizard.createAnnotation("AAnnotation", getPackageName());
		wizard.createCDIComponent(CDIWizardType.QUALIFIER, className, getPackageName(), null);
	
		editResourceUtil.replaceClassContentByResource(QuickFixTestBase.class
				.getResourceAsStream("/resources/quickfix/qualifier/QualifierWithAnnotation.java.cdi"), false);
		editResourceUtil.replaceInEditor("QualifierComponent", className);
	
		checkQuickFix(CDIAnnotationsType.NONBINDING, CDIWizardType.QUALIFIER);
				
		editResourceUtil.replaceInEditor("@Nonbinding AAnnotation", "String[]");
		
		checkQuickFix(CDIAnnotationsType.NONBINDING, CDIWizardType.QUALIFIER);
	}
}
