package org.hibernate.netbeans.console.editor.hql;

import java.io.IOException;
import javax.swing.JEditorPane;
import javax.swing.text.StyledDocument;
import org.hibernate.netbeans.console.editor.CookieContainer;
import org.openide.cookies.EditorCookie;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.nodes.CookieSet;
import org.openide.nodes.Node;
import org.openide.nodes.Node.Cookie;
import org.openide.text.DataEditorSupport;
import org.openide.text.Line;
import org.openide.util.Task;

public class HqlDataObject extends MultiDataObject implements CookieContainer {
    
    public HqlDataObject(FileObject pf, HqlDataLoader loader) throws DataObjectExistsException, IOException {
        super(pf, loader);
        CookieSet cookies = getCookieSet();
        cookies.add((Node.Cookie) DataEditorSupport.create(this, getPrimaryEntry(), cookies));
    }
    
    protected Node createNodeDelegate() {
        return new HqlDataNode(this);
    }
    
    public void addCookie(Cookie k) {
        getCookieSet().add(k);
    }
    
}
