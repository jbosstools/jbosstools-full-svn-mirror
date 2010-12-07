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

import org.jboss.tools.vpe.editor.util.Docbook;

/**
 * Class for <note>
 * 
 * @author Denis Vinnichek (dvinnichek)
 */
public class DocbookNoteTemplate extends ElementWithGeneratedOutputTemplate {
	
	@Override
	protected String getGeneratedText() {
		return Docbook.ELEMENT_NOTE.toUpperCase();
	}

}
