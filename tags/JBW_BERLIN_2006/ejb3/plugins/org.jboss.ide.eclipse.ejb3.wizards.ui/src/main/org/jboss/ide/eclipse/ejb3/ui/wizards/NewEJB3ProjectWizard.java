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
package org.jboss.ide.eclipse.ejb3.ui.wizards;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.core.internal.resources.ResourceException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.util.CoreUtility;
import org.eclipse.jdt.internal.ui.wizards.JavaProjectWizardSecondPage;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.jboss.ide.eclipse.ejb3.core.classpath.EJB3ClasspathContainer;
import org.jboss.ide.eclipse.ejb3.ui.EJB3WizardsUIPlugin;
import org.jboss.ide.eclipse.ejb3.ui.wizards.pages.JBossEJB3LibrariesPage;
import org.jboss.ide.eclipse.jdt.aop.core.AopCorePlugin;
import org.jboss.ide.eclipse.jdt.aop.core.classpath.AopJdk15ClasspathContainer;

/**
 * @author Marshall
 */
public class NewEJB3ProjectWizard extends Wizard implements INewWizard
{

   protected WizardNewProjectCreationPage page1;

   protected JBossEJB3LibrariesPage page2;

   public void addPages()
   {
      page1 = new WizardNewProjectCreationPage("EJB3 Project Wizard");
      page1.setDescription("Create a EJB 3.0 Project in the workspace or in an external location.");
      page1.setTitle("Create a EJB3 Project");

      page2 = new JBossEJB3LibrariesPage();
      page2.setDescription("Select a JBoss configuration that has the JBoss EJB3 container installed.");
      page2.setTitle("Select a JBoss configuration");
      addPage(page1);
      addPage(page2);
   }

