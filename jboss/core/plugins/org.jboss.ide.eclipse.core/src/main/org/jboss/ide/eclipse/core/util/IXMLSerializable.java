/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.core.util;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public interface IXMLSerializable
{
   /**
    * Render a XML tree
    *
    * @param doc   Description of the Parameter
    * @param node  Description of the Parameter
    */
   public void writeToXml(Document doc, Node node);


   /**
    * Read from a XML tree
    *
    * @param node  Description of the Parameter
    */
   public void readFromXml(Node node);


   /**
    * Read from a XML tree recursively
    *
    * @param node       Description of the Parameter
    * @param recursive  Description of the Parameter
    */
   public void readFromXml(Node node, boolean recursive);
}
