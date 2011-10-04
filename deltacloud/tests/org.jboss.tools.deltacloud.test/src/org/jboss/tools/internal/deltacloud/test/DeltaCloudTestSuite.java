/*******************************************************************************
 * Copyright (c) 2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.internal.deltacloud.test;

import org.jboss.tools.internal.deltacloud.test.core.job.CloudSchedulingRulesTest;
import org.jboss.tools.internal.deltacloud.test.ui.utils.URIUtilsTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author Andre Dietisheim
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
	CloudSchedulingRulesTest.class,
	URIUtilsTest.class})
public class DeltaCloudTestSuite {
}
