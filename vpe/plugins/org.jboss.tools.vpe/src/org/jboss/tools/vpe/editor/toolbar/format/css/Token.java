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
package org.jboss.tools.vpe.editor.toolbar.format.css;

/**
 * @author Igels
 */
public class Token {

	/*
	 * Original token value with space symbols at begin and end.
	 */
	private String cleanValue;

	/*
	 * Token value without space symbols at begin and end.
	 */
	private String dirtyValue;

	public Token(String dirtyValue) {
		this.dirtyValue = dirtyValue;
		this.cleanValue = dirtyValue.trim();
	}

	/**
	 * @return Returns the cleanValue. Original token value with space symbols at begin and end
	 */
	public String getCleanValue() {
		return cleanValue;
	}

	/**
	 * @return Returns the dirtyValue. Token value without space symbols at begin and end.
	 */
	public String getDirtyValue() {
		return toString();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return dirtyValue;
	}
}