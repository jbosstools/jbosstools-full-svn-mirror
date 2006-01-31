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
package org.jboss.ide.eclipse.jdt.aop.ui.wizards;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.ui.util.CoreUtility;
import org.eclipse.jdt.internal.ui.wizards.JavaProjectWizardSecondPage;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.jboss.ide.eclipse.jdt.aop.core.AopCorePlugin;
import org.jboss.ide.eclipse.jdt.aop.core.classpath.AopJdk14ClasspathContainer;
import org.jboss.ide.eclipse.jdt.aop.core.classpath.AopJdk15ClasspathContainer;
import org.jboss.ide.eclipse.jdt.aop.core.project.AopProjectNature;
import org.jboss.ide.eclipse.jdt.aop.ui.AopUiPlugin;

/**
 * @author Marshall
 * 
 * A Project wizard for creating a new Aop Project
 */
public class NewAopProjectWizard extends Wizard implements INewWizard
{
   public static final int JAVA_VERSION_14 = 14;

   public static final int JAVA_VERSION_15 = 15;

   protected WizardNewProjectCreationPage page1;

   protected int javaVersion = 0;

   public void addPages()
   {
      page1 = new WizardNewProjectCreationPage("JBossAOP Project Wizard");
      page1.setDescription("Create a JBossAOP Project in the workspace or in an external location.");
      page1.setTitle("Create a JBossAOP Project");
      addPage(page1);
   }

   public void createPageControls(Composite pageContainer)
   {
      super.createPageControls(pageContainer);
      addJavaVersionRadioButton(((Composite) page1.getControl()));
   }

   private void addJavaVersionRadioButton(Composite parent)
   {
      Group versionGroup = new Group(parent, SWT.NONE);
      GridLayout layout = new GridLayout();
      layout.numColumns = 2;
      versionGroup.setLayout(layout);
      versionGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
      versionGroup.setText("Java Version");

      final Button java14 = new Button(versionGroup, SWT.RADIO | SWT.RIGHT);
      final Button java15 = new Button(versionGroup, SWT.RADIO | SWT.RIGHT);
      java14.setText("Java 1.4");
      java15.setText("Java 1.5");

      class JavaVersionRadioButtonListener implements SelectionListener
      {
         private int myVersion;

         private Button myButton;

         public JavaVersionRadioButtonListener(int version, Button button)
         {
            myButton = button;
            myVersion = version;
         }

         public void widgetSelected(SelectionEvent e)
         {
            if (myButton.getSelection())
               javaVersion = myVersion;
         }

         public void widgetDefaultSelected(SelectionEvent e)
         {
            widgetSelected(e);
         }
      }

      java14.addSelectionListener(new JavaVersionRadioButtonListener(JAVA_VERSION_14, java14));
      java15.addSelectionListener(new JavaVersionRadioButtonListener(JAVA_VERSION_15, java15));

      // default to 1.4
      java14.setSelection(true);
      javaVersion = JAVA_VERSION_14;

   }

   private ArrayList findSourcePaths(IProject project) throws CoreException
   {
      final ArrayList sourcePaths = new ArrayList();
      project.accept(new IResourceVisitor()
      {
         public boolean visit(IResource resource) throws CoreException
         {
            if (resource.getType() == IResource.FILE && resource.getFileExtension() != null
                  && resource.getFileExtension().equals("java"))
            {
               IPath dir = resource.getFullPath().removeLastSegments(1);
               if (!sourcePaths.contains(dir))
               {
                  sourcePaths.add(dir);
               }
            }
            return true;
         }
      });

      return sourcePaths;
   }

   private class OutputLocationFinder implements IResourceVisitor
   {
      private IPath outputLocation;

      public OutputLocationFinder()
      {
         outputLocation = null;
      }

      public IPath getOuptutLocation()
      {
         return outputLocation;
      }

