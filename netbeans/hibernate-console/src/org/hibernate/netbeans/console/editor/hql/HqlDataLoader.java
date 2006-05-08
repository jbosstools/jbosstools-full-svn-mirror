package org.hibernate.netbeans.console.editor.hql;

import java.io.IOException;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.UniFileLoader;
import org.openide.util.NbBundle;

public class HqlDataLoader extends UniFileLoader {
    
    public static final String REQUIRED_MIME = "text/x-hql";
    
    private static final long serialVersionUID = 1L;
    
    public HqlDataLoader() {
        super("org.hibernate.netbeans.console.HqlDataObject");
    }
    
    protected String defaultDisplayName() {
        return NbBundle.getMessage(HqlDataLoader.class, "LBL_Hql_loader_name");
    }
    
    protected void initialize() {
        super.initialize();
        getExtensions().addMimeType(REQUIRED_MIME);
    }
    
    protected MultiDataObject createMultiObject(FileObject primaryFile) throws DataObjectExistsException, IOException {
        return new HqlDataObject(primaryFile, this);
    }
    
    protected String actionsContext() {
        return "Loaders/" + REQUIRED_MIME + "/Actions";
    }
    
}
