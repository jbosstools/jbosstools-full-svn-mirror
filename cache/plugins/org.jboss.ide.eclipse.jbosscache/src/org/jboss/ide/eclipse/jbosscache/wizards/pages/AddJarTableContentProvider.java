/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.wizards.pages;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.jboss.ide.eclipse.jbosscache.model.config.CacheConfigParams;

/**
 * This class represents content provider for cache related jars
 * @author Gurkaner
 */
public class AddJarTableContentProvider implements IStructuredContentProvider
{

   private static Object[] EMPTY = new Object[0];

   public Object[] getElements(Object inputElement)
   {
      if (inputElement instanceof CacheConfigParams)
      {
         CacheConfigParams params = (CacheConfigParams) inputElement;
         return params.getConfJarUrls().toArray();
      }
      return EMPTY;
   }

   public void dispose()
   {
   }

   public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
   {
      // TODO Auto-generated method stub

   }

}
