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
 * A class that holds various constants that are used to handle google eclipse
 * plugin specific functionalities.
 * 
 * @author adietish
 */
public class IGoogleEclipsePluginConstants {

	public static final String GWT_NATURE = "com.google.gwt.eclipse.core.gwtNature"; //$NON-NLS-1$

	/** The gwt class path container id. */
	public static final String GWT_CONTAINER_ID = "com.google.gwt.eclipse.core.GWT_CONTAINER"; //$NON-NLS-1$

	/** The gwt compiler settings key. */
	public static final String COMPILESETTINGS_KEY = "gwtCompileSettings"; //$NON-NLS-1$

	
	/** The if of the gdt plugin. */
	public static final String GDT_PLUGIN_ID = "com.google.gdt.eclipse.core"; //$NON-NLS-1$

	/** The war source directory key. */
	public static final String WAR_SRCDIR_KEY = "warSrcDir"; //$NON-NLS-1$

	/** the path to the war directory. */
	public static final String WAR_SRCDIR_ISOUTPUT_KEY = "warSrcDirIsOutput"; //$NON-NLS-1$
	
	/** flag that tells the google plugin to compile to/run from the war directory. @see #WAR_SRCDIR_ISOUTPUT_KEY */
	public static final String WAR_SRCDIR_ISOUTPUT_DEFAULTVALUE = "true"; //$NON-NLS-1$

	/** the folder to compile to. Should be a sub-folder of the WAR_SRCDIR, otherwise google proj. validator complains. */
	public static final String OUTPUT_FOLDER_DEFAULTVALUE = "/WEB-INF/classes"; //$NON-NLS-1$

	/** the zip-file that holds the samples source that's shipped with the plugin */
	public static final String SAMPLE_HELLO_SRC_ZIP_FILENAME = "/sample-hello-src.zip"; //$NON-NLS-1$

	/** the zip-file that holds the samples web-content that's shipped with the plugin */
	public static final String SAMPLE_HELLO_WEBCONTENT_ZIP_FILENAME = "/sample-hello-webContent.zip"; //$NON-NLS-1$

	public static final String SERVLET_NAME = "greetServlet"; //$NON-NLS-1$

	public static final String SERVLET_CLASS = "org.jboss.tools.gwt.server.GreetingServiceImpl"; //$NON-NLS-1$

	public static final String SERVLET_MAPPING = "/gwt_jboss/greet"; //$NON-NLS-1$

	public static final String WELCOME_FILE = "Gwt_jboss.html"; //$NON-NLS-1$

	public static final String GWT_SDK_BUNDLENAME = "com.google.gwt.eclipse.sdkbundle"; //$NON-NLS-1$

	public static final String GWT_SERVLET_NAME = "gwt-servlet.jar"; //$NON-NLS-1$

	public static final String WEB_INF_LIB = "/WEB-INF/lib"; //$NON-NLS-1$;
	
}
