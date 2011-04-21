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

package org.jboss.tools.vpe.resref.core;

import org.eclipse.osgi.util.NLS;

/**
 * The Class Messages.
 * 
 * @author Eugene Stherbin
 */
public final class Messages extends NLS {
    
    /** The Constant BUNDLE_NAME. */
    private static final String BUNDLE_NAME = "org.jboss.tools.vpe.resref.core.messages";//$NON-NLS-1$
    
    static {
        // load message values from bundle file
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);       
    }
    
    /**
     * The Constructor.
     */
    private Messages(){}
    
    
    public static String VRD_TITLE_IMAGE_CANNOT_BE_RESOLVED;
    public static String VRD_DEFAULT_WINDOW_TITLE;
    public static String VRD_DEFAULT_TITLE;
    public static String VRD_DEFAULT_MESSAGE;

	public static String VRD_LABEL_ADD;
	public static String VRD_LABEL_EDIT;
	public static String VRD_LABEL_REMOVE;
	public static String SCOPE_GROUP_NAME;
	public static String SCOPE_PAGE;
	public static String SCOPE_PAGE_SHORT;
	public static String SCOPE_FOLDER;
	public static String SCOPE_FOLDER_SHORT;
	public static String SCOPE_PROJECT;
	public static String SCOPE_PROJECT_SHORT;
	public static String SCOPE_GLOBAL;
	public static String SCOPE_GLOBAL_SHORT;
	public static String WIZARD_PAGE_SHOULD_BE_INITIALIZED;
	
    
	public static String VRD_INCLUDED_CSS_FILES;
	public static String VRD_INCLUDED_TAG_LIBS;
	public static String VRD_SUBSTITUTED_EL_EXPRESSIONS;
	public static String VRD_ACTUAL_RUN_TIME_ABSOLUTE_FOLDER;
	public static String VRD_ACTUAL_RUN_TIME_RELATIVE_FOLDER;
	public static String VRD_ACTUAL_RUN_TIME_FOLDERS;

	public static String VRD_ADD_CSS_PREFERENCE;
	public static String VRD_ADD_CSS_PREFERENCE_MESSAGE;
	public static String VRD_ADD_TAGLIB_PREFERENCE;
	public static String VRD_ADD_TAGLIB_PREFERENCE_MESSAGE;
	public static String VRD_ADD_EL_PREFERENCE;
	public static String VRD_ADD_EL_PREFERENCE_MESSAGE;
	public static String ADD_GLOBAL_EL_PREFERENCE_MESSAGE;
	
	public static String VRD_PAGE_DESIGN_OPTIONS_ABOUT;
	public static String VRD_ACTUAL_RUN_TIME_FOLDERS_ABOUT;
	public static String VRD_INCLUDED_CSS_FILES_ABOUT;
	public static String VRD_INCLUDED_TAG_LIBS_ABOUT;
	public static String VRD_SUBSTITUTED_EL_EXPRESSIONS_ABOUT;
	
	public static String FOLDER_PATH;
	public static String SELECT_FOLDER_DIALOG_TITLE;
	
	public static String CSS_WIZARD_PAGE_NAME;
	public static String CSS_FILE_PATH;
	public static String CSS_FILE_DOES_NOT_EXIST;
	public static String CSS_FILE_PATH_SHOULD_BE_SET;

	public static String TAGLIB_WIZARD_PAGE_NAME;
	public static String TAGLIB_URI;
	public static String TAGLIB_PREFIX;
	public static String INCORRECT_PREFIX;
	public static String INCORRECT_URI;
	public static String PREFIX_SHOULD_BE_SET;
	public static String URI_SHOULD_BE_SET;
	
	
	public static String GLOBAL_EL_WIZARD_PAGE_NAME;
	public static String EL_WIZARD_PAGE_NAME;
	public static String EL_NAME;
	public static String EL_VALUE;
	public static String EL_NAME_SHOULD_BE_SET;
	public static String INVALID_EL_EXPRESSION;
	public static String CANNOT_PARSE_SCOPE_VALUE;
	public static String EL_EXPRESSION_ALREADY_EXISTS;
}
