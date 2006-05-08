package org.hibernate.netbeans.console.editor.hql;

import org.openide.cookies.EditorCookie;
import org.openide.nodes.Node;

/**
 * @author leon
 */
public interface HqlEditorCookie extends Node.Cookie {
    
    void convertHql2Java();

    void convertJava2Hql();

}
