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
package org.jboss.ide.eclipse.jdt.ui.wizards;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.internal.corext.util.JavaModelUtil;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.dialogs.StatusInfo;
import org.eclipse.jdt.internal.ui.dialogs.StatusUtil;
import org.eclipse.jdt.internal.ui.dialogs.TypeSelectionDialog2;
import org.eclipse.jdt.internal.ui.viewsupport.IViewPartInputProvider;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.Separator;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringButtonDialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringDialogField;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.contentoutline.ContentOutline;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.jdt.ui.JDTUIMessages;
import org.jboss.ide.eclipse.jdt.ui.wizards.util.FieldsAdapter;
import org.jboss.ide.eclipse.jdt.ui.wizards.util.FieldsAdapterListener;
import org.jboss.ide.eclipse.jdt.ui.wizards.util.TypeChooser;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public abstract class ClassFragmentWizardPage extends WizardPage implements TypeChooser, FieldsAdapterListener
{
   /** Description of the Field */
   protected StringDialogField nameDialogField;

   /** Description of the Field */
   protected IStatus nameStatus;

   /** Description of the Field */
   protected StringButtonDialogField typeDialogField;

   /** Description of the Field */
   protected IStatus typeStatus;

   private FieldsAdapter adapter;

   private IPackageFragment currPackage;

   private IPackageFragmentRoot currRoot;

   private IWorkspaceRoot root;

   /**
    *Constructor for the ClassFragmentWizardPage object
    *
    * @param pageName  Description of the Parameter
    */
   public ClassFragmentWizardPage(String pageName)
   {
      super(pageName);
      this.setTitle(JDTUIMessages.getString("ClassFragmentWizardPage.title"));//$NON-NLS-1$
      this.setDescription(JDTUIMessages.getString("ClassFragmentWizardPage.description"));//$NON-NLS-1$

      this.nameStatus = new StatusInfo();
      this.typeStatus = new StatusInfo();

      this.createContent();
   }

   /**
    *Constructor for the ClassFragmentWizardPage object
    *
    * @param root  Description of the Parameter
    */
   public ClassFragmentWizardPage(IWorkspaceRoot root)
   {
      this(ClassFragmentWizardPage.class.getName());
      this.root = root;
   }

   /**
    * Description of the Method
    *
    * @param filter  Description of the Parameter
    * @return        Description of the Return Value
    */
   public IType chooseType(String filter)
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
            IJavaSearchConstants.TYPE);
      dialog.setTitle(JDTUIMessages.getString("ClassFragmentWizardPage.type.selection.title"));//$NON-NLS-1$
      dialog.setMessage(JDTUIMessages.getString("ClassFragmentWizardPage.type.selection.message"));//$NON-NLS-1$
      if (filter != null)
      {
         dialog.setFilter(filter);
      }

      if (dialog.open() == Window.OK)
      {
         return (IType) dialog.getFirstResult();
      }
      return null;
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

      this.createControl(composite, nColumns);
      this.setControl(composite);
   }

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
      return this.adapter;
   }

   /**
    * Gets the fieldName attribute of the FieldWizardPage object
    *
    * @return   The fieldName value
    */
   public String getFragmentName()
   {
      return this.nameDialogField.getText();
   }

   /**
    * Gets the fieldType attribute of the FieldWizardPage object
    *
    * @return   The fieldType value
    */
   public String getFragmentType()
   {
      return this.typeDialogField.getText();
   }

   /**
    * Gets the packageFragment attribute of the ClassFragmentWizardPage object
    *
    * @return   The packageFragment value
    */
   public IPackageFragment getPackageFragment()
   {
      return this.currPackage;
   }

   /**
    * Gets the packageFragmentRoot attribute of the ClassFragmentWizardPage object
    *
    * @return   The packageFragmentRoot value
    */
   public IPackageFragmentRoot getPackageFragmentRoot()
   {
      return this.currRoot;
   }

   /**
    * Gets the workspaceRoot attribute of the ClassFragmentWizardPage object
    *
    * @return   The workspaceRoot value
    */
   public IWorkspaceRoot getWorkspaceRoot()
   {
      return this.root;
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
      this.initContent();

      this.nameStatus = this.fragmentNameChanged();
      this.typeStatus = this.fragmentTypeChanged();
      this.updateStatus(this.findMostSevereStatus());
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
    * Sets the visible attribute of the ClassFragmentWizardPage object
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
   protected abstract void createControl(Composite composite, int nColumns);

   /**
    * Description of the Method
    *
    * @param composite  Description of the Parameter
    * @param nColumns   Description of the Parameter
    */
   protected void createSeparator(Composite composite, int nColumns)
   {
      (new Separator(SWT.SEPARATOR | SWT.HORIZONTAL)).doFillIntoGrid(composite, nColumns,
            convertHeightInCharsToPixels(1));
   }

   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   protected IStatus findMostSevereStatus()
   {
      return StatusUtil.getMostSevere(new IStatus[]
      {this.nameStatus, this.typeStatus});
   }

   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   protected abstract IStatus fragmentNameChanged();

   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   protected abstract IStatus fragmentTypeChanged();

   /**
    * Gets the initialJavaElement attribute of the ClassFragmentWizardPage object
    *
    * @param selection  Description of the Parameter
    * @return           The initialJavaElement value
    */
   protected IJavaElement getInitialJavaElement(IStructuredSelection selection)
   {
      IJavaElement jelem = null;
      if (selection != null && !selection.isEmpty())
      {
         Object selectedElement = selection.getFirstElement();
         if (selectedElement instanceof IAdaptable)
         {
            IAdaptable adaptable = (IAdaptable) selectedElement;

            jelem = (IJavaElement) adaptable.getAdapter(IJavaElement.class);
            if (jelem == null)
            {
               IResource resource = (IResource) adaptable.getAdapter(IResource.class);
               if (resource != null && resource.getType() != IResource.ROOT)
               {
                  while (jelem == null && resource.getType() != IResource.PROJECT)
                  {
                     resource = resource.getParent();
                     jelem = (IJavaElement) resource.getAdapter(IJavaElement.class);
                  }
                  if (jelem == null)
                  {
                     jelem = JavaCore.create(resource);// java project
                  }
               }
            }
         }
      }
      if (jelem == null)
      {
         IWorkbenchPart part = JavaPlugin.getActivePage().getActivePart();
         if (part instanceof ContentOutline)
         {
            part = JavaPlugin.getActivePage().getActiveEditor();
         }

         if (part instanceof IViewPartInputProvider)
         {
            Object elem = ((IViewPartInputProvider) part).getViewPartInput();
            if (elem instanceof IJavaElement)
            {
               jelem = (IJavaElement) elem;
            }
         }
      }

      if (jelem == null || jelem.getElementType() == IJavaElement.JAVA_MODEL)
      {
         try
         {
            IJavaProject[] projects = JavaCore.create(AbstractPlugin.getWorkspace().getRoot()).getJavaProjects();
            if (projects.length == 1)
            {
               jelem = projects[0];
            }
         }
         catch (JavaModelException e)
         {
            JavaPlugin.log(e);
         }
      }
      return jelem;
   }

   /**
    * Description of the Method
    *
    * @param field  Description of the Parameter
    */
   protected void handleFieldChanged(DialogField field)
   {
      if (field == this.nameDialogField)
      {
         this.nameStatus = this.fragmentNameChanged();
      }
      if (field == this.typeDialogField)
      {
         this.typeStatus = this.fragmentTypeChanged();
      }
   }

   /**
    * Description of the Method
    *
    * @param elem  Description of the Parameter
    */
   protected void initContainerPage(IJavaElement elem)
   {
      IPackageFragmentRoot initRoot = null;
      if (elem != null)
      {
         initRoot = JavaModelUtil.getPackageFragmentRoot(elem);
         if (initRoot == null || initRoot.isArchive())
         {
            IJavaProject jproject = elem.getJavaProject();
            if (jproject != null)
            {
               try
               {
                  initRoot = null;
                  if (jproject.exists())
                  {
                     IPackageFragmentRoot[] roots = jproject.getPackageFragmentRoots();
                     for (int i = 0; i < roots.length; i++)
                     {
                        if (roots[i].getKind() == IPackageFragmentRoot.K_SOURCE)
                        {
                           initRoot = roots[i];
                           break;
                        }
                     }
                  }
               }
               catch (JavaModelException e)
               {
                  JavaPlugin.log(e);
               }
               if (initRoot == null)
               {
                  initRoot = jproject.getPackageFragmentRoot(jproject.getResource());
               }
            }
         }
      }
      this.currRoot = initRoot;
      this.currPackage = (IPackageFragment) elem.getAncestor(IJavaElement.PACKAGE_FRAGMENT);
   }

   /** Description of the Method */
   protected abstract void initContent();

   /**
    * Description of the Method
    *
    * @param jproject                Description of the Parameter
    * @param sclassName              Description of the Parameter
    * @return                        Description of the Return Value
    * @exception JavaModelException  Description of the Exception
    */
   protected IType resolveTypeName(IJavaProject jproject, String sclassName) throws JavaModelException
   {
      if (!jproject.exists())
      {
         return null;
      }
      IType type = null;
      if (type == null && this.currPackage != null)
      {
         String packName = this.currPackage.getElementName();
         // search in own package
         if (!this.currPackage.isDefaultPackage())
         {
            type = jproject.findType(packName, sclassName);
         }
         // search in java.lang
         if (type == null && !"java.lang".equals(packName)//$NON-NLS-1$
         )
         {
            type = jproject.findType("java.lang", sclassName);//$NON-NLS-1$
         }
      }
      // search fully qualified
      if (type == null)
      {
         type = jproject.findType(sclassName);
      }
      return type;
   }

   /** Sets the focus attribute of the ClassFragmentWizardPage object */
   protected void setFocus()
   {
      this.nameDialogField.setFocus();
   }

   /**
    * Description of the Method
    *
    * @param status  Description of the Parameter
    */
   protected void updateStatus(IStatus status)
   {
      this.setPageComplete(!status.matches(IStatus.ERROR));
      if (this.isCurrentPage())
      {
         StatusUtil.applyToStatusLine(this, status);
      }
   }
}
