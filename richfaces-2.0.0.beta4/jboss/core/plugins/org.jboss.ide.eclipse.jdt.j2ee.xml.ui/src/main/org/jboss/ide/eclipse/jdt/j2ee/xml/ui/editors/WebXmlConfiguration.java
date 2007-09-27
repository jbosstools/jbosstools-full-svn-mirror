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
public class WebXmlConfiguration extends J2EEXMLConfiguration
{
   /**
    *Constructor for the WebXmlConfiguration object
    *
    * @param tools  Description of the Parameter
    */
   public WebXmlConfiguration(XMLTextTools tools)
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
      contributor.addTypeChoiceTag("env-entry-type", "class", null);//$NON-NLS-1$ //$NON-NLS-2$
      contributor.addTypeChoiceTag("exception-type", "class", "java.lang.Exception");//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
      contributor.addTypeChoiceTag("filter-class", "class", "javax.servlet.Filter");//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
      contributor.addTypeChoiceTag("listener-class", "class", "javax.servlet.ServletContextListener");//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
      contributor.addTypeChoiceTag("resource-env-ref-type", "all", null);//$NON-NLS-1$ //$NON-NLS-2$
      contributor.addTypeChoiceTag("res-type", "all", null);//$NON-NLS-1$ //$NON-NLS-2$
      contributor.addTypeChoiceTag("servlet-class", "class", "javax.servlet.Servlet");//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
   }
}
