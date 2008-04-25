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
package org.jboss.ide.eclipse.xdoclet.test;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.jboss.ide.eclipse.core.test.util.JavaProjectHelper;
import org.jboss.ide.eclipse.core.test.util.TestFileUtil;
import org.jboss.ide.eclipse.core.test.util.TestUIUtil;
import org.jboss.ide.eclipse.jdt.core.util.JavaProjectUtil;
import org.jboss.ide.eclipse.jdt.j2ee.core.classpath.J2EE14ClasspathContainer;
import org.jboss.ide.eclipse.xdoclet.run.XDocletRunPlugin;
import org.jboss.ide.eclipse.xdoclet.run.model.XDocletAttribute;
import org.jboss.ide.eclipse.xdoclet.run.model.XDocletConfiguration;
import org.jboss.ide.eclipse.xdoclet.run.model.XDocletDataRepository;
import org.jboss.ide.eclipse.xdoclet.run.model.XDocletElement;
import org.jboss.ide.eclipse.xdoclet.run.model.XDocletTask;
import org.jboss.ide.eclipse.xdoclet.run.ui.dialogs.ConfigurationEditDialog;
import org.jboss.ide.eclipse.xdoclet.run.ui.properties.ConfigurationPropertyPage;

import junit.framework.TestCase;

public class XDocletTest extends TestCase
{

   protected IJavaProject xdocletTestProject;
   protected ConfigurationPropertyPage propertyPage;
   protected PreferenceDialog dialog;
   
   protected void setUp() throws Exception
   {
      xdocletTestProject = JavaProjectHelper.createJavaProject("xdocletTestProject", new String[] {"/src"}, "/bin");
      String xdocletTestProjectRoot = XDocletTestPlugin.getDefault().getBaseDir();
      xdocletTestProjectRoot += File.separator + "xdocletTestProject";
      
      TestFileUtil.copyDirectory (new File(xdocletTestProjectRoot), xdocletTestProject.getProject().getLocation().toFile(), true);
      IClasspathEntry[] rawClasspath = xdocletTestProject.getRawClasspath();
      IClasspathEntry j2ee14 = JavaCore.newContainerEntry(new Path(J2EE14ClasspathContainer.CLASSPATH_CONTAINER));
      
      xdocletTestProject.setRawClasspath(JavaProjectUtil.mergeClasspathEntry(rawClasspath, j2ee14),
            xdocletTestProject.getOutputLocation(), new NullProgressMonitor());
      
      xdocletTestProject.getProject().refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
   }
   
   protected void tearDown() throws Exception
   {
      xdocletTestProject.getProject().delete(true, true, new NullProgressMonitor());
      TestUIUtil.waitForJobs();
   }
  
   protected void openXDocletPrefsDialog ()
   {
      dialog = PreferencesUtil.createPropertyDialogOn(
            PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
            xdocletTestProject, "org.jboss.ide.eclipse.xdoclet.run.ui.properties.ConfigurationPropertyPage",
            null, null);
      
      dialog.setBlockOnOpen(false);
      dialog.open();
      
      assertTrue (dialog.getSelectedPage() instanceof ConfigurationPropertyPage);
      
      propertyPage = (ConfigurationPropertyPage) dialog.getSelectedPage();
   }
   
   protected XDocletElement clone (XDocletElement element)
   {
      return element == null ? null : (XDocletElement) element.clone();
   }
   
   protected XDocletTask findXDocletTask (String taskName)
   {
      XDocletDataRepository repository = XDocletRunPlugin.getDefault().getXDocletDataRepository();
      Collection tasks = repository.getTasks();
      
      XDocletTask xdocletTask = null;
      
      for (Iterator iter = tasks.iterator(); iter.hasNext(); )
      {
         XDocletTask task = (XDocletTask) iter.next();
         if (task.getName().equals(taskName)) {
            xdocletTask = task;
            break;
         }
      }
      
      return xdocletTask;
   }
   
   protected XDocletAttribute useXDocletAttribute (XDocletElement parent, String name)
   {
      XDocletAttribute attr = findXDocletAttribute(parent, name);
      if (attr != null)
         attr.setUsed(true);
      
      return attr;
   }
   
   protected XDocletAttribute findXDocletAttribute (XDocletElement element, String name)
   {
      for (Iterator iter = element.getAttributes().iterator(); iter.hasNext(); )
      {
         XDocletAttribute attr = (XDocletAttribute) iter.next();
         if (attr.getName().equals(name))
         {
            return attr;
         }
      }
      return null;
   }
   
