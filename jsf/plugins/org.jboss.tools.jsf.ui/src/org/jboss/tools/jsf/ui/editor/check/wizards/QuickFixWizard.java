/*******************************************************************************
 * Copyright (c) 2007-2010 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.jsf.ui.editor.check.wizards;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.internal.ide.IDEInternalWorkbenchImages;
import org.eclipse.ui.internal.ide.StatusUtil;
import org.eclipse.ui.statushandlers.StatusManager;
import org.eclipse.ui.views.markers.internal.MarkerMessages;

/**
 * 
 * @author yzhishko
 *
 */

@SuppressWarnings("restriction")
public class QuickFixWizard extends Wizard {

	private Map resolutionMap;
	private String description;

	/**
	 * Create the wizard with the map of resolutions.
	 * 
	 * @param description
	 *            the description of the problem
	 * @param resolutions
	 *            Map key {@link IMarkerResolution} value {@link IMarker} []
	 * @param site
	 *            the {@link IWorkbenchPartSite} to open the markers in
	 */
	public QuickFixWizard(String description, Map resolutions) {
		super();
		this.resolutionMap = resolutions;
		this.description = description;
		setDefaultPageImageDescriptor(IDEInternalWorkbenchImages
				.getImageDescriptor(IDEInternalWorkbenchImages.IMG_DLGBAN_QUICKFIX_DLG));
		setNeedsProgressMonitor(true);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	public void addPages() {
		super.addPages();
		addPage(new QuickFixPage(description, resolutionMap));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	public boolean performFinish() {
		IRunnableWithProgress finishRunnable = new IRunnableWithProgress() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.jface.operation.IRunnableWithProgress#run(org.eclipse
			 * .core.runtime.IProgressMonitor)
			 */
			public void run(IProgressMonitor monitor) {
				IWizardPage[] pages = getPages();
				monitor.beginTask(MarkerMessages.MarkerResolutionDialog_Fixing,
						(10 * pages.length) + 1);
				monitor.worked(1);
				for (int i = 0; i < pages.length; i++) {
					// Allow for cancel event processing
					getShell().getDisplay().readAndDispatch();
					if (monitor.isCanceled())
						return;
					QuickFixPage wizardPage = (QuickFixPage) pages[i];
					wizardPage
							.performFinish(new SubProgressMonitor(monitor, 10));
					monitor.worked(1);
				}
				monitor.done();

			}
		};

		try {
			getContainer().run(false, true, finishRunnable);
		} catch (InvocationTargetException e) {
			StatusManager.getManager().handle(
					StatusUtil.newStatus(IStatus.ERROR,
							e.getLocalizedMessage(), e));
			return false;
		} catch (InterruptedException e) {
			StatusManager.getManager().handle(
					StatusUtil.newStatus(IStatus.ERROR,
							e.getLocalizedMessage(), e));
			return false;
		}

		return true;
	}

}
