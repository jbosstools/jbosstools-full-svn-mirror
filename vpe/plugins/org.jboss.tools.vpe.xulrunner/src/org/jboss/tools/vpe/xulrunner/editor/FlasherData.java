/*******************************************************************************
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.xulrunner.editor;

import org.mozilla.interfaces.nsIDOMElement;

/**
 * 
 * Transfer object which contains flasher
 * 
 * @author mareshkau
 *
 */
public class FlasherData {

	private nsIDOMElement element;
	private String selectionColor;
	
	public FlasherData(String drawColor,nsIDOMElement element){
		this.selectionColor=drawColor;
		this.element=element;
	}
	
	protected nsIDOMElement getElement() {
		return element;
	}
	protected String getSelectionColor() {
		return selectionColor;
	}
}
