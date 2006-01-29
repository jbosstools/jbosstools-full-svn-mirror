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
package org.jboss.ide.eclipse.ui.util;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.debug.internal.ui.DefaultLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.jboss.ide.eclipse.ui.IUIConstants;
import org.jboss.ide.eclipse.ui.UIImages;

/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class ProjectLabelProvider extends DefaultLabelProvider
{

   /**Constructor for the ConfigurationLabelProvider object */
   public ProjectLabelProvider()
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
      if (element instanceof IProject)
      {
         return UIImages.getImage(IUIConstants.IMG_OBJS_JAVA_MODEL);
      }
      if (element instanceof IFolder)
      {
         return UIImages.getImage(IUIConstants.IMG_OBJS_FOLDER);
      }
      return UIImages.getImage(IUIConstants.IMG_OBJS_FILE);
   }

   /**
    * Gets the text attribute of the ConfigurationLabelProvider object
    *
    * @param element  Description of the Parameter
    * @return         The text value
    */
   public String getText(Object element)
   {
      if (element instanceof IResource)
      {
         return ((IResource) element).getName();
      }
      return element.toString();
   }
}
