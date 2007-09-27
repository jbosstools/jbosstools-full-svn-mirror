/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.j2ee.ui.actions.ejb;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.jdt.ui.actions.AddClassFragmentAction;
import org.jboss.ide.eclipse.jdt.ui.wizards.util.TypeHierarchyUtil;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public abstract class AddEJBFragmentAction extends AddClassFragmentAction
{
   /**Constructor for the AddEJBFragmentAction object */
   public AddEJBFragmentAction() { }


   /** Description of the Method */
   protected void checkEnabled()
   {
      this.setEnable(false);

      if (this.getSelection() != null && (this.getSelection() instanceof IStructuredSelection))
      {
         try
         {
            IStructuredSelection iSelection = (IStructuredSelection) this.getSelection();
            Object element = iSelection.getFirstElement();
            if (element instanceof IType)
            {
               IType type = (IType) element;
               ITypeHierarchy hierarchy = TypeHierarchyUtil.getInstance().getTypeHierarchy(type);

               if (hierarchy != null)
               {
                  IType[] types = hierarchy.getAllInterfaces();
                  List interfaces = new ArrayList();
                  for (int i = 0; i < types.length; i++)
                  {
                     interfaces.add(types[i].getFullyQualifiedName());
                  }

                  String[] requiredInterfaces = this.getRequiredInterfaces();
                  for (int i = 0; i < requiredInterfaces.length; i++)
                  {
                     if (interfaces.contains(requiredInterfaces[i]))
                     {
                        this.setEnable(true);
                        break;
                     }
                  }
               }
            }
         }
         catch (JavaModelException jme)
         {
            AbstractPlugin.logError("Cannot find hierarchy or the interfaces", jme);//$NON-NLS-1$
         }
      }
   }


   /**
    * Gets the requiredInterfaces attribute of the AddEJBFragmentAction object
    *
    * @return   The requiredInterfaces value
    */
   protected abstract String[] getRequiredInterfaces();
}
