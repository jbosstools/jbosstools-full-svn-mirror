/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.vpe.editor.template;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import org.jboss.tools.vpe.editor.context.VpePageContext;

public class VpeEmptyPseudoContentCreator extends VpePseudoContentCreator {

	private static VpePseudoContentCreator INSTANCE = null;

	public static final VpePseudoContentCreator getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new VpeEmptyPseudoContentCreator();
		}
		return INSTANCE;
	}

	public void setPseudoContent(VpePageContext pageContext, Node sourceContainer, Node visualContainer, Document visualDocument) {
	}
}
