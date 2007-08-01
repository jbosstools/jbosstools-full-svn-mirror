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
package org.jboss.ide.eclipse.packaging.core;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Hashtable;
import java.util.Map;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jdt.core.IJavaProject;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.core.ProjectChangeListener;
import org.jboss.ide.eclipse.core.util.ProjectUtil;
import org.jboss.ide.eclipse.core.util.ResourceUtil;
import org.jboss.ide.eclipse.packaging.core.builder.PackagingBuilder;
import org.jboss.ide.eclipse.packaging.core.configuration.ProjectConfigurations;
import org.jboss.ide.eclipse.packaging.core.configuration.StandardConfigurations;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class PackagingCorePlugin extends AbstractPlugin
{
   private StandardConfigurations configurations;

   private Map projectConfigurations = new Hashtable();

   /** The shared instance */
   private static PackagingCorePlugin plugin;

   /** Name of the Ant build file */
   public final static String BUILD_FILE = "packaging-build.xml";//$NON-NLS-1$

   /** Name for the standards configurations */
   public final static String GENERICS_FILE = "resources/standards.xml";//$NON-NLS-1$

   /** Name of the configuration file per project */
   public final static String PROJECT_FILE = ".packaging";//$NON-NLS-1$

   /** Stylesheet to transform configuration file to Ant build file */
   public final static String XSL_FILE = "resources/configuration2packaging.xsl";//$NON-NLS-1$

   public final static QualifiedName QNAME_PACKAGING_ENABLED = new QualifiedName("org.jboss.ide.eclipse.packaging",
         "enabled");

   /** The constructor. */
   public PackagingCorePlugin()
   {
      plugin = this;
   }

   public void start(BundleContext context) throws Exception
   {
      super.start(context);
      
      initializePackagingProjects();
   }

   private void testAndSetPackagingEnabled(IProject project) {
		try {
			if (project != null && project.isAccessible()) {
				if (ProjectUtil.projectHasBuilder(project,
						PackagingBuilder.BUILDER_ID)) {
					project.setSessionProperty(QNAME_PACKAGING_ENABLED, "true");
				}
			}
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
   
   private void initializePackagingProjects() {
		IProject projects[] = ResourcesPlugin.getWorkspace().getRoot()
				.getProjects();

		for (int i = 0; i < projects.length; i++) {
			testAndSetPackagingEnabled(projects[i]);
		}

		new ProjectChangeListener() {
			public void projectChanged(IProject project, int kind) {
				testAndSetPackagingEnabled(project);
			}
		}.register();
	}
   
   /**
	 * Gets the projectConfigurations attribute of the PackagingPlugin object
	 * 
	 * @param project
	 *            Description of the Parameter
	 * @return The projectConfigurations value
	 */
   public ProjectConfigurations getProjectConfigurations(IJavaProject project)
   {
      ProjectConfigurations config = (ProjectConfigurations) projectConfigurations.get(project);
      if (config == null)
      {
         config = new ProjectConfigurations(project);
      }
      return config;
   }

   /**
    * Gets the standardConfigurations attribute of the CorePlugin object
    *
    * @return   The standardConfigurations value
    */
   public StandardConfigurations getStandardConfigurations()
   {
      if (this.configurations == null)
      {
         this.configurations = new StandardConfigurations();
      }
      return this.configurations;
   }

   /**
    * Returns the shared instance.
    *
    * @return   The default value
    */
   public static PackagingCorePlugin getDefault()
   {
      return plugin;
   }

   /**
    * Convenience method which returns the unique identifier of this plugin.
    *
    * @return   The unique indentifier value
    */
   public static String getUniqueIdentifier()
   {
      if (getDefault() == null)
      {
         // If the default instance is not yet initialized,
         // return a static identifier. This identifier must
         // match the plugin id defined in plugin.xml
         return "org.jboss.ide.eclipse.packaging.core";//$NON-NLS-1$
      }
      return getDefault().getBundle().getSymbolicName();
   }

   public IFile createBuildFile(IProject project) throws IOException, CoreException, TransformerException
   {
      IFile file = project.getFile(PackagingCorePlugin.PROJECT_FILE);
      IFile buildFile = project.getFile(PackagingCorePlugin.BUILD_FILE);

      // If the packaging project file exists, then process it
      if (file.exists() && file.getModificationStamp() > buildFile.getModificationStamp())
      {
         TransformerFactory tFactory = TransformerFactory.newInstance();

         InputStream is = null;
         byte[] bytes;
         try
         {
            // Gets the XSL file
            URL xslFile = PackagingCorePlugin.getDefault().find(new Path(PackagingCorePlugin.XSL_FILE));
            is = new BufferedInputStream(xslFile.openStream());
            Source stylesheet = new StreamSource(is);

            // Create a new transformer on the stylesheet
            Transformer transformer = tFactory.newTransformer(stylesheet);

            Source source = new StreamSource(file.getContents());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Result result = new StreamResult(baos);

            // Apply the XSL style
            transformer.setOutputProperty("indent", "yes");//$NON-NLS-1$ //$NON-NLS-2$
            transformer.transform(source, result);

            bytes = baos.toByteArray();
         }
         finally
         {
            // Ensure that the input stream is closed.
            if (is != null)
            {
               try
               {
                  is.close();
               }
               catch (Throwable ignore)
               {
               }
            }
         }

         File buildFile2 = buildFile.getRawLocation().toFile();
         FileOutputStream fos = new FileOutputStream(buildFile2);
         fos.write(bytes);
         fos.close();

         ResourceUtil.safeRefresh(buildFile, IResource.DEPTH_ONE);

         // Save the file
         //         if (!buildFile.exists())
         //         {
         //            buildFile.create(null, true, null);
         //         }
         //         buildFile.setContents(new ByteArrayInputStream(bytes), true, true, null);
      }
      return buildFile;
   }

   public void enablePackagingBuilder(IJavaProject project, boolean enable)
   {
      try
      {
         if (enable)
         {
            if (!ProjectUtil.projectHasBuilder(project.getProject(), PackagingBuilder.BUILDER_ID))
            {
               ProjectUtil.addProjectBuilder(project.getProject(), PackagingBuilder.BUILDER_ID);
               project.getProject().setSessionProperty(QNAME_PACKAGING_ENABLED, "true");
            }
         }
         else
         {
            if (ProjectUtil.projectHasBuilder(project.getProject(), PackagingBuilder.BUILDER_ID))
            {
               ProjectUtil.removeProjectBuilder(project.getProject(), PackagingBuilder.BUILDER_ID);
               project.getProject().setSessionProperty(QNAME_PACKAGING_ENABLED, null);
            }
         }
      }
      catch (CoreException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }
}
