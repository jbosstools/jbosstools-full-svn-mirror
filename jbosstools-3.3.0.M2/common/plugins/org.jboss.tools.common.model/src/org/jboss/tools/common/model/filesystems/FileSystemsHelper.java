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
package org.jboss.tools.common.model.filesystems;

import org.jboss.tools.common.model.*;
import org.jboss.tools.common.model.filesystems.impl.FileSystemsImpl;
import org.jboss.tools.common.model.filesystems.impl.Libs;

public class FileSystemsHelper {
	public static final String FILE_SYSTEMS = "FileSystems"; //$NON-NLS-1$
	
	public static XModelObject getFileSystems(XModel model) {
		return model == null ? null : model.getByPath(FILE_SYSTEMS);
	}

	public static XModelObject getFileSystem(XModel model, String name) {
		return model.getByPath(FILE_SYSTEMS + XModelObjectConstants.SEPARATOR + name);
	}
	
	public static XModelObject getWebRoot(XModel model) {
		return model.getByPath(FILE_SYSTEMS + "/WEB-ROOT"); //$NON-NLS-1$
	}

	public static XModelObject getWebInf(XModel model) {
		return model.getByPath(FILE_SYSTEMS + "/WEB-INF"); //$NON-NLS-1$
	}

	public static Libs getLibs(XModel model) {
		XModelObject fs = getFileSystems(model);
		return (!(fs instanceof FileSystemsImpl)) ? null : ((FileSystemsImpl)fs).getLibs();
	}

	public static Libs getLibs(XModelObject object) {
		if(object instanceof FileSystemsImpl) {
			return  ((FileSystemsImpl)object).getLibs();
		}
		return object == null ? null : getLibs(object.getModel());
	}

}