   private ArrayList findSourcePaths(IProject project) throws CoreException
   {
      final ArrayList sourcePaths = new ArrayList();
      project.accept(new IResourceVisitor()
      {
         public boolean visit(IResource resource) throws CoreException
         {
            if (resource.getType() == IResource.FILE && resource.getFileExtension().equals("java"))
            {
               IPath dir = resource.getFullPath().removeLastSegments(1);

               for (int i = 0; i < dir.segmentCount(); i++)
               {
                  if (dir.segment(i).equalsIgnoreCase("org") || dir.segment(i).equalsIgnoreCase("net")
                        || dir.segment(i).equalsIgnoreCase("com") || dir.segment(i).equalsIgnoreCase("edu"))
                  {
                     dir = dir.uptoSegment(i - 1);
                     break;
                  }
               }
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

      createJndiProperties(srcLocation);
      project.setRawClasspath(configureClasspathEntries(entries), outputLocation, monitor);
   }

   private void createFolder(IPath location, IProgressMonitor monitor) throws CoreException
   {
      if (!ResourcesPlugin.getWorkspace().getRoot().exists(location))
      {
         IFolder folder = ResourcesPlugin.getWorkspace().getRoot().getFolder(location);
         try
         {
            CoreUtility.createFolder(folder, true, true, monitor);
         }
         catch (ResourceException e)
         {

         }
      }
   }

   private IClasspathEntry[] getDefaultClasspathEntries() throws CoreException
   {
      return new IClasspathEntry[]
      {JavaRuntime.getDefaultJREContainerEntry()};
   }

   private IClasspathEntry[] configureClasspathEntries(ArrayList classpathEntries) throws CoreException
   {
      IClasspathEntry defaultEntries[] = getDefaultClasspathEntries();
      for (int i = 0; i < defaultEntries.length; i++)
      {
         classpathEntries.add(defaultEntries[i]);
      }

      classpathEntries.add(JavaCore.newContainerEntry(new Path(AopJdk15ClasspathContainer.CONTAINER_ID)));
      classpathEntries.add(JavaCore.newContainerEntry(new Path(EJB3ClasspathContainer.CONTAINER_ID).append(page2
            .getServer().getServer().getName())));

      return (IClasspathEntry[]) classpathEntries.toArray(new IClasspathEntry[classpathEntries.size()]);
   }

   private void createJndiProperties(IPath srcPath) throws CoreException
   {
      IPath jndiPath = srcPath.append("jndi.properties");
      IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(jndiPath);

      String jndiProps = "java.naming.factory.initial=org.jnp.interfaces.NamingContextFactory\n"
            + "java.naming.factory.url.pkgs=org.jboss.naming:org.jnp.interfaces\n"
            + "java.naming.provider.url=localhost:1099\n";

      file.create(new ByteArrayInputStream(jndiProps.getBytes()), true, new NullProgressMonitor());
   }

   private class JndiPropertiesFileFilter implements FileFilter
   {
      public boolean accept(File file)
      {
         return (file.getName().equals("jndi.properties"));
      }
   }

   private boolean is15Classpath(IJavaProject javaProject)
   {
      try
      {
         return javaProject.findType("java.lang.Enum") != null; //$NON-NLS-1$
      }
      catch (JavaModelException e)
      {
         // ignore
         return false;
      }
   }

   public boolean performFinish()
   {
      IRunnableWithProgress op = new IRunnableWithProgress()
      {
         public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
         {
            try
            {
               monitor.beginTask("Configuring EJB3 Classpath...", 1);
               boolean page2Finished = page2.finish();

               if (!page2Finished)
               {
                  throw new InterruptedException();
               }

               monitor.worked(1);

               monitor.beginTask("Applying natures..", 2);
               IProject project = page1.getProjectHandle();

               JavaProjectWizardSecondPage.createProject(project, page1.getLocationPath(), monitor);
               AopCorePlugin.addProjectNature(project, JavaCore.NATURE_ID);
               monitor.worked(1);

               IJavaProject javaProject = JavaCore.create(project);
               javaProject.setRawClasspath(getDefaultClasspathEntries(), monitor);
               monitor.worked(1);

               if (!is15Classpath(javaProject))
               {
                  EJB3WizardsUIPlugin
                        .warn("EJB3 Projects require a 5.0 JRE to compile. "
                              + "Since a 5.0 JRE isn't available, a simple java project will be created with your current default JRE. "
                              + "After you have installed a 5.0 JRE you can add JBoss EJB3 Libraries to your Java Project under "
                              + "Project Properties > Java Build Path > Libraries > Add Library.");

                  return;
               }

               //EJB3ProjectNature.ensureAopProjectNature(javaProject);
               AopCorePlugin.getDefault().setJava50CompilerCompliance(javaProject);

               ArrayList sourcePaths = findSourcePaths(project);
               IPath outputLocation = findOutputLocation(project);

               if (sourcePaths.size() > 0)
               {
                  ArrayList cpEntries = new ArrayList();
                  boolean jndiPropertiesExists = false;
                  JndiPropertiesFileFilter jndiFilter = new JndiPropertiesFileFilter();

                  for (Iterator iter = sourcePaths.iterator(); iter.hasNext();)
                  {
                     IPath sourcePath = (IPath) iter.next();
                     cpEntries.add(JavaCore.newSourceEntry(sourcePath));

                     if (!jndiPropertiesExists)
                     {
                        File propFiles[] = sourcePath.toFile().listFiles(jndiFilter);
                        if (propFiles != null && propFiles.length > 0)
                        {
                           jndiPropertiesExists = true;
                        }
                     }
                  }

                  if (!jndiPropertiesExists)
                  {
                     IPath sourcePath = (IPath) sourcePaths.iterator().next();
                     try
                     {
                        createJndiProperties(sourcePath);
                     }
                     catch (Exception e)
                     {

                     }
                  }

                  if (outputLocation == null)
                  {
                     EJB3WizardsUIPlugin.alert("While importing existing source, JBossIDE was unable to find"
                           + " a location to place compiled java class files. The directory \"bin\" will"
                           + " be created under the project directory and used as an output location instead.");

                     outputLocation = project.getProject().getFullPath().append(new Path("/bin"));
                     createFolder(outputLocation, monitor);
                  }

                  javaProject.getProject().getPersistentProperty(EJB3ClasspathContainer.JBOSS_EJB3_CONFIGURATION);
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
      catch (InterruptedException e)
      {
         return false;
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

   public boolean canFinish()
   {
      if (page2 == null)
         return false;

      return page2.getServer() != null;
   }

}
