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
package org.jboss.ide.eclipse.jdt.aop.core;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.corext.util.JavaModelUtil;
import org.jboss.ide.eclipse.jdt.aop.core.classpath.AopClasspathContainer;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Advice;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Aop;
import org.jboss.ide.eclipse.jdt.aop.core.model.AopModel;
import org.jboss.ide.eclipse.jdt.aop.core.util.JaxbAopUtil;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class AopCorePlugin extends Plugin
{
   //The shared instance.
   private static AopCorePlugin plugin;

   //Resource bundle.
   private ResourceBundle resourceBundle;

   private HashMap projectDescriptors, projectReports, projectClassLoaders;

   private static IJavaProject currentJavaProject;

   /**
    * The constructor.
    */
   public AopCorePlugin()
   {
      super();
      plugin = this;
   }

   public void start(BundleContext context) throws Exception
   {
      super.start(context);

      projectDescriptors = new HashMap();
      projectReports = new HashMap();
      projectClassLoaders = new HashMap();

      try
      {
         resourceBundle = ResourceBundle.getBundle("org.jboss.ide.eclipse.jdt.aop.core.AopCorePluginResources");
      }
      catch (MissingResourceException x)
      {
         resourceBundle = null;
      }
   }

   /**
    * Returns the shared instance.
    */
   public static AopCorePlugin getDefault()
   {
      return plugin;
   }

   /**
    * Return a set of AopDescriptor objects that are associated with each project
    * 
    * @param project
    * @return A list of project descriptors
    */
   public Set getProjectDescriptors(IJavaProject project)
   {
      return (Set) projectDescriptors.get(project);
   }

   public AopDescriptor getDefaultDescriptor(IJavaProject project)
   {
      return getDefaultDescriptor(project, true);
   }

   /**
    * Returns the default AopDescriptor for this project.
    * 
    * @param project the project whose AopDescriptor to get
    * @param create whether or not to create a default descriptor if one doesn't exist in the project
    * @return The default AopDescriptor for this project.
    */
   public AopDescriptor getDefaultDescriptor(IJavaProject project, boolean create)
   {
      //System.out.println("[aop-core-plugin] get default descriptor: project="+(project==null?"null":project.getElementName())+",create="+create);

      if (project == null)
         return null;

      Set descriptors = getProjectDescriptors(project);
      if (descriptors != null)
      {
         // The default descriptor is the first?
         // Eventually we will need to see if there is a $project-root/jboss-aop.xml , and set that as default,
         // otherwise we'll use the first found element

         return (AopDescriptor) descriptors.iterator().next();
      }
      else
      {
         if (create)
         {
            // Create and add the default descriptor

            IPath location = getProjectLocation(project.getProject());
            registerProjectDescriptor(project, location.append("jboss-aop.xml").toFile());
            return (AopDescriptor) getProjectDescriptors(project).iterator().next();
         }
         return null;
      }
   }

   /**
    * Get the base directory of this plugin
    */
   public String getBaseDir()
   {
      try
      {
         URL installURL = Platform.asLocalURL(this.getBundle().getEntry("/"));//$NON-NLS-1$
         return installURL.getFile().toString();
      }
      catch (IOException ioe)
      {
         ioe.printStackTrace();
      }
      return null;
   }

   /**
    * Register an aop project descriptor (*-aop.xml) with this project. Each
    * project can have multiple descriptors.
    * 
    * @param project
    * @param relativePath
    *                 The path of the aop project descriptor in the project.
    */
   public void registerProjectDescriptor(IJavaProject project, IPath relativePath)
   {
      IPath location = getProjectLocation(project.getProject());

      IPath descriptorPath = location.append(relativePath);
      registerProjectDescriptor(project, descriptorPath.toFile());
   }

   /**
    * Register an aop project descriptor (*-aop.xml) with this project. Each project can have multiple descriptors.
    * 
    * @param project
    * @param aopFile A file object that represents an aop project descriptor.
    */
   public void registerProjectDescriptor(IJavaProject project, File aopFile)
   {
      if (!projectDescriptors.containsKey(project))
      {
         projectDescriptors.put(project, new TreeSet());
      }

      Set descriptors = getProjectDescriptors(project);
      Aop aop = JaxbAopUtil.instance().unmarshal(aopFile);
      AopDescriptor descriptor = new AopDescriptor();
      descriptor.setAop(aop);
      descriptor.setFile(aopFile);

      descriptors.add(descriptor);
   }

   /**
    * Update the system aop path with the classpath of a IJavaProject
    * 
    * @param project A java project.
    */
   public void updateAopPath(IJavaProject project)
   {
      String descriptorPaths = getDescriptorPaths(project);
      //System.out.println("[aop-core-plugin] setting jboss aop path: " + descriptorPaths);
      System.setProperty("jboss.aop.path", descriptorPaths);
   }

   /**
    * Return a list of the descriptors for this project seperated by the system path separator
    */
   public String getDescriptorPaths(IJavaProject project)
   {
      Set descriptors = getProjectDescriptors(project);
      if (descriptors == null)
      {
         AopDescriptor descriptor = getDefaultDescriptor(project);
         descriptors = getProjectDescriptors(project);
      }
      String descriptorPaths = new String();

      Iterator dIter = descriptors.iterator();
      while (dIter.hasNext())
      {
         AopDescriptor descriptor = (AopDescriptor) dIter.next();
         String path = descriptor.getFile().getAbsolutePath();

         descriptorPaths += path;
         if (dIter.hasNext())
         {
            descriptorPaths += File.pathSeparator;
         }
      }

      return descriptorPaths;
   }

   public void cleanProject(IJavaProject project) throws CoreException
   {
      project.getProject().build(IncrementalProjectBuilder.CLEAN_BUILD, new NullProgressMonitor());
   }

   public String getAopSignature(IJavaElement element)
   {
      if (element instanceof IMethod)
      {
         return getAopSignature((IMethod) element);
      }
      else if (element instanceof IField)
      {
         return getAopSignature((IField) element);
      }

      return null;
   }

   public String getAopSignature(IMethod method)
   {
      try
      {
         IType type = (IType) method.getParent();

         String className = type.getFullyQualifiedName();
         String methodName = method.isConstructor() ? "new" : method.getElementName();
         String modifiers = Flags.toString(method.getFlags());
         String returnTypeSig = JavaModelUtil.getResolvedTypeName(method.getReturnType(), method.getDeclaringType());
         IType retType = null;
         try
         {
            retType = JavaModelUtil.findType(getCurrentJavaProject(), returnTypeSig);
         }
         catch (JavaModelException jme)
         {
         }
         String fqReturnClass;
         if (retType == null)
         {
            fqReturnClass = returnTypeSig;
         }
         else
         {
            fqReturnClass = retType.getFullyQualifiedName();
         }

         String params[] = method.getParameterTypes();
         String signature = modifiers + " ";

         if (!method.isConstructor())
            signature += fqReturnClass + " ";

         signature += className + "->" + methodName + "(";

         for (int i = 0; i < params.length; i++)
         {
            String paramSig = JavaModelUtil.getResolvedTypeName(params[i], method.getDeclaringType());

            String fqParamClass;
            IType paramType = null;
            try
            {
               paramType = JavaModelUtil.findType(getCurrentJavaProject(), paramSig);
            }
            catch (JavaModelException jme)
            {
            }

            if (paramType == null)
            {
               fqParamClass = paramSig;
            }
            else
            {
               fqParamClass = paramType.getFullyQualifiedName();
            }
            signature += fqParamClass;

            if (i < params.length - 1)
            {
               signature += ", ";
            }
         }
         signature += ")";

         return signature;
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }

      return null;
   }

   public String getAopSignature(IField field)
   {
      try
      {
         IType type = (IType) field.getParent();
         String className = type.getFullyQualifiedName();
         String fieldName = field.getElementName();
         String modifiers = Flags.toString(field.getFlags());

         String fieldResolvedType = JavaModelUtil.getResolvedTypeName(field.getTypeSignature(), field
               .getDeclaringType());

         IType fieldType = null;
         try
         {
            fieldType = JavaModelUtil.findType(getCurrentJavaProject(), fieldResolvedType);
         }
         catch (JavaModelException jme)
         {
         }

         String fqClass;
         if (fieldType == null)
         {
            fqClass = fieldResolvedType;
         }
         else
         {
            fqClass = fieldType.getFullyQualifiedName();
         }

         return modifiers + " " + fqClass + " " + className + "->" + fieldName;
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }

      return null;
   }

   public URLClassLoader getProjectClassLoader(IJavaProject project)
   {
      ArrayList pathElements = getProjectClassPathURLs(project);
      URL urlPaths[] = (URL[]) pathElements.toArray(new URL[pathElements.size()]);

      return new URLClassLoader(urlPaths, Thread.currentThread().getContextClassLoader());
   }

   public URLClassLoader getPluginClassLoader()
   {
      ArrayList pathElements = new ArrayList();

      String jars[] = new String[]
      {"jaxb-api.jar", "namespace.jar", "jax-qname.jar", "jaxb-impl.jar", "jaxb-libs.jar", "relaxngDatatype.jar",
            "jboss-aop.jar", "jboss-common.jar", "qdox.jar", "concurrent.jar", "trove.jar", "javassist.jar"};

      for (int i = 0; i < jars.length; i++)
      {
         IPath relative = new Path(jars[i]);
         URL bundleURL = Platform.find(getBundle(), relative);
         try
         {
            URL fileURL = Platform.asLocalURL(bundleURL);
            pathElements.add(fileURL);
         }
         catch (IOException e)
         {
            e.printStackTrace();
         }
      }
      URL urlPaths[] = (URL[]) pathElements.toArray(new URL[pathElements.size()]);

      return new URLClassLoader(urlPaths, Thread.currentThread().getContextClassLoader());
   }

   private static URL getRawLocationURL(IPath simplePath) throws MalformedURLException
   {
      File file = getRawLocationFile(simplePath);
      return file.toURL();
   }

   private static File getRawLocationFile(IPath simplePath)
   {
      IResource resource = ResourcesPlugin.getWorkspace().getRoot().findMember(simplePath);
      File file = null;
      if (resource != null)
      {
         file = ResourcesPlugin.getWorkspace().getRoot().findMember(simplePath).getRawLocation().toFile();
      }
      else
      {
         file = simplePath.toFile();
      }
      return file;
   }

   public ArrayList getProjectClassPathURLs(IJavaProject project)
   {
      ArrayList pathElements = new ArrayList();

      try
      {
         IClasspathEntry paths[] = project.getResolvedClasspath(true);
         if (paths != null)
         {
            for (int i = 0; i < paths.length; i++)
            {
               IClasspathEntry path = paths[i];
               if (path.getEntryKind() == IClasspathEntry.CPE_LIBRARY)
               {
                  URL url = getRawLocationURL(path.getPath());
                  pathElements.add(url);
               }
               else if (path.getEntryKind() == IClasspathEntry.CPE_CONTAINER)
               {
                  IClasspathContainer container = JavaCore.getClasspathContainer(path.getPath(), project);

                  if (container instanceof AopClasspathContainer)
                  {
                     AopClasspathContainer aopContainer = (AopClasspathContainer) container;
                     IPath aopPaths[] = aopContainer.getAopJarPaths();
                     for (int j = 0; j < aopPaths.length; j++)
                     {
                        URL url = aopPaths[j].toFile().toURL();
                        pathElements.add(url);
                     }
                  }

               }
            }
         }

         IPath location = getProjectLocation(project.getProject());
         IPath outputPath = location.append(project.getOutputLocation().removeFirstSegments(1));

         pathElements.add(outputPath.toFile().toURL());
      }
      catch (JavaModelException e)
      {
         e.printStackTrace();
      }
      catch (MalformedURLException e)
      {
         e.printStackTrace();
      }

      return pathElements;
   }

   /**
    * Add the given project nature to the given project (if it isn't already added).
    */
   public static boolean addProjectNature(IProject project, String natureId)
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
      }
      catch (CoreException e)
      {
         e.printStackTrace();
      }
      return added;
   }

   public void initModel(IJavaProject project, IProgressMonitor monitor)
   {
      AopModel.instance().initModel(project, monitor);
   }

   public void updateModel(IJavaProject project, IProgressMonitor monitor)
   {
      AopModel.instance().updateModel(project, monitor);
   }


   public IPath getProjectLocation(IProject project)
   {
      if (project.getRawLocation() == null)
      {
         return project.getLocation();
      }
      else
         return project.getRawLocation();
   }

   public static IJavaProject getCurrentJavaProject()
   {
      return currentJavaProject;
   }

   public static void setCurrentJavaProject(IJavaProject currentJavaProject)
   {
      AopCorePlugin.currentJavaProject = currentJavaProject;
   }

   public IMethod findAdviceMethod(IType type, String methodName)
   {
      try
      {
         if (type != null)
         {
            IMethod methods[] = type.getMethods();
            for (int i = 0; i < methods.length; i++)
            {
               if (methods[i].getElementName().equals(methodName) && methods[i].getNumberOfParameters() == 1
                     && methods[i].getParameterTypes()[0].indexOf("Invocation") != -1)
               {
                  return methods[i];
               }
            }
         }
      }
      catch (JavaModelException e)
      {
         e.printStackTrace();
      }
      return null;
   }

   public IMethod findAdviceMethod(Advice advice)
   {
      try
      {

         IType type = currentJavaProject.findType(advice.getAspect());
         return findAdviceMethod(type, advice.getName());

      }
      catch (JavaModelException e)
      {
         e.printStackTrace();
      }
      return null;
   }

   public IMethod getInvokeMethod(IType type)
   {
      try
      {
         IMethod methods[] = type.getMethods();
         for (int i = 0; i < methods.length; i++)
         {
            if (methods[i].getElementName().equals("invoke") && methods[i].getNumberOfParameters() == 1
                  && methods[i].getParameterTypes()[0].indexOf("Invocation") != -1)
            {
               return methods[i];
            }
         }
      }
      catch (JavaModelException e)
      {
         e.printStackTrace();
      }

      return null;
   }

   public boolean hasJava50CompilerCompliance(IJavaProject javaProject)
   {
      String compliance = javaProject.getOption(JavaCore.COMPILER_COMPLIANCE, true);
      return JavaCore.VERSION_1_5.equals(compliance);
   }

   public void setJava50CompilerCompliance(IJavaProject javaProject)
   {
      Map map = javaProject.getOptions(false);
      JavaModelUtil.set50CompilanceOptions(map);
      javaProject.setOptions(map);
   }
}
