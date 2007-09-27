/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.xdoclet.run.util;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.jboss.ide.eclipse.xdoclet.run.model.XDocletData;

/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 * @created   20 mars 2003
 * @todo      Javadoc to complete
 */
public class ConfigurationContentProvider implements ITreeContentProvider
{
   /**Constructor for the ConfigurationContentProvider object */
   public ConfigurationContentProvider()
   {
      super();
   }


   /**
    *Description of the Method
    *
    * @see   org.eclipse.jface.viewers.IContentProvider#dispose()
    */
   public void dispose() { }


   /**
    * Gets the children attribute of the ConfigurationContentProvider object
    *
    * @param parentElement  Description of the Parameter
    * @return               The children value
    * @see                  org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
    */
   public Object[] getChildren(Object parentElement)
   {
      if (parentElement instanceof XDocletData)
      {
         return ((XDocletData) parentElement).getNodes().toArray();
      }
      return null;
   }


   /**
    * Gets the elements attribute of the ConfigurationContentProvider object
    *
    * @param inputElement  Description of the Parameter
    * @return              The elements value
    * @see                 org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
    */
   public Object[] getElements(Object inputElement)
   {
      if (inputElement instanceof XDocletData)
      {
         return ((XDocletData) inputElement).getNodes().toArray();
      }
      return null;
   }


   /**
    * Gets the parent attribute of the ConfigurationContentProvider object
    *
    * @param element  Description of the Parameter
    * @return         The parent value
    * @see            org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
    */
   public Object getParent(Object element)
   {
      if (element instanceof XDocletData)
      {
         return ((XDocletData) element).getParent();
      }
      return null;
   }


   /**
    * Description of the Method
    *
    * @param element  Description of the Parameter
    * @return         Description of the Return Value
    * @see            org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
    */
   public boolean hasChildren(Object element)
   {
      if (element instanceof XDocletData)
      {
         return !((XDocletData) element).isEmpty();
      }
      return false;
   }


   /**
    * Description of the Method
    *
    * @param viewer    Description of the Parameter
    * @param oldInput  Description of the Parameter
    * @param newInput  Description of the Parameter
    * @see             org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
    */
   public void inputChanged(Viewer viewer, Object oldInput, Object newInput) { }
}
