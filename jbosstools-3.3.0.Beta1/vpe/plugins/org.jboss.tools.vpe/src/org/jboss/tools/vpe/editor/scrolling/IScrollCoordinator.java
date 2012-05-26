/*******************************************************************************
 * Copyright (c) 2007-2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.vpe.editor.scrolling;

/**
 * Interface that coordinates scroll position in source and visual editors 
 * @author dmaliarevich
 */
public interface IScrollCoordinator {
	/**
	 * Computes visual editor scroll position 
	 * based on the source editor text position
	 * 
	 * @return visual editor scroll position
	 */
	int computeVisualPositionFromSource();
	
	/**
	 * Computes source editor text position 
	 * based on the visual editor scroll position
	 * 
	 * @return source editor scroll position
	 */
	int computeSourcePositionFromVisual();
}
