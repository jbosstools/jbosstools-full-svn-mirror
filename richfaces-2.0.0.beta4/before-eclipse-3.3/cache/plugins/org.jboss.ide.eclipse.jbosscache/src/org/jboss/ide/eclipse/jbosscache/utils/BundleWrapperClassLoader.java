/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.utils;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

import org.osgi.framework.Bundle;

public class BundleWrapperClassLoader extends ClassLoader
{
    private Bundle bundle;
    public BundleWrapperClassLoader(Bundle bundle) {
        super();
        this.bundle = bundle;
    }
    /* (non-Javadoc)
     * @see java.lang.ClassLoader#findClass(java.lang.String)
     */
    public Class findClass(String name) throws ClassNotFoundException {
        return bundle.loadClass(name);
    }
    /* (non-Javadoc)
     * @see java.lang.ClassLoader#findResource(java.lang.String)
     */
    public URL findResource(String name) {
        return bundle.getResource(name);
    }
    
    /* (non-Javadoc)
     * @see java.lang.ClassLoader#findResources(java.lang.String)
     */
    protected Enumeration findResources(String name) throws IOException {
        return bundle.getResources(name);
    }
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof BundleWrapperClassLoader)) {
            return false;
        }
        return bundle == ((BundleWrapperClassLoader) obj).bundle;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return bundle.hashCode();
    }
    public String toString() {
        return "BundleWrapperClassLoader(" + bundle.toString() + ")";  //$NON-NLS-1$ //$NON-NLS-2$
    }   

}
