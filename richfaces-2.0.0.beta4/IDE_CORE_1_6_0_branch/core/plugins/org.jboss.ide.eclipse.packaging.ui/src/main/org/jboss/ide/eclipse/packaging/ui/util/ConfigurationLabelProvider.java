/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.ide.eclipse.packaging.ui.util;

import org.eclipse.debug.internal.ui.DefaultLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.jboss.ide.eclipse.packaging.core.model.PackagingArchive;
import org.jboss.ide.eclipse.packaging.core.model.PackagingFile;
import org.jboss.ide.eclipse.packaging.core.model.PackagingFolder;
import org.jboss.ide.eclipse.packaging.ui.IPackagingUIConstants;
import org.jboss.ide.eclipse.packaging.ui.PackagingUIImages;
import org.jboss.ide.eclipse.ui.IUIConstants;
import org.jboss.ide.eclipse.ui.UIImages;

/**
 * @author    letiemble
 * @version   $Revision$
 */
public class ConfigurationLabelProvider extends DefaultLabelProvider
{

   /**Constructor for the ConfigurationLabelProvider object */
   public ConfigurationLabelProvider()
   {
   }

   /**
    * Gets the image attribute of the ConfigurationLabelProvider object
    *
    * @param element  Description of the Parameter
    * @return         The image value
    */
   public Image getImage(Object element)
   {
      if (element instanceof PackagingFolder)
      {
         if (((PackagingFolder) element).isLocal())
         {
            return UIImages.getImage(IUIConstants.IMG_OBJS_FOLDER);
         }
         return PackagingUIImages.getImage(IPackagingUIConstants.IMG_OBJS_EXT_FOLDER);
      }
      if (element instanceof PackagingFile)
      {
         if (((PackagingFile) element).isLocal())
         {
            return UIImages.getImage(IUIConstants.IMG_OBJS_FILE);
         }
         return PackagingUIImages.getImage(IPackagingUIConstants.IMG_OBJS_EXT_FILE);
      }
      if (element instanceof PackagingArchive)
      {
         if (((PackagingArchive) element).isExploded())
         {
            return PackagingUIImages.getImage(IPackagingUIConstants.IMG_OBJS_EXPLODED_JAR);
         }
         return PackagingUIImages.getImage(IPackagingUIConstants.IMG_OBJS_JAR);
      }
      return null;
   }

   /**
    * Gets the text attribute of the ConfigurationLabelProvider object
    *
    * @param element  Description of the Parameter
    * @return         The text value
    */
   public String getText(Object element)
   {
      return element.toString();
   }
}
