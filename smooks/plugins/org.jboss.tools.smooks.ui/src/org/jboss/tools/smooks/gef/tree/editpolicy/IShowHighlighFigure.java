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
package org.jboss.tools.smooks.gef.tree.editpolicy;

import org.eclipse.swt.graphics.Color;

/**
 * @author Dart
 *
 */
public interface IShowHighlighFigure {
	void showHighlightBackgroudColor(Color color);
	
	void showbackOldbackgroundColor(Color color);
}