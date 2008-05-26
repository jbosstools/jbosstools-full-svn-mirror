/******************************************************************************* 
 * Copyright (c) 2007 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.seam.pages.xml.model;

import org.jboss.tools.common.model.loaders.impl.SimpleWebFileLoader;
import org.jboss.tools.common.model.util.XModelObjectLoaderUtil;

public class SeamPages12FileLoader extends SimpleWebFileLoader {
    
    public SeamPages12FileLoader() {}

    protected XModelObjectLoaderUtil createUtil() {
        return new SeamPagesLoaderUtil();
    }
    
    protected boolean isCheckingDTD() {
    	return true;
    }
    
    protected boolean isCheckingSchema() {
    	return false;
    }

}
