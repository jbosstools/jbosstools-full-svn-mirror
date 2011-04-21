/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.xdoclet.assist.model;

import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.jdom.Document;
import org.jdom.Element;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   17 mai 2003
 */
public class AdditionalValuesXMLManager
{

   /**
    * Adds a feature to the DocumentToDocletTree attribute of the AdditionalValuesXMLManager object
    *
    * @param docletTree  The feature to be added to the DocumentToDocletTree attribute
    * @param document    The feature to be added to the DocumentToDocletTree attribute
    */
   public void addDocumentToDocletTree(
         DocletTree docletTree,
         Document document)
   {

      Element rootElement = document.getRootElement();
      List valueElements = rootElement.getChildren(IDocletConstants.VALUE_TAG);

      DocletElement parentDocletElement;
      DocletElement additonalValueDocletElement;
      for (Iterator valueIterator = valueElements.iterator();
            valueIterator.hasNext();
            )
      {
         Element valueElement = (Element) valueIterator.next();
         Element pathElement = valueElement.getChild(IDocletConstants.PATH_TAG);
         StringTokenizer pathTokenizer =
               new StringTokenizer(
               pathElement.getAttributeValue(IDocletConstants.NAME_ATTRIBUTE),
               ".");//$NON-NLS-1$
         String[] path = new String[pathTokenizer.countTokens()];
         for (int i = 0; pathTokenizer.hasMoreTokens(); i++)
         {
            path[i] = pathTokenizer.nextToken();
         }
         if ((parentDocletElement = docletTree.getNode(path)) != null)
         {
            additonalValueDocletElement =
                  parentDocletElement.addChild(
                  valueElement.getAttributeValue(IDocletConstants.NAME_ATTRIBUTE));
            additonalValueDocletElement.getNode().getAdditionalAttributes().put(
                  IDocletConstants.ATTR_ADDITIONAL_VALUE,
                  new Marker());
            if (valueElement.getChild(IDocletConstants.PARSING_TAG) != null)
            {
               additonalValueDocletElement.getNode().getAdditionalAttributes().put(
                     IDocletConstants.ATTR_PARSING,
                     new Marker());
            }
         }
      }
   }

   //	public void removeAdditonalValuesFromDocletTree(DocletTree docletTree) {
   //	}

}
