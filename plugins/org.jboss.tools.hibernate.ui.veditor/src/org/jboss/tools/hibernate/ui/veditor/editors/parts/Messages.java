/*******************************************************************************
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.hibernate.ui.veditor.editors.parts;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jboss.tools.hibernate.ui.veditor.editors.parts.messages"; //$NON-NLS-1$

	private Messages() {
	}

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	public static String Colors_PersistentClassR;
	
	public static String Colors_PersistentClassG;
	
	public static String Colors_PersistentClassB;
	
	public static String Colors_PersistentFieldR;
	
	public static String Colors_PersistentFieldG;
	
	public static String Colors_PersistentFieldB;
	
	public static String Colors_DatabaseTableR;
	
	public static String Colors_DatabaseTableG;
	
	public static String Colors_DatabaseTableB;
	
	public static String Colors_DatabaseColumnR;
	
	public static String Colors_DatabaseColumnG;
	
	public static String Colors_DatabaseColumnB;

	public static String ORMEDITPARTFACTORY_CANOT_CREATE_PART_FOR_MODEL_ELEMENT;

	public static String ORMEDITPARTFACTORY_NULL;

	public static String TREEPARTFACTORY_CANOT_CREATE_PART_FOR_MODEL_ELEMENT;

	public static String TREEPARTFACTORY_NULL;
	
}
