/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.j2ee.jsp.core.compiler.jasper;

import java.util.Map;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.jdt.core.IBuffer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IOpenable;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IRegion;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.WorkingCopyOwner;
import org.eclipse.jdt.core.eval.IEvaluationContext;
import org.eclipse.jdt.internal.core.JavaProject;
import org.eclipse.jdt.internal.core.builder.NameEnvironment;
import org.jboss.ide.eclipse.jdt.j2ee.core.classpath.J2EE14ClasspathContainer;
import org.jboss.ide.eclipse.jdt.j2ee.jsp.core.classpath.JasperClasspathContainer;

/**
 * Extends the NameEnvironment for a JavaProject to dynamically add
 * the required libraries to successfully compile the JSP Java source files.
 *
 * When a JSP Java source file is compiler, Jasper needs the Jasper runtime
 * library and the J2EE 1.4 Servlet/JSP libraries.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class JSPNameEnvironment extends NameEnvironment
{

   /**
    * Initialize with a decorator.
    *
    * @param javaProject  Description of the Parameter
    */
   public JSPNameEnvironment(IJavaProject javaProject)
   {
      super(new JavaProjectDecorator(javaProject));
   }


   /**
    * JavaProject decorator that returns hacked IClasspathEntry[] to
    * include the needed libraries.
    *
    * @author    Laurent Etiemble
    * @version   $Revision$
    */
   private static class JavaProjectDecorator extends JavaProject implements IJavaProject
   {
      private IClasspathEntry[] j2eeEntries;
      private IClasspathEntry[] jasperEntries;
      private JavaProject project;


      /**
       *Constructor for the JavaProjectDecorator object
       *
       * @param project  Description of the Parameter
       */
      public JavaProjectDecorator(IJavaProject project)
      {
         this.project = (JavaProject) project;

         this.j2eeEntries = new J2EE14ClasspathContainer(new Path(J2EE14ClasspathContainer.CLASSPATH_CONTAINER)).getClasspathEntries();
         this.jasperEntries = new JasperClasspathContainer(new Path(JasperClasspathContainer.CLASSPATH_CONTAINER)).getClasspathEntries();
      }


      /**
       * Description of the Method
       *
       * @exception JavaModelException  Description of the Exception
       */
      public void close()
         throws JavaModelException
      {
         project.close();
      }


      /**
       * Description of the Method
       *
       * @param obj  Description of the Parameter
       * @return     Description of the Return Value
       */
      public boolean equals(Object obj)
      {
         return project.equals(obj);
      }


      /**
       * Description of the Method
       *
       * @return   Description of the Return Value
       */
      public boolean exists()
      {
         return project.exists();
      }


      /**
       * Description of the Method
       *
       * @param path                    Description of the Parameter
       * @return                        Description of the Return Value
       * @exception JavaModelException  Description of the Exception
       */
      public IJavaElement findElement(IPath path)
         throws JavaModelException
      {
         return project.findElement(path);
      }


      /**
       * Description of the Method
       *
       * @param path                    Description of the Parameter
       * @param owner                   Description of the Parameter
       * @return                        Description of the Return Value
       * @exception JavaModelException  Description of the Exception
       */
      public IJavaElement findElement(IPath path, WorkingCopyOwner owner)
         throws JavaModelException
      {
         return project.findElement(path, owner);
      }


      /**
       * Description of the Method
       *
       * @param path                    Description of the Parameter
       * @return                        Description of the Return Value
       * @exception JavaModelException  Description of the Exception
       */
      public IPackageFragment findPackageFragment(IPath path)
         throws JavaModelException
      {
         return project.findPackageFragment(path);
      }


      /**
       * Description of the Method
       *
       * @param path                    Description of the Parameter
       * @return                        Description of the Return Value
       * @exception JavaModelException  Description of the Exception
       */
      public IPackageFragmentRoot findPackageFragmentRoot(IPath path)
         throws JavaModelException
      {
         return project.findPackageFragmentRoot(path);
      }


      /**
       * Description of the Method
       *
       * @param entry  Description of the Parameter
       * @return       Description of the Return Value
       */
      public IPackageFragmentRoot[] findPackageFragmentRoots(IClasspathEntry entry)
      {
         return project.findPackageFragmentRoots(entry);
      }


      /**
       * Description of the Method
       *
       * @param fullyQualifiedName      Description of the Parameter
       * @return                        Description of the Return Value
       * @exception JavaModelException  Description of the Exception
       */
      public IType findType(String fullyQualifiedName)
         throws JavaModelException
      {
         return project.findType(fullyQualifiedName);
      }


      /**
       * Description of the Method
       *
       * @param packageName             Description of the Parameter
       * @param typeQualifiedName       Description of the Parameter
       * @return                        Description of the Return Value
       * @exception JavaModelException  Description of the Exception
       */
      public IType findType(String packageName, String typeQualifiedName)
         throws JavaModelException
      {
         return project.findType(packageName, typeQualifiedName);
      }


      /**
       * Description of the Method
       *
       * @param packageName             Description of the Parameter
       * @param typeQualifiedName       Description of the Parameter
       * @param owner                   Description of the Parameter
       * @return                        Description of the Return Value
       * @exception JavaModelException  Description of the Exception
       */
      public IType findType(String packageName, String typeQualifiedName, WorkingCopyOwner owner)
         throws JavaModelException
      {
         return project.findType(packageName, typeQualifiedName, owner);
      }


      /**
       * Description of the Method
       *
       * @param fullyQualifiedName      Description of the Parameter
       * @param owner                   Description of the Parameter
       * @return                        Description of the Return Value
       * @exception JavaModelException  Description of the Exception
       */
      public IType findType(String fullyQualifiedName, WorkingCopyOwner owner)
         throws JavaModelException
      {
         return project.findType(fullyQualifiedName, owner);
      }


      /**
       * Gets the adapter attribute of the JavaProjectDecorator object
       *
       * @param adapter  Description of the Parameter
       * @return         The adapter value
       */
      public Object getAdapter(Class adapter)
      {
         return project.getAdapter(adapter);
      }


      /**
       * Gets the allPackageFragmentRoots attribute of the JavaProjectDecorator object
       *
       * @return                        The allPackageFragmentRoots value
       * @exception JavaModelException  Description of the Exception
       */
      public IPackageFragmentRoot[] getAllPackageFragmentRoots()
         throws JavaModelException
      {
         return project.getAllPackageFragmentRoots();
      }


      /**
       * Gets the ancestor attribute of the JavaProjectDecorator object
       *
       * @param ancestorType  Description of the Parameter
       * @return              The ancestor value
       */
      public IJavaElement getAncestor(int ancestorType)
      {
         return project.getAncestor(ancestorType);
      }


      /**
       * Gets the buffer attribute of the JavaProjectDecorator object
       *
       * @return                        The buffer value
       * @exception JavaModelException  Description of the Exception
       */
      public IBuffer getBuffer()
         throws JavaModelException
      {
         return project.getBuffer();
      }


      /**
       * Gets the children attribute of the JavaProjectDecorator object
       *
       * @return                        The children value
       * @exception JavaModelException  Description of the Exception
       */
      public IJavaElement[] getChildren()
         throws JavaModelException
      {
         return project.getChildren();
      }


      /**
       * Gets the correspondingResource attribute of the JavaProjectDecorator object
       *
       * @return                        The correspondingResource value
       * @exception JavaModelException  Description of the Exception
       */
      public IResource getCorrespondingResource()
         throws JavaModelException
      {
         return project.getCorrespondingResource();
      }


      /**
       * Gets the cycleMarker attribute of the JavaProjectDecorator object
       *
       * @return   The cycleMarker value
       */
      public IMarker getCycleMarker()
      {
         return this.project.getCycleMarker();
      }


      /**
       * Gets the elementName attribute of the JavaProjectDecorator object
       *
       * @return   The elementName value
       */
      public String getElementName()
      {
         return project.getElementName();
      }


      /**
       * Gets the elementType attribute of the JavaProjectDecorator object
       *
       * @return   The elementType value
       */
      public int getElementType()
      {
         return project.getElementType();
      }


      /**
       * Insert J2EE 1.4 and Jasper libraries before Project classpath
       *
       * @param ignoreUnresolvedVariable  Description of the Parameter
       * @param generateMarkerOnError     Description of the Parameter
       * @param preferredClasspaths       Description of the Parameter
       * @param preferredOutputs          Description of the Parameter
       * @return                          The expandedClasspath value
       * @exception JavaModelException    Description of the Exception
       */
      public IClasspathEntry[] getExpandedClasspath(boolean ignoreUnresolvedVariable, boolean generateMarkerOnError, Map preferredClasspaths,
         Map preferredOutputs)
         throws JavaModelException
      {
         IClasspathEntry[] entries = this.project.getExpandedClasspath(ignoreUnresolvedVariable, generateMarkerOnError, preferredClasspaths,
            preferredOutputs);

         IClasspathEntry[] newEntries = new IClasspathEntry[j2eeEntries.length + jasperEntries.length + entries.length];
         System.arraycopy(this.j2eeEntries, 0, newEntries, 0, this.j2eeEntries.length);
         System.arraycopy(this.jasperEntries, 0, newEntries, this.j2eeEntries.length, this.jasperEntries.length);
         System.arraycopy(entries, 0, newEntries, this.j2eeEntries.length + this.jasperEntries.length, entries.length);
         return newEntries;
      }


      /**
       * Gets the handleIdentifier attribute of the JavaProjectDecorator object
       *
       * @return   The handleIdentifier value
       */
      public String getHandleIdentifier()
      {
         return project.getHandleIdentifier();
      }


      /**
       * Gets the javaModel attribute of the JavaProjectDecorator object
       *
       * @return   The javaModel value
       */
      public IJavaModel getJavaModel()
      {
         return project.getJavaModel();
      }


      /**
       * Gets the javaProject attribute of the JavaProjectDecorator object
       *
       * @return   The javaProject value
       */
      public IJavaProject getJavaProject()
      {
         return project.getJavaProject();
      }


      /**
       * Gets the nonJavaResources attribute of the JavaProjectDecorator object
       *
       * @return                        The nonJavaResources value
       * @exception JavaModelException  Description of the Exception
       */
      public Object[] getNonJavaResources()
         throws JavaModelException
      {
         return project.getNonJavaResources();
      }


      /**
       * Gets the openable attribute of the JavaProjectDecorator object
       *
       * @return   The openable value
       */
      public IOpenable getOpenable()
      {
         return project.getOpenable();
      }


      /**
       * Gets the option attribute of the JavaProjectDecorator object
       *
       * @param optionName              Description of the Parameter
       * @param inheritJavaCoreOptions  Description of the Parameter
       * @return                        The option value
       */
      public String getOption(String optionName, boolean inheritJavaCoreOptions)
      {
         return project.getOption(optionName, inheritJavaCoreOptions);
      }


      /**
       * Gets the options attribute of the JavaProjectDecorator object
       *
       * @param inheritJavaCoreOptions  Description of the Parameter
       * @return                        The options value
       */
      public Map getOptions(boolean inheritJavaCoreOptions)
      {
         return project.getOptions(inheritJavaCoreOptions);
      }


      /**
       * Gets the outputLocation attribute of the JavaProjectDecorator object
       *
       * @return                        The outputLocation value
       * @exception JavaModelException  Description of the Exception
       */
      public IPath getOutputLocation()
         throws JavaModelException
      {
         return project.getOutputLocation();
      }


      /**
       * Gets the packageFragmentRoot attribute of the JavaProjectDecorator object
       *
       * @param jarPath  Description of the Parameter
       * @return         The packageFragmentRoot value
       */
      public IPackageFragmentRoot getPackageFragmentRoot(String jarPath)
      {
         return project.getPackageFragmentRoot(jarPath);
      }


      /**
       * Gets the packageFragmentRoot attribute of the JavaProjectDecorator object
       *
       * @param resource  Description of the Parameter
       * @return          The packageFragmentRoot value
       */
      public IPackageFragmentRoot getPackageFragmentRoot(IResource resource)
      {
         return project.getPackageFragmentRoot(resource);
      }


      /**
       * Gets the packageFragmentRoots attribute of the JavaProjectDecorator object
       *
       * @return                        The packageFragmentRoots value
       * @exception JavaModelException  Description of the Exception
       */
      public IPackageFragmentRoot[] getPackageFragmentRoots()
         throws JavaModelException
      {
         return project.getPackageFragmentRoots();
      }

      //        public IPackageFragmentRoot[] getPackageFragmentRoots(IClasspathEntry entry) {
      //            return project.getPackageFragmentRoots(entry);
      //        }

      /**
       * Gets the packageFragments attribute of the JavaProjectDecorator object
       *
       * @return                        The packageFragments value
       * @exception JavaModelException  Description of the Exception
       */
      public IPackageFragment[] getPackageFragments()
         throws JavaModelException
      {
         return project.getPackageFragments();
      }


      /**
       * Gets the parent attribute of the JavaProjectDecorator object
       *
       * @return   The parent value
       */
      public IJavaElement getParent()
      {
         return project.getParent();
      }


      /**
       * Gets the path attribute of the JavaProjectDecorator object
       *
       * @return   The path value
       */
      public IPath getPath()
      {
         return project.getPath();
      }


      /**
       * Gets the primaryElement attribute of the JavaProjectDecorator object
       *
       * @return   The primaryElement value
       */
      public IJavaElement getPrimaryElement()
      {
         return project.getPrimaryElement();
      }


      /**
       * Gets the project attribute of the JavaProjectDecorator object
       *
       * @return   The project value
       */
      public IProject getProject()
      {
         return project.getProject();
      }


      /**
       * Gets the rawClasspath attribute of the JavaProjectDecorator object
       *
       * @return                        The rawClasspath value
       * @exception JavaModelException  Description of the Exception
       */
      public IClasspathEntry[] getRawClasspath()
         throws JavaModelException
      {
         return project.getRawClasspath();
      }


      /**
       * Gets the requiredProjectNames attribute of the JavaProjectDecorator object
       *
       * @return                        The requiredProjectNames value
       * @exception JavaModelException  Description of the Exception
       */
      public String[] getRequiredProjectNames()
         throws JavaModelException
      {
         return project.getRequiredProjectNames();
      }


      /**
       * Gets the resolvedClasspath attribute of the JavaProjectDecorator object
       *
       * @param ignoreUnresolvedEntry   Description of the Parameter
       * @return                        The resolvedClasspath value
       * @exception JavaModelException  Description of the Exception
       */
      public IClasspathEntry[] getResolvedClasspath(boolean ignoreUnresolvedEntry)
         throws JavaModelException
      {
         return project.getResolvedClasspath(ignoreUnresolvedEntry);
      }


      /**
       * Gets the resource attribute of the JavaProjectDecorator object
       *
       * @return   The resource value
       */
      public IResource getResource()
      {
         return project.getResource();
      }


      /**
       * Gets the schedulingRule attribute of the JavaProjectDecorator object
       *
       * @return   The schedulingRule value
       */
      public ISchedulingRule getSchedulingRule()
      {
         return project.getSchedulingRule();
      }


      /**
       * Gets the underlyingResource attribute of the JavaProjectDecorator object
       *
       * @return                        The underlyingResource value
       * @exception JavaModelException  Description of the Exception
       */
      public IResource getUnderlyingResource()
         throws JavaModelException
      {
         return project.getUnderlyingResource();
      }


      /**
       * Description of the Method
       *
       * @return   Description of the Return Value
       */
      public boolean hasBuildState()
      {
         return project.hasBuildState();
      }


      /**
       * Description of the Method
       *
       * @return                        Description of the Return Value
       * @exception JavaModelException  Description of the Exception
       */
      public boolean hasChildren()
         throws JavaModelException
      {
         return project.hasChildren();
      }


      /**
       * Description of the Method
       *
       * @param entries  Description of the Parameter
       * @return         Description of the Return Value
       */
      public boolean hasClasspathCycle(IClasspathEntry[] entries)
      {
         return project.hasClasspathCycle(entries);
      }


      /**
       * Description of the Method
       *
       * @return                        Description of the Return Value
       * @exception JavaModelException  Description of the Exception
       */
      public boolean hasUnsavedChanges()
         throws JavaModelException
      {
         return project.hasUnsavedChanges();
      }


      /**
       * Description of the Method
       *
       * @return   Description of the Return Value
       */
      public int hashCode()
      {
         return project.hashCode();
      }


      /**
       * Gets the consistent attribute of the JavaProjectDecorator object
       *
       * @return   The consistent value
       */
      public boolean isConsistent()
      {
         return true;
      }


      /**
       * Gets the onClasspath attribute of the JavaProjectDecorator object
       *
       * @param resource  Description of the Parameter
       * @return          The onClasspath value
       */
      public boolean isOnClasspath(IResource resource)
      {
         return project.isOnClasspath(resource);
      }


      /**
       * Gets the onClasspath attribute of the JavaProjectDecorator object
       *
       * @param element  Description of the Parameter
       * @return         The onClasspath value
       */
      public boolean isOnClasspath(IJavaElement element)
      {
         return project.isOnClasspath(element);
      }


      /**
       * Gets the open attribute of the JavaProjectDecorator object
       *
       * @return   The open value
       */
      public boolean isOpen()
      {
         return project.isOpen();
      }


      /**
       * Gets the readOnly attribute of the JavaProjectDecorator object
       *
       * @return   The readOnly value
       */
      public boolean isReadOnly()
      {
         return project.isReadOnly();
      }


      /**
       * Gets the structureKnown attribute of the JavaProjectDecorator object
       *
       * @return                        The structureKnown value
       * @exception JavaModelException  Description of the Exception
       */
      public boolean isStructureKnown()
         throws JavaModelException
      {
         return project.isStructureKnown();
      }


      /**
       * Description of the Method
       *
       * @param progress                Description of the Parameter
       * @exception JavaModelException  Description of the Exception
       */
      public void makeConsistent(IProgressMonitor progress)
         throws JavaModelException
      {
         project.makeConsistent(progress);
      }


      /**
       * Description of the Method
       *
       * @return   Description of the Return Value
       */
      public IEvaluationContext newEvaluationContext()
      {
         return project.newEvaluationContext();
      }


      /**
       * Description of the Method
       *
       * @param region                  Description of the Parameter
       * @param monitor                 Description of the Parameter
       * @return                        Description of the Return Value
       * @exception JavaModelException  Description of the Exception
       */
      public ITypeHierarchy newTypeHierarchy(IRegion region, IProgressMonitor monitor)
         throws JavaModelException
      {
         return project.newTypeHierarchy(region, monitor);
      }


      /**
       * Description of the Method
       *
       * @param region                  Description of the Parameter
       * @param owner                   Description of the Parameter
       * @param monitor                 Description of the Parameter
       * @return                        Description of the Return Value
       * @exception JavaModelException  Description of the Exception
       */
      public ITypeHierarchy newTypeHierarchy(IRegion region, WorkingCopyOwner owner, IProgressMonitor monitor)
         throws JavaModelException
      {
         return project.newTypeHierarchy(region, owner, monitor);
      }


      /**
       * Description of the Method
       *
       * @param type                    Description of the Parameter
       * @param region                  Description of the Parameter
       * @param monitor                 Description of the Parameter
       * @return                        Description of the Return Value
       * @exception JavaModelException  Description of the Exception
       */
      public ITypeHierarchy newTypeHierarchy(IType type, IRegion region, IProgressMonitor monitor)
         throws JavaModelException
      {
         return project.newTypeHierarchy(type, region, monitor);
      }


      /**
       * Description of the Method
       *
       * @param type                    Description of the Parameter
       * @param region                  Description of the Parameter
       * @param owner                   Description of the Parameter
       * @param monitor                 Description of the Parameter
       * @return                        Description of the Return Value
       * @exception JavaModelException  Description of the Exception
       */
      public ITypeHierarchy newTypeHierarchy(IType type, IRegion region, WorkingCopyOwner owner, IProgressMonitor monitor)
         throws JavaModelException
      {
         return project.newTypeHierarchy(type, region, owner, monitor);
      }


      /**
       * Description of the Method
       *
       * @param progress                Description of the Parameter
       * @exception JavaModelException  Description of the Exception
       */
      public void open(IProgressMonitor progress)
         throws JavaModelException
      {
         project.open(progress);
      }


      /**
       * Description of the Method
       *
       * @return   Description of the Return Value
       */
      public IPath readOutputLocation()
      {
         return project.readOutputLocation();
      }


      /**
       * Description of the Method
       *
       * @return   Description of the Return Value
       */
      public IClasspathEntry[] readRawClasspath()
      {
         return project.readRawClasspath();
      }


      /**
       * Description of the Method
       *
       * @param progress                Description of the Parameter
       * @param force                   Description of the Parameter
       * @exception JavaModelException  Description of the Exception
       */
      public void save(IProgressMonitor progress, boolean force)
         throws JavaModelException
      {
         project.save(progress, force);
      }


      /**
       * Sets the option attribute of the JavaProjectDecorator object
       *
       * @param optionName   The new option value
       * @param optionValue  The new option value
       */
      public void setOption(String optionName, String optionValue)
      {
         project.setOption(optionName, optionValue);
      }


      /**
       * Sets the options attribute of the JavaProjectDecorator object
       *
       * @param newOptions  The new options value
       */
      public void setOptions(Map newOptions)
      {
         project.setOptions(newOptions);
      }


      /**
       * Sets the outputLocation attribute of the JavaProjectDecorator object
       *
       * @param path                    The new outputLocation value
       * @param monitor                 The new outputLocation value
       * @exception JavaModelException  Description of the Exception
       */
      public void setOutputLocation(IPath path, IProgressMonitor monitor)
         throws JavaModelException
      {
         project.setOutputLocation(path, monitor);
      }


      /**
       * Sets the rawClasspath attribute of the JavaProjectDecorator object
       *
       * @param entries                 The new rawClasspath value
       * @param outputLocation          The new rawClasspath value
       * @param monitor                 The new rawClasspath value
       * @exception JavaModelException  Description of the Exception
       */
      public void setRawClasspath(IClasspathEntry[] entries, IPath outputLocation, IProgressMonitor monitor)
         throws JavaModelException
      {
         project.setRawClasspath(entries, outputLocation, monitor);
      }


      /**
       * Sets the rawClasspath attribute of the JavaProjectDecorator object
       *
       * @param entries                 The new rawClasspath value
       * @param monitor                 The new rawClasspath value
       * @exception JavaModelException  Description of the Exception
       */
      public void setRawClasspath(IClasspathEntry[] entries, IProgressMonitor monitor)
         throws JavaModelException
      {
         project.setRawClasspath(entries, monitor);
      }


      /**
       * Description of the Method
       *
       * @return   Description of the Return Value
       */
      public String toString()
      {
         return project.toString();
      }
   }
}
