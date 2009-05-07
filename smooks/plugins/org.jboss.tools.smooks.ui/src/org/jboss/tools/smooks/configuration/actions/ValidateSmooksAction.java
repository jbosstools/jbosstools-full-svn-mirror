/*******************************************************************************
 * Copyright (c) 2008 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.configuration.actions;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.ui.dialogs.DiagnosticDialog;
import org.eclipse.emf.common.ui.viewer.IViewerProvider;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ISetSelectionTarget;
import org.jboss.tools.smooks.configuration.SmooksConfigurationActivator;
import org.jboss.tools.smooks.configuration.validate.SmooksMarkerHelper;
import org.jboss.tools.smooks.model.validate.SmooksModelValidator;

/**
 * @author Dart (dpeng@redhat.com)
 * 
 */
public class ValidateSmooksAction extends Action {

	private Resource resource = null;

	private EditingDomain editingDomain;

	private SmooksModelValidator validator = null;

	private SmooksMarkerHelper markerHelper = new SmooksMarkerHelper();

	@Override
	public void run() {
		final Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		IRunnableWithProgress runnableWithProgress = new IRunnableWithProgress() {
			public void run(final IProgressMonitor progressMonitor) throws InvocationTargetException,
					InterruptedException {
				try {
					final Diagnostic diagnostic = validate(progressMonitor);
					shell.getDisplay().asyncExec(new Runnable() {
						public void run() {
							if (progressMonitor.isCanceled()) {
								handleDiagnostic(Diagnostic.CANCEL_INSTANCE);
							} else {
								handleDiagnostic(diagnostic);
							}
						}
					});
				} finally {
					progressMonitor.done();
				}
			}
		};

		if (markerHelper != null) {
			runnableWithProgress = markerHelper.getWorkspaceModifyOperation(runnableWithProgress);
		}

		try {
			// This runs the operation, and shows progress.
			// (It appears to be a bad thing to fork this onto another thread.)
			//
			new ProgressMonitorDialog(shell).run(true, true, runnableWithProgress);
		} catch (Exception exception) {
			SmooksConfigurationActivator.getDefault().log(exception);
		}
	}

	protected void handleDiagnostic(Diagnostic diagnostic) {
		int severity = diagnostic.getSeverity();
		String title = null;
		String message = null;

		if (severity == Diagnostic.ERROR || severity == Diagnostic.WARNING) {
			title = "Error";
			message = "Validate Messages";
		} else {
			title = "Information";
			message = "Validate success";
		}

		int result = 0;
		if (diagnostic.getSeverity() == Diagnostic.OK) {
//			MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), title,
//					message);
//			result = Window.CANCEL;
			return;
		} else {
			result = DiagnosticDialog.open(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), title,
					message, diagnostic);
		}

		if (markerHelper != null) {
			Resource resource = editingDomain.getResourceSet().getResources().get(0);
			if (resource != null) {
				markerHelper.deleteMarkers(resource);
			}

			if (result == Window.OK) {
				if (!diagnostic.getChildren().isEmpty()) {
					List<?> data = (diagnostic.getChildren().get(0)).getData();
					if (!data.isEmpty() && data.get(0) instanceof EObject) {
						Object part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
								.getActivePart();
						if (part instanceof ISetSelectionTarget) {
							((ISetSelectionTarget) part).selectReveal(new StructuredSelection(data.get(0)));
						} else if (part instanceof IViewerProvider) {
							Viewer viewer = ((IViewerProvider) part).getViewer();
							if (viewer != null) {
								viewer.setSelection(new StructuredSelection(data.get(0)), true);
							}
						}
					}
				}

				if (resource != null) {
					for (Diagnostic childDiagnostic : diagnostic.getChildren()) {
						markerHelper.createMarkers(resource, childDiagnostic);
					}
				}
			}
		}
	}

	protected Diagnostic validate(IProgressMonitor progressMonitor) {
		if (resource != null && editingDomain != null) {
			validator = new SmooksModelValidator(resource.getContents(), editingDomain);
			return validator.validate(progressMonitor);
		}
		return Diagnostic.OK_INSTANCE;
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public EditingDomain getEditingDomain() {
		return editingDomain;
	}

	public void setEditingDomain(EditingDomain editingDomain) {
		this.editingDomain = editingDomain;
	}
}
