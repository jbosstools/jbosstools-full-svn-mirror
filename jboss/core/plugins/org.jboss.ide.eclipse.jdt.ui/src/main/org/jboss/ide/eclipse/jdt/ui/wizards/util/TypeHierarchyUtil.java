/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.ui.wizards.util;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.ITypeHierarchyChangedListener;
import org.eclipse.jdt.core.JavaModelException;

/**
 * Utility class for type hierarchies manipulation
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class TypeHierarchyUtil implements ITypeHierarchyChangedListener
{
   private Map hierarchies = new HashMap();
   private static TypeHierarchyUtil instance = new TypeHierarchyUtil();


   /** Avoid instantiation */
   private TypeHierarchyUtil() { }


   /**
    * Gets the typeHierarchy attribute of the TypeHierarchyUtil object
    *
    * @param type                    Description of the Parameter
    * @return                        The typeHierarchy value
    * @exception JavaModelException  Description of the Exception
    */
   public ITypeHierarchy getTypeHierarchy(IType type)
      throws JavaModelException
   {
      if (type == null)
      {
         return null;
      }

      ITypeHierarchy hierarchy = null;
      synchronized (this.hierarchies)
      {
         hierarchy = (ITypeHierarchy) this.hierarchies.get(type);
         if (hierarchy == null)
         {
            hierarchy = type.newTypeHierarchy(new NullProgressMonitor());
            this.hierarchies.put(type, hierarchy);
            hierarchy.addTypeHierarchyChangedListener(this);
         }
      }
      return hierarchy;
   }


   /**
    * Description of the Method
    *
    * @param typeHierarchy  Description of the Parameter
    */
   public void typeHierarchyChanged(ITypeHierarchy typeHierarchy)
   {
      synchronized (this.hierarchies)
      {
         try
         {
            typeHierarchy.refresh(new NullProgressMonitor());
         }
         catch (JavaModelException jme)
         {
            // Do nothing
         }
      }
   }


   /**
    * Gets the instance attribute of the TypeHierarchyUtil class
    *
    * @return   The instance value
    */
   public static TypeHierarchyUtil getInstance()
   {
      return instance;
   }
}
