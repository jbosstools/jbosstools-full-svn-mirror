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
package org.jboss.tools.vpe.editor.template;

import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.mozilla.interfaces.nsIDOMKeyEvent;

/**
 * interface for handling of keyEvent by himself template
 * 
 * @author Sergey Dzmitrovich
 * 
 */
public interface ITemplateKeyEventHandler {

	/**
	 * handle keyEvent
	 * 
	 * @param pageContext
	 * @param keyEvent -
	 *            happens when element of current template is selected and
	 *            is pressed key
	 * @return
	 */
	boolean handleKeyPress(VpePageContext pageContext, nsIDOMKeyEvent keyEvent);

}
