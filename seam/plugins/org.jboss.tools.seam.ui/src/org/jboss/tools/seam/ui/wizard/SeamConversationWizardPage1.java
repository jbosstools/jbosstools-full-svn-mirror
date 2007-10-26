/*******************************************************************************
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.seam.ui.wizard;

import java.util.Map;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.jboss.tools.seam.ui.SeamUIMessages;
import org.jboss.tools.seam.ui.internal.project.facet.IValidator;
import org.jboss.tools.seam.ui.internal.project.facet.ValidatorFactory;

/**
 * @author eskimo
 *
 */
public class SeamConversationWizardPage1 extends SeamBaseWizardPage {

	public SeamConversationWizardPage1(IStructuredSelection initialSelection) {
		super("seam.new.conversation.page1",SeamUIMessages.SEAM_CONVERSATION_WIZARD_PAGE1_SEAM_CONVERSATION,null, initialSelection);
		setMessage(getDefaultMessageText());
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.seam.ui.wizard.SeamBaseWizardPage#getDefaultMessageText()
	 */
	@Override
	public String getDefaultMessageText() {
		return "Create a new Seam conversation";
	}
}