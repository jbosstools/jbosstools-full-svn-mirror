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
package org.jboss.tools.common.model.filesystems.impl.handlers;

import java.util.*;
import org.jboss.tools.common.meta.action.XActionInvoker;
import org.jboss.tools.common.meta.action.impl.handlers.PasteHandler;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.filesystems.impl.FolderImpl;
import org.jboss.tools.common.model.util.XModelObjectLoaderUtil;

public class FolderPasteHandler extends PasteHandler {
	
	protected void onChildPasted(XModelObject child) {
		if(child instanceof FolderImpl) {
			((FolderImpl)child).save();
			XModelObjectLoaderUtil.updateModifiedOnSave(child);
			return;
		}
		XActionInvoker.invoke("SaveActions.Save", child, new Properties());
	}

}
