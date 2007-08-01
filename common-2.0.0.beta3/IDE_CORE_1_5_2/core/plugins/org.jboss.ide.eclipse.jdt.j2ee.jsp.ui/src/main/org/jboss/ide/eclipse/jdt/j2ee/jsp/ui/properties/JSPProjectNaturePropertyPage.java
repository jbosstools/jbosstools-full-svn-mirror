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
package org.jboss.ide.eclipse.jdt.j2ee.jsp.ui.properties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.PropertyPage;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.jdt.j2ee.jsp.core.JDTJ2EEJSPCorePlugin;
import org.jboss.ide.eclipse.jdt.j2ee.jsp.core.compiler.jasper.JSPProject;
import org.jboss.ide.eclipse.jdt.j2ee.jsp.core.compiler.jasper.JSPProjectManager;
import org.jboss.ide.eclipse.jdt.j2ee.jsp.ui.JDTJ2EEJSPUIMessages;
import org.jboss.ide.eclipse.jdt.j2ee.jsp.ui.decorators.JSPNatureDecorator;
import org.jboss.ide.eclipse.ui.dialogs.FolderSelectionDialog;
import org.jboss.ide.eclipse.ui.util.ProjectContentProvider;
import org.jboss.ide.eclipse.ui.util.ProjectLabelProvider;

