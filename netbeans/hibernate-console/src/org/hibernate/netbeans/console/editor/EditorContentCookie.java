package org.hibernate.netbeans.console.editor;

import org.openide.nodes.Node;

/**
 * @author leon
 */
public interface EditorContentCookie extends Node.Cookie {
    
    String getActiveContent();
    
    void doRunWithContent(String content);
    
}
