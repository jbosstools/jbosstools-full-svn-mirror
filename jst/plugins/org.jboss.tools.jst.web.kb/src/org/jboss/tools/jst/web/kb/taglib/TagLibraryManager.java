/******************************************************************************* 
 * Copyright (c) 2009 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.kb.taglib;

import org.eclipse.core.resources.IProject;
import org.jboss.tools.jst.web.kb.IKbProject;
import org.jboss.tools.jst.web.kb.KbProjectFactory;

/**
 * @author Alexey Kazakov
 */
public class TagLibraryManager {

	/**
	 * Returns all tag libraries which have given URI and which are available in the project.
	 * @param project
	 * @param uri
	 * @return
	 */
	public static ITagLibrary[] getLibraries(IProject project, String uri) {
		IKbProject kbProject = KbProjectFactory.getKbProject(project, true);
		if(kbProject == null) {
			return new ITagLibrary[0];
		}
		return kbProject.getTagLibraries(uri);
	}
}