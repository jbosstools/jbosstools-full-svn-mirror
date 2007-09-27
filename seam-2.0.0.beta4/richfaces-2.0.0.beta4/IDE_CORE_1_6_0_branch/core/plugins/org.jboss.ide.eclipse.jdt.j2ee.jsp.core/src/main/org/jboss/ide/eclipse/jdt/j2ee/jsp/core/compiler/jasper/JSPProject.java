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
package org.jboss.ide.eclipse.jdt.j2ee.jsp.core.compiler.jasper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import javax.servlet.ServletContext;

import org.apache.jasper.JasperException;
import org.apache.jasper.JspCompilationContext;
import org.apache.jasper.compiler.BeanRepository;
import org.apache.jasper.compiler.Collector;
import org.apache.jasper.compiler.ELFunctionMapper;
import org.apache.jasper.compiler.ErrorDispatcher;
import org.apache.jasper.compiler.Generator;
import org.apache.jasper.compiler.JspConfig;
import org.apache.jasper.compiler.JspRuntimeContext;
import org.apache.jasper.compiler.JspUtil;
import org.apache.jasper.compiler.Node;
import org.apache.jasper.compiler.PageInfo;
import org.apache.jasper.compiler.ParserController;
import org.apache.jasper.compiler.ScriptingVariabler;
import org.apache.jasper.compiler.ServletWriter;
import org.apache.jasper.compiler.SmapUtil;
import org.apache.jasper.compiler.TagFileProcessor;
import org.apache.jasper.compiler.TagPluginManager;
import org.apache.jasper.compiler.TextOptimizer;
import org.apache.jasper.compiler.TldLocationsCache;
import org.apache.jasper.compiler.Validator;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.compiler.Compiler;
import org.eclipse.jdt.internal.compiler.DefaultErrorHandlingPolicies;
import org.eclipse.jdt.internal.compiler.batch.Main;
import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;
import org.eclipse.jdt.internal.compiler.env.INameEnvironment;
import org.eclipse.jdt.internal.compiler.problem.DefaultProblemFactory;
import org.eclipse.jdt.internal.core.JavaProject;
import org.eclipse.jdt.internal.core.builder.JavaBuilder;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.jdt.j2ee.jsp.core.compiler.JSPClassLoader;
import org.jboss.ide.eclipse.jdt.j2ee.jsp.core.compiler.JSPMarkerFactory;

