/*******************************************************************************
 * Copyright (c) 2006 University College London Software Systems Engineering
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * 	Bruno Wassermann - initial API and implementation
 *******************************************************************************/
package org.jboss.tools.bpel.runtimes;

/**
 *
 *
 * @author Bruno Wassermann, written Jun 29, 2006
 */
public interface IBPELModuleFacetConstants {
    
	public final static String BPEL_CONTENT_FOLDER =  "BPELFacetInstallDataModelProvider.contentRoot";
    public final static String BPEL_CONTENT_DEFAULT_FOLDER =  "bpelContent";


	// module types
	public final static String BPEL_MODULE_TYPE = "jbt.bpel.module"; //$NON-NLS-1$
	
	// module type versions
	public final static String BPEL11_VERSION = "1.1"; // $NON-NLS-1$
	public final static String BPEL20_VERSION = "2.0"; // $NON-NLS-1$
	
	// facet template
	public final static String BPEL20_FACET_TEMPLATE = "jbt.template.bpel.core";  //$NON-NLS-1$
	
	// facet
	// this facet has been deprecated
	public final static String JBT_BPEL_PROJECT_FACET = "jbt.bpel.facet.core"; //$NON-NLS-1$ 
	public final static String BPEL_PROJECT_FACET = "bpel.facet.core"; //$NON-NLS-1$ 
	
	// bpel file extension
	public final static String BPEL_FILE_EXTENSION = "bpel";  //$NON-NLS-1$
	public final static String DOT_BPEL_FILE_EXTENSION = "." + BPEL_FILE_EXTENSION;  //$NON-NLS-1$
	
	// default content folder
	public final static String BPEL_CONTENT = "bpelContent"; //$NON-NLS-1$
}
