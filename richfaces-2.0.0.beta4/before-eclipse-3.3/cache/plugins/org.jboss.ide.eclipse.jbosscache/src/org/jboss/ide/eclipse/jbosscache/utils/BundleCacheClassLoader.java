/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.utils;

import java.net.URL;
import java.net.URLClassLoader;

public class BundleCacheClassLoader extends URLClassLoader
{
   private ClassLoader wrapperBundleLoader;
   private ClassLoader fContextClassloader= null;
   
    public BundleCacheClassLoader(URL[] urls, ClassLoader pluginLoaders) {
        super(urls, ClassLoader.getSystemClassLoader());
        this.wrapperBundleLoader = pluginLoaders;
    }
    
    protected Class findClass(String name) throws ClassNotFoundException {
        Class result = null;
            
        result= loadClassPlugins(name); 
        
        if (result == null) {
            result = loadClassURLs(name);
        }
        if (result == null) {
            throw new ClassNotFoundException(name);
        }
        return result;
    }

    protected Class loadClassURLs(String name) {
        try {
            return super.findClass(name);
        } catch (ClassNotFoundException e) {
            // Ignore exception now. If necessary we'll throw
            // a ClassNotFoundException in findClass(String)
        }
        return null;
    }

    protected Class loadClassPlugins(String name) {
        //remove this classloader as the context classloader
        //when loading classes from plugins...see bug 94471
       if (fContextClassloader != null) {
           Thread.currentThread().setContextClassLoader(fContextClassloader);
       }
       try {
        Class result = null;
                try {
                    result = wrapperBundleLoader.loadClass(name);
                } catch (ClassNotFoundException e) {
                    // Ignore exception now. If necessary we'll throw
                    // a ClassNotFoundException in loadClass(String)
                }

        return result;
       } finally {
           Thread.currentThread().setContextClassLoader(this);
       }
    }
    
   
   public void setPluginContextClassloader(ClassLoader classLoader) {
       fContextClassloader= classLoader;
   }
}