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
package org.jboss.tools.smooks.ui.gef;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.graphics.Color;

/**
 * @author Dart Peng
 * @Date Jul 25, 2008
 */
public interface IGraphUIProvider extends ILabelProvider {
	/**
	 * 
	 * @param model
	 * @return
	 */
	public String getTypeText(Object model);

	public Color getBackgroundColor();

	public void dispose();

}
