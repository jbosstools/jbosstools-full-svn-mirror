/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.xdoclet.assist.model.conditions;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   17 mai 2003
 */
public class JavaElementAnalyzer
{

   IJavaElement javaElement;


   /**
    * Constructor for JavaElementAnalyzer.
    *
    * @param javaElement  Description of the Parameter
    */
   public JavaElementAnalyzer(IJavaElement javaElement)
   {
      super();
      setJavaElement(javaElement);
   }


   /**
    * Returns the javaElement.
    *
    * @return   IJavaElement
    */
   public IJavaElement getJavaElement()
   {
      return javaElement;
   }


   /**
    * @return   The package value
    * @see      xjavadoc.XProgramElement#getContainingPackage()
    */
   public IPackageFragment getPackage()
   {
      return getType().getPackageFragment();
   }


   /**
    * @return   The type value
    * @see      xjavadoc.XProgramElement#getContainingClass()
    */
   public IType getType()
   {
      if (javaElement instanceof IMethod)
      {
         return (IType) ((IMethod) javaElement).getParent();
      }
      else if (javaElement instanceof IType)
      {
         return (IType) javaElement;
      }
      else
      {
         return null;
      }
   }


   /**
    * Sets the javaElement.
    *
    * @param javaElement  The javaElement to set
    */
   public void setJavaElement(IJavaElement javaElement)
   {
      if (javaElement == null)
      {
         throw new IllegalArgumentException();
      }
      this.javaElement = javaElement;
   }

}
