/******************************************************************************* 
 * Copyright (c) 2011 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.ide.eclipse.as.ui.wizards;

import org.eclipse.swt.widgets.Composite;

public class JBoss7ServerWizardFragment extends JBossServerWizardFragment {

	public JBoss7ServerWizardFragment() {
	}

	protected void createRuntimeGroup(Composite main) {
		createRuntimeGroup2(main);
		fillRuntimeGroupStandard();
	}

}
