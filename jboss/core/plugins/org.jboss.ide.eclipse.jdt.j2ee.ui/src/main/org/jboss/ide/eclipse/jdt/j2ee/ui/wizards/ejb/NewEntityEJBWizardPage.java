/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.j2ee.ui.wizards.ejb;

import java.util.Arrays;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.jdom.IDOMCompilationUnit;
import org.eclipse.jdt.core.jdom.IDOMType;
import org.eclipse.jdt.internal.ui.wizards.NewWizardMessages;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.SelectionButtonDialogFieldGroup;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.jboss.ide.eclipse.jdt.j2ee.ui.JDTJ2EEUIMessages;
import org.jboss.ide.eclipse.jdt.j2ee.ui.wizards.J2EENamingUtil;
import org.jboss.ide.eclipse.jdt.ui.wizards.DOMClassWizardPage;
import org.jboss.ide.eclipse.jdt.ui.wizards.util.FieldsUtil;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class NewEntityEJBWizardPage extends DOMClassWizardPage
{
   /** Description of the Field */
   protected SelectionButtonDialogFieldGroup accessButtons;
   /** Description of the Field */
   protected SelectionButtonDialogFieldGroup typeButtons;

   private final static String PAGE_NAME = NewEntityEJBWizardPage.class.getName();


   /**
    *Constructor for the NewEntityEJBWizardPage object
    *
    * @param root  Description of the Parameter
    */
   public NewEntityEJBWizardPage(IWorkspaceRoot root)
   {
      super(true, PAGE_NAME);
      this.setTitle(JDTJ2EEUIMessages.getString("NewEntityEJBWizardPage.title"));//$NON-NLS-1$
      this.setDescription(JDTJ2EEUIMessages.getString("NewEntityEJBWizardPage.description"));//$NON-NLS-1$
   }


   /**
    * Adds a feature to the Content attribute of the NewEntityEJBWizardPage object
    *
    * @param type      The feature to be added to the Content attribute
    * @param compUnit  The feature to be added to the Content attribute
    * @param dType     The feature to be added to the Content attribute
    * @param monitor   The feature to be added to the Content attribute
    */
   protected void addContent(IType type, IDOMCompilationUnit compUnit, IDOMType dType, IProgressMonitor monitor)
   {
      Templates manager = new Templates();
      String beanName = J2EENamingUtil.stripEJBSuffix(type.getElementName());

      if (isCMP11())
      {
         // Add the custom comment on top of the Servlet
         String comment = manager.getString("wizards.ejb.entity.cmp11.class.comment", new Object[]{beanName, getAccessString()});//$NON-NLS-1$
         dType.setComment(comment);
      }
      else if (isCMP2x())
      {
         // Add the custom comment on top of the Servlet
         String comment = manager.getString("wizards.ejb.entity.cmp2x.class.comment", new Object[]{beanName, getAccessString()});//$NON-NLS-1$
         dType.setComment(comment);

         // Make sure type is abstract
         dType.setFlags(dType.getFlags() | Flags.AccAbstract);
      }
      else
      {
         // Add the custom comment on top of the Servlet
         String comment = manager.getString("wizards.ejb.entity.bmp.class.comment", new Object[]{beanName, getAccessString()});//$NON-NLS-1$
         dType.setComment(comment);
      }
   }


   /**
    * Description of the Method
    *
    * @param composite  Description of the Parameter
    * @param nColumns   Description of the Parameter
    */
   protected void createAccessSelectionControls(Composite composite, int nColumns)
   {
      FieldsUtil.createSelectionButtonDialogFieldGroupControls(this.accessButtons, composite, nColumns);
   }


   /** Description of the Method */
   protected void createContent()
   {
      super.createContent();

      String[] buttonNames;

      // Constructors and inherited must be left as first elements
      buttonNames = new String[]{JDTJ2EEUIMessages.getString("NewEntityEJBWizardPage.Remote_6"), JDTJ2EEUIMessages.getString("NewEntityEJBWizardPage.Local_7"), JDTJ2EEUIMessages.getString("NewEntityEJBWizardPage.Both_8")};//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

      accessButtons = new SelectionButtonDialogFieldGroup(SWT.RADIO, buttonNames, 3);
      accessButtons.setLabelText(JDTJ2EEUIMessages.getString("NewEntityEJBWizardPage.Select_the_access_of_the_EJB_9"));//$NON-NLS-1$

      // Constructors and inherited must be left as first elements
      buttonNames = new String[]{NewWizardMessages.getString("NewClassWizardPage.methods.constructors"), //$NON-NLS-1$
      NewWizardMessages.getString("NewClassWizardPage.methods.inherited"),//$NON-NLS-1$
      };

      fMethodStubsButtons = new SelectionButtonDialogFieldGroup(SWT.CHECK, buttonNames, 2);
      fMethodStubsButtons.setLabelText(NewWizardMessages.getString("NewClassWizardPage.methods.label"));//$NON-NLS-1$

      // Constructors and inherited must be left as first elements
      buttonNames = new String[]{JDTJ2EEUIMessages.getString("NewEntityEJBWizardPage.CMP_1.1_10"), JDTJ2EEUIMessages.getString("NewEntityEJBWizardPage.CMP_2.x_11"), JDTJ2EEUIMessages.getString("NewEntityEJBWizardPage.BMP_12")};//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

      typeButtons = new SelectionButtonDialogFieldGroup(SWT.RADIO, buttonNames, 3);
      typeButtons.setDialogFieldListener(this.getFieldsAdapter());
      typeButtons.setLabelText(JDTJ2EEUIMessages.getString("NewEntityEJBWizardPage.Select_the_type_of_the_EJB_13"));//$NON-NLS-1$
   }


   /**
    * Description of the Method
    *
    * @param composite  Description of the Parameter
    * @param nColumns   Description of the Parameter
    */
   protected void createControls(Composite composite, int nColumns)
   {
      this.createContainerControls(composite, nColumns);
      this.createPackageControls(composite, nColumns);
      this.createSeparator(composite, nColumns);
      this.createTypeNameControls(composite, nColumns);
      this.createSuperClassControls(composite, nColumns);
      this.createSuperInterfacesControls(composite, nColumns);

      this.createSeparator(composite, nColumns);
      this.createTypeSelectionControls(composite, nColumns);
      this.createSeparator(composite, nColumns);
      this.createAccessSelectionControls(composite, nColumns);

      this.createSeparator(composite, nColumns);
      this.createMethodStubSelectionControls(composite, nColumns);

      this.setSuperInterfaces(Arrays.asList(new String[]{"javax.ejb.EntityBean"}), //$NON-NLS-1$
      true);//$NON-NLS-1$
   }


   /**
    * Description of the Method
    *
    * @param composite  Description of the Parameter
    * @param nColumns   Description of the Parameter
    */
   protected void createMethodStubSelectionControls(Composite composite, int nColumns)
   {
      FieldsUtil.createSelectionButtonDialogFieldGroupControls(this.fMethodStubsButtons, composite, nColumns);
   }


   /**
    * Description of the Method
    *
    * @param composite  Description of the Parameter
    * @param nColumns   Description of the Parameter
    */
   protected void createTypeSelectionControls(Composite composite, int nColumns)
   {
      FieldsUtil.createSelectionButtonDialogFieldGroupControls(this.typeButtons, composite, nColumns);
   }


   /**
    * Gets the accessString attribute of the NewEntityEJBWizardPage object
    *
    * @return   The accessString value
    */
   protected String getAccessString()
   {
      String access = null;

      if (accessButtons.isSelected(0))
      {
         access = "remote";//$NON-NLS-1$
      }
      if (accessButtons.isSelected(1))
      {
         access = "local";//$NON-NLS-1$
      }
      if (accessButtons.isSelected(2))
      {
         access = "both";//$NON-NLS-1$
      }

      return access;
   }


   /** Description of the Method */
   protected void initContent()
   {
      super.initContent();

      // Constructor is checked
      this.fMethodStubsButtons.setSelection(0, true);
      // Inherited is checked
      this.fMethodStubsButtons.setSelection(1, true);
      // ejbCreate is unchecked
      this.fMethodStubsButtons.setSelection(2, true);

      // CMP 2.x by default
      this.typeButtons.setSelection(1, true);

      // Local access by default
      this.accessButtons.setSelection(1, true);
   }


   /**
    * Gets the cMP11 attribute of the NewEntityEJBWizardPage object
    *
    * @return   The cMP11 value
    */
   private boolean isCMP11()
   {
      return typeButtons.isSelected(0);
   }


   /**
    * Gets the cMP2x attribute of the NewEntityEJBWizardPage object
    *
    * @return   The cMP2x value
    */
   private boolean isCMP2x()
   {
      return typeButtons.isSelected(1);
   }
}