   protected XDocletElement findXDocletSubElement (XDocletElement parent, String name)
   {
      Collection elements = XDocletRunPlugin.getDefault().getXDocletDataRepository().getElements(parent);
      for (Iterator iter = elements.iterator(); iter.hasNext(); )
      {
         XDocletElement element = (XDocletElement) iter.next();
         if (element.getName().equals(name))
            return element;
      }
      return null;
   }
   
   
   protected XDocletConfiguration findXDocletConfiguration (String name)
   {
      List list = propertyPage.getConfigurationListViewer().getConfigurations();
      for (Iterator iter = list.iterator(); iter.hasNext(); )
      {
         XDocletConfiguration configuration = (XDocletConfiguration) iter.next();
         
         if (configuration.getName().equals(name))
            return configuration;
      }
      return null;
   }
   
   protected void createTestConfiguration ()
   {
      openXDocletPrefsDialog();
      
      propertyPage.getButton(ConfigurationPropertyPage.BUTTON_ENABLE_XDOCLET).setSelection(true);
      
      Display.getDefault().asyncExec(new Runnable() {
         public void run () {
            // don't wanna run into a race condition..
            TestUIUtil.delay(500);
            
            Dialog dialog = propertyPage.getConfigurationListViewer().getCurrentDialog();
            
            assertTrue (dialog instanceof ConfigurationEditDialog);
            
            ConfigurationEditDialog editDialog = (ConfigurationEditDialog) dialog;
            editDialog.getNameText().setText("xdoclet-test");
            
            TestUIUtil.clickButton(TestUIUtil.getDialogButton(editDialog, Dialog.OK));
         }
      });
      
      TestUIUtil.clickButton(propertyPage.getButton(ConfigurationPropertyPage.BUTTON_ADD));
      TestUIUtil.delay(1000);
      
      XDocletConfiguration configuration = findXDocletConfiguration("xdoclet-test");
      assertNotNull(configuration);

      propertyPage.getConfigurationListViewer().getTableViewer().setSelection(new StructuredSelection(configuration));
      XDocletElement ejbdocletTask = clone(findXDocletTask("ejbdoclet"));
      
      configuration.addNode(ejbdocletTask);
      XDocletElement deploymentDescriptor = clone(findXDocletSubElement(ejbdocletTask, "deploymentdescriptor"));
      XDocletElement fileset = clone(findXDocletSubElement(ejbdocletTask, "fileset"));
      XDocletElement homeInterface = clone(findXDocletSubElement(ejbdocletTask, "homeinterface"));
      XDocletElement jboss = clone(findXDocletSubElement(ejbdocletTask, "jboss"));
      XDocletElement packageSubstitution = clone(findXDocletSubElement(ejbdocletTask, "packageSubstitution"));
      XDocletElement remoteInterface = clone(findXDocletSubElement(ejbdocletTask, "remoteinterface"));
      assertNotNull(deploymentDescriptor);
      assertNotNull(fileset);
      assertNotNull(homeInterface);
      assertNotNull(jboss);
      assertNotNull(packageSubstitution);
      assertNotNull(remoteInterface);
      
      XDocletAttribute destDir = useXDocletAttribute(ejbdocletTask, "destDir");
      XDocletAttribute ejbSpec = useXDocletAttribute(ejbdocletTask, "ejbSpec");
      assertNotNull(destDir);
      assertNotNull(ejbSpec);
      destDir.setValue("src");
      ejbSpec.setValue("2.0");
      
      ejbdocletTask.addNode(deploymentDescriptor);
      destDir = useXDocletAttribute(deploymentDescriptor, "destDir");
      assertNotNull(destDir);
      destDir.setValue("src/META-INF");
      
      ejbdocletTask.addNode(fileset);
      XDocletAttribute dir = useXDocletAttribute(fileset, "dir");
      XDocletAttribute includes = useXDocletAttribute(fileset, "includes");
      assertNotNull(dir);
      assertNotNull(includes);
      dir.setValue("src");
      includes.setValue("**/*Bean.java");
      
      ejbdocletTask.addNode(homeInterface);
      ejbdocletTask.addNode(remoteInterface);
      
      ejbdocletTask.addNode(jboss);
      destDir = useXDocletAttribute(jboss, "destDir");
      XDocletAttribute version = useXDocletAttribute(jboss, "Version");
      assertNotNull(destDir);
      assertNotNull(version);
      destDir.setValue("src/META-INF");
      version.setValue("3.0");
      
      ejbdocletTask.addNode(packageSubstitution);
      XDocletAttribute packages = useXDocletAttribute(packageSubstitution, "packages");
      XDocletAttribute substituteWith = useXDocletAttribute(packageSubstitution, "substituteWith");
      assertNotNull(packages);
      assertNotNull(substituteWith);
      packages.setValue("beans");
      substituteWith.setValue("interfaces");
      
      propertyPage.getConfigurationViewer().refresh();
      
      TestUIUtil.clickButton(TestUIUtil.getDialogButton(dialog, Dialog.OK));
      
      TestUIUtil.delay(1000);
      TestUIUtil.waitForJobs();
   }
}
