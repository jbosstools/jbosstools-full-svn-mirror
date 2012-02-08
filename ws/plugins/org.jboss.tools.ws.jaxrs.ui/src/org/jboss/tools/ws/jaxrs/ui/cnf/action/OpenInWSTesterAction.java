/******************************************************************************* 
 * Copyright (c) 2012 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Brian Fitzpatrick - Initial API and implementation 
 ******************************************************************************/

package org.jboss.tools.ws.jaxrs.ui.cnf.action;

import java.lang.reflect.Method;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.navigator.CommonViewer;
import org.jboss.tools.ws.jaxrs.ui.cnf.UriPathTemplateElement;
import org.jboss.tools.ws.jaxrs.ui.internal.utils.Logger;

/**
 * @author bfitzpat
 *
 */
public class OpenInWSTesterAction extends Action implements ISelectionChangedListener {

	private ISelection selection = null;

	public OpenInWSTesterAction() {
		super("Open in JBoss WS Tester");
	}

	/**
	 * (non-Javadoc)
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		ITreeSelection treeSelection = ((ITreeSelection) selection);
		@SuppressWarnings("rawtypes")
		List selections = treeSelection.toList();
		if (selections.isEmpty()) {
			return;
		}
		Object selectedObject = selections.get(0);
		try {
			if (selectedObject instanceof UriPathTemplateElement) {
				UriPathTemplateElement element = (UriPathTemplateElement) selectedObject;
				String uriPathTemplate = element.getEndpoint().getUriPathTemplate();
				String uriPrefix = "http://[domain]:[port]"; //$NON-NLS-1$
				
				// Now we call the WS Tester through Reflection so there's no direct plug-in dependency
				IWorkbench wb = PlatformUI.getWorkbench();
				IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
				IWorkbenchPage page = win.getActivePage();				
				String id = "org.jboss.tools.ws.ui.tester.views.TestWSView"; //$NON-NLS-1$
				try {
					IViewPart part = page.showView(id);
					if (part != null && 
							part.getClass().getName().equalsIgnoreCase(
									"org.jboss.tools.ws.ui.views.JAXRSWSTestView2")) { //$NON-NLS-1$
						Class<?> c = part.getClass();
						Class<?> parmtypes[] = new Class[2];
						parmtypes[0] = String.class;
						parmtypes[1] = String.class;
						Method setURL = c.getDeclaredMethod("setJAXRS", parmtypes); //$NON-NLS-1$
						if (setURL != null) {
							Object arglist[] = new Object[2];
				            arglist[0] =  uriPrefix + uriPathTemplate;
				            arglist[1] = element.getEndpoint().getHttpMethod().getHttpVerb();
				            setURL.invoke(part, arglist);
				        }
					}
				} catch (PartInitException e) {
					e.printStackTrace();
					Logger.error("Failed to open WS Tester", e);
				}
			}
		} catch (Exception e) {
			Logger.error("Failed to open WS Tester", e);
		}
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		Object source = event.getSource();
		if (source instanceof CommonViewer) {
			this.selection = ((CommonViewer) source).getSelection();
		}
	}

	public void setSelection(ISelection selection) {
		this.selection = selection;

	}

}
