/*******************************************************************************
 * Copyright (c) 2008 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.seam.ui.test.wizard;

public class Seam20WARNewOperationTest extends Seam12WARNewOperationTest {
	@Override
	void setUpSeamProjects() {
		setUpSeamProject(warProject, AbstractSeamNewOperationTest.SEAM_2_0);
	}
}
