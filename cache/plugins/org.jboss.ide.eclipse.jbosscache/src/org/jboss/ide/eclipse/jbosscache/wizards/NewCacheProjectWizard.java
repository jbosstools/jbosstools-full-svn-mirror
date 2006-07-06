/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.wizards;

import java.awt.Dialog;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.ui.util.CoreUtility;
import org.eclipse.jdt.internal.ui.wizards.JavaProjectWizardSecondPage;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.eclipse.ui.internal.PerspectiveHelper;
import org.eclipse.ui.internal.registry.PerspectiveRegistry;
import org.jboss.ide.eclipse.jbosscache.ICacheConstants;
import org.jboss.ide.eclipse.jbosscache.JBossCachePlugin;
import org.jboss.ide.eclipse.jbosscache.classpath.CacheVersion124CpContainer;
import org.jboss.ide.eclipse.jbosscache.classpath.CacheVersion130CpContainer;
import org.jboss.ide.eclipse.jbosscache.editors.input.CacheFileEditorInput;
import org.jboss.ide.eclipse.jbosscache.internal.CacheMessages;

public class NewCacheProjectWizard extends Wizard implements INewWizard
{
   private WizardNewProjectCreationPage page = null;
//   private Button btnCache124 = null;
//   private Button btnCache130 = null;
//   private boolean is124 = true;

   public void init(IWorkbench workbench, IStructuredSelection selection)
   {
      page = new WizardNewProjectCreationPage("JBoss Cache Project Wizard"); //$NON-NLS-1$
      page.setTitle(CacheMessages.NewCacheProjectWizard_Title);
      page.setDescription(CacheMessages.NewCacheProjectWizard_Description);
      addPage(page);
      setWindowTitle("New Cache Project");
   }

   public void createPageControls(Composite pageContainer)
   {
      super.createPageControls(pageContainer);
      addCacheVersionGroup((Composite)page.getControl());
   }

   private void addCacheVersionGroup(Composite pageContainer)
   {
//      Group group = new Group(pageContainer,SWT.SHADOW_ETCHED_IN);
//      group.setText("Cache Version");
//      GridLayout gridLayout = new GridLayout(2,false);
//      group.setLayout(gridLayout);
//      group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
//      
//      btnCache124 = new Button(group,SWT.RADIO | SWT.RIGHT);
//      btnCache124.setText("Cache 1.2.4");
//      btnCache124.setSelection(true);//default
//      
//      btnCache130 = new Button(group,SWT.RADIO | SWT.RIGHT);
//      btnCache130.setText("Cache 1.3.0");
//      btnCache130.setEnabled(false);
//      
//      btnCache124.addSelectionListener(new SelectionAdapter(){
//
//         public void widgetSelected(SelectionEvent e)
//         {
//            is124 = true;
//         }         
//      });
//      
//      btnCache130.addSelectionListener(new SelectionAdapter(){
//         public void widgetSelected(SelectionEvent e)
//         {
//            is124 = false;
//         }                  
//      });
//      
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
      if (true)
      {
         classpathEntries.add(JavaCore.newContainerEntry(new Path(CacheVersion124CpContainer.CONTAINER_ID)));
      }
      else
      {
         classpathEntries.add(JavaCore.newContainerEntry(new Path(CacheVersion130CpContainer.CONTAINER_ID)));
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
               IProject project = page.getProjectHandle();
                

               JavaProjectWizardSecondPage.createProject(project, page.getLocationPath(), monitor);
               monitor.worked(1);

               IJavaProject javaProject = JavaCore.create(project);
               addProjectNature(project,JavaCore.NATURE_ID);
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
                     String alertString = "While importing existing source, JBossIDE was unable to find"
                        + " a location to place compiled java class files. The directory \"bin\" will"
                        + " be created under the project directory and used as an output location instead.";
                     
                     MessageDialog dialog = new MessageDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                           "JBossIDE/Cache - Alert", Display.getDefault().getSystemImage(SWT.ICON_INFORMATION), alertString,
                           MessageDialog.INFORMATION, new String[]
                           {"OK",}, 0);

                     dialog.setBlockOnOpen(true);

                     dialog.open();
                     

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
         ResourcesPlugin.getWorkspace().getRoot()
         .refreshLocal(IWorkspaceRoot.DEPTH_INFINITE, new NullProgressMonitor());
         
         
         IPath path = page.getProjectHandle().getLocation();
         path = path.append(new Path("cache.cfg.xml"));
         File file = path.toFile();
         
         CacheFileEditorInput edInput = new CacheFileEditorInput(file);

         PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(edInput,
         "org.eclipse.wst.xml.ui.internal.tabletree.XMLMultiPageEditorPart");
         
         IPerspectiveRegistry perspReg = PlatformUI.getWorkbench().getPerspectiveRegistry();
         IPerspectiveDescriptor desc = perspReg.findPerspectiveWithId("org.eclipse.jdt.ui.JavaPerspective");

         if(MessageDialog.openQuestion(getShell(),"Option","Do you like to go Java perspective?"))
            PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().setPerspective(desc);
         

      }
      catch (Exception e)
      {
         e.printStackTrace();
      }

      return true;
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
   
   /**
    * Add the given project nature to the given project (if it isn't already added).
    */
   public  boolean addProjectNature(IProject project, String natureId)
   {
      boolean added = false;
      try
      {
         if (project != null && natureId != null)
         {
            IProjectDescription desc = project.getDescription();

            if (!project.hasNature(natureId))
            {
               String natureIds[] = desc.getNatureIds();
               String newNatureIds[] = new String[natureIds.length + 1];

               System.arraycopy(natureIds, 0, newNatureIds, 1, natureIds.length);
               newNatureIds[0] = natureId;
               desc.setNatureIds(newNatureIds);

               project.getProject().setDescription(desc, new NullProgressMonitor());
               added = true;
            }
         }
         
         addDefaultXml(project);
         
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
      return added;
   }

   private void addDefaultXml(IProject project) throws Exception{
      IPath location = project.getLocation();
      IPath fileLocation = location.append(new Path("cache.cfg.xml"));
      
      File crFile = fileLocation.toFile();
      try{
         crFile.createNewFile();
         
         String path = Platform.asLocalURL(JBossCachePlugin.getDefault().getBundle().getResource("org/jboss/ide/eclipse/jbosscache/cache.cfg.xml")).getFile().toString();
                                   
         File file = new File(path);
         
         FileInputStream stream = new FileInputStream(file);
         FileOutputStream outStream = new FileOutputStream(crFile);
         
         byte[] by = new byte[stream.available()];
         stream.read(by,0,stream.available());
         
         outStream.write(by);
         outStream.flush();
         outStream.close();
         
         stream.close();
         
      }catch(Exception e){
         e.printStackTrace();
      }

   }

   
}