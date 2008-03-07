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
package org.jboss.tools.vpe.editor.mapping;

import org.mozilla.interfaces.nsIDOMNode;
import org.w3c.dom.Attr;

/**
 * 
 * @author Sergey Dzmitrovich
 * 
 * Keep information about output attribute. Set up a correspondence source node
 * and visual node
 * 
 * 
 */
public class VpeAttributeData {

	/**
	 * source presentation of attribute
	 */
	private Attr sourceAttr;

	/**
	 * visual presentation of attribute
	 */
	private nsIDOMNode visualAttr;

	/**
	 * mark if editable
	 */
	private boolean isEditable;

	public VpeAttributeData(Attr sourceAttr, nsIDOMNode visualAttr,
			boolean isEditable) {
		this.sourceAttr = sourceAttr;
		this.visualAttr = visualAttr;
		this.isEditable = isEditable;

	}

	public VpeAttributeData(Attr sourceAttr, nsIDOMNode visualAttr) {
		this.sourceAttr = sourceAttr;
		this.visualAttr = visualAttr;
		this.isEditable = true;

	}

	/**
	 * get source
	 * 
	 * @return
	 */
	public Attr getSourceAttr() {
		return sourceAttr;
	}

	/**
	 * set source
	 * 
	 * @param sourceAttr
	 */
	public void setSourceAttr(Attr sourceAttr) {
		this.sourceAttr = sourceAttr;
	}

	/**
	 * get visual
	 * 
	 * @return
	 */
	public nsIDOMNode getVisualAttr() {
		return visualAttr;
	}

	/**
	 * set visual
	 * 
	 * @param visualAttr
	 */
	public void setVisualAttr(nsIDOMNode visualAttr) {
		this.visualAttr = visualAttr;
	}

	/**
	 * is editable
	 * 
	 * @return
	 */
	public boolean isEditable() {
		return isEditable;
	}

	/**
	 * set editable
	 * 
	 * @param isEditable
	 */
	public void setEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}

}
