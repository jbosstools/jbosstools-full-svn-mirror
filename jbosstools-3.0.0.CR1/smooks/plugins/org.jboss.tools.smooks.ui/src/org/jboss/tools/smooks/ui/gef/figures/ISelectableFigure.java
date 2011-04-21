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
package org.jboss.tools.smooks.ui.gef.figures;

/**
 * 
 * @author Dart Peng
 * 
 * Create time : Jul 21, 2008
 */
public interface ISelectableFigure {
	public void setSelected(boolean selected);

	public boolean getSelected();
	
	public void setFocus(boolean focus);
	
	public boolean getFocus();

}
