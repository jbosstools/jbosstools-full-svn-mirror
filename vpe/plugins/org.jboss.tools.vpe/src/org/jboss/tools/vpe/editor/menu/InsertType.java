/*******************************************************************************
 * Copyright (c) 2007-2009 Red Hat, Inc. Distributed under license by
 * Red Hat, Inc. All rights reserved. This program is made available
 * under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributor: Red Hat,
 * Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.editor.menu;

import org.jboss.tools.vpe.messages.VpeUIMessages;

/**
 * Contains items for the VPE context menu.
 * 
 * @author yradtsevich
 */
public enum InsertType {
	INSERT_AROUND(VpeUIMessages.INSERT_AROUND),
	INSERT_BEFORE(VpeUIMessages.INSERT_BEFORE),
	INSERT_AFTER(VpeUIMessages.INSERT_AFTER),
	INSERT_INTO(VpeUIMessages.INSERT_INTO),
	REPLACE_WITH(VpeUIMessages.REPLACE_WITH);

	private String message;

	private InsertType(String message) {
		this.message = message;
	}
	public String getMessage() {
		return message;
	}
}
