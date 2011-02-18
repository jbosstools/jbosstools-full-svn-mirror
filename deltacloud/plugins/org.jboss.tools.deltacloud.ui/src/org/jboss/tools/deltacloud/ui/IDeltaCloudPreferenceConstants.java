/*******************************************************************************
 * Copyright (c) 2010 Red Hat Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Incorporated - initial API and implementation
 *******************************************************************************/
package org.jboss.tools.deltacloud.ui;

public interface IDeltaCloudPreferenceConstants {
	
	public static final String DONT_CONFIRM_CREATE_INSTANCE = "dont_confirm_create_instance"; //$NON-NLS-1$
	public static final String DONT_CONFIRM_STOP_INSTANCE = "dont_confirm_stop_instance"; //$NON-NLS-1$
	public static final String AUTO_CONNECT_INSTANCE = "auto_connect_instance"; //$NON-NLS-1$
	public static final String LAST_EC2_KEYNAME = "last_ec2_keyname"; //$NON-NLS-1$
	public static final String DEFAULT_KEY_DIR = "default_key_directory"; //$NON-NLS-1$

	public static final String CLOUD_NAME_PROPOSAL_KEY = "cloud_name";//$NON-NLS-1$
	public static final String CLOUD_USERNAME_PROPOSAL_KEY = "cloud_username";//$NON-NLS-1$
	public static final String CLOUD_URL_PROPOSAL_KEY = "cloud_url";//$NON-NLS-1$
	public static final String CLOUD_LAST_INSTANCE_KEY = "last_cloud_instance_view"; //$NON-NLS-1$
	public static final String CLOUD_LAST_IMAGE_KEY = "last_cloud_image_view"; //$NON-NLS-1$

	public static final String INSTANCE_NAME_PROPOSAL_KEY = "instance_name";//$NON-NLS-1$
	public static final String INSTANCE_IMAGE_PROPOSAL_KEY = "instance_image";//$NON-NLS-1$
	public static final String INSTANCE_KEY_PROPOSAL_KEY = "instance_key";//$NON-NLS-1$
}
