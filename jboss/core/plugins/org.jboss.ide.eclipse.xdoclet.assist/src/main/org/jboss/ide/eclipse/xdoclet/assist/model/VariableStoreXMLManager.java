/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.xdoclet.assist.model;

import java.util.Iterator;

import org.jdom.Document;
import org.jdom.Element;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   17 mai 2003
 */
public class VariableStoreXMLManager
{

   /**
    * Sets the variableStoreFromDocument attribute of the VariableStoreXMLManager object
    *
    * @param document  The new variableStoreFromDocument value
    * @return          Description of the Return Value
    */
   public VariableStore setVariableStoreFromDocument(Document document)
   {
      VariableStore variableStore = new VariableStore();

      Element variablesElement = document.getRootElement();
      // Handle variables
      Element variable;

      for (Iterator variables =
            variablesElement.getChildren(IDocletConstants.VARIABLE_TAG).iterator();
            variables.hasNext();
            )
      {
         variable = (Element) variables.next();

         variableStore.addVariable(
               new Variable(
               variable.getAttribute(IDocletConstants.NAME_ATTRIBUTE).getValue(),
               variable.getAttribute(IDocletConstants.PATTERN_ATTRIBUTE).getValue(),
               variable.getAttribute(IDocletConstants.SYSTEM_VARIABLE_ATTRIBUTE)
               .getValue()));
      }
      return variableStore;
   }
}
