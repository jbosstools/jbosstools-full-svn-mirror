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
public class ConnectorXmlConfiguration extends J2EEXMLConfiguration
{
   /**
    *Constructor for the ConnectorXmlConfiguration object
    *
    * @param tools  Description of the Parameter
    */
   public ConnectorXmlConfiguration(XMLTextTools tools)
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
      contributor.addTypeChoiceTag("connection-impl-class", "class", null);//$NON-NLS-1$ //$NON-NLS-2$
      contributor.addTypeChoiceTag("connection-interface", "class", null);//$NON-NLS-1$ //$NON-NLS-2$
      contributor.addTypeChoiceTag("connectionfactory-interface", "class", null);//$NON-NLS-1$ //$NON-NLS-2$
      contributor.addTypeChoiceTag("connectionfactory-impl-class", "class", null);//$NON-NLS-1$ //$NON-NLS-2$
      contributor.addTypeChoiceTag("managedconnectionfactory-class", "class", "javax.resource.spi.ManagedConnectionFactory");//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
      contributor.addTypeChoiceTag("credential-interface", "interface", null);//$NON-NLS-1$ //$NON-NLS-2$
      contributor.addTypeChoiceTag("activationspec-class", "class", "javax.resource.spi.ActivationSpec");//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
      contributor.addTypeChoiceTag("adminobject-interface", "interface", null);//$NON-NLS-1$ //$NON-NLS-2$
      contributor.addTypeChoiceTag("adminobject-class", "class", null);//$NON-NLS-1$ //$NON-NLS-2$
      contributor.addTypeChoiceTag("messagelistener-type", "interface", null);//$NON-NLS-1$ //$NON-NLS-2$
      contributor.addTypeChoiceTag("resourceadapter-class", "class", "javax.resource.spi.ResourceAdapter");//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
   }
}
