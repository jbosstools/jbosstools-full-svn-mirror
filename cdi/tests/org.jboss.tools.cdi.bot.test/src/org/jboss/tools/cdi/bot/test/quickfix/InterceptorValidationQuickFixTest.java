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
 * Test operates on quick fixes used for validation errors of CDI Interceptor component
 * 
 * @author Jaroslav Jankovic
 */

@Require(clearProjects = true, perspective = "Java EE", 
		server = @Server(state = ServerState.NotRunning, 
		version = "6.0", operator = ">="))
@RunWith(RequirementAwareSuite.class)
@SuiteClasses({ CDIAllBotTests.class })
public class InterceptorValidationQuickFixTest extends QuickFixTestBase {
	

	@Override
	public String getProjectName() {
		return "CDIQuickFixInterceptorTest";
	}
	
	// https://issues.jboss.org/browse/JBIDE-7680
	@Test
	public void testSessionAnnotation() {
			
		String className = "Interceptor1";
		
		wizard.createCDIComponentWithContent(CDIWizardType.INTERCEPTOR, className, 
				getPackageName(), null, "/resources/quickfix/interceptor/" +
						"InterceptorWithStateless.java.cdi");

		editResourceUtil.replaceInEditor("InterceptorComponent", className);
		
		checkQuickFix(CDIAnnotationsType.STATELESS, CDIWizardType.INTERCEPTOR);
			
	}
	
	// https://issues.jboss.org/browse/JBIDE-7636
	@Test
	public void testNamedAnnotation() {
		
		String className = "Interceptor2";
		
		wizard.createCDIComponentWithContent(CDIWizardType.INTERCEPTOR, className, 
				getPackageName(), null, "/resources/quickfix/interceptor/" +
						"InterceptorWithNamed.java.cdi");

		editResourceUtil.replaceInEditor("InterceptorComponent", className);
		
		checkQuickFix(CDIAnnotationsType.NAMED, CDIWizardType.INTERCEPTOR);
		
	}
	
	// https://issues.jboss.org/browse/JBIDE-7683
	@Test
	public void testProducer() {
		
		String className = "Interceptor3";
		
		wizard.createCDIComponentWithContent(CDIWizardType.INTERCEPTOR, className, 
				getPackageName(), null, "/resources/quickfix/interceptor/" +
						"InterceptorWithProducer.java.cdi");

		editResourceUtil.replaceInEditor("InterceptorComponent", className);
		
		checkQuickFix(CDIAnnotationsType.PRODUCES, CDIWizardType.INTERCEPTOR);
		
	}
	
	// https://issues.jboss.org/browse/JBIDE-7684
	@Test
	public void testDisposesAnnotation() {
		
		String className = "Interceptor4";
		
		wizard.createCDIComponentWithContent(CDIWizardType.INTERCEPTOR, className, 
				getPackageName(), null, "/resources/quickfix/interceptor/" +
						"InterceptorWithDisposes.java.cdi");

		editResourceUtil.replaceInEditor("InterceptorComponent", className);
		
		checkQuickFix(CDIAnnotationsType.DISPOSES, CDIWizardType.INTERCEPTOR);
		
	}
	
	// https://issues.jboss.org/browse/JBIDE-7685
	@Test
	public void testObservesAnnotation() {
		
		String className = "Interceptor5";
		
		wizard.createCDIComponentWithContent(CDIWizardType.INTERCEPTOR, className, 
				getPackageName(), null, "/resources/quickfix/interceptor/" +
						"InterceptorWithDisposes.java.cdi");
		
		editResourceUtil.replaceInEditor("import javax.enterprise.inject.Disposes;", 
				"import javax.enterprise.event.Observes;");
		editResourceUtil.replaceInEditor("@Disposes", "@Observes");
		editResourceUtil.replaceInEditor("InterceptorComponent", className);
		
		checkQuickFix(CDIAnnotationsType.OBSERVES, CDIWizardType.INTERCEPTOR);
			
	}
	
	// https://issues.jboss.org/browse/JBIDE-7686
	@Test
	public void testSpecializesAnnotation() {
		
		String className = "Interceptor6";
		
		wizard.createCDIComponentWithContent(CDIWizardType.INTERCEPTOR, className, 
				getPackageName(), null, "/resources/quickfix/interceptor/" +
						"InterceptorWithSpecializes.java.cdi");

		editResourceUtil.replaceInEditor("InterceptorComponent", className);
		
		checkQuickFix(CDIAnnotationsType.SPECIALIZES, CDIWizardType.INTERCEPTOR);
			
	}
	
}
