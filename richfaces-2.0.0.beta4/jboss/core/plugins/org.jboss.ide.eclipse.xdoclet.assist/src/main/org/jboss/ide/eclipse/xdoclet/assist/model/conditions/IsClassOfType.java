/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.xdoclet.assist.model.conditions;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   17 mai 2003
 */
public class IsClassOfType extends Condition
{
   private final String className;


   /**
    *Constructor for the IsClassOfType object
    *
    * @param className  Description of the Parameter
    */
   public IsClassOfType(String className)
   {
      this.className = className;
   }


   /**
    * Description of the Method
    *
    * @param member                  Description of the Parameter
    * @return                        Description of the Return Value
    * @exception JavaModelException  Description of the Exception
    */
   public boolean evalInternal(IMember member) throws JavaModelException
   {
      IType type = (new JavaElementAnalyzer(member)).getType();
      if (type != null)
      {
         if (type.getFullyQualifiedName().equals(className))
         {
            return true;
         }
         ITypeHierarchy hierarchy =
               type.newSupertypeHierarchy(new NullProgressMonitor());
         IType[] superTypes = hierarchy.getAllSupertypes(type);
         for (int i = 0; i < superTypes.length; i++)
         {
            if (superTypes[i].getFullyQualifiedName().equals(className))
            {
               return true;
            }
         }
      }
      return false;
   }


   /**
    * Returns the className.
    *
    * @return   String
    */
   public String getClassName()
   {
      return className;
   }


   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public String toString()
   {
      return getClass().getName() + " : " + className;//$NON-NLS-1$
   }

}
