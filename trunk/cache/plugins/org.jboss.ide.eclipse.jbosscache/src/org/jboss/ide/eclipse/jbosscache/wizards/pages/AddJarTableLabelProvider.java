/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.wizards.pages;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.jboss.ide.eclipse.jbosscache.ICacheConstants;
import org.jboss.ide.eclipse.jbosscache.JBossCachePlugin;

/**
 * This class represents cache related jar label provider
 * @author Gurkaner
 */
public class AddJarTableLabelProvider implements ITableLabelProvider
{

   public Image getColumnImage(Object element, int columnIndex)
   {
      return JBossCachePlugin.getDefault().getImageRegistry().get(ICacheConstants.IMAGE_KEY_EXTERNAL_JAR);
   }

   public String getColumnText(Object element, int columnIndex)
   {
      return element.toString();
   }

   public void addListener(ILabelProviderListener listener)
   {
      // TODO Auto-generated method stub

   }

   public void dispose()
   {
      // TODO Auto-generated method stub

   }

   public boolean isLabelProperty(Object element, String property)
   {
      // TODO Auto-generated method stub
      return false;
   }

   public void removeListener(ILabelProviderListener listener)
   {
      // TODO Auto-generated method stub

   }

}
