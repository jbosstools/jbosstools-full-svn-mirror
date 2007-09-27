/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.j2ee.xml.ui.editors;

import org.jboss.ide.eclipse.jdt.xml.ui.editors.XMLConfiguration;
import org.jboss.ide.eclipse.jdt.xml.ui.editors.XMLEditor;
import org.jboss.ide.eclipse.jdt.xml.ui.editors.XMLTextTools;


/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class EjbXmlEditor extends XMLEditor
{
   /**Constructor for the EjbXmlEditor object */
   public EjbXmlEditor() { }


   /**
    * Gets the xMLConfiguration attribute of the EjbXmlEditor object
    *
    * @param xmlTextTools  Description of the Parameter
    * @return              The xMLConfiguration value
    */
   protected XMLConfiguration getXMLConfiguration(XMLTextTools xmlTextTools)
   {
      return new EjbXmlConfiguration(xmlTextTools);
   }
}
