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
package org.jboss.tools.smooks.ui.gef.editparts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.NodeEditPart;

/**
 * @author Dart Peng
 * 
 */
public interface IConnectableEditPart extends NodeEditPart {
	public IFigure getAnchroFigure();
}
