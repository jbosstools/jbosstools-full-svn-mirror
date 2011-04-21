/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
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
package org.jboss.ide.eclipse.jdt.j2ee.ui.wizards.ejb;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.debug.internal.ui.DefaultLabelProvider;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaConventions;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.core.jdom.IDOMCompilationUnit;
import org.eclipse.jdt.core.jdom.IDOMField;
import org.eclipse.jdt.core.jdom.IDOMMethod;
import org.eclipse.jdt.core.jdom.IDOMType;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.internal.corext.util.JavaModelUtil;
import org.eclipse.jdt.internal.ui.dialogs.StatusInfo;
import org.eclipse.jdt.internal.ui.dialogs.StatusUtil;
import org.eclipse.jdt.internal.ui.dialogs.TypeSelectionDialog2;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.LayoutUtil;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringButtonDialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringDialogField;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.jdt.core.classpath.ClassPathContainerRepository;
import org.jboss.ide.eclipse.jdt.core.util.ClassNamingUtil;
import org.jboss.ide.eclipse.jdt.core.util.JavaProjectUtil;
import org.jboss.ide.eclipse.jdt.j2ee.ui.JDTJ2EEUIMessages;
import org.jboss.ide.eclipse.jdt.j2ee.ui.JDTJ2EEUIPlugin;
import org.jboss.ide.eclipse.jdt.test.core.classpath.JUnitClasspathContainer;
import org.jboss.ide.eclipse.jdt.ui.wizards.DOMClassWizardPage;
import org.jboss.ide.eclipse.jdt.ui.wizards.util.FieldsUtil;
import org.jboss.ide.eclipse.ui.util.ListContentProvider;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class NewEJBTestClientWizardPage extends DOMClassWizardPage
{

   /** Description of the Field */
   protected StringButtonDialogField homeInterfaceField;

   /** Description of the Field */
   protected IStatus homeInterfaceStatus;

   /** Description of the Field */
   protected StringDialogField jndiNameField;

   /** Description of the Field */
   protected IStatus jndiNameStatus;

   /** Description of the Field */
   protected List methods = new Vector();

   /** Description of the Field */
   protected CheckboxTableViewer methodsViewer;

   /** Description of the Field */
   protected StringButtonDialogField remoteInterfaceField;

   /** Description of the Field */
   protected IStatus remoteInterfaceStatus;

   /** Description of the Field */
   protected List testedMethods = new Vector();

   /** Description of the Field */
   protected static Map VALUES = new HashMap();

   private final static String PAGE_NAME = NewEJBTestClientWizardPage.class.getName();

   /**
    *Constructor for the NewEJBTestClientWizardPage object
    *
    * @param root  Description of the Parameter
    */
   public NewEJBTestClientWizardPage(IWorkspaceRoot root)
   {
      super(true, PAGE_NAME);
      this.setTitle(JDTJ2EEUIMessages.getString("NewEJBTestClientWizardPage.title"));//$NON-NLS-1$
      this.setDescription(JDTJ2EEUIMessages.getString("NewEJBTestClientWizardPage.description"));//$NON-NLS-1$

      this.homeInterfaceStatus = new StatusInfo();
      this.remoteInterfaceStatus = new StatusInfo();
   }

   /**
    * Gets the homeInterface attribute of the NewEJBTestClientWizardPage object
    *
    * @return   The homeInterface value
    */
   public String getHomeInterface()
   {
      return this.homeInterfaceField.getText();
   }

   /**
    * Gets the jNDIName attribute of the NewEJBTestClientWizardPage object
    *
    * @return   The jNDIName value
    */
   public String getJNDIName()
   {
      return this.jndiNameField.getText();
   }

   /**
    * Gets the remoteInterface attribute of the NewEJBTestClientWizardPage object
    *
    * @return   The remoteInterface value
    */
   public String getRemoteInterface()
   {
      return this.remoteInterfaceField.getText();
   }

   /**
    * Description of the Method
    *
    * @param selection  Description of the Parameter
    */
   public void init(IStructuredSelection selection)
   {
      super.init(selection);

      this.jndiNameStatus = this.jndiNameChanged();
      this.remoteInterfaceStatus = this.remoteInterfaceChanged();
      this.homeInterfaceStatus = this.homeInterfaceChanged();
      this.updateStatus(this.findMostSevereStatus());
   }

   /**
    * Gets the createConstructors attribute of the NewEJBTestClientWizardPage object
    *
    * @return   The createConstructors value
    */
   public boolean isCreateConstructors()
   {
      return true;
   }

   /**
    * Gets the createInherited attribute of the NewEJBTestClientWizardPage object
    *
    * @return   The createInherited value
    */
   public boolean isCreateInherited()
   {
      return true;
   }

   /**
    * Description of the Method
    *
    * @param field  Description of the Parameter
    */
   public void pageChangeControlPressed(DialogField field)
   {
      super.pageChangeControlPressed(field);

      if (field == this.homeInterfaceField)
      {
         IType type = this.chooseEJBInterface();
         if (type != null)
         {
            this.homeInterfaceField.setText(JavaModelUtil.getFullyQualifiedName(type));
         }
      }
      if (field == this.remoteInterfaceField)
      {
         IType type = this.chooseEJBInterface();
         if (type != null)
         {
            this.remoteInterfaceField.setText(JavaModelUtil.getFullyQualifiedName(type));
            this.checkRemoteInterface();
         }
      }
   }

   /**
    * Adds a feature to the Content attribute of the NewEJBTestClientWizardPage object
    *
    * @param type      The feature to be added to the Content attribute
    * @param compUnit  The feature to be added to the Content attribute
    * @param dType     The feature to be added to the Content attribute
    * @param monitor   The feature to be added to the Content attribute
    */
   protected void addContent(IType type, IDOMCompilationUnit compUnit, IDOMType dType, IProgressMonitor monitor)
   {
      // Add the JUnit classpath container to the project
      try
      {
         IJavaProject project = getPackageFragmentRoot().getJavaProject();
         IClasspathEntry[] entries = project.getRawClasspath();

         entries = JavaProjectUtil.mergeClasspathEntry(entries, ClassPathContainerRepository.getInstance().getEntry(
               JUnitClasspathContainer.CLASSPATH_CONTAINER));

         project.setRawClasspath(entries, monitor);
      }
      catch (CoreException ce)
      {
         AbstractPlugin.logError("Unable to compute classpath", ce);//$NON-NLS-1$
         JDTJ2EEUIPlugin.getDefault().showErrorMessage(
               JDTJ2EEUIMessages.getString("NewEJBTestClientWizardPage.error.message.junit.classpath"));//$NON-NLS-1$
         return;
      }

      Templates manager = new Templates();
      String comment;
      IDOMField field;
      IDOMMethod method;

      String jndiName = this.getJNDIName();
      String remoteIntf = this.getRemoteInterface();
      String homeIntf = this.getHomeInterface();

      // Add the custom comment on top of the Servlet
      comment = manager.getString("wizards.ejb.testclient.class.comment", new Object[]{type.getElementName()});//$NON-NLS-1$
      dType.setComment(comment);

      field = this.buildField(manager, "wizards.ejb.testclient.field.home", new Object[]{homeIntf});//$NON-NLS-1$
      dType.addChild(field);

      // getInitialContext method
      method = this.buildMethod(manager, "wizards.ejb.testclient.method.getInitialContext");//$NON-NLS-1$
      dType.addChild(method);

      //	getHome method
      method = this.buildMethod(manager, "wizards.ejb.testclient.method.getHome", new Object[]{jndiName, homeIntf});//$NON-NLS-1$
      dType.addChild(method);

      //	setUp method
      method = this.buildMethod(manager, "wizards.ejb.testclient.method.setUp");//$NON-NLS-1$
      dType.addChild(method);

      for (int i = 0; i < this.testedMethods.size(); i++)
      {
         try
         {
            IMethod m = (IMethod) this.testedMethods.get(i);
            method = this.getDOMFactory().createMethod();
            String name = "test" + ClassNamingUtil.capitalize(m.getElementName());//$NON-NLS-1$
            String returnType = Signature.toString(m.getReturnType());
            method.setName(name);
            method.setExceptions(new String[]
            {"Exception"});//$NON-NLS-1$

            comment = manager
                  .getString(
                        "wizards.ejb.testclient.method.test.comment", new Object[]{remoteIntf, Signature.toString(m.getSignature(), m.getElementName(), m.getParameterNames(), false, false)});//$NON-NLS-1$
            method.setComment(comment);

            // Create the body;
            StringBuffer body = new StringBuffer();
            body.append("{\n");//$NON-NLS-1$
            body.append(remoteIntf);
            body.append(" instance;\n");//$NON-NLS-1$
            if (!"void".equals(returnType)//$NON-NLS-1$
            )
            {
               body.append(Signature.toString(m.getReturnType()));
               body.append(" result;\n");//$NON-NLS-1$
            }
            body.append("\n");//$NON-NLS-1$
            body.append(JDTJ2EEUIMessages.getString("NewEJBTestClientWizardPage.comment.parameters"));//$NON-NLS-1$
            for (int j = 0; j < m.getNumberOfParameters(); j++)
            {
               String pType = Signature.toString(m.getParameterTypes()[j]);
               body.append(pType);
               body.append(" param");//$NON-NLS-1$
               body.append(j);
               body.append(" = ");//$NON-NLS-1$
               if (VALUES.containsKey(pType))
               {
                  body.append(VALUES.get(pType));
               }
               else
               {
                  body.append("null");//$NON-NLS-1$
               }
               body.append(";\n");//$NON-NLS-1$
            }

            body.append("\n");//$NON-NLS-1$
            body.append(JDTJ2EEUIMessages.getString("NewEJBTestClientWizardPage.comment.creation"));//$NON-NLS-1$
            body.append("instance = this.home.create();\n");//$NON-NLS-1$
            body.append("\n");//$NON-NLS-1$
            body.append(JDTJ2EEUIMessages.getString("NewEJBTestClientWizardPage.comment.call"));//$NON-NLS-1$
            if (!"void".equals(returnType)//$NON-NLS-1$
            )
            {
               body.append("result = ");//$NON-NLS-1$
            }
            body.append("instance.");//$NON-NLS-1$
            body.append(m.getElementName());
            body.append("(");//$NON-NLS-1$
            for (int j = 0; j < m.getNumberOfParameters(); j++)
            {
               if (j > 0)
               {
                  body.append(", ");//$NON-NLS-1$
               }
               body.append("param");//$NON-NLS-1$
               body.append(j);
            }
            body.append(");\n");//$NON-NLS-1$
            body.append("\n");//$NON-NLS-1$
            body.append(JDTJ2EEUIMessages.getString("NewEJBTestClientWizardPage.comment.assert"));//$NON-NLS-1$
            if (!"void".equals(returnType)//$NON-NLS-1$
            )
            {

               body.append("// assertNotNull(result);\n");//$NON-NLS-1$
            }

            body.append("}\n");//$NON-NLS-1$

            method.setBody(body.toString());
            dType.addChild(method);
         }
         catch (JavaModelException jme)
         {
            AbstractPlugin.logError("Cannot create test method", jme);//$NON-NLS-1$
         }
      }

      this.addImport(compUnit, "java.util.Hashtable");//$NON-NLS-1$
      this.addImport(compUnit, "javax.rmi.PortableRemoteObject");//$NON-NLS-1$
      this.addImport(compUnit, "javax.naming.Context");//$NON-NLS-1$
      this.addImport(compUnit, "javax.naming.InitialContext");//$NON-NLS-1$
   }

   /** Description of the Method */
   protected void checkRemoteInterface()
   {
      try
      {
         String remoteInterface = this.remoteInterfaceField.getText();
         IType type = this.findType(this.getPackageFragmentRoot().getJavaProject(), remoteInterface);
         IMethod[] typeMethods = type.getMethods();

         this.methods.clear();
         this.testedMethods.clear();

         for (int i = 0; i < typeMethods.length; i++)
         {
            IMethod m = typeMethods[i];
            this.methods.add(m);
         }
         this.refreshMethods();
      }
      catch (JavaModelException jme)
      {
         // Do nothing
      }
   }

   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   protected IType chooseEJBInterface()
   {
      IPackageFragmentRoot root = getPackageFragmentRoot();
      if (root == null)
      {
         return null;
      }

      IJavaElement[] elements = new IJavaElement[]
      {root.getJavaProject()};
      IJavaSearchScope scope = SearchEngine.createJavaSearchScope(elements);

      TypeSelectionDialog2 dialog = new TypeSelectionDialog2(getShell(), false, getWizard().getContainer(), scope,
            IJavaSearchConstants.INTERFACE);
      dialog.setTitle(JDTJ2EEUIMessages.getString("NewEJBTestClientWizardPage.dialog.title"));//$NON-NLS-1$
      dialog.setMessage(JDTJ2EEUIMessages.getString("NewEJBTestClientWizardPage.dialog.message"));//$NON-NLS-1$

      if (dialog.open() == TypeSelectionDialog2.OK)
      {
         return (IType) dialog.getFirstResult();
      }
      return null;
   }

   /** Description of the Method */
   protected void createContent()
   {
      super.createContent();

      this.jndiNameField = new StringDialogField();
      this.jndiNameField.setDialogFieldListener(this.getFieldsAdapter());
      this.jndiNameField.setLabelText(JDTJ2EEUIMessages.getString("NewEJBTestClientWizardPage.label.jndi"));//$NON-NLS-1$

      this.remoteInterfaceField = new StringButtonDialogField(this.getFieldsAdapter());
      this.remoteInterfaceField.setDialogFieldListener(this.getFieldsAdapter());
      this.remoteInterfaceField.setLabelText(JDTJ2EEUIMessages
            .getString("NewEJBTestClientWizardPage.label.remote.interface"));//$NON-NLS-1$
      this.remoteInterfaceField.setButtonLabel(JDTJ2EEUIMessages.getString("NewEJBTestClientWizardPage.button.browse"));//$NON-NLS-1$

      this.homeInterfaceField = new StringButtonDialogField(this.getFieldsAdapter());
      this.homeInterfaceField.setDialogFieldListener(this.getFieldsAdapter());
      this.homeInterfaceField.setLabelText(JDTJ2EEUIMessages
            .getString("NewEJBTestClientWizardPage.label.home.interface"));//$NON-NLS-1$
      this.homeInterfaceField.setButtonLabel(JDTJ2EEUIMessages.getString("NewEJBTestClientWizardPage.button.browse"));//$NON-NLS-1$
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
      this.createSeparator(composite, nColumns);
      FieldsUtil.createStringDialogFieldControls(this.jndiNameField, composite, nColumns);
      FieldsUtil.createStringButtonDialogFieldControls(this.remoteInterfaceField, composite, nColumns);
      FieldsUtil.createStringButtonDialogFieldControls(this.homeInterfaceField, composite, nColumns);
      this.createSeparator(composite, nColumns);
      this.createMethodListControls(composite, nColumns);

      this.setSuperClass("junit.framework.TestCase", false);//$NON-NLS-1$
   }

   /**
    * Description of the Method
    *
    * @param composite  Description of the Parameter
    * @param nColumns   Description of the Parameter
    */
   protected void createMethodListControls(Composite composite, int nColumns)
   {
      FieldsUtil.createLabelControls(
            JDTJ2EEUIMessages.getString("NewEJBTestClientWizardPage.label.methods"), composite, nColumns);//$NON-NLS-1$
      DialogField.createEmptySpace(composite);

      Table exceptionList = new Table(composite, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL | SWT.CHECK);
      GridData layoutData = new GridData(GridData.FILL_BOTH);
      exceptionList.setLayoutData(layoutData);
      LayoutUtil.setHorizontalSpan(exceptionList, nColumns - 1);

      this.methodsViewer = new CheckboxTableViewer(exceptionList);
      this.methodsViewer.setContentProvider(new ListContentProvider());
      this.methodsViewer.setLabelProvider(new MethodLabelProvider());
      this.methodsViewer.setInput(this.methods);
      this.methodsViewer.addCheckStateListener(new ICheckStateListener()
      {
         public void checkStateChanged(CheckStateChangedEvent event)
         {
            IMethod m = (IMethod) event.getElement();
            if (event.getChecked())
            {
               testedMethods.add(m);
            }
            else
            {
               testedMethods.remove(m);
            }
         }
      });
   }

   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   protected IStatus findMostSevereStatus()
   {
      return StatusUtil.getMostSevere(new IStatus[]
      {this.fContainerStatus, this.fPackageStatus, this.fTypeNameStatus, this.jndiNameStatus,
            this.remoteInterfaceStatus, this.homeInterfaceStatus});
   }

   /**
    * Description of the Method
    *
    * @param project                 Description of the Parameter
    * @param typeName                Description of the Parameter
    * @return                        Description of the Return Value
    * @exception JavaModelException  Description of the Exception
    */
   protected IType findType(IJavaProject project, String typeName) throws JavaModelException
   {
      if (project.exists())
      {
         return project.findType(typeName);
      }
      return null;
   }

   /**
    * Description of the Method
    *
    * @param field  Description of the Parameter
    */
   protected void handleFieldChanged(DialogField field)
   {
      super.handleFieldChanged(field);

      if (field == this.jndiNameField)
      {
         this.jndiNameStatus = this.jndiNameChanged();
      }
      if (field == this.remoteInterfaceField)
      {
         this.remoteInterfaceStatus = this.remoteInterfaceChanged();
      }
      if (field == this.homeInterfaceField)
      {
         this.homeInterfaceStatus = this.homeInterfaceChanged();
      }
   }

   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   protected IStatus homeInterfaceChanged()
   {
      StatusInfo status = new StatusInfo();
      IPackageFragmentRoot root = this.getPackageFragmentRoot();

      String ftype = this.getHomeInterface();
      // must not be empty
      if (ftype.length() == 0)
      {
         status.setError(JDTJ2EEUIMessages.getString("NewEJBTestClientWizardPage.error.home.interface.empty"));//$NON-NLS-1$
         return status;
      }
      IStatus val = JavaConventions.validateJavaTypeName(ftype);
      if (val.getSeverity() == IStatus.ERROR)
      {
         status.setError(JDTJ2EEUIMessages.getString("NewEJBTestClientWizardPage.error.home.interface.invalid"));//$NON-NLS-1$
         return status;
      }
      if (root != null)
      {
         try
         {
            IType type = this.findType(root.getJavaProject(), ftype);
            if (type == null)
            {
               status.setWarning(JDTJ2EEUIMessages
                     .getString("NewEJBTestClientWizardPage.warning.home.interface.inexistant"));//$NON-NLS-1$
               return status;
            }
            else
            {
            }
         }
         catch (JavaModelException e)
         {
            status.setError(JDTJ2EEUIMessages.getString("NewEJBTestClientWizardPage.error.home.interface.invalid"));//$NON-NLS-1$
            AbstractPlugin.logError("Error while checking method home interface", e);//$NON-NLS-1$
         }
      }
      else
      {
         status.setError("");//$NON-NLS-1$
      }
      return status;
   }

   /** Description of the Method */
   protected void initContent()
   {
      super.initContent();

      // All is unchecked
      this.fMethodStubsButtons.setSelection(0, false);
      this.fMethodStubsButtons.setSelection(1, false);

      // Default prefix
      this.jndiNameField.setText("ejb/");//$NON-NLS-1$
   }

   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   protected IStatus jndiNameChanged()
   {
      StatusInfo status = new StatusInfo();
      IPackageFragmentRoot root = this.getPackageFragmentRoot();

      String name = this.getJNDIName();
      // must not be empty
      if (name.length() == 0)
      {
         status.setError(JDTJ2EEUIMessages.getString("NewEJBTestClientWizardPage.error.jndi.empty"));//$NON-NLS-1$
         return status;
      }
      return status;
   }

   /** Description of the Method */
   protected void refreshMethods()
   {
      this.methodsViewer.refresh();
      this.methodsViewer.setCheckedElements(this.testedMethods.toArray());
   }

   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   protected IStatus remoteInterfaceChanged()
   {
      StatusInfo status = new StatusInfo();
      IPackageFragmentRoot root = this.getPackageFragmentRoot();

      String ftype = this.getRemoteInterface();
      // must not be empty
      if (ftype.length() == 0)
      {
         status.setError(JDTJ2EEUIMessages.getString("NewEJBTestClientWizardPage.error.remote.interface.empty"));//$NON-NLS-1$
         return status;
      }
      IStatus val = JavaConventions.validateJavaTypeName(ftype);
      if (val.getSeverity() == IStatus.ERROR)
      {
         status.setError(JDTJ2EEUIMessages.getString("NewEJBTestClientWizardPage.error.remote.interface.invalid"));//$NON-NLS-1$
         return status;
      }
      if (root != null)
      {
         try
         {
            IType type = this.findType(root.getJavaProject(), ftype);
            if (type == null)
            {
               status.setWarning(JDTJ2EEUIMessages
                     .getString("NewEJBTestClientWizardPage.warning.remote.interface.inexistant"));//$NON-NLS-1$
               return status;
            }
            else
            {
            }
         }
         catch (JavaModelException e)
         {
            status.setError(JDTJ2EEUIMessages.getString("NewEJBTestClientWizardPage.error.remote.interface.invalid"));//$NON-NLS-1$
            AbstractPlugin.logError("Error while checking method home interface", e);//$NON-NLS-1$
         }
      }
      else
      {
         status.setError("");//$NON-NLS-1$
      }
      return status;
   }

   /**
    * Description of the Class
    *
    * @author    Laurent Etiemble
    * @version   $Revision$
    */
   private class MethodLabelProvider extends DefaultLabelProvider
   {
      /**
       * Gets the text attribute of the MethodLabelProvider object
       *
       * @param element  Description of the Parameter
       * @return         The text value
       */
      public String getText(Object element)
      {
         IMethod m = (IMethod) element;
         String result = JDTJ2EEUIMessages.getString("NewEJBTestClientWizardPage.not.available");//$NON-NLS-1$
         try
         {
            result = Signature.toString(m.getSignature(), m.getElementName(), m.getParameterNames(), false, false);
         }
         catch (JavaModelException jme)
         {
            // Ignore it
         }

         return result;
      }
   }

   static
   {
      VALUES.put("byte", "0");//$NON-NLS-1$ //$NON-NLS-2$
      VALUES.put("char", "'\0'");//$NON-NLS-1$ //$NON-NLS-2$
      VALUES.put("int", "0");//$NON-NLS-1$ //$NON-NLS-2$
      VALUES.put("long", "0");//$NON-NLS-1$ //$NON-NLS-2$
      VALUES.put("short", "0");//$NON-NLS-1$ //$NON-NLS-2$
      VALUES.put("double", "0");//$NON-NLS-1$ //$NON-NLS-2$
      VALUES.put("float", "0");//$NON-NLS-1$ //$NON-NLS-2$
      VALUES.put("boolean", "false");//$NON-NLS-1$ //$NON-NLS-2$
   }
}
