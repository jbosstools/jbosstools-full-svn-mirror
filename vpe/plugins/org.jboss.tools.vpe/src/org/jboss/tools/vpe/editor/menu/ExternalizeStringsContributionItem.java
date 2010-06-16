/*******************************************************************************
 * Copyright (c) 2007-2010 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.editor.menu;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.swt.widgets.Menu;
import org.jboss.tools.vpe.editor.menu.action.ExternalizeStringsAction;
import org.jboss.tools.vpe.messages.VpeUIMessages;

public class ExternalizeStringsContributionItem extends ActionContributionItem {

	public ExternalizeStringsContributionItem() {
		super(new ExternalizeStringsAction());
	}

	@Override
	public void fill(Menu parent, int index) {
		getAction().setText(VpeUIMessages.EXTERNALIZE_STRINGS);
		super.fill(parent, index);
	}
	
}
