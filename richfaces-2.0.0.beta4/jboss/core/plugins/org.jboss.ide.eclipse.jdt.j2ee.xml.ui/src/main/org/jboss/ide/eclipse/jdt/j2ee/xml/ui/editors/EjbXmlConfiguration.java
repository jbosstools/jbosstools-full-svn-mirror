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
public class EjbXmlConfiguration extends J2EEXMLConfiguration
{
   /**
    *Constructor for the EjbXmlConfiguration object
    *
    * @param tools  Description of the Parameter
    */
   public EjbXmlConfiguration(XMLTextTools tools)
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
      contributor.addTypeChoiceTag("cmr-field-type", "all", null);//$NON-NLS-1$ //$NON-NLS-2$
      contributor.addTypeChoiceTag("ejb-class", "class", "javax.ejb.EnterpriseBean");//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
      contributor.addTypeChoiceTag("home", "interface", "javax.ejb.EJBHome");//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
      contributor.addTypeChoiceTag("remote", "interface", "javax.ejb.EJBObject");//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
      contributor.addTypeChoiceTag("local-home", "interface", "javax.ejb.EJBLocalHome");//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
      contributor.addTypeChoiceTag("local", "interface", "javax.ejb.EJBLocalObject");//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
      contributor.addTypeChoiceTag("method-param", "class", null);//$NON-NLS-1$ //$NON-NLS-2$
      contributor.addTypeChoiceTag("prim-key-class", "class", null);//$NON-NLS-1$ //$NON-NLS-2$
      contributor.addTypeChoiceTag("resource-env-ref-type", "all", null);//$NON-NLS-1$ //$NON-NLS-2$
      contributor.addTypeChoiceTag("res-type", "all", null);//$NON-NLS-1$ //$NON-NLS-2$
   }
}
