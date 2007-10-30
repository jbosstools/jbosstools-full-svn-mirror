/*******************************************************************************
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.seam.ui;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.internal.ui.packageview.PackageExplorerPart;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IStartup;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.componentcore.resources.IVirtualFolder;
import org.jboss.tools.seam.core.SeamCorePlugin;

/**
 * Workaround for https://bugs.eclipse.org/bugs/show_bug.cgi?id=207146
 * @author eskimo
 *
 */
public class WorkaroundFor207146 implements IStartup{

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IStartup#earlyStartup()
	 */
	public void earlyStartup() {
		ResourcesPlugin.getWorkspace().addResourceChangeListener(new WebContentUpdater());
	}

	public static class WebContentUpdater implements IResourceChangeListener {
		public void resourceChanged(IResourceChangeEvent event) {
			ManifestChangeDetector visitor = new ManifestChangeDetector();
			try {
				event.getDelta().accept(visitor);
			} catch (CoreException e) {
				SeamCorePlugin.getPluginLog().logError(e);
			}
			if(visitor.skip) return; // skip listener if MANIFEST.MF and WEB-INF were changed

			IResourceDelta[] delta = event.getDelta().getAffectedChildren();

			// go trough changed resources
			for (IResourceDelta resourceDelta : delta) {
				IProject prj = resourceDelta.getResource().getProject();
				IVirtualComponent comp = ComponentCore.createComponent(prj);
				if(comp==null) continue;
				final IVirtualFolder root = comp.getRootFolder();
				// check that changes in WebContent folder
				if(event.getDelta().findMember(root.getUnderlyingFolder().getFullPath())==null) {
					return;
				}
				// Refresh Package Explorer
				Display display = Display.getDefault();
				if(display==null) {
					return;
				}
				display.asyncExec(new Runnable() {
					public void run() {
						PackageExplorerPart p = PackageExplorerPart.getFromActivePerspective();
						if(p!=null) {
							TreeViewer tv = p.getTreeViewer();
							if(tv!=null) {
								tv.refresh();
							}
						}
					}
				});

				return;
			}
		}
	}

	/**
	 * Find if there is MANIFEST.MF or WEB-INF resources in IResourceDelta
	 * @author eskimo
	 *
	 */
	public static class ManifestChangeDetector implements IResourceDeltaVisitor {

		/**
		 * TODO handle case when something is changed in WEB-INF folder 
		 */
		public boolean visit(IResourceDelta delta) throws CoreException {
			if(skip) return false; // skip everything if MANIFEST.MF || WEB-INF are found already
			if("MANIFEST.MF".equals(delta.getResource().getLocation().lastSegment()) 
					|| "META-INF".equals(delta.getResource().getLocation().lastSegment())){
				skip = true;
				return false;
			}
			return true;
		}

		boolean skip = false;
	}
}