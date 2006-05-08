package org.hibernate.netbeans.console.editor.bsh;

import java.io.IOException;
import org.hibernate.netbeans.console.editor.CookieContainer;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.nodes.CookieSet;
import org.openide.nodes.Node;
import org.openide.text.DataEditorSupport;

public class BshDataObject extends MultiDataObject implements CookieContainer {
    
    public BshDataObject(FileObject pf, BshDataLoader loader) throws DataObjectExistsException, IOException {
        super(pf, loader);
        CookieSet cookies = getCookieSet();
        cookies.add((Node.Cookie) DataEditorSupport.create(this, getPrimaryEntry(), cookies));
    }
    
    protected Node createNodeDelegate() {
        return new BshDataNode(this);
    }

    public void addCookie(Node.Cookie k) {
        getCookieSet().add(k);
    }
    
}
