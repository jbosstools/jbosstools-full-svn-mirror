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
package org.jboss.tools.vpe.editor.toolbar.format.handler;

/**
 * @author Igels
 */
public class UnderlineFormatHandler extends SimpleTagHandler {

	private static String TAG_NAME = "u"; //$NON-NLS-1$
	
	private static String TAG_STYLE = "TEXT-DECORATION"; //$NON-NLS-1$

	private static String TAG_STYLE_VALUE = "underline"; //$NON-NLS-1$
	/**
	 * Constructor
	 */
	public UnderlineFormatHandler() {
		super();
	}

	/**
	 * @see org.jboss.tools.vpe.editor.toolbar.format.handler.SimpleTagHandler#doIgnoreCase()
	 */
	protected boolean equalsWrappingTagName(String tagName) {
		return TAG_NAME.equalsIgnoreCase(tagName);
	}

	/**
	 * @see org.jboss.tools.vpe.editor.toolbar.format.handler.SimpleTagHandler#getWrappingTagName()
	 */
	protected String getWrappingTagName() {
		return TAG_NAME;
	}
	
	/**
	 * 
	 * @return
	 */
	protected String getWrappingTagStyle() {
	    return TAG_STYLE;
	}

	@Override
	protected boolean equalsWrappingTagStyle(String tagStyle) {
	    return TAG_STYLE.equalsIgnoreCase(tagStyle);
	}

	@Override
	protected String getWrappingTagStyleValue() {
	    return TAG_STYLE_VALUE;
	}
}