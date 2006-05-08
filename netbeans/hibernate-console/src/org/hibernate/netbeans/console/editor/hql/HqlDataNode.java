package org.hibernate.netbeans.console.editor.hql;

import org.openide.loaders.DataNode;
import org.openide.nodes.Children;

public class HqlDataNode extends DataNode {
    
    public HqlDataNode(HqlDataObject obj) {
        super(obj, Children.LEAF);
    }
    
    
}