/**
 * Core of the JSP Compilation. This class holds everything related
 * to a Java project such as the local Servlet context.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class JSPProject implements IResourceChangeListener
{
   private boolean active = false;

   private int classpathHashcode = -1;

   private Compiler compiler;

   private JSPCompilationEnvironment jasperEnvironment;

   private IJavaProject javaProject;

   private Map jspInfos = new Hashtable();

   private INameEnvironment nameEnvironment;

   private IProject project;

   private URLClassLoader projectLoader;

   private JSPCompilerRequestor requestor;

   private JSPServletContext servletContext;

   private String uriRoot;

   private IFolder uriRootFolder;

   /**
    *Constructor for the JSPProject object
    *
    * @param project  Description of the Parameter
    */
   JSPProject(IProject project)
   {
      this.project = project;
      this.javaProject = JavaCore.create(this.project);

      this.reset();
   }

   /**
    * Description of the Method
    *
    * @param file  Description of the Parameter
    * @return      Description of the Return Value
    */
   public JSPElementInfo compileJSP(IFile file)
   {
      JSPElementInfo info = null;
      if (this.active && this.isInDocroot(file) && this.hasRightExtension(file))
      {
         info = this.internalCompileJSP(file, true);
         if (info != null)
         {
            String key = file.getProjectRelativePath().toString();
            this.jspInfos.put(key, info);
         }
      }
      return info;
   }

   /**
    * Gets the classLoader attribute of the JSPProject object
    *
    * @return   The classLoader value
    */
   public URLClassLoader getClassLoader()
   {
      try
      {
         IProject project = this.getProject();
         IJavaProject jProject = JavaCore.create(project);
         URL u = null;
         IPath path = null;

         // Compute the classpath hashcode
         IClasspathEntry[] entries = jProject.getResolvedClasspath(true);
         List urls = new ArrayList();
         if (this.projectLoader == null || this.hasClasspathChanged())
         {
            for (int i = 0; i < entries.length; i++)
            {
               IClasspathEntry entry = entries[i];
               switch (entry.getEntryKind())
               {
                  case IClasspathEntry.CPE_SOURCE :
                     path = entry.getOutputLocation();
                     if (path == null)
                     {
                        path = jProject.getOutputLocation();
                     }
                     break;
                  case IClasspathEntry.CPE_LIBRARY :
                     path = entry.getPath();
                     break;
               }
               if (path != null)
               {
                  IResource res = AbstractPlugin.getWorkspace().getRoot().findMember(path);
                  if (res != null)
                  {
                     u = res.getLocation().toFile().toURL();
                  }
                  else
                  {
                     u = path.toFile().toURL();
                  }
                  urls.add(u);
               }
            }
            URL[] result = (URL[]) urls.toArray(new URL[urls.size()]);
            this.projectLoader = new JSPClassLoader(result, JSPProject.class.getClassLoader());
         }
      }
      catch (Exception e)
      {
         this.projectLoader = new URLClassLoader(new URL[0], JSPProject.class.getClassLoader());
      }
      return this.projectLoader;
   }

   /**
    * Gets the compilerRequestor attribute of the JSPProject object
    *
    * @return   The compilerRequestor value
    */
   public JSPCompilerRequestor getCompilerRequestor()
   {
      if (this.requestor == null)
      {
         this.requestor = new JSPCompilerRequestor();
      }
      return this.requestor;
   }

   /**
    * Gets the environment attribute of the JSPProject object
    *
    * @return   The environment value
    */
   public JSPCompilationEnvironment getEnvironment()
   {
      if (this.jasperEnvironment == null)
      {
         this.jasperEnvironment = new JSPCompilationEnvironment();
      }
      return this.jasperEnvironment;
   }

   /**
    * Gets the jSPInfo attribute of the JSPProject object
    *
    * @param file  Description of the Parameter
    * @return      The jSPInfo value
    */
   public JSPElementInfo getJSPInfo(IFile file)
   {
      String key = file.getProjectRelativePath().toString();
      return (JSPElementInfo) this.jspInfos.get(key);
   }

   /**
    * Gets the jSPURI attribute of the JSPProject object
    *
    * @param file  Description of the Parameter
    * @return      The jSPURI value
    */
   public String getJSPURI(IFile file)
   {
      String jspUri = null;
      String s = file.getLocation().toString();
      String uriRoot = this.getEnvironment().getUriRoot();
      jspUri = s.substring(uriRoot.length());
      return jspUri;
   }

   /**
    * Gets the javaProject attribute of the JSPProject object
    *
    * @return   The javaProject value
    */
   public IJavaProject getJavaProject()
   {
      return this.javaProject;
   }

   /**
    * Gets the nameEnvironment attribute of the JSPProject object
    *
    * @return   The nameEnvironment value
    */
   public INameEnvironment getNameEnvironment()
   {
      if (this.nameEnvironment == null)
      {
         this.nameEnvironment = new JSPNameEnvironment(this.javaProject);
      }
      return this.nameEnvironment;
   }

   /**
    * Gets the project attribute of the JSPProject object
    *
    * @return   The project value
    */
   public IProject getProject()
   {
      return this.project;
   }

   /**
    * Gets the servletContext attribute of the JSPProject object
    *
    * @return   The servletContext value
    */
   public JSPServletContext getServletContext()
   {
      return this.servletContext;
   }

   /**
    * Gets the uriRoot attribute of the JSPProject object
    *
    * @return   The uriRoot value
    */
   public String getUriRoot()
   {
      return this.uriRoot;
   }

   /**
    * Gets the uriRootFolder attribute of the JSPProject object
    *
    * @return   The uriRootFolder value
    */
   public IFolder getUriRootFolder()
   {

      return this.uriRootFolder;
   }

   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public boolean hasClasspathChanged()
   {
      boolean changed = false;

      try
      {
         IProject project = this.getProject();
         IJavaProject jProject = JavaCore.create(project);

         // Compute the classpath hashcode
         IClasspathEntry[] entries = jProject.getResolvedClasspath(true);
         int result = 17;
         for (int i = 0; i < entries.length; i++)
         {
            result = result * 37 + entries[i].getPath().toString().hashCode();
         }

         // If the hashcode if different, then recompute it
         changed = (this.projectLoader == null || result != classpathHashcode);
         classpathHashcode = result;
      }
      catch (JavaModelException e)
      {
         changed = true;
         classpathHashcode = -1;
      }

      return changed;
   }

   /**
    * @return   Returns the active.
    */
   public boolean isActive()
   {
      return active;
   }

   /** Description of the Method */
   public void reset()
   {
      try
      {
         IFolder folder = this.getUriRootFolder();
         if (folder != null)
         {
            JSPMarkerFactory.deleteMarkers(folder);
         }
      }
      catch (CoreException e)
      {
         // Do nothing
      }

      String webapp = JSPProjectManager.getPropertyFromWorkspace(this.project, JSPProjectManager.QNAME_WEBROOT);
      if (webapp != null)
      {
         this.uriRootFolder = null;
         this.setUriRoot(webapp);
         this.setScratchDir("." + webapp);//$NON-NLS-1$

         this.compiler = null;
         this.projectLoader = null;
         this.nameEnvironment = null;
         this.jspInfos.clear();
         this.initializeServletContext();
         this.active = true;
      }
      else
      {
         this.active = false;
      }
   }

   /**
    * Description of the Method
    *
    * @param event  Description of the Parameter
    */
   public void resourceChanged(IResourceChangeEvent event)
   {
      IResource resource = event.getResource();
      if ((resource != null) && (resource.getType() == IResource.PROJECT) && (resource == this.getProject()))
      {
         switch (event.getType())
         {
            case IResourceChangeEvent.PRE_CLOSE :
            case IResourceChangeEvent.PRE_DELETE :
               AbstractPlugin.getWorkspace().removeResourceChangeListener(this);
               JSPProjectManager.removeJSPProject(this.getProject());
               break;
            case IResourceChangeEvent.POST_CHANGE :
               this.reset();
               break;
            default :
         // Do nothing
         }
      }
   }

   /**
    * Sets the scratchDir attribute of the JSPProject object
    *
    * @param scratchDir  The new scratchDir value
    */
   public void setScratchDir(String scratchDir)
   {
      IFolder folder = this.getProject().getFolder(scratchDir);
      this.getEnvironment().setScratchDir(folder.getLocation().toFile());
   }

   /**
    * Sets the uriRoot attribute of the JSPProject object
    *
    * @param uriRoot  The new uriRoot value
    */
   public void setUriRoot(String uriRoot)
   {
      // Store the uriRoot;
      this.uriRoot = uriRoot;

      this.uriRootFolder = this.getProject().getFolder(this.uriRoot);
      this.getEnvironment().setUriRoot(this.uriRootFolder.getLocation().toString());
   }

   /**
    * Description of the Method
    *
    * @param info  Description of the Parameter
    */
   private synchronized void compileJava(JSPElementInfo info)
   {
      String content = info.getContent();
      String javaFileName = info.getJavaFileName();
      String javaEncoding = info.getJavaEncoding();

      JSPCompilerRequestor requestor = this.getCompilerRequestor();
      requestor.setInfo(info);

      Compiler compiler = this.getCompiler();

      JSPCompilationUnit unit = info.getCompilationUnit();
      compiler.compile(new ICompilationUnit[]
      {unit});
   }

   /**
    * Gets the compiler attribute of the JSPProject object
    *
    * @return   The compiler value
    */
   private Compiler getCompiler()
   {
      if (this.compiler == null)
      {
    	  IProject project = getProject();
    	  IJavaProject javaProject = JavaCore.create(project);
    	           
    	  this.compiler = new Compiler(this.getNameEnvironment(), DefaultErrorHandlingPolicies.exitAfterAllProblems(),
               javaProject.getOptions(true), this.getCompilerRequestor(), new DefaultProblemFactory(Locale.getDefault()));
      }
      return this.compiler;
   }

   /**
    * Gets the jspCompilationContext attribute of the JSPProject object
    *
    * @param jspUri  Description of the Parameter
    * @return        The jspCompilationContext value
    */
   private JspCompilationContext getJspCompilationContext(String jspUri)
   {
      ServletContext context = this.getEnvironment().getContext();
      JspRuntimeContext rctxt = this.getEnvironment().getRuntimeContext();
      JspCompilationContext clctxt = new JspCompilationContext(jspUri, false, this.getEnvironment(), context, null,
            rctxt);
      return clctxt;
   }

   /**
    * Description of the Method
    *
    * @param file  Description of the Parameter
    * @return      Description of the Return Value
    */
   private boolean hasRightExtension(IFile file)
   {
      String filename = file.toString();
      if (filename.endsWith("jsp")//$NON-NLS-1$
      )
      {
         return true;
      }
      if (filename.endsWith("jspx")//$NON-NLS-1$
      )
      {
         return true;
      }
      if (filename.endsWith("jspf")//$NON-NLS-1$
      )
      {
         return true;
      }
      return false;
   }

   /** Description of the Method */
   private void initializeServletContext()
   {
      try
      {
         String uriRoot = this.getEnvironment().getUriRoot();

         this.servletContext = new JSPServletContext(new PrintWriter(System.out), new File(uriRoot).toURL());
         this.getEnvironment().setContext(this.servletContext);

         TldLocationsCache tldLocationsCache = new JSPTldLocationsCache(servletContext, true, this.getClassLoader());
         this.getEnvironment().setTldLocationsCache(tldLocationsCache);

         JspRuntimeContext rctxt = new JspRuntimeContext(servletContext, this.getEnvironment());
         this.getEnvironment().setRuntimeContext(rctxt);

         JspConfig jspConfig = new JspConfig(servletContext);
         this.getEnvironment().setJspConfig(jspConfig);

         TagPluginManager tagPluginManager = new TagPluginManager(servletContext);
         this.getEnvironment().setTagPluginManager(tagPluginManager);
      }
      catch (MalformedURLException mfue)
      {
         AbstractPlugin.log(mfue);
      }
   }

   /**
    * Description of the Method
    *
    * @param file             Description of the Parameter
    * @param javaCompilation  Description of the Parameter
    * @return                 Description of the Return Value
    */
   private JSPElementInfo internalCompileJSP(IFile file, boolean javaCompilation)
   {
      JSPElementInfo info = null;

      if (this.isInDocroot(file))
      {
         try
         {
            JSPMarkerFactory.deleteMarkers(file);
            info = this.parseJSP(file, this.getClassLoader());
            if (javaCompilation)
            {
               this.compileJava(info);
            }
         }
         catch (CoreException e)
         {
            AbstractPlugin.log(e);
         }
         catch (FileNotFoundException e)
         {
            AbstractPlugin.log(e);
         }
         catch (JasperException e)
         {
            AbstractPlugin.log(e);
         }
         catch (IOException e)
         {
            AbstractPlugin.log(e);
         }
         catch (Throwable t)
         {
            // Swallow any another exception
            // Jasper is not so good at catching NPE or IAE
         }
      }

      return info;
   }

   /**
    * Gets the inDocroot attribute of the JSPProject object
    *
    * @param file  Description of the Parameter
    * @return      The inDocroot value
    */
   private boolean isInDocroot(IFile file)
   {
      IPath path = file.getProjectRelativePath();
      IPath rootPath = this.getUriRootFolder().getProjectRelativePath();
      return (rootPath.isPrefixOf(path));
   }

   /**
    * Description of the Method
    *
    * @param file                          Description of the Parameter
    * @param loader                        Description of the Parameter
    * @return                              Description of the Return Value
    * @exception FileNotFoundException     Description of the Exception
    * @exception JasperException           Description of the Exception
    * @exception IOException               Description of the Exception
    * @exception IllegalArgumentException  Description of the Exception
    * @exception NullPointerException      Description of the Exception
    */
   private JSPElementInfo parseJSP(IFile file, URLClassLoader loader) throws FileNotFoundException, JasperException,
         IOException, IllegalArgumentException, NullPointerException
   {
      Node.Nodes pageNodes = null;
      String[] smapStr = null;
      String jspUri = this.getJSPURI(file);

      JspCompilationContext clctxt = this.getJspCompilationContext(jspUri);
      clctxt.setClassLoader(loader);

      String javaFileName = clctxt.getServletJavaFileName();
      String javaEncoding = clctxt.getOptions().getJavaEncoding();

      JSPElementInfo info = new JSPElementInfo(file, jspUri, javaFileName, javaEncoding);
      ErrorDispatcher errDispatcher = info.getErrorDispatcher();

      PageInfo pageInfo = new PageInfo(new BeanRepository(clctxt.getClassLoader(), errDispatcher));

      pageInfo.setELIgnored(true);
      pageInfo.setScriptingInvalid(false);
      pageInfo.setIncludePrelude(new Vector());
      pageInfo.setIncludeCoda(new Vector());

      ServletWriter writer = null;

      StringWriter osw = new StringWriter();
      writer = new ServletWriter(new PrintWriter(osw));
      clctxt.setWriter(writer);

      // Reset the temporary variable counter for the generator.
      JspUtil.resetTemporaryVariableName();

      // Parse the file
      org.apache.jasper.compiler.Compiler compiler = new JSPCompiler(clctxt, errDispatcher, pageInfo);
      ParserController parserCtl = new JSPParserController(clctxt, compiler, errDispatcher);
      pageNodes = parserCtl.parse(clctxt.getJspFile());
      info.setNodes(pageNodes);

      // Validate and process attributes
      Validator.validate(compiler, pageNodes);

      // Collect page info
      Collector.collect(compiler, pageNodes);

      // Compile (if necessary) and load the tag files referenced in
      // this compilation unit.
      TagFileProcessor tfp = new TagFileProcessor();
      tfp.loadTagFiles(compiler, pageNodes);

      // Determine which custom tag needs to declare which scripting vars
      ScriptingVariabler.set(pageNodes, errDispatcher);

      // Optimizations by Tag Plugins
      TagPluginManager tagPluginManager = jasperEnvironment.getTagPluginManager();
      tagPluginManager.apply(pageNodes, errDispatcher, pageInfo);

      // Optimization: concatenate contiguous template texts.
      TextOptimizer.concatenate(compiler, pageNodes);

      // Generate static function mapper codes.
      ELFunctionMapper.map(compiler, pageNodes);

      // generate servlet .java file
      Generator.generate(writer, compiler, pageNodes);
      writer.close();
      writer = null;

      // The writer is only used during the compile, dereference
      // it in the JspCompilationContext when done to allow it
      // to be GC'd and save memory.
      clctxt.setWriter(null);

      info.setContent(osw.toString());
      info.setPageInfo(pageInfo);

      File outputFile = new File(info.getJavaFileName());
      outputFile.getParentFile().mkdirs();
      FileWriter fw = new FileWriter(outputFile);
      fw.write(info.getContent());
      fw.close();

      //
      // JSR45 Support
      if (!jasperEnvironment.isSmapSuppressed())
      {
         smapStr = SmapUtil.generateSmap(clctxt, pageNodes);
         info.setSmap(smapStr);
      }

      return info;
   }
}
