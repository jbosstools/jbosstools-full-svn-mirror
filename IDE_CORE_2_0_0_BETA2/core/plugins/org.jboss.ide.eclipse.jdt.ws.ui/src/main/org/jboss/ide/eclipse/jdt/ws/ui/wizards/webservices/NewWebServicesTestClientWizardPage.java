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
package org.jboss.ide.eclipse.jdt.ws.ui.wizards.webservices;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.dialogs.StatusInfo;
import org.eclipse.jdt.internal.ui.dialogs.StatusUtil;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.LayoutUtil;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.SelectionButtonDialogFieldGroup;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringDialogField;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.core.util.NameValuePair;
import org.jboss.ide.eclipse.core.util.ProjectUtil;
import org.jboss.ide.eclipse.jdt.core.classpath.ClassPathContainerRepository;
import org.jboss.ide.eclipse.jdt.core.util.JavaProjectUtil;
import org.jboss.ide.eclipse.jdt.core.wizards.generation.IGenerationEngine;
import org.jboss.ide.eclipse.jdt.test.core.classpath.JUnitClasspathContainer;
import org.jboss.ide.eclipse.jdt.ui.wizards.ClassWizardPage;
import org.jboss.ide.eclipse.jdt.ui.wizards.util.FieldsUtil;
import org.jboss.ide.eclipse.jdt.ws.core.classpath.Axis12ClasspathContainer;
import org.jboss.ide.eclipse.jdt.ws.core.classpath.WSIBasicProfile10ClasspathContainer;
import org.jboss.ide.eclipse.jdt.ws.core.generation.WSDL2JavaGenerationEngine;
import org.jboss.ide.eclipse.jdt.ws.ui.JDTWSUIMessages;
import org.jboss.ide.eclipse.jdt.ws.ui.JDTWSUIPlugin;
import org.jboss.ide.eclipse.jdt.ws.ui.dialog.MappingEditDialog;
import org.jboss.ide.eclipse.ui.dialogs.FileSelectionDialog;
import org.jboss.ide.eclipse.ui.util.ListContentProvider;
import org.jboss.ide.eclipse.ui.util.ProjectContentProvider;
import org.jboss.ide.eclipse.ui.util.ProjectLabelProvider;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class NewWebServicesTestClientWizardPage extends ClassWizardPage
{
   /** Description of the Field */
   protected List mappings = new Vector();

   /** Description of the Field */
   protected StructuredViewer mappingsViewer;

   /** Description of the Field */
   protected SelectionButtonDialogFieldGroup optionsButtons;

   /** Description of the Field */
   protected StringDialogField passwordField;

   /** Description of the Field */
   protected SelectionButtonDialogFieldGroup typeVersionButtons;

   /** Description of the Field */
   protected StringDialogField usernameField;

   /** Description of the Field */
   protected Button wsdlExternalLocationBrowseButton;

   /** Description of the Field */
   protected Text wsdlLocationField;

   /** Description of the Field */
   protected StatusInfo wsdlStatus = new StatusInfo();

   /** Description of the Field */
   protected Button wsdlWorkspaceLocationBrowseButton;

   private IStructuredSelection selection;

   private final static String PAGE_NAME = NewWebServicesTestClientWizardPage.class.getName();

   /**
    *Constructor for the NewWebServicesTestClientWizardPage object
    *
    * @param root  Description of the Parameter
    */
   public NewWebServicesTestClientWizardPage(IWorkspaceRoot root)
   {
      super(true, PAGE_NAME);
      this.setTitle(JDTWSUIMessages.getString("NewWebServicesTestClientWizardPage.title"));//$NON-NLS-1$
      this.setDescription(JDTWSUIMessages.getString("NewWebServicesTestClientWizardPage.description"));//$NON-NLS-1$
   }

   /**
    * Description of the Method
    *
    * @param engine   Description of the Parameter
    * @param monitor  Description of the Parameter
    */
   public void generate(IGenerationEngine engine, IProgressMonitor monitor)
   {
      // Add the Axis classpath container to the project
      try
      {
         IJavaProject project = getPackageFragmentRoot().getJavaProject();
         IClasspathEntry[] entries = project.getRawClasspath();

         entries = JavaProjectUtil.mergeClasspathEntry(entries, ClassPathContainerRepository.getInstance().getEntry(
               Axis12ClasspathContainer.CLASSPATH_CONTAINER));
         entries = JavaProjectUtil.mergeClasspathEntry(entries, ClassPathContainerRepository.getInstance().getEntry(
               WSIBasicProfile10ClasspathContainer.CLASSPATH_CONTAINER));
         if (this.mustGenerateTestCase())
         {
            entries = JavaProjectUtil.mergeClasspathEntry(entries, ClassPathContainerRepository.getInstance().getEntry(
                  JUnitClasspathContainer.CLASSPATH_CONTAINER));
         }

         project.setRawClasspath(entries, monitor);
      }
      catch (JavaModelException jme)
      {
         AbstractPlugin.logError("Unable to compute classpath", jme);//$NON-NLS-1$
         JDTWSUIPlugin.getDefault().showErrorMessage(
               JDTWSUIMessages.getString("NewWebServicesTestClientWizardPage.error.classpath.junit"));//$NON-NLS-1$
         return;
      }
      catch (CoreException ce)
      {
         AbstractPlugin.logError("Unable to merge classpath entries", ce);//$NON-NLS-1$
         JDTWSUIPlugin.getDefault().showErrorMessage(
               JDTWSUIMessages.getString("NewWebServicesTestClientWizardPage.error.classpath.axis"));//$NON-NLS-1$
         return;
      }

      // Create the generation engine
      WSDL2JavaGenerationEngine wsEngine = (WSDL2JavaGenerationEngine) engine;

      wsEngine.setWSDL(this.wsdlLocationField.getText());
      wsEngine.setUsername(this.usernameField.getText());
      wsEngine.setPassword(this.passwordField.getText());

      if (!this.getPackageText().equals("")//$NON-NLS-1$
      )
      {

         wsEngine.setPackageName(this.getPackageText());
      }
      wsEngine.setOutputDir(this.getOutputLocation().getLocation().toString());

      wsEngine.setTypeMappingVersion(this.getTypeVersion());

      wsEngine.setAllWanted(this.optionsButtons.isSelected(2));
      wsEngine.setHelperWanted(this.optionsButtons.isSelected(3));
      wsEngine.setImports(!this.optionsButtons.isSelected(0));
      wsEngine.setNowrap(!this.optionsButtons.isSelected(1));
      wsEngine.setTestCaseWanted(this.mustGenerateTestCase());

      wsEngine.setNamespaceMap(null);

      try
      {
         wsEngine.generate(null, monitor);

         IResource sourceRoot = getPackageFragmentRoot().getResource();
         sourceRoot.refreshLocal(IResource.DEPTH_INFINITE, monitor);
      }
      catch (CoreException ce)
      {
         AbstractPlugin.logError("Error while refreshing workspace", ce);//$NON-NLS-1$
      }
      catch (Exception e)
      {
         AbstractPlugin.logError("Error while creating Web Services Test Client", e);//$NON-NLS-1$
         JDTWSUIPlugin.getDefault().showErrorMessage(
               JDTWSUIMessages.getString("NewWebServicesTestClientWizardPage.error.generation"));//$NON-NLS-1$
      }
   }

   /**
    * Gets the mappings attribute of the NewWebServicesTestClientWizardPage object
    *
    * @return   The mappings value
    */
   public Map getMappings()
   {
      Map result = new HashMap();
      for (int i = 0; i < this.mappings.size(); i++)
      {
         NameValuePair pair = (NameValuePair) this.mappings.get(i);
         result.put(pair.getName(), pair.getValue());
      }
      return result;
   }

   /**
    * Gets the outputLocation attribute of the NewWebServicesTestClientWizardPage object
    *
    * @return   The outputLocation value
    */
   public IResource getOutputLocation()
   {
      return this.getPackageFragmentRoot().getResource();
   }

   /**
    * Gets the typeVersion attribute of the NewWebServicesTestClientWizardPage object
    *
    * @return   The typeVersion value
    */
   public String getTypeVersion()
   {
      String result = "1.1";//$NON-NLS-1$
      if (this.typeVersionButtons.isSelected(1))
      {
         result = "1.2";//$NON-NLS-1$
      }
      return result;
   }

   /**
    * Description of the Method
    *
    * @param selection  Description of the Parameter
    */
   public void init(IStructuredSelection selection)
   {
      super.init(selection);
      this.selection = selection;
   }

   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public boolean mustGenerateTestCase()
   {
      return this.optionsButtons.isSelected(4);
   }

   /** Adds a feature to the SelectExternal attribute of the NewWebServicesTestClientWizardPage object */
   protected void addSelectExternal()
   {
      FileDialog dialog = new FileDialog(AbstractPlugin.getShell(), SWT.OPEN);
      String result = dialog.open();
      if (result != null)
      {
         IPath path = new Path(result);
         this.wsdlLocationField.setText(path.toString());
      }
      this.validateWSDLLocation();
   }

   /** Adds a feature to the SelectLocal attribute of the NewWebServicesTestClientWizardPage object */
   protected void addSelectLocal()
   {
      FileSelectionDialog dialog = new FileSelectionDialog(AbstractPlugin.getShell(), new ProjectLabelProvider(),
            new ProjectContentProvider());

      // Select all projects as input
      dialog.setInput(ProjectUtil.getAllOpenedProjects());

      // If there is a selection, use it
      if (this.selection != null)
      {
         //         dialog.setInitialSelection(this.selection);
      }

      // Open the dialog and wait for the result
      if (dialog.open() == IDialogConstants.OK_ID)
      {
         IResource resource = (IResource) dialog.getFirstResult();
         this.wsdlLocationField.setText(resource.getLocation().toString());
      }
      this.validateWSDLLocation();
   }

   /** Description of the Method */
   protected void createContent()
   {
      String[] buttonNames;

      buttonNames = new String[]
      {
            JDTWSUIMessages.getString("NewWebServicesTestClientWizardPage.label.version.11"), JDTWSUIMessages.getString("NewWebServicesTestClientWizardPage.label.version.12")};//$NON-NLS-1$ //$NON-NLS-2$
      this.typeVersionButtons = new SelectionButtonDialogFieldGroup(SWT.RADIO, buttonNames, 2);
      this.typeVersionButtons.setLabelText(JDTWSUIMessages
            .getString("NewWebServicesTestClientWizardPage.label.version"));//$NON-NLS-1$

      buttonNames = new String[]
      {
            JDTWSUIMessages.getString("NewWebServicesTestClientWizardPage.label.options.import"), JDTWSUIMessages.getString("NewWebServicesTestClientWizardPage.label.options.unwrap"), JDTWSUIMessages.getString("NewWebServicesTestClientWizardPage.label.options.unreferenced"), JDTWSUIMessages.getString("NewWebServicesTestClientWizardPage.label.options.helper"), JDTWSUIMessages.getString("NewWebServicesTestClientWizardPage.label.options.junit")};//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
      this.optionsButtons = new SelectionButtonDialogFieldGroup(SWT.CHECK, buttonNames, 2);
      this.optionsButtons.setLabelText(JDTWSUIMessages.getString("NewWebServicesTestClientWizardPage.label.options"));//$NON-NLS-1$

      this.usernameField = new StringDialogField();
      this.usernameField.setLabelText(JDTWSUIMessages.getString("NewWebServicesTestClientWizardPage.label.username"));//$NON-NLS-1$

      this.passwordField = new StringDialogField();
      this.passwordField.setLabelText(JDTWSUIMessages.getString("NewWebServicesTestClientWizardPage.label.password"));//$NON-NLS-1$
   }

   /**
    * Description of the Method
    *
    * @param composite  Description of the Parameter
    * @param nColumns   Description of the Parameter
    */
   protected void createControls(Composite composite, int nColumns)
   {
      this.createWSDLControls(composite, nColumns);
      FieldsUtil.createLabelControls(
            JDTWSUIMessages.getString("NewWebServicesTestClientWizardPage.label.credentials"), composite, nColumns);//$NON-NLS-1$
      this.createSeparator(composite, nColumns);
      this.createContainerControls(composite, nColumns);
      this.createPackageControls(composite, nColumns);
      this.createSeparator(composite, nColumns);
      this.createMappingsControls(composite, nColumns);
      this.createSeparator(composite, nColumns);
      FieldsUtil.createSelectionButtonDialogFieldGroupControls(this.typeVersionButtons, composite, nColumns);
      FieldsUtil.createSelectionButtonDialogFieldGroupControls(this.optionsButtons, composite, nColumns);

      this.validateWSDLLocation();
   }

   /**
    * Description of the Method
    *
    * @param composite  Description of the Parameter
    * @param nColumns   Description of the Parameter
    */
   protected void createMappingsControls(Composite composite, int nColumns)
   {
      Label lbl = new Label(composite, SWT.NONE);
      lbl.setText(JDTWSUIMessages.getString("NewWebServicesTestClientWizardPage.label.namespace"));//$NON-NLS-1$
      lbl.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));

      Table table = new Table(composite, SWT.BORDER | SWT.SINGLE | SWT.FULL_SELECTION | SWT.V_SCROLL);
      GridData layoutData = new GridData(GridData.FILL_BOTH);
      table.setLayoutData(layoutData);

      LayoutUtil.setHorizontalSpan(table, nColumns - 2);

      this.mappingsViewer = new TableViewer(table);
      this.mappingsViewer.setContentProvider(new ListContentProvider());
      this.mappingsViewer.setLabelProvider(new MappingLabelProvider());
      this.mappingsViewer.setInput(this.mappings);

      Composite buttonComposite = new Composite(composite, SWT.NONE);
      GridLayout layout = new GridLayout(1, true);
      layout.marginHeight = 0;
      layout.marginWidth = 0;
      buttonComposite.setLayout(layout);
      buttonComposite.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));

      Button addButton = new Button(buttonComposite, SWT.PUSH);
      addButton.setText(JDTWSUIMessages.getString("NewWebServicesTestClientWizardPage.button.add"));//$NON-NLS-1$
      addButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

      addButton.addSelectionListener(new SelectionAdapter()
      {
         public void widgetSelected(SelectionEvent e)
         {
            doAddMapping();
         }
      });

      Button removeButton = new Button(buttonComposite, SWT.PUSH);
      removeButton.setText(JDTWSUIMessages.getString("NewWebServicesTestClientWizardPage.button.remove"));//$NON-NLS-1$
      removeButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

      removeButton.addSelectionListener(new SelectionAdapter()
      {
         public void widgetSelected(SelectionEvent e)
         {
            doRemoveMapping();
         }
      });
   }

   /**
    * Description of the Method
    *
    * @param nColumns   Description of the Parameter
    * @param composite  Description of the Parameter
    */
   protected void createWSDLControls(Composite composite, int nColumns)
   {
      Label label = new Label(composite, SWT.NONE);
      label.setText(JDTWSUIMessages.getString("NewWebServicesTestClientWizardPage.label.location.hint"));//$NON-NLS-1$
      GridData data = new GridData(GridData.FILL_HORIZONTAL);
      data.horizontalSpan = nColumns;
      label.setLayoutData(data);

      label = new Label(composite, SWT.NONE);
      label.setText(JDTWSUIMessages.getString("NewWebServicesTestClientWizardPage.label.wsdl"));//$NON-NLS-1$

      // Location entry field
      wsdlLocationField = new Text(composite, SWT.BORDER);
      data = new GridData(GridData.FILL_HORIZONTAL);
      data.horizontalSpan = nColumns - 2;
      wsdlLocationField.setLayoutData(data);
      wsdlLocationField.addModifyListener(new ModifyListener()
      {
         public void modifyText(ModifyEvent e)
         {
            validateWSDLLocation();
         }
      });

      // Browse button
      this.wsdlWorkspaceLocationBrowseButton = new Button(composite, SWT.PUSH);
      this.wsdlWorkspaceLocationBrowseButton.setText(JDTWSUIMessages
            .getString("NewWebServicesTestClientWizardPage.button.browse.workspace"));//$NON-NLS-1$
      data = new GridData(GridData.FILL_HORIZONTAL);
      this.wsdlWorkspaceLocationBrowseButton.setLayoutData(data);
      this.wsdlWorkspaceLocationBrowseButton.addSelectionListener(new SelectionAdapter()
      {
         public void widgetSelected(SelectionEvent event)
         {
            addSelectLocal();
         }
      });

      label = new Label(composite, SWT.NONE);
      label.setText("");//$NON-NLS-1$
      data = new GridData(GridData.FILL_HORIZONTAL);
      data.horizontalSpan = nColumns - 1;
      label.setLayoutData(data);

      // Browse button
      this.wsdlExternalLocationBrowseButton = new Button(composite, SWT.PUSH);
      this.wsdlExternalLocationBrowseButton.setText(JDTWSUIMessages
            .getString("NewWebServicesTestClientWizardPage.button.browse.external"));//$NON-NLS-1$
      data = new GridData(GridData.FILL_HORIZONTAL);
      this.wsdlExternalLocationBrowseButton.setLayoutData(data);
      this.wsdlExternalLocationBrowseButton.addSelectionListener(new SelectionAdapter()
      {
         public void widgetSelected(SelectionEvent event)
         {
            addSelectExternal();
         }
      });

      this.initWsdlFile(this.selection);
   }

   /** Description of the Method */
   protected void doAddMapping()
   {
      MappingEditDialog dialog = new MappingEditDialog(AbstractPlugin.getShell());
      if (dialog.open() == IDialogConstants.OK_ID)
      {
         String name = dialog.getName();
         String type = dialog.getPakkage();
         this.mappings.add(new NameValuePair(name, type));
         this.mappingsViewer.refresh();
      }
   }

   /** Description of the Method */
   protected void doRemoveMapping()
   {
      ISelection selection = this.mappingsViewer.getSelection();
      if (selection != null)
      {
         Object o = ((IStructuredSelection) selection).getFirstElement();
         this.mappings.remove(o);
         this.mappingsViewer.refresh();
      }
   }

   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   protected IStatus findMostSevereStatus()
   {
      return StatusUtil.getMostSevere(new IStatus[]
      {this.fContainerStatus, this.wsdlStatus});
   }

   /** Description of the Method */
   protected void initContent()
   {
      // 1.1 type
      this.typeVersionButtons.setSelection(0, true);

      // Check basic options
      this.optionsButtons.setSelection(0, true);
      this.optionsButtons.setSelection(1, true);
   }

   /**
    * Description of the Method
    *
    * @param selection  Description of the Parameter
    */
   protected void initWsdlFile(IStructuredSelection selection)
   {
      if (selection == null)
      {
         return;
      }
      IAdaptable adaptable = (IAdaptable) selection.getFirstElement();
      IResource resource = (IResource) adaptable.getAdapter(IResource.class);
      if (resource != null && resource.getType() == IResource.FILE && resource.getFileExtension().equals("wsdl")//$NON-NLS-1$
      )
      {

         this.wsdlLocationField.setText(resource.getLocation().toOSString());
      }
   }

   /** Sets the focus attribute of the NewWebServicesTestClientWizardPage object */
   protected void setFocus()
   {
      this.wsdlLocationField.setFocus();
   }

   /** Description of the Method */
   protected void validateWSDLLocation()
   {
      String wsdl = this.wsdlLocationField.getText();
      if (wsdl == null || wsdl.length() < 1)
      {
         wsdlStatus.setError(JDTWSUIMessages.getString("NewWebServicesTestClientWizardPage.error.wsdl.unspecified"));//$NON-NLS-1$
      }
      else
      {
         wsdlStatus.setOK();
      }
      this.updateStatus(this.findMostSevereStatus());
   }

   /**
    * Description of the Class
    *
    * @author    Laurent Etiemble
    * @version   $Revision$
    */
   private class MappingLabelProvider extends LabelProvider implements ITableLabelProvider
   {
      /**
       * Gets the columnImage attribute of the ParameterLabelProvider object
       *
       * @param element      Description of the Parameter
       * @param columnIndex  Description of the Parameter
       * @return             The columnImage value
       */
      public Image getColumnImage(Object element, int columnIndex)
      {
         return null;
      }

      /**
       * Gets the columnText attribute of the ParameterLabelProvider object
       *
       * @param element      Description of the Parameter
       * @param columnIndex  Description of the Parameter
       * @return             The columnText value
       */
      public String getColumnText(Object element, int columnIndex)
      {
         NameValuePair pair = (NameValuePair) element;
         switch (columnIndex)
         {
            case 0 :
               return pair.getName() + " [" + pair.getValue() + "]";//$NON-NLS-1$ //$NON-NLS-2$
            default :
         // Can't happen
         }
         return null;
      }
   }
}
