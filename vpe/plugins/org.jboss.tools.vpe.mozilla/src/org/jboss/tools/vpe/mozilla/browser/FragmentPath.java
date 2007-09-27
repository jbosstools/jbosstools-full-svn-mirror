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
package org.jboss.tools.vpe.mozilla.browser;

import java.io.IOException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

/**
 * @author Sergey Vasilyev
 */
public abstract class FragmentPath implements IFragmentPath {
	private String pluginId;

	public FragmentPath(String pluginId) {
		this.pluginId = pluginId;
	} // FragmentPath(String)
	
	public String getPluginId() {
		return pluginId;
	} // String getPluginId()
	
	public abstract String getFragmentId();
	public abstract String getResultLocation(String location);
	
	public IPath getPath() {
		Bundle bundle = Platform.getBundle(getFragmentId());
		String location=null;
		try {
			location = getResultLocation(FileLocator.resolve(bundle.getEntry("/")).getFile());
		} catch (IOException e) {
			ProblemReporter.reportProblem(e);
		}		
		IPath path = null;
		if (location.startsWith(".")) {
			path = new Path(Platform.getInstallLocation().getURL().getPath());
			path = path.append(location);
		} else {
			path = new Path(location);
		}
		path = path.append("os").append(Platform.getOS()).append(Platform.getOSArch());

//		Bundle bundle = Platform.getBundle(getFragmentId());
//		IPath path = new Path(getResultLocation(bundle.getLocation()));
//		path = path.append("os").append(Platform.getOS()).append(Platform.getOSArch());
		return path;
	} // getPath()

} // class FragmentPath
