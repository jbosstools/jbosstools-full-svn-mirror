package org.hibernate.netbeans.console;

import org.openide.cookies.CloseCookie;
import org.openide.cookies.OpenCookie;
import org.openide.nodes.Node;

/**
 * @author leon
 */
public interface SessionFactoryCookie extends Node.Cookie, OpenCookie, CloseCookie {
    
    public void delete();

    public void copy();
    
    public void reload();
    
    public boolean configure();

    public boolean isSessionOpen();

    public void updateSchema();
    
}
