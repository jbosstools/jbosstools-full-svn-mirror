package org.hibernate.netbeans.console.editor.bsh;

import org.openide.loaders.DataNode;
import org.openide.nodes.Children;

public class BshDataNode extends DataNode {
    
    public BshDataNode(BshDataObject obj) {
        super(obj, Children.LEAF);
    }
    
}
