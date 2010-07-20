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
package org.jboss.tools.smooks.configuration.editors;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IEditorInput;
import org.eclipse.wst.sse.ui.StructuredTextEditor;

/**
 * @author Dart Peng (dpeng@redhat.com)
 * Date Apr 1, 2009
 */
public class SmooksXMLEditor extends StructuredTextEditor {

	/* (non-Javadoc)
	 * @see org.eclipse.wst.sse.ui.StructuredTextEditor#doSetInput(org.eclipse.ui.IEditorInput)
	 */
	@Override
	public void doSetInput(IEditorInput input) throws CoreException {
		// TODO Auto-generated method stub
		super.doSetInput(input);
	}

}
