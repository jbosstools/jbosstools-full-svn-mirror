/*******************************************************************************
 * Copyright (c) 2007-2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.xulrunner.editor;

/**
 * Selection listener. It is fired when selection rectangle is changed.
 * 
 * @author Yahor Radtsevich (yradtsevich)
 */
public interface IVpeSelectionListener {
	public void selectionChanged();
}
