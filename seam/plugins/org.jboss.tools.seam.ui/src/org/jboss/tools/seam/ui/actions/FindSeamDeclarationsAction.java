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

package org.jboss.tools.seam.ui.actions;

import org.jboss.tools.seam.ui.SeamUIMessages;
import org.jboss.tools.seam.ui.handlers.FindSeamHandler;
import org.jboss.tools.seam.ui.search.SeamSearchScope;

/**
 * Find Seam Declarations action
 * 
 * @deprecated This class is subject to remove. Use {@link FindSeamDeclarationHandler} instead.
 * 
 * @author Jeremy
 */
public class FindSeamDeclarationsAction extends FindSeamAction {

	/**
	 * Constructs the FindSeamDeclarationAction
	 */
	public FindSeamDeclarationsAction() {
		setText(SeamUIMessages.FIND_DECLARATIONS_ACTION_ACTION_NAME);
		setDescription(SeamUIMessages.FIND_DECLARATIONS_ACTION_DESCRIPTION);
		setToolTipText(SeamUIMessages.FIND_DECLARATIONS_ACTION_TOOL_TIP);
	}

	/**
	 * Returns the search limitation to declarations 
	 */
	protected int getLimitTo() {
		return SeamSearchScope.SEARCH_FOR_DECLARATIONS;
	}
}
