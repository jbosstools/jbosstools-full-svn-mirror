/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.ui.pages;

import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.jdt.ui.wizards.IClasspathContainerPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.jboss.ide.eclipse.jdt.core.classpath.ClassPathContainerRepository;
import org.jboss.ide.eclipse.jdt.ui.JDTUIMessages;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public abstract class ClasspathContainerPage extends WizardPage implements IClasspathContainerPage
{
   private final static String PAGE_NAME = ClasspathContainerPage.class.getName();


   /**Constructor for the ClasspathContainerPage object */
   public ClasspathContainerPage()
   {
      super(PAGE_NAME);

      this.setTitle(JDTUIMessages.getString("ClasspathContainerPage.title"));//$NON-NLS-1$
      this.setDescription(JDTUIMessages.getString("ClasspathContainerPage.description"));//$NON-NLS-1$
      this.setImageDescriptor(JavaPluginImages.DESC_WIZBAN_ADD_LIBRARY);
   }


   /**
    * Description of the Method
    *
    * @param parent  Description of the Parameter
    */
   public void createControl(Composite parent)
   {
      Composite top = new Composite(parent, SWT.NONE);
      GridLayout layout = new GridLayout(1, true);
      top.setLayout(layout);

      Label lbl = new Label(top, SWT.NONE);
      lbl.setText(JDTUIMessages.getString("ClasspathContainerPage.text.start") + this.getClasspathEntryDescription() + JDTUIMessages.getString("ClasspathContainerPage.text.end"));//$NON-NLS-1$ //$NON-NLS-2$
      GridData gd = new GridData(GridData.FILL_HORIZONTAL);
      lbl.setLayoutData(gd);

      this.setControl(top);
   }


   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public boolean finish()
   {
      return true;
   }


   /**
    * Gets the selection attribute of the ClasspathContainerPage object
    *
    * @return   The selection value
    */
   public IClasspathEntry getSelection()
   {
      return this.getClasspathEntry();
   }


   /**
    * Sets the selection attribute of the ClasspathContainerPage object
    *
    * @param containerEntry  The new selection value
    */
   public void setSelection(IClasspathEntry containerEntry) { }


   /**
    * Gets the classpathContainerId attribute of the ClasspathContainerPage object
    *
    * @return   The classpathContainerId value
    */
   protected abstract String getClasspathContainerId();


   /**
    * Gets the classpathEntry attribute of the ClasspathContainerPage object
    *
    * @return   The classpathEntry value
    */
   protected IClasspathEntry getClasspathEntry()
   {
      return ClassPathContainerRepository.getInstance().getEntry(this.getClasspathContainerId());
   }


   /**
    * Gets the classpathEntryDescription attribute of the ClasspathContainerPage object
    *
    * @return   The classpathEntryDescription value
    */
   protected abstract String getClasspathEntryDescription();
}
