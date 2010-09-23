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

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.jboss.tools.vpe.editor.util.Constants;

public class VpeDebug {
	public static final boolean PRINT_SOURCE_MUTATION_EVENT;
	public static final boolean PRINT_SOURCE_SELECTION_EVENT;
	public static final boolean PRINT_SOURCE_MODEL_LIFECYCLE_EVENT;

	public static final boolean PRINT_VISUAL_MUTATION_EVENT;
	public static final boolean PRINT_VISUAL_SELECTION_EVENT;
	public static final boolean PRINT_VISUAL_CONTEXTMENU_EVENT;
	public static final boolean PRINT_VISUAL_MOUSE_EVENT;
	public static final boolean PRINT_VISUAL_DRAGDROP_EVENT;
	public static final boolean PRINT_VISUAL_KEY_EVENT;
	public static final boolean PRINT_VISUAL_INNER_DRAGDROP_EVENT;

	public static final boolean VISUAL_ADD_PSEUDO_ELEMENT;

	public static final boolean VISUAL_CONTEXTMENU_DUMP_SOURCE;
	public static final boolean VISUAL_CONTEXTMENU_DUMP_SELECTED_ELEMENT;
	public static final boolean VISUAL_CONTEXTMENU_DUMP_MAPPING;
	public static final boolean VISUAL_DUMP_PRINT_HASH;

	public static final boolean VISUAL_CONTEXTMENU_TEST;
	
	public static final List<String> VISUAL_DUMP_IGNORED_ATTRIBUTES ;

	// usePrintStackTrace = false - The exception is sent on our site 
	// usePrintStackTrace = true - The exception is printed through printStackTrace 
	public static final boolean USE_PRINT_STACK_TRACE;
	
	static {
		PRINT_SOURCE_MUTATION_EVENT = "true".equals(Platform.getDebugOption(VpePlugin.PLUGIN_ID + "/debug/source/mutation_event")); //$NON-NLS-1$ //$NON-NLS-2$
		PRINT_SOURCE_SELECTION_EVENT = "true".equals(Platform.getDebugOption(VpePlugin.PLUGIN_ID + "/debug/source/selection_event")); //$NON-NLS-1$ //$NON-NLS-2$
		PRINT_SOURCE_MODEL_LIFECYCLE_EVENT = "true".equals(Platform.getDebugOption(VpePlugin.PLUGIN_ID + "/debug/source/model_lifecycle_event")); //$NON-NLS-1$ //$NON-NLS-2$

		PRINT_VISUAL_MUTATION_EVENT = "true".equals(Platform.getDebugOption(VpePlugin.PLUGIN_ID + "/debug/visual/mutation_event")); //$NON-NLS-1$ //$NON-NLS-2$
		PRINT_VISUAL_SELECTION_EVENT = "true".equals(Platform.getDebugOption(VpePlugin.PLUGIN_ID + "/debug/visual/selection_event")); //$NON-NLS-1$ //$NON-NLS-2$
		PRINT_VISUAL_CONTEXTMENU_EVENT = "true".equals(Platform.getDebugOption(VpePlugin.PLUGIN_ID + "/debug/visual/contextmenu_event")); //$NON-NLS-1$ //$NON-NLS-2$
		PRINT_VISUAL_MOUSE_EVENT = "true".equals(Platform.getDebugOption(VpePlugin.PLUGIN_ID + "/debug/visual/mouse_event")); //$NON-NLS-1$ //$NON-NLS-2$
		PRINT_VISUAL_DRAGDROP_EVENT = "true".equals(Platform.getDebugOption(VpePlugin.PLUGIN_ID + "/debug/visual/dragdrop_event")); //$NON-NLS-1$ //$NON-NLS-2$
		PRINT_VISUAL_KEY_EVENT = "true".equals(Platform.getDebugOption(VpePlugin.PLUGIN_ID + "/debug/visual/key_event")); //$NON-NLS-1$ //$NON-NLS-2$
		PRINT_VISUAL_INNER_DRAGDROP_EVENT = "true".equals(Platform.getDebugOption(VpePlugin.PLUGIN_ID + "/debug/visual/inner_dragdrop_event")); //$NON-NLS-1$ //$NON-NLS-2$

		VISUAL_ADD_PSEUDO_ELEMENT = "true".equals(Platform.getDebugOption(VpePlugin.PLUGIN_ID + "/debug/visual/add_pseudo_element")); //$NON-NLS-1$ //$NON-NLS-2$

		VISUAL_CONTEXTMENU_DUMP_SOURCE = "true".equals(Platform.getDebugOption(VpePlugin.PLUGIN_ID + "/debug/visual/contextmenu/dump_source")); //$NON-NLS-1$ //$NON-NLS-2$
		VISUAL_CONTEXTMENU_DUMP_SELECTED_ELEMENT = "true".equals(Platform.getDebugOption(VpePlugin.PLUGIN_ID + "/debug/visual/contextmenu/dump_selected_element")); //$NON-NLS-1$ //$NON-NLS-2$
		VISUAL_CONTEXTMENU_DUMP_MAPPING = "true".equals(Platform.getDebugOption(VpePlugin.PLUGIN_ID + "/debug/visual/contextmenu/dump_mapping")); //$NON-NLS-1$ //$NON-NLS-2$
		VISUAL_DUMP_PRINT_HASH = "true".equals(Platform.getDebugOption(VpePlugin.PLUGIN_ID + "/debug/visual/dump_print_hash")); //$NON-NLS-1$ //$NON-NLS-2$
		VISUAL_DUMP_IGNORED_ATTRIBUTES = Platform
				.getDebugOption(VpePlugin.PLUGIN_ID
						+ "/debug/visual/ignored_attributes") != null ? Arrays //$NON-NLS-1$
				.asList(Platform.getDebugOption(
						VpePlugin.PLUGIN_ID
								+ "/debug/visual/ignored_attributes").split( //$NON-NLS-1$
						Constants.COMMA)) : null; 
		VISUAL_CONTEXTMENU_TEST = "true".equals(Platform.getDebugOption(VpePlugin.PLUGIN_ID + "/debug/visual/contextmenu/show_test")); //$NON-NLS-1$ //$NON-NLS-2$

		USE_PRINT_STACK_TRACE = "true".equals(Platform.getDebugOption(VpePlugin.PLUGIN_ID + "/debug/use_PrintStackTrace")); //$NON-NLS-1$ //$NON-NLS-2$
		
	}
}
