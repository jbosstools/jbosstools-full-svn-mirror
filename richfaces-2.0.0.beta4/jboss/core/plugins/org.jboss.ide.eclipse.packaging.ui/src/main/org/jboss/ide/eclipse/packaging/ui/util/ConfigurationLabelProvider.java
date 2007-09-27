/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
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
   public ConfigurationLabelProvider() { }


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
