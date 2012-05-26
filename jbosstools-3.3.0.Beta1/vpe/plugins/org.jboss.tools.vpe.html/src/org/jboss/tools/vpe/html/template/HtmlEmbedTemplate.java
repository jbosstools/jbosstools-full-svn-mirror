/******************************************************************************* 
 * Copyright (c) 2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.html.template;

import java.util.Arrays;
import java.util.List;

import org.jboss.tools.vpe.editor.util.HTML;

/**
 * Template for {@code <embed>} tag.
 * 
 * @author Yahor Radtsevich
 */
public class HtmlEmbedTemplate extends HtmlFlashAbstractTemplate {
	List<String> ATTRIBUTES_TO_BE_COPIED = Arrays.asList(
			"align", "height", "hidden", "hspace",	 //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$
			"pluginspage", "src", "vspace", "width");//$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$

	@Override
	public String getTagName() {
		return HTML.TAG_EMBED;
	}

	@Override
	public List<String> getAttributesToBeCopied() {
		return ATTRIBUTES_TO_BE_COPIED;
	}

}
