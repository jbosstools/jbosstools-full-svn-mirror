/*******************************************************************************
 * Copyright (c) 2011 Red Hat Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Incorporated - initial API and implementation
 *******************************************************************************/
package org.jboss.tools.openshift.express.internal.ui;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.jboss.tools.openshift.express.internal.ui.utils.ImageRepository;

public class OpenShiftImages {

	private static final String ICONS_FOLDER = "icons/";

	private static final ImageRepository repo = 
			new ImageRepository(ICONS_FOLDER, OpenShiftUIActivator.getDefault(), OpenShiftUIActivator.getDefault().getImageRegistry());

	public static final ImageDescriptor OPENSHIFT_LOGO_DARK = repo.create("openshift-logo-dark.png"); //$NON-NLS-1$ 
	public static final ImageDescriptor OPENSHIFT_LOGO_WHITE = repo.create("openshift-logo-white.png"); //$NON-NLS-1$ 
	public static final ImageDescriptor OPENSHIFT_LOGO_WHITE_MEDIUM = repo.create("openshift-logo-white-medium.png"); //$NON-NLS-1$ 	
	public static final Image OPENSHIFT_LOGO_WHITE_MEDIUM_IMG = repo.getImage("openshift-logo-white-medium.png"); //$NON-NLS-1$ 	
}
