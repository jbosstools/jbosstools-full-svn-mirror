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
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.jboss.ide.eclipse.core.util.ProjectUtil;
import org.jboss.ide.eclipse.packages.core.model.IPackage;
import org.jboss.ide.eclipse.packages.core.model.PackagesCore;
import org.jboss.ide.eclipse.ui.util.ActionWithDelegate;

public class BuildPackagesAction extends ActionWithDelegate implements IWorkbenchWindowActionDelegate {

	private IWorkbenchWindow window;
	
	public void dispose() {
	}

	public void run() {
		if (getSelection() != null)
		{
			IProject project = ProjectUtil.getProject(getSelection());
			if (project != null)
			{
				ProgressMonitorDialog dialog = new ProgressMonitorDialog(window.getShell());
				IProgressMonitor monitor = dialog.getProgressMonitor();
				//dialog.setBlockOnOpen(false);
				dialog.open();
				
				IPackage[] packages = PackagesCore.getProjectPackages(project, monitor);
				for (int i = 0; i < packages.length; i++)
				{
					PackagesCore.buildPackage(packages[i], monitor);
				}
				
				dialog.close();
			}
		}
	}

	public void init(IWorkbenchWindow window) {
		this.window = window;
	}
}
