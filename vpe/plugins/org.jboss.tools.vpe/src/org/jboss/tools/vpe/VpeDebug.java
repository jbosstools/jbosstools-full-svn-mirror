/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.vpe;

public class VpeDebug {
	public static final boolean printSourceMutationEvent = false;
	public static final boolean printSourceSelectionEvent = false;
	public static final boolean printSourceModelLifecycleEvent = false;

	public static final boolean printVisualMutationEvent = false;
	public static final boolean printVisualSelectionEvent = false;
	public static final boolean printVisualContextMenuEvent = false;
	public static final boolean printVisualMouseEvent = false;
	public static final boolean printVisualDragDropEvent = false;
	public static final boolean printVisualKeyEvent = false;
	public static final boolean printVisualInnerDragDropEvent = false;

	public static final boolean visualAddPseudoElement = false;

	public static final boolean visualContextMenuDumpSource = true;
	public static final boolean visualContextMenuDumpMapping = false;

	public static final boolean visualContextMenuTest = false;

	// usePrintStackTrace = false - The exception is sent on our site 
	// usePrintStackTrace = true - The exception is printed through printStackTrace 
	public static final boolean usePrintStackTrace = false;
}
