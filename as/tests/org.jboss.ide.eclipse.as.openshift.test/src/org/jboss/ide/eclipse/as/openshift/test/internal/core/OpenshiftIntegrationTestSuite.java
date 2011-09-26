/******************************************************************************* 
 * Copyright (c) 2007 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.ide.eclipse.as.openshift.test.internal.core;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;


@RunWith(Suite.class)
@Suite.SuiteClasses({
	ApplicationIntegrationTest.class,
	CartridgesIntegrationTest.class,
	DomainIntegrationTest.class,
	ListCartridgesIntegrationTest.class,
	UserInfoIntegrationTest.class,
	ApplicationLogReaderIntegrationTest.class
})
/**
 * @author André Dietisheim
 */
public class OpenshiftIntegrationTestSuite {

}
