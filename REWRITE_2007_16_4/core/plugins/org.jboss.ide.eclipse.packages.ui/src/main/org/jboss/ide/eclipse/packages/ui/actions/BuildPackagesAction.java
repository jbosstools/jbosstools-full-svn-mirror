/*
 * JBoss, a division of Red Hat
 * Copyright 2006, Red Hat Middleware, LLC, and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.ide.eclipse.packages.ui.actions;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.jboss.ide.eclipse.core.util.ProjectUtil;
import org.jboss.ide.eclipse.packages.core.model.IPackage;
import org.jboss.ide.eclipse.packages.core.model.PackagesCore;
import org.jboss.ide.eclipse.packages.ui.PackagesUIPlugin;
import org.jboss.ide.eclipse.packages.ui.views.ProjectPackagesView;
import org.jboss.ide.eclipse.ui.util.ActionWithDelegate;

public class BuildPackagesAction extends ActionWithDelegate implements IWorkbenchWindowActionDelegate {

	private IWorkbenchWindow window;
	
	public void dispose() {
	}

	public void run() {
		IProject project = null;
		IStructuredSelection selection = getSelection();
		
		if (selection != null)
		{
			project = ProjectUtil.getProject(selection);
		}
		if (project == null)
		{
			if (ProjectPackagesView.instance() != null)
				project = ProjectPackagesView.instance().getCurrentProject();
		}
		
		ProgressMonitorDialog dialog = new ProgressMonitorDialog(window.getShell());
		IProgressMonitor monitor = dialog.getProgressMonitor();
		
		if (project != null)
		{
			dialog.open();
			
			PackagesCore.buildProject(project, monitor);
			
			dialog.close();
		}
		else {
			if (selection.getFirstElement() instanceof IPackage)
			{
				dialog.open();
				
				IPackage pkg = (IPackage) selection.getFirstElement();
				PackagesCore.buildPackage(pkg, monitor);
				
				dialog.close();
			}
		}
	}

	public void init(IWorkbenchWindow window) {
		this.window = window;
	}
	
	public ImageDescriptor getImageDescriptor() {
		return PackagesUIPlugin.getImageDescriptor(PackagesUIPlugin.IMG_BUILD_PACKAGES);
	}
	
	public String getText() {
		return "Build Packages";
	}
	
	public String getToolTipText () {
		return "Build Packages";
	}
}
