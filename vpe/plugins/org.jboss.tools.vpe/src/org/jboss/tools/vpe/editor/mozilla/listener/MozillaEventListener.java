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
package org.jboss.tools.vpe.editor.mozilla.listener;

/**
 * Listener of Mozilla events. Aggregates several kinds of listeners
 * in itself (see the declaration).
 */
public interface MozillaEventListener extends MozillaDndListener,
		MozillaMouseListener, MozillaKeyListener,
		MozillaTooltipListener, MozillaSelectionListener,
		MozillaContextMenuListener, MozillaResizeListener {
	//nsIDOMMutationListener
// 	these methods are not used
//	void subtreeModified(nsIDOMMutationEvent mutationEvent);
//	void nodeInserted(nsIDOMMutationEvent mutationEvent);
//	void nodeRemoved(nsIDOMMutationEvent mutationEvent);
//	void nodeRemovedFromDocument(nsIDOMMutationEvent mutationEvent);
//	void nodeInsertedIntoDocument(nsIDOMMutationEvent mutationEvent);
//	void attrModified(nsIDOMMutationEvent mutationEvent);
//	void characterDataModified(nsIDOMMutationEvent mutationEvent);
}
