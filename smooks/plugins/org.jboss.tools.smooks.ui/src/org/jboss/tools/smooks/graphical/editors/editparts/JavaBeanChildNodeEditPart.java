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
package org.jboss.tools.smooks.graphical.editors.editparts;

import org.jboss.tools.smooks.gef.tree.editparts.TreeNodeEditPart;

/**
 * @author Dart
 *
 */
public class JavaBeanChildNodeEditPart extends TreeNodeEditPart {

	@Override
	protected boolean isDragLink() {
		return false;
	}

}
