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
public class TldConfiguration extends J2EEXMLConfiguration
{
   /**
    *Constructor for the TldConfiguration object
    *
    * @param tools  Description of the Parameter
    */
   public TldConfiguration(XMLTextTools tools)
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
      contributor.addTypeChoiceTag("function-class", "class", null);//$NON-NLS-1$ //$NON-NLS-2$
      contributor.addTypeChoiceTag("listener-class", "class", "javax.servlet.ServletContextListener");//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
      contributor.addTypeChoiceTag("tag-class", "class", "javax.servlet.jsp.tagext.Tag");//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
      contributor.addTypeChoiceTag("tei-class", "class", "javax.servlet.jsp.tagext.TagExtraInfo");//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
      contributor.addTypeChoiceTag("validator-class", "class", "javax.servlet.jsp.tagext.TagLibraryValidator");//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
      contributor.addTypeChoiceTag("variable-class", "class", null);//$NON-NLS-1$ //$NON-NLS-2$
   }
}
