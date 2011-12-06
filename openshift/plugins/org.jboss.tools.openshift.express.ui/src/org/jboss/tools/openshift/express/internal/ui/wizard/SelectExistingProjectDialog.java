/*******************************************************************************
 * Copyright (c) 2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.openshift.express.internal.ui.wizard;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.ide.IDE.SharedImages;
import org.eclipse.wst.common.componentcore.internal.util.IModuleConstants;
import org.jboss.tools.openshift.egit.core.EGitUtils;

/**
 * @author André Dietisheim
 */
public class SelectExistingProjectDialog extends ElementListSelectionDialog {

	public SelectExistingProjectDialog(String openShiftAppName, Shell shell) {
		super(shell, new ProjectLabelProvider());
		setTitle("Project Selection");
		setMessage(NLS.bind(
				"Select an existing project for {0}.\nOnly java projects which are not Team shared can be used.",
				openShiftAppName));
		setMultipleSelection(false);
		setAllowDuplicates(false);
		setElements(getProjects());
	}

	private Object[] getProjects() {
		List<IProject> projects = new ArrayList<IProject>();
		for (IProject project : ResourcesPlugin.getWorkspace().getRoot().getProjects()) {
			if (isValid(project)) {
				projects.add(project);
			}
		}
		return projects.toArray();
	}

	private boolean isValid(IProject project) {
		if (EGitUtils.isShared(project)) {
			return false;
		}

		if (EGitUtils.hasDotGitFolder(project)) {
			return false;
		}

		if (!hasModuleNature(project)) {
			return false;
		}

		return true;
	}

	private boolean hasModuleNature(IProject project) {
		try {
			return project.hasNature(IModuleConstants.MODULE_NATURE_ID);
		} catch(CoreException e) {
			return false;
		}
	}

	private static class ProjectLabelProvider extends LabelProvider {

		@Override
		public Image getImage(Object element) {
			return PlatformUI.getWorkbench().getSharedImages().getImage(SharedImages.IMG_OBJ_PROJECT);
		}

		@Override
		public String getText(Object element) {
			if (!(element instanceof IProject)) {
				return null;
			}

			return ((IProject) element).getName();
		}

	}

}
