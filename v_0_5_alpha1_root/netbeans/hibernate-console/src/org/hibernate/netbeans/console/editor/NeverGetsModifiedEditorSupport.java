/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */


package org.hibernate.netbeans.console.editor;

import java.io.IOException;
import org.hibernate.netbeans.console.SessionFactoryNode;
import org.openide.ErrorManager;
import org.openide.cookies.SaveCookie;
import org.openide.filesystems.FileLock;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.loaders.MultiDataObject;
import org.openide.text.DataEditorSupport;

/**
 * @author leon
 */
public class NeverGetsModifiedEditorSupport extends DataEditorSupport implements SaveCookie {
    
    public NeverGetsModifiedEditorSupport(DataObject dobj) {
        super(dobj, new Environment(dobj));
    }

    private static class Environment extends DataEditorSupport.Env {

        private FileLock lock;
        
        private boolean modified;
        
        public Environment(DataObject obj) {
            super(obj);
        }
        
        protected FileObject getFile() {
            return getDataObject().getPrimaryFile();
        }

        protected FileLock takeLock() throws IOException {
            MultiDataObject obj = (MultiDataObject) getDataObject();
            if (lock == null || !lock.isValid()) {
                lock = obj.getPrimaryEntry().takeLock();
            }
            return lock;
        }

        public void markModified() throws IOException {
            modified = true;
        }

        public void unmarkModified() {
            modified = false;
            if (lock != null && lock.isValid()) {
                lock.releaseLock();
            }
        }

        public boolean isModified() {
            return modified;
        }
        
    }

    protected boolean notifyModified() {
        if (!super.notifyModified()) {
            return false;
        }
        DataObject dobj = getDataObject();
        if (dobj.getCookie(SaveCookie.class)  == null) {
            ((CookieContainer) dobj).addCookie(this);
        }
        dobj.setModified(false);
        return true;
    }

    public void save() throws IOException {
        saveDocument();
    }
    
    protected static DataObject createDataObject(SessionFactoryNode node, String termination){
        try {
            FileObject storageFile = node.getDescriptor().getStorageFile();
            FileObject folder = storageFile.getParent();
            String name = storageFile.getName() + termination;
            FileObject queryFile = folder.getFileObject(name);
            if (queryFile == null) {
                queryFile = folder.createData(name);
            }
            return DataObject.find(queryFile);
        } catch (DataObjectNotFoundException ex) {
            ErrorManager.getDefault().notify(ErrorManager.EXCEPTION, ex);
            return null; // TODO
        } catch (IOException ex) {
            ErrorManager.getDefault().notify(ErrorManager.EXCEPTION, ex);
            return null; // TODO
        }
    }
    
    
}
