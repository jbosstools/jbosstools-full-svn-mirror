/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.common.model.filesystems.impl;

import org.jboss.tools.common.model.*;
import org.jboss.tools.common.model.filesystems.*;

public class FileSystemsTreeConstraint implements XFilteredTreeConstraint {

    public FileSystemsTreeConstraint() {}

    public void update(XModel model) {}
    
    String excludeExtensions = ".project.struts.classpath.";

    public boolean accepts(XModelObject object) {
        String entity = object.getModelEntity().getName();
        if("FileAnyAuxiliary".equals(entity)) return false;
        if(object.getFileType() == XFileObject.SYSTEM) {
            String s = object.getAttributeValue("info");
            return (s == null || s.indexOf("hidden=yes") < 0);
        }
        if(object.getFileType() == XFileObject.FILE) {
        	String ext = "." + object.getAttributeValue("extension") + ".";
        	if(excludeExtensions.indexOf(ext) >= 0) return false;
        	if(".rule-sets".equals(object.getAttributeValue("name"))) return false;        	
        } else if(object.getFileType() == XFileObject.FOLDER) {
			String[] ns = getHiddenFolderNames();
			String n = object.get("NAME");
			for (int i = 0; i < ns.length; i++) if(ns[i].equalsIgnoreCase(n)) return false;
        }
        return true;
    }

    public boolean isHidingAllChildren(XModelObject object) {
        if(object.getFileType() > XFileObject.NONE
           && "true".equals(object.get("overlapped"))) return true;
//        String entity = object.getModelEntity().getName();
        return false; ///entity.equals("FilePROPERTIES");
    }

    public boolean isHidingSomeChildren(XModelObject object) {
        if(object.getModelEntity().getName().equals("FileSystems")) return true;
        if(object.getFileType() < XFileObject.FOLDER) return false;
        return true;
    }

    private static String[] names = new String[]{"CVS", ".svn"};

    protected String[] getHiddenFolderNames() {
        return names;
    }

}

