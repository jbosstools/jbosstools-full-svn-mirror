/*******************************************************************************
 * Copyright (c) 2008 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.graphical.editors.model.freemarker;

import org.jboss.tools.smooks.configuration.editors.xml.TagObject;

/**
 * @author Dart
 *
 */
public class CSVNodeModel extends TagObject {
	private boolean isRecord = false;
	
	private char sperator = ',';
	
	private char quto = '\"';

	/**
	 * @return the isRecord
	 */
	public boolean isRecord() {
		return isRecord;
	}

	/**
	 * @param isRecord the isRecord to set
	 */
	public void setRecord(boolean isRecord) {
		this.isRecord = isRecord;
	}

	/**
	 * @return the sperator
	 */
	public char getSperator() {
		return sperator;
	}

	/**
	 * @param sperator the sperator to set
	 */
	public void setSperator(char sperator) {
		this.sperator = sperator;
	}

	/**
	 * @return the quto
	 */
	public char getQuto() {
		return quto;
	}

	/**
	 * @param quto the quto to set
	 */
	public void setQuto(char quto) {
		this.quto = quto;
	}
	
	
	
	
}
