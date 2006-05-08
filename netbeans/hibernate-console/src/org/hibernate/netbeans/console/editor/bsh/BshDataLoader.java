package org.hibernate.netbeans.console.editor.bsh;

import java.io.IOException;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.UniFileLoader;
import org.openide.util.NbBundle;

public class BshDataLoader extends UniFileLoader {
    
    public static final String REQUIRED_MIME = "text/x-hbsh";
    
    private static final long serialVersionUID = 1L;
    
    public BshDataLoader() {
        super("org.hibernate.netbeans.console.editor.bsh.BshDataObject");
    }
    
    protected String defaultDisplayName() {
        return NbBundle.getMessage(BshDataLoader.class, "LBL_Bsh_loader_name");
    }
    
    protected void initialize() {
        super.initialize();
        getExtensions().addMimeType(REQUIRED_MIME);
    }
    
    protected MultiDataObject createMultiObject(FileObject primaryFile) throws DataObjectExistsException, IOException {
        return new BshDataObject(primaryFile, this);
    }
    
    protected String actionsContext() {
        return "Loaders/" + REQUIRED_MIME + "/Actions";
    }
    
}