/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class JSPProjectNaturePropertyPage extends PropertyPage
{
   private Button natureEnableButton;

   private Button webappRootBrowseButton;

   private Text webappRootText;

   /**Constructor for the JSPProjectNaturePropertyPage object */
   public JSPProjectNaturePropertyPage()
   {
   }

   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public boolean performOk()
   {
      this.doAssignNature();

      final IProject p = this.getProject();
      Job job = new Job(JDTJ2EEJSPUIMessages.getString("JSPProjectNaturePropertyPage.title")//$NON-NLS-1$
      )
      {

         protected IStatus run(IProgressMonitor monitor)
         {
            try
            {
               p.build(IncrementalProjectBuilder.FULL_BUILD, JDTJ2EEJSPCorePlugin.JSP_BUILDER_ID, null, monitor);
            }
            catch (CoreException e)
            {
               // Do nothing
            }
            return Status.OK_STATUS;
         }
      };
      job.setPriority(Job.BUILD);
      job.schedule();

      return super.performOk();
   }

   /**
    * Description of the Method
    *
    * @param ancestor  Description of the Parameter
    * @return          Description of the Return Value
    */
   protected Control createContents(Composite ancestor)
   {
      GridData layoutData;
      Label description;
      Composite parent = new Composite(ancestor, SWT.NONE);

      GridLayout layout = new GridLayout(3, false);
      parent.setLayout(layout);

      // Description label for the page
      description = new Label(parent, SWT.NONE);
      description.setText("This page allows to switch the JSP compilation for this Java Project");//$NON-NLS-1$
      layoutData = new GridData(GridData.FILL_HORIZONTAL);
      layoutData.horizontalSpan = 3;
      description.setLayoutData(layoutData);

      // Checkbox to enable/disable the JSP nature
      this.natureEnableButton = new Button(parent, SWT.CHECK);
      this.natureEnableButton.setText("Check to enable the JSP compilation");//$NON-NLS-1$
      this.natureEnableButton.setSelection(this.hasNature());
      layoutData = new GridData(GridData.FILL_HORIZONTAL);
      layoutData.horizontalSpan = 3;
      this.natureEnableButton.setLayoutData(layoutData);

      // Description label for WebApp
      description = new Label(parent, SWT.NONE);
      description.setText("Web Application Root folder:");//$NON-NLS-1$

      this.webappRootText = new Text(parent, SWT.BORDER);
      String webapp = JSPProjectManager.getPropertyFromWorkspace(this.getProject(), JSPProjectManager.QNAME_WEBROOT);
      if (webapp != null)
      {
         this.webappRootText.setText(webapp);
      }
      this.webappRootText.setEditable(false);
      layoutData = new GridData(GridData.FILL_HORIZONTAL);
      layoutData.grabExcessHorizontalSpace = true;
      this.webappRootText.setLayoutData(layoutData);

      this.webappRootBrowseButton = new Button(parent, SWT.PUSH);
      this.webappRootBrowseButton.setText(JDTJ2EEJSPUIMessages.getString("JSPProjectNaturePropertyPage.button.browse"));//$NON-NLS-1$
      layoutData = new GridData(GridData.GRAB_HORIZONTAL);
      this.webappRootBrowseButton.setLayoutData(layoutData);

      this.webappRootBrowseButton.addSelectionListener(new SelectionAdapter()
      {
         public void widgetSelected(SelectionEvent e)
         {
            assignLocation();
         }
      });

      return parent;
   }

   /** Description of the Method */
   protected void performApply()
   {
      this.doAssignNature();
   }

   /** Adds a feature to the Nature attribute of the JSPProjectNaturePropertyPage object */
   private void addNature()
   {
      if (!hasNature())
      {
         try
         {
            IProject project = this.getProject();

            IProjectDescription description = project.getDescription();
            String[] natures = description.getNatureIds();
            String[] newNatures = new String[natures.length + 1];
            System.arraycopy(natures, 0, newNatures, 0, natures.length);
            newNatures[natures.length] = JDTJ2EEJSPCorePlugin.JSP_NATURE_ID;

            description.setNatureIds(newNatures);
            project.setDescription(description, new NullProgressMonitor());
         }
         catch (CoreException e)
         {
            // Something went wrong
         }
      }
   }

   /** Description of the Method */
   private void assignLocation()
   {
      FolderSelectionDialog dialog = new FolderSelectionDialog(AbstractPlugin.getShell(), new ProjectLabelProvider(),
            new ProjectContentProvider());
      dialog.setAcceptFolderOnly(true);

      // Select all projects as input
      List projects = new ArrayList();
      projects.add(this.getProject());
      dialog.setInput(projects);

      // If there is a selection, use it
      String webApp = this.webappRootText.getText();
      if (!"".equals(webApp)//$NON-NLS-1$
      )
      {

         IFolder folder = this.getProject().getFolder(webApp);
         if (folder.exists())
         {
            dialog.setInitialSelection(folder);
         }
      }

      // Open the dialog and wait for the result
      if (dialog.open() == IDialogConstants.OK_ID)
      {
         IResource resource = (IResource) dialog.getFirstResult();
         if (resource instanceof IFolder)
         {
            IFolder folder = (IFolder) resource;
            this.webappRootText.setText(folder.getProjectRelativePath().toString());
         }
      }
   }

   /** Description of the Method */
   private void doAssignNature()
   {
      JSPProject jspProject = JSPProjectManager.getJSPProject(this.getProject());

      boolean enable = this.natureEnableButton.getSelection();
      String webapp = this.webappRootText.getText();
      if (!enable || "".equals(webapp)//$NON-NLS-1$
      )
      {
         webapp = null;
      }

      JSPProjectManager.setPropertyToWorkspace(this.getProject(), JSPProjectManager.QNAME_WEBROOT, webapp);

      // Remove all the decoration
      JSPNatureDecorator decorator = JSPNatureDecorator.getDeployedDecorator();
      if (decorator != null)
         decorator.refresh(jspProject.getUriRootFolder());

      // Reset the project
      jspProject.reset();

      // Refresh decoration
      if (decorator != null)
         decorator.refresh(jspProject.getUriRootFolder());

      if (enable)
      {
         this.addNature();
      }
      else
      {
         this.removeNature();
      }
   }

   /**
    * Gets the project attribute of the JSPProjectNaturePropertyPage object
    *
    * @return   The project value
    */
   private IProject getProject()
   {
      IProject project = (IProject) (this.getElement().getAdapter(IProject.class));
      return project;
   }

   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   private boolean hasNature()
   {
      boolean value = false;
      try
      {
         IProject project = this.getProject();
         value = project.hasNature(JDTJ2EEJSPCorePlugin.JSP_NATURE_ID);
      }
      catch (CoreException ce)
      {
      }
      return value;
   }

   /** Description of the Method */
   private void removeNature()
   {
      if (hasNature())
      {
         try
         {
            IProject project = this.getProject();

            IProjectDescription description = project.getDescription();
            String[] natures = description.getNatureIds();

            List newNatures = new ArrayList(Arrays.asList(natures));
            newNatures.remove(JDTJ2EEJSPCorePlugin.JSP_NATURE_ID);

            description.setNatureIds((String[]) newNatures.toArray(new String[newNatures.size()]));
            project.setDescription(description, new NullProgressMonitor());
         }
         catch (CoreException e)
         {
            // Something went wrong
         }
      }
   }

}
