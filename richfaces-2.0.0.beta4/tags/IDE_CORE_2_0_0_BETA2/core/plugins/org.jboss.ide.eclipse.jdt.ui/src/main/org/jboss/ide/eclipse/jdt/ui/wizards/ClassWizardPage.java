/*
 * JBoss, a division of Red Hat
 * Copyright 2006, Red Hat Middleware, LLC, and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.ide.eclipse.jdt.ui.wizards;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.internal.ui.dialogs.StatusUtil;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.SelectionButtonDialogFieldGroup;
import org.eclipse.jdt.ui.wizards.NewTypeWizardPage;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.jboss.ide.eclipse.jdt.core.wizards.generation.IGenerationEngine;
import org.jboss.ide.eclipse.jdt.ui.wizards.util.FieldsAdapter;
import org.jboss.ide.eclipse.jdt.ui.wizards.util.FieldsAdapterListener;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public abstract class ClassWizardPage extends NewTypeWizardPage implements FieldsAdapterListener
{
   /** Description of the Field */
   protected SelectionButtonDialogFieldGroup fMethodStubsButtons;

   private FieldsAdapter adapter;

   private final static String PAGE_NAME = ClassWizardPage.class.getName();

   /**
    *Constructor for the ClassWizardPage object
    *
    * @param isClass  Description of the Parameter
    * @param page     Description of the Parameter
    */
   public ClassWizardPage(boolean isClass, String page)
   {
      super(isClass, page);
      this.createContent();
   }

   /**
    *Constructor for the ClassWizardPage object
    *
    * @param root  Description of the Parameter
    */
   public ClassWizardPage(IWorkspaceRoot root)
   {
      this(true, PAGE_NAME);
   }

   /**
    * Description of the Method
    *
    * @param parent  Description of the Parameter
    */
   public void createControl(Composite parent)
   {
      this.initializeDialogUnits(parent);

      Composite composite = new Composite(parent, SWT.NONE);

      int nColumns = 4;

      GridLayout layout = new GridLayout();
      layout.numColumns = nColumns;
      composite.setLayout(layout);

      this.createControls(composite, nColumns);
      this.setControl(composite);
   }

   /**
    * Description of the Method
    *
    * @param engine   Description of the Parameter
    * @param monitor  Description of the Parameter
    */
   public abstract void generate(IGenerationEngine engine, IProgressMonitor monitor);

   /**
    * Gets the fieldsAdapter attribute of the ClassFragmentWizardPage object
    *
    * @return   The fieldsAdapter value
    */
   public synchronized FieldsAdapter getFieldsAdapter()
   {
      if (this.adapter == null)
      {
         this.adapter = new FieldsAdapter(this);
      }
      return adapter;
   }

   /**
    * Description of the Method
    *
    * @param selection  Description of the Parameter
    */
   public void init(IStructuredSelection selection)
   {
      IJavaElement jelem = this.getInitialJavaElement(selection);

      this.initContainerPage(jelem);
      this.initTypePage(jelem);
      this.updateStatus(this.findMostSevereStatus());

      this.initContent();
   }

   /**
    * Description of the Method
    *
    * @param field  Description of the Parameter
    */
   public void pageChangeControlPressed(DialogField field)
   {
   }

   /**
    * Description of the Method
    *
    * @param field  Description of the Parameter
    */
   public void pageDialogFieldChanged(DialogField field)
   {
      this.handleFieldChanged(field);
      this.updateStatus(this.findMostSevereStatus());
   }

   /**
    * Sets the visible attribute of the ClassWizardPage object
    *
    * @param visible  The new visible value
    */
   public void setVisible(boolean visible)
   {
      super.setVisible(visible);
      if (visible)
      {
         this.setFocus();
      }
   }

   /** Description of the Method */
   protected abstract void createContent();

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
      this.createModifierControls(composite, nColumns);
      this.createSuperClassControls(composite, nColumns);
      this.createSuperInterfacesControls(composite, nColumns);
   }

   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   protected IStatus findMostSevereStatus()
   {
      return StatusUtil.getMostSevere(new IStatus[]
      {fContainerStatus, fPackageStatus, fTypeNameStatus, fModifierStatus, fSuperClassStatus, fSuperInterfacesStatus});
   }

   /**
    * Description of the Method
    *
    * @param field  Description of the Parameter
    */
   protected void handleFieldChanged(DialogField field)
   {
   }

   /**
    * Description of the Method
    *
    * @param fieldName  Description of the Parameter
    */
   protected void handleFieldChanged(String fieldName)
   {
      super.handleFieldChanged(fieldName);
      this.updateStatus(this.findMostSevereStatus());
   }

   /** Description of the Method */
   protected abstract void initContent();
}