      public boolean visit(IResource resource) throws CoreException
      {
         if (resource.getType() == IResource.FILE && resource.getFileExtension().equals("class"))
         {
            outputLocation = resource.getFullPath().removeLastSegments(1);
            return false;
         }
         return true;
      }
   }

   private IPath findOutputLocation(IProject project) throws CoreException
   {
      OutputLocationFinder finder = new OutputLocationFinder();
      project.accept(finder);

      return finder.getOuptutLocation();
   }

   private void createSrcAndBin(IJavaProject project, IProgressMonitor monitor) throws CoreException
   {
      IPath srcLocation = project.getProject().getFullPath().append(new Path("/src"));
      IPath outputLocation = project.getProject().getFullPath().append(new Path("/bin"));

      createFolder(srcLocation, monitor);
      createFolder(outputLocation, monitor);

      ArrayList entries = new ArrayList();
      entries.add(JavaCore.newSourceEntry(srcLocation));

      project.setRawClasspath(configureClasspathEntries(entries), outputLocation, monitor);
   }

   private void createFolder(IPath location, IProgressMonitor monitor) throws CoreException
   {
      if (!ResourcesPlugin.getWorkspace().getRoot().exists(location))
      {
         IFolder folder = ResourcesPlugin.getWorkspace().getRoot().getFolder(location);
         CoreUtility.createFolder(folder, true, true, monitor);
      }
   }

   private IClasspathEntry[] configureClasspathEntries(ArrayList classpathEntries) throws CoreException
   {
      classpathEntries.add(JavaRuntime.getDefaultJREContainerEntry());
      if (javaVersion == JAVA_VERSION_15)
      {
         classpathEntries.add(JavaCore.newContainerEntry(new Path(AopJdk15ClasspathContainer.CONTAINER_ID)));
      }
      else
      {
         classpathEntries.add(JavaCore.newContainerEntry(new Path(AopJdk14ClasspathContainer.CONTAINER_ID)));
      }

      return (IClasspathEntry[]) classpathEntries.toArray(new IClasspathEntry[classpathEntries.size()]);
   }

   public boolean performFinish()
   {
      IRunnableWithProgress op = new IRunnableWithProgress()
      {
         public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
         {
            try
            {
               monitor.beginTask("Applying natures..", 2);
               IProject project = page1.getProjectHandle();

               JavaProjectWizardSecondPage.createProject(project, page1.getLocationPath(), monitor);
               AopCorePlugin.addProjectNature(project, JavaCore.NATURE_ID);
               monitor.worked(1);

               IJavaProject javaProject = JavaCore.create(project);
               AopProjectNature.ensureAopProjectNature(javaProject);
               monitor.worked(1);

               ArrayList sourcePaths = findSourcePaths(project);
               IPath outputLocation = findOutputLocation(project);

               if (sourcePaths.size() > 0)
               {
                  ArrayList cpEntries = new ArrayList();
                  for (Iterator iter = sourcePaths.iterator(); iter.hasNext();)
                  {
                     IPath sourcePath = (IPath) iter.next();
                     cpEntries.add(JavaCore.newSourceEntry(sourcePath));
                  }

                  if (outputLocation == null)
                  {
                     AopUiPlugin.alert("While importing existing source, JBossIDE was unable to find"
                           + " a location to place compiled java class files. The directory \"bin\" will"
                           + " be created under the project directory and used as an output location instead.");

                     outputLocation = project.getProject().getFullPath().append(new Path("/bin"));
                     createFolder(outputLocation, monitor);
                  }

                  javaProject.setRawClasspath(configureClasspathEntries(cpEntries), outputLocation, monitor);
               }
               else
               {
                  createSrcAndBin(javaProject, monitor);
               }
            }
            catch (CoreException e)
            {
               e.printStackTrace();
            }

            monitor.done();
         }
      };

      try
      {
         new ProgressMonitorDialog(getShell()).run(false, true, op);
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }

      return true;
   }

   public void init(IWorkbench workbench, IStructuredSelection selection)
   {
   }

}
