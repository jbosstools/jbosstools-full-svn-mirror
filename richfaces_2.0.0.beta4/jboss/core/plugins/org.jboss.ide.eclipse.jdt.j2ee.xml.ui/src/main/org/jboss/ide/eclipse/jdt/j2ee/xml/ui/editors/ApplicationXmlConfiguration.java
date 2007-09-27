/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.j2ee.xml.ui.editors;

import org.jboss.ide.eclipse.jdt.xml.ui.assist.contributor.TypeChoiceContributor;
import org.jboss.ide.eclipse.jdt.xml.ui.editors.XMLTextTools;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class ApplicationXmlConfiguration extends J2EEXMLConfiguration
{
   /**
    *Constructor for the ApplicationXmlConfiguration object
    *
    * @param tools  Description of the Parameter
    */
   public ApplicationXmlConfiguration(XMLTextTools tools)
   {
      super(tools);
   }


   /**
    * Description of the Method
    *
    * @param contributor  Description of the Parameter
    */
   protected void populateTypeChoiceContributor(TypeChoiceContributor contributor)
   {
   }
}
