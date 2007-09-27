/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.xml.ui.outline;

import java.util.List;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.jboss.ide.eclipse.jdt.ui.IJDTUIConstants;
import org.jboss.ide.eclipse.jdt.ui.JDTUIImages;
import org.jboss.ide.eclipse.jdt.xml.ui.JDTXMLUIMessages;
import org.jboss.ide.eclipse.jdt.xml.ui.reconciler.XMLNode;
import org.jboss.ide.eclipse.jdt.xml.ui.text.scanners.XMLPartitionScanner;

/*
 * This file contains materials derived from the
 * XMen project. License can be found at :
 * http://www.eclipse.org/legal/cpl-v10.html
 */
/**
 * A label provider for XML elements in the XML outline View.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class XMLLabelProvider extends LabelProvider
{
   private final static int TEXT_MAX_LENGTH = 15;


   /**
    * Returns an image for the specified <code>object</code>.  If
    * the object is an <code>XMLNode</code> then the image is
    * loaded from the cache.
    *
    * @param object  Description of the Parameter
    * @return        The image value
    * @see           ILabelProvider#getImage(Object)
    */
   public Image getImage(Object object)
   {
      if (object instanceof XMLNode)
      {
         return this.selectImage((XMLNode) object);
      }
      return super.getImage(object);
   }


   /**
    * Gets the text attribute of the XMLLabelProvider object
    *
    * @param object  Description of the Parameter
    * @return        The text value
    */
   public String getText(Object object)
   {
      if (object instanceof XMLNode)
      {
         return this.selectLabelText((XMLNode) object);
      }
      return super.getText(object);
   }


   /**
    * Description of the Method
    *
    * @param element  Description of the Parameter
    * @return         Description of the Return Value
    */
   private Image selectImage(XMLNode element)
   {
      String type = element.getType();

      if (type.equals(XMLPartitionScanner.XML_TAG))
      {
         return JDTUIImages.getImage(IJDTUIConstants.IMG_OBJ_TAG);
      }

      // Test to see if the element is an empty tag
      if (type.equals(XMLPartitionScanner.XML_EMPTY_TAG))
      {
         return JDTUIImages.getImage(IJDTUIConstants.IMG_OBJ_EMPTYTAG);
      }

      // Test whether the element is an attribute
      if (type.equals(XMLPartitionScanner.XML_ATTRIBUTE))
      {
         return JDTUIImages.getImage(IJDTUIConstants.IMG_OBJ_ATTRIBUTE);
      }

      // Test if the element is a processing instruction <?.....
      if (type.equals(XMLPartitionScanner.XML_PI))
      {
         return JDTUIImages.getImage(IJDTUIConstants.IMG_OBJ_PI);
      }

      // Test if the element is an XML declaration <!....
      if (type.equals(XMLPartitionScanner.XML_DECL) || type.equals(XMLPartitionScanner.XML_START_DECL))
      {
         return JDTUIImages.getImage(IJDTUIConstants.IMG_OBJ_PI);
      }

      // Test if the element is a comment <!--
      if (type.equals(XMLPartitionScanner.XML_COMMENT))
      {
         return JDTUIImages.getImage(IJDTUIConstants.IMG_OBJ_COMMENT);
      }

      return null;
   }


   /**
    * Returns the string to display in the label for the XMLElement.  This is used
    * to provide a somewhat friendlier string than would otherwise be provided by
    * <code>XMLNode.getLabel()</code>.
    *
    * @param element  Description of the Parameter
    * @return         Description of the Return Value
    */
   private String selectLabelText(XMLNode element)
   {
      String type = element.getType();

      if (type.equals(XMLPartitionScanner.XML_ATTRIBUTE))
      {
         return element.getName() + "=\"" + element.getValue() + "\"";//$NON-NLS-1$
      }

      if (type.equals(XMLPartitionScanner.XML_PI))
      {
         if (element.getName().startsWith(XMLPartitionScanner.XML_DECL))
         {
            return JDTXMLUIMessages.getString("XMLLabelProvider.xml.file");//$NON-NLS-1$
         }
         return element.getName().substring(1);
      }
      if (type.equals(XMLPartitionScanner.XML_DECL) || type.equals(XMLPartitionScanner.XML_COMMENT))
      {
         return element.getName().substring(1);
      }

      if (type.equals(XMLPartitionScanner.XML_TEXT))
      {
         if (element.getContent().length() < TEXT_MAX_LENGTH)
         {
            return element.getContent();
         }
         return element.getContent().substring(0, TEXT_MAX_LENGTH) + "...";//$NON-NLS-1$
      }

      if(element.getType().equals(XMLPartitionScanner.XML_TAG)) {
      	// TODO: make this a user choice
      	List attributes = element.getAttributes();
      	if(attributes.size()>0) {
      		return element.getName() + " " + selectLabelText((XMLNode) attributes.get(0));	
      	}
      }

      // We could also test to see whether the element has a qualified name
      // and return a different image if it did.  Could use label decorators
      // instead. See comment above.
      return element.getName();
   }
}
