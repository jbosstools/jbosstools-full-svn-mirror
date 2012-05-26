/*******************************************************************************
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.gwt.core;

/**
 * A class that holds various constants that are used to handle GWT Designer
 * plugin specific functionalities.
 * 
 * @author adietish
 */
public class IGWTDesignerConstants {

	public static final String GWTDESIGNER_NATURE = "com.instantiations.designer.gwt.GWTNature"; //$NON-NLS-1$

	public static final String GWTDESIGNER_BUILDER = "com.instantiations.designer.gwt.GWTBuilder"; //$NON-NLS-1$

	/** the plugin that contributes GWT Designer nature and builder */
	public static final String GWTDESIGNER_PLUGIN_REGEXP = "com\\.instantiations\\.designer\\.gwt";

}
