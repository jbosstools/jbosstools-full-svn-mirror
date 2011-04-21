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
package org.jboss.tools.vpe.ui.test;

import org.w3c.dom.Node;

/**
 * @author Sergey Dzmitrovich
 * @author Yahor Radtsevich (yradtsevich)
 *
 */
public class DOMComparisonException extends Exception {
	private static final long serialVersionUID = 7127064462771778364L;
	private Node node;

	public DOMComparisonException(String message, Node node) {
		super(message);
		this.node = node;
	}

	public Node getNode() {
		return node;
	}
}
