/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
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
   public ProjectLabelProvider() { }


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
