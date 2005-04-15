/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.j2ee.ui.wizards.web;

import java.util.Arrays;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.jdom.IDOMCompilationUnit;
import org.eclipse.jdt.core.jdom.IDOMType;
import org.eclipse.swt.widgets.Composite;
import org.jboss.ide.eclipse.jdt.j2ee.ui.JDTJ2EEUIMessages;
import org.jboss.ide.eclipse.jdt.j2ee.ui.wizards.J2EENamingUtil;
import org.jboss.ide.eclipse.jdt.ui.wizards.DOMClassWizardPage;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class NewFilterWizardPage extends DOMClassWizardPage
{
   private final static String PAGE_NAME = NewFilterWizardPage.class.getName();


   /**
    *Constructor for the NewFilterWizardPage object
    *
    * @param root  Description of the Parameter
    */
   public NewFilterWizardPage(IWorkspaceRoot root)
   {
      super(true, PAGE_NAME);
      this.setTitle(JDTJ2EEUIMessages.getString("NewFilterWizardPage.title"));//$NON-NLS-1$
      this.setDescription(JDTJ2EEUIMessages.getString("NewFilterWizardPage.description"));//$NON-NLS-1$
   }


   /**
    * Adds a feature to the Content attribute of the NewFilterWizardPage object
    *
    * @param type      The feature to be added to the Content attribute
    * @param compUnit  The feature to be added to the Content attribute
    * @param dType     The feature to be added to the Content attribute
    * @param monitor   The feature to be added to the Content attribute
    */
   protected void addContent(IType type, IDOMCompilationUnit compUnit, IDOMType dType, IProgressMonitor monitor)
   {
      Templates manager = new Templates();
      String filterName = J2EENamingUtil.stripFilterSuffix(type.getElementName());

      String comment = manager.getString("wizards.web.filter.class.comment", new Object[]{filterName});//$NON-NLS-1$
      dType.setComment(comment);
   }


   /**
    * Description of the Method
    *
    * @param composite  Description of the Parameter
    * @param nColumns   Description of the Parameter
    */
   protected void createControls(Composite composite, int nColumns)
   {
      super.createControls(composite, nColumns);
      this.setSuperInterfaces(Arrays.asList(new String[]{"javax.servlet.Filter"}), //$NON-NLS-1$
      true);
   }
}
