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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.debug.internal.ui.DefaultLabelProvider;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaConventions;
import org.eclipse.jdt.internal.corext.util.JavaModelUtil;
import org.eclipse.jdt.internal.ui.dialogs.StatusInfo;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.LayoutUtil;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringButtonDialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringDialogField;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.core.util.NameValuePair;
import org.jboss.ide.eclipse.jdt.ui.JDTUIMessages;
import org.jboss.ide.eclipse.jdt.ui.dialogs.ExceptionEditDialog;
import org.jboss.ide.eclipse.jdt.ui.dialogs.ParameterEditDialog;
import org.jboss.ide.eclipse.jdt.ui.wizards.util.FieldsUtil;
import org.jboss.ide.eclipse.ui.util.ListContentProvider;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public abstract class MethodWizardPage extends ClassFragmentWizardPage
{
   /** Description of the Field */
   protected List exceptions = new Vector();

   /** Description of the Field */
   protected StructuredViewer exceptionsViewer;

   /** Description of the Field */
   protected List parameters = new Vector();

   /** Description of the Field */
   protected StructuredViewer parametersViewer;

   /** Primitive type list */
   private static List PRIMITIVES = new ArrayList();

   /**
    *Constructor for the MethodWizardPage object
    *
    * @param pageName  Description of the Parameter
    */
   public MethodWizardPage(String pageName)
   {
      super(pageName);
      this.setTitle(JDTUIMessages.getString("MethodWizardPage.title"));//$NON-NLS-1$
      this.setDescription(JDTUIMessages.getString("MethodWizardPage.description"));//$NON-NLS-1$
   }

   /**
    * Gets the exceptions attribute of the NewBusinessMethodWizardPage object
    *
    * @return   The exceptions value
    */
   public String[] getExceptions()
   {
      return (String[]) this.exceptions.toArray(new String[this.exceptions.size()]);
   }

   /**
    * Gets the methodName attribute of the MethodWizardPage object
    *
    * @return   The methodName value
    */
   public String getMethodName()
   {
      return this.getFragmentName();
   }

   /**
    * Gets the parameterNames attribute of the MethodWizardPage object
    *
    * @return   The parameterNames value
    */
   public String[] getParameterNames()
   {
      String[] names = new String[this.parameters.size()];
      for (int i = 0; i < this.parameters.size(); i++)
      {
         NameValuePair pair = (NameValuePair) this.parameters.get(i);
         names[i] = pair.getName();
      }
      return names;
   }

   /**
    * Gets the parameterValues attribute of the MethodWizardPage object
    *
    * @return   The parameterValues value
    */
   public String[] getParameterTypes()
   {
      String[] types = new String[this.parameters.size()];
      for (int i = 0; i < this.parameters.size(); i++)
      {
         NameValuePair pair = (NameValuePair) this.parameters.get(i);
         types[i] = pair.getValue();
      }
      return types;
   }

   /**
    * Gets the returnType attribute of the MethodWizardPage object
    *
    * @return   The returnType value
    */
   public String getReturnType()
   {
      return this.getFragmentType();
   }

   /**
    * Description of the Method
    *
    * @param field  Description of the Parameter
    */
   public void pageChangeControlPressed(DialogField field)
   {
      super.pageChangeControlPressed(field);

      if (field == this.typeDialogField)
      {
         IType type = chooseType(this.getReturnType());
         if (type != null)
         {
            this.typeDialogField.setText(JavaModelUtil.getFullyQualifiedName(type));
         }
      }
   }

   /** Description of the Method */
   protected void createContent()
   {
      String[] buttonNames;

      this.nameDialogField = new StringDialogField();
      this.nameDialogField.setDialogFieldListener(this.getFieldsAdapter());
      this.nameDialogField.setLabelText(JDTUIMessages.getString("MethodWizardPage.label.name"));//$NON-NLS-1$

      this.typeDialogField = new StringButtonDialogField(this.getFieldsAdapter());
      this.typeDialogField.setDialogFieldListener(this.getFieldsAdapter());
      this.typeDialogField.setLabelText(JDTUIMessages.getString("MethodWizardPage.label.returnType"));//$NON-NLS-1$
      this.typeDialogField.setButtonLabel(JDTUIMessages.getString("MethodWizardPage.button.browse"));//$NON-NLS-1$
   }

   /**
    * Description of the Method
    *
    * @param composite  Description of the Parameter
    * @param nColumns   Description of the Parameter
    */
   protected void createControl(Composite composite, int nColumns)
   {
      this.createMethodNameControls(composite, nColumns);
      this.createReturnTypeControls(composite, nColumns);
      this.createSeparator(composite, nColumns);
      this.createParametersControls(composite, nColumns);
      this.createSeparator(composite, nColumns);
      this.createExceptionsControls(composite, nColumns);
   }

   /**
    * Description of the Method
    *
    * @param composite  Description of the Parameter
    * @param nColumns   Description of the Parameter
    */
   protected void createExceptionsControls(Composite composite, int nColumns)
   {
      Label lbl = new Label(composite, SWT.NONE);
      lbl.setText(JDTUIMessages.getString("MethodWizardPage.label.exceptions"));//$NON-NLS-1$
      lbl.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));

      Table exceptionList = new Table(composite, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL);
      GridData layoutData = new GridData(GridData.FILL_BOTH);
      exceptionList.setLayoutData(layoutData);
      LayoutUtil.setHorizontalSpan(exceptionList, nColumns - 2);

      this.exceptionsViewer = new TableViewer(exceptionList);
      this.exceptionsViewer.setContentProvider(new ListContentProvider());
      this.exceptionsViewer.setInput(this.exceptions);

      Composite buttonComposite = new Composite(composite, SWT.NONE);
      GridLayout layout = new GridLayout(1, true);
      layout.marginHeight = 0;
      layout.marginWidth = 0;
      buttonComposite.setLayout(layout);
      buttonComposite.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));

      Button addButton = new Button(buttonComposite, SWT.PUSH);
      addButton.setText(JDTUIMessages.getString("MethodWizardPage.button.add"));//$NON-NLS-1$
      addButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

      addButton.addSelectionListener(new SelectionAdapter()
      {
         public void widgetSelected(SelectionEvent e)
         {
            doAddException();
         }
      });

      Button removeButton = new Button(buttonComposite, SWT.PUSH);
      removeButton.setText(JDTUIMessages.getString("MethodWizardPage.button.remove"));//$NON-NLS-1$
      removeButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

      removeButton.addSelectionListener(new SelectionAdapter()
      {
         public void widgetSelected(SelectionEvent e)
         {
            doRemoveException();
         }
      });
   }

   /**
    * Description of the Method
    *
    * @param composite  Description of the Parameter
    * @param nColumns   Description of the Parameter
    */
   protected void createMethodNameControls(Composite composite, int nColumns)
   {
      FieldsUtil.createStringDialogFieldControls(this.nameDialogField, composite, nColumns);
   }

   /**
    * Description of the Method
    *
    * @param composite  Description of the Parameter
    * @param nColumns   Description of the Parameter
    */
   protected void createParametersControls(Composite composite, int nColumns)
   {
      Label lbl = new Label(composite, SWT.NONE);
      lbl.setText(JDTUIMessages.getString("MethodWizardPage.label.parameters"));//$NON-NLS-1$
      lbl.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));

      Table parameterList = new Table(composite, SWT.BORDER | SWT.SINGLE | SWT.FULL_SELECTION | SWT.V_SCROLL);
      GridData layoutData = new GridData(GridData.FILL_BOTH);
      parameterList.setLayoutData(layoutData);

      LayoutUtil.setHorizontalSpan(parameterList, nColumns - 2);

      this.parametersViewer = new TableViewer(parameterList);
      this.parametersViewer.setContentProvider(new ListContentProvider());
      this.parametersViewer.setLabelProvider(new ParameterLabelProvider());
      this.parametersViewer.setInput(this.parameters);

      Composite buttonComposite = new Composite(composite, SWT.NONE);
      GridLayout layout = new GridLayout(1, true);
      layout.marginHeight = 0;
      layout.marginWidth = 0;
      buttonComposite.setLayout(layout);
      buttonComposite.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));

      Button addButton = new Button(buttonComposite, SWT.PUSH);
      addButton.setText(JDTUIMessages.getString("MethodWizardPage.button.add"));//$NON-NLS-1$
      addButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

      addButton.addSelectionListener(new SelectionAdapter()
      {
         public void widgetSelected(SelectionEvent e)
         {
            doAddParameter();
         }
      });

      Button removeButton = new Button(buttonComposite, SWT.PUSH);
      removeButton.setText(JDTUIMessages.getString("MethodWizardPage.button.remove"));//$NON-NLS-1$
      removeButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

      removeButton.addSelectionListener(new SelectionAdapter()
      {
         public void widgetSelected(SelectionEvent e)
         {
            doRemoveParameter();
         }
      });
   }

   /**
    * Description of the Method
    *
    * @param composite  Description of the Parameter
    * @param nColumns   Description of the Parameter
    */
   protected void createReturnTypeControls(Composite composite, int nColumns)
   {
      FieldsUtil.createStringButtonDialogFieldControls(this.typeDialogField, composite, nColumns);
   }

   /** Description of the Method */
   protected void doAddException()
   {
      ExceptionEditDialog dialog = new ExceptionEditDialog(AbstractPlugin.getShell(), this);
      if (dialog.open() == IDialogConstants.OK_ID)
      {
         String type = dialog.getType();
         this.exceptions.add(type);
         this.exceptionsViewer.refresh();
      }
   }

   /** Description of the Method */
   protected void doAddParameter()
   {
      ParameterEditDialog dialog = new ParameterEditDialog(AbstractPlugin.getShell(), this);
      if (dialog.open() == IDialogConstants.OK_ID)
      {
         String name = dialog.getName();
         String type = dialog.getType();
         this.parameters.add(new NameValuePair(name, type));
         this.parametersViewer.refresh();
      }
   }

   /** Description of the Method */
   protected void doRemoveException()
   {
      ISelection selection = this.exceptionsViewer.getSelection();
      if (selection != null)
      {
         Object o = ((IStructuredSelection) selection).getFirstElement();
         this.exceptions.remove(o);
         this.exceptionsViewer.refresh();
      }
   }

   /** Description of the Method */
   protected void doRemoveParameter()
   {
      ISelection selection = this.parametersViewer.getSelection();
      if (selection != null)
      {
         Object o = ((IStructuredSelection) selection).getFirstElement();
         this.parameters.remove(o);
         this.parametersViewer.refresh();
      }
   }

   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   protected IStatus fragmentNameChanged()
   {
      StatusInfo status = new StatusInfo();

      String name = this.getFragmentName();
      // must not be empty
      if (name.length() == 0)
      {
         status.setError(JDTUIMessages.getString("MethodWizardPage.error.method.name.empty"));//$NON-NLS-1$
         return status;
      }
      IStatus val = JavaConventions.validateMethodName(name);
      if (val.getSeverity() == IStatus.ERROR)
      {
         status.setError(MessageFormat.format(
               JDTUIMessages.getString("MethodWizardPage.error.method.name.invalid"), new Object[]{val.getMessage()}));//$NON-NLS-1$
         return status;
      }
      else if (val.getSeverity() == IStatus.WARNING)
      {
         status.setWarning(MessageFormat.format(JDTUIMessages
               .getString("MethodWizardPage.error.method.name.discouraged"), new Object[]{val.getMessage()}));//$NON-NLS-1$
      }

      return status;
   }

   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   protected IStatus fragmentTypeChanged()
   {
      StatusInfo status = new StatusInfo();
      //      IPackageFragmentRoot root = this.getPackageFragmentRoot();
      //
      //      String ftype = this.getFragmentType();
      //      // can be empty
      //      if (ftype.length() == 0)
      //      {
      //         return status;
      //      }
      //      if (PRIMITIVES.contains(ftype))
      //      {
      //         return status;
      //      }
      //      IStatus val = JavaConventions.validateJavaTypeName(ftype);
      //      if (val.getSeverity() == IStatus.ERROR)
      //      {
      //         status.setError(JDTUIMessages.getString("MethodWizardPage.error.method.type.invalid"));//$NON-NLS-1$
      //         return status;
      //      }
      //      if (root != null)
      //      {
      //         try
      //         {
      //            IType type = resolveTypeName(root.getJavaProject(), ftype);
      //            if (type == null)
      //            {
      //               status.setWarning(JDTUIMessages.getString("MethodWizardPage.error.method.type.inexistant"));//$NON-NLS-1$
      //               return status;
      //            }
      //            else
      //            {
      //            }
      //         }
      //         catch (JavaModelException e)
      //         {
      //            status.setError(JDTUIMessages.getString("MethodWizardPage.error.method.type.invalid"));//$NON-NLS-1$
      //            AbstractPlugin.logError("Error while checking method return type", e);//$NON-NLS-1$
      //         }
      //      }
      //      else
      //      {
      //         status.setError("");//$NON-NLS-1$
      //      }
      return status;
   }

   /**
    * Description of the Class
    *
    * @author    Laurent Etiemble
    * @version   $Revision$
    */
   private class ParameterLabelProvider extends DefaultLabelProvider
   {
      /**
       * Gets the text attribute of the ParameterLabelProvider object
       *
       * @param element  Description of the Parameter
       * @return         The text value
       */
      public String getText(Object element)
      {
         NameValuePair pair = (NameValuePair) element;
         return pair.getName() + " [" + pair.getValue() + "]";//$NON-NLS-1$ //$NON-NLS-2$
      }
   }

   static
   {
      PRIMITIVES.add("boolean");//$NON-NLS-1$
      PRIMITIVES.add("byte");//$NON-NLS-1$
      PRIMITIVES.add("char");//$NON-NLS-1$
      PRIMITIVES.add("double");//$NON-NLS-1$
      PRIMITIVES.add("float");//$NON-NLS-1$
      PRIMITIVES.add("int");//$NON-NLS-1$
      PRIMITIVES.add("long");//$NON-NLS-1$
      PRIMITIVES.add("short");//$NON-NLS-1$
      PRIMITIVES.add("void");//$NON-NLS-1$
   }
}
