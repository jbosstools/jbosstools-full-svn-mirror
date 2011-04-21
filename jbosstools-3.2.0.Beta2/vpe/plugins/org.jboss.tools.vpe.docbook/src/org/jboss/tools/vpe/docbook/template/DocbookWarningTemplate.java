/*******************************************************************************
 * Copyright (c) 2007-2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.vpe.docbook.template;

import org.jboss.tools.vpe.docbook.template.util.Docbook;

/**
 * Class for <warning>
 * 
 * @author Denis Vinnichek (dvinnichek)
 */
public class DocbookWarningTemplate extends ElementWithGeneratedOutputTemplate {

	private static final String BORDER = "1px solid red"; //$NON-NLS-1$

	@Override
	protected String getGeneratedText() {
		return Docbook.ELEMENT_WARNING.toUpperCase();
	}

	@Override
	protected String getBorder() {
		return BORDER;
	}

}
