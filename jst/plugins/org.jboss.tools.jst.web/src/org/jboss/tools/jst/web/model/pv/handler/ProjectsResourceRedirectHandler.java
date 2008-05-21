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
package org.jboss.tools.jst.web.model.pv.handler;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.QualifiedName;
import org.jboss.tools.common.meta.action.impl.handlers.DefaultRedirectHandler;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.jst.web.WebModelPlugin;

public class ProjectsResourceRedirectHandler extends DefaultRedirectHandler {

	protected XModelObject getTrueSource(XModelObject source) {
		IProject p = EclipseResourceUtil.getProject(source);
		if(p == null) return source.getModel().getByPath("FileSystems/src");
		IResource r = null;
		try {
			QualifiedName n = new QualifiedName("", action.getName() + "_lastPath");
			String path = p.getPersistentProperty(n);
			if(path != null) r = p.getWorkspace().getRoot().findMember(path);
		} catch (Exception e) {
			WebModelPlugin.getPluginLog().logError(e);
		}
		if(r == null) r = EclipseResourceUtil.getJavaSourceRoot(p);
		XModelObject o = EclipseResourceUtil.getObjectByResource(r);
		return (o == null) ? source.getModel().getByPath("FileSystems/src") : o;
	}

}
