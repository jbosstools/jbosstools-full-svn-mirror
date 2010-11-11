/*******************************************************************************
 * Copyright (c) 2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.deltacloud.ui.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.rse.core.IRSECoreRegistry;
import org.eclipse.rse.core.IRSESystemType;
import org.eclipse.rse.core.RSECorePlugin;
import org.eclipse.rse.core.model.IHost;
import org.eclipse.rse.core.model.ISystemRegistry;
import org.eclipse.rse.core.model.SystemStartHere;
import org.eclipse.rse.core.subsystems.IConnectorService;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jboss.tools.deltacloud.core.DeltaCloudInstance;
import org.jboss.tools.deltacloud.ui.Activator;
import org.jboss.tools.deltacloud.ui.views.CVMessages;
import org.jboss.tools.internal.deltacloud.ui.utils.UIUtils;

/**
 * @author Andre Dietisheim
 */
public class ShowInRemoteSystemExplorerHandler extends AbstractHandler implements IHandler {

	private static final String VIEW_REMOTESYSEXPLORER_ID = "org.eclipse.rse.ui.view.systemView";
	private final static String RSE_CONNECTING_MSG = "ConnectingRSE.msg"; //$NON-NLS-1$

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		if (selection instanceof IStructuredSelection) {
			DeltaCloudInstance instance = UIUtils.getFirstAdaptedElement(selection, DeltaCloudInstance.class);
			launchRemoteSystemExplorer(instance);
		}

		return Status.OK_STATUS;
	}

	private void launchRemoteSystemExplorer(DeltaCloudInstance instance) {
		String hostname = instance.getHostName();
		IRSESystemType sshType = getRSESystemType();
		String connectionName = instance.getName() + " [" + instance.getId() + "]"; //$NON-NLS-1$ //$NON-NLS-2$
		try {
			ISystemRegistry registry = SystemStartHere.getSystemRegistry();
			IHost host = registry.createHost(sshType, connectionName, hostname, null);
			if (host != null) {
				host.setDefaultUserId("root"); //$NON-NLS-1$
				IConnectorService[] services = host.getConnectorServices();
				if (services.length > 0) {
					final IConnectorService service = services[0];
					Job connect = new Job(CVMessages.getFormattedString(RSE_CONNECTING_MSG, connectionName)) {
						@Override
						protected IStatus run(IProgressMonitor monitor) {
							try {
								service.connect(monitor);
								Display.getDefault().asyncExec(new Runnable() {
									@Override
									public void run() {
										try {
											PlatformUI.getWorkbench().getActiveWorkbenchWindow()
													.getActivePage()
													.showView(VIEW_REMOTESYSEXPLORER_ID);
										} catch (PartInitException e) {
											// TODO Auto-generated catch
											// block
											Activator.log(e);
										}
									}
								});
								return Status.OK_STATUS;
							} catch (Exception e) {
								return Status.CANCEL_STATUS;
							}
						}
					};
					connect.setUser(true);
					connect.schedule();
				}
			} else {
				// Assume failure is due to name already in use
				Display.getDefault().asyncExec(new Runnable() {

					@Override
					public void run() {
						try {
							PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
									.showView(VIEW_REMOTESYSEXPLORER_ID);
						} catch (PartInitException e) {
							Activator.log(e);
						}
					}

				});
			}
		} catch (Exception e) {
			Activator.log(e);
		}
	}

	private IRSESystemType getRSESystemType() {
		IRSESystemType sshType = null;
		RSECorePlugin rsep = RSECorePlugin.getDefault();
		IRSECoreRegistry coreRegistry = rsep.getCoreRegistry();
		IRSESystemType[] sysTypes = coreRegistry.getSystemTypes();
		for (IRSESystemType sysType : sysTypes) {
			if (sysType.getId().equals(IRSESystemType.SYSTEMTYPE_SSH_ONLY_ID))
				sshType = sysType;
		}
		return sshType;
	}
}
