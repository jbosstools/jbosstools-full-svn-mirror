/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.deployer.ui.util;

import org.eclipse.debug.internal.ui.DefaultLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.jboss.ide.eclipse.deployer.core.target.ITarget;
import org.jboss.ide.eclipse.deployer.ui.TargetUIAdapter;

/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class TargetLabelProvider extends DefaultLabelProvider
{
   /**Constructor for the ConfigurationLabelProvider object */
   public TargetLabelProvider() { }


   /**
    * Gets the image attribute of the ConfigurationLabelProvider object
    *
    * @param element  Description of the Parameter
    * @return         The image value
    */
   public Image getImage(Object element)
   {
      if (element instanceof ITarget)
      {
         return TargetUIAdapter.getInstance().getImage((ITarget) element);
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
