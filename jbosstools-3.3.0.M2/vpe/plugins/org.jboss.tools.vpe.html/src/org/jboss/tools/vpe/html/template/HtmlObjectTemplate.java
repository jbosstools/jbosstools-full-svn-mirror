package org.jboss.tools.vpe.html.template;

import java.util.Arrays;
import java.util.List;

import org.jboss.tools.vpe.editor.util.HTML;

/**
 * Template for {@code <object>} tag.
 * 
 * @author Yahor Radtsevich
 */
public class HtmlObjectTemplate extends HtmlFlashAbstractTemplate {
	List<String> ATTRIBUTES_TO_BE_COPIED = Arrays.asList(
			"align", "height", "hspace", "vspace",  //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$
			"width", "id", "dir");					//$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$

	@Override
	public String getTagName() {
		return HTML.TAG_OBJECT;
	}

	@Override
	public List<String> getAttributesToBeCopied() {
		return ATTRIBUTES_TO_BE_COPIED;
	}

}
