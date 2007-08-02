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
package org.jboss.tools.vpe.editor.mozilla;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;

public class MozillaSelectionProvider implements ISelectionProvider {

	public ISelection getSelection() {
		return null;
	}

	public void setSelection(ISelection selection) {
	}

	public void addSelectionChangedListener(ISelectionChangedListener listener) {
	}

	public void removeSelectionChangedListener(ISelectionChangedListener listener) {
	}
}
