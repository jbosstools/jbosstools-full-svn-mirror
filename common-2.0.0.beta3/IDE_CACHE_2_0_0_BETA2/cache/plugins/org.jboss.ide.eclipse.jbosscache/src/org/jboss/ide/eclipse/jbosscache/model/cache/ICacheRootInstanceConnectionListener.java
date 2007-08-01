/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.model.cache;

/**
 * Listener interface to register root cache instance connect/disconnect events
 * @author Gurkaner
 */
public interface ICacheRootInstanceConnectionListener
{

   /**
    * When this root instance connect, call all register members
    * @param rootInstance
    */
   void cacheRootInstanceConnected(ICacheRootInstance rootInstance);

   /**
    * When this root instance disconnect, call all register members
    * @param rootInstance
    */
   void cacheRootInstanceDisConnected(ICacheRootInstance rootInstance);
}
