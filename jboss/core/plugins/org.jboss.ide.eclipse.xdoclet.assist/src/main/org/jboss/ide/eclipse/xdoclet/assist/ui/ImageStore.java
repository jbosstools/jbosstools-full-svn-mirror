/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.xdoclet.assist.ui;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.jboss.ide.eclipse.xdoclet.assist.model.DocletElement;
import org.jboss.ide.eclipse.xdoclet.assist.model.DocletType;
import org.jboss.ide.eclipse.xdoclet.assist.model.TemplateElement;
import org.jboss.ide.eclipse.xdoclet.assist.model.TemplateTree;
import org.jboss.ide.eclipse.xdoclet.ui.IXDocletUIConstants;
import org.jboss.ide.eclipse.xdoclet.ui.XDocletUIImages;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   17 mai 2003
 */
public class ImageStore
{
   /**
    * Gets the image attribute of the XDocletImageStore object
    *
    * @param o  Description of the Parameter
    * @return   The image value
    */
   public static Image getImage(Object o)
   {
      if (o instanceof TemplateElement)
      {
         o = ((TemplateElement) o).getDocletElement();
      }
      if (o instanceof TemplateTree)
      {
         return XDocletUIImages.getImage(IXDocletUIConstants.IMG_OBJS_TEMPLATE);
      }
      else if (o instanceof DocletElement)
      {
         DocletElement docletElement = (DocletElement) o;
         if (docletElement.getType() == DocletType.NAMESPACE)
         {
            return XDocletUIImages.getImage(IXDocletUIConstants.IMG_OBJS_NAMESPACE);
         }
         else if (docletElement.getType() == DocletType.COMMAND)
         {
            return XDocletUIImages.getImage(IXDocletUIConstants.IMG_OBJS_COMMAND);
         }
         else if (docletElement.getType() == DocletType.NON_DISCRETE_ATTRIBUTE)
         {
            return XDocletUIImages.getImage(IXDocletUIConstants.IMG_OBJS_NON_DISCRETE_ATTRIBUTE);
         }
         else if (docletElement.getType() == DocletType.DISCRETE_ATTRIBUTE)
         {
            return XDocletUIImages.getImage(IXDocletUIConstants.IMG_OBJS_DISCRETE_ATTRIBUTE);
         }
         else if (docletElement.getType() == DocletType.VALUE_WITH_VARIABLE)
         {
            return XDocletUIImages.getImage(IXDocletUIConstants.IMG_OBJS_VALUE_VARIABLE);
         }
         else if (docletElement.getType() == DocletType.VALUE_WITHOUT_VARIABLE)
         {
            return XDocletUIImages.getImage(IXDocletUIConstants.IMG_OBJS_VALUE);
         }
      }
//		else if (o instanceof ContextTree)
//			return getImage(IMAGE_KEY_CONTEXT_GROUP);
//		else if (o instanceof ContextElement)
//			return getImage(IMAGE_KEY_CONTEXT_ELEMENT);

      return ImageDescriptor.getMissingImageDescriptor().createImage();
   }
}
