/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.xdoclet.assist.completion;

import java.util.ArrayList;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   17 mai 2003
 * @todo      Javadoc to complete
 */
public class DocletStructure
{
   /** Description of the Field */
   public ArrayList attributes = new ArrayList();
   /** Description of the Field */
   public String command;
   /** Description of the Field */
   public boolean lastElementIsAttribute;
   /** Description of the Field */
   public String namespace;
   /** Description of the Field */
   public String wordLeftOfCursor;


   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public String toString()
   {
      return "NS: " + namespace //$NON-NLS-1$
      + " COMMAND: " //$NON-NLS-1$
      + command
            + " ATTR_SIZE: " //$NON-NLS-1$
      + attributes.size()
            + " WORD_LEFT_OF_CURSOR: " //$NON-NLS-1$
      + wordLeftOfCursor
            + " LAST_ELEMENT_ATTR: " //$NON-NLS-1$
      + lastElementIsAttribute;
   }
}
