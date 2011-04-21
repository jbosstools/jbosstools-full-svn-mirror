/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.xml.ui.outline;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/*
 * This file contains materials derived from the
 * XMen project. License can be found at :
 * http://www.eclipse.org/legal/cpl-v10.html
 */
/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class XMLChildProvider implements ITreeContentProvider
{

   /**Constructor for the XMLChildProvider object */
   public XMLChildProvider() { }


   /*
    * @see IContentProvider#dispose()
    */
   /** Description of the Method */
   public void dispose()
   {
   }


   /*
    * @see ITreeContentProvider#getChildren(Object)
    */
   /**
    * Gets the children attribute of the XMLChildProvider object
    *
    * @param parent  Description of the Parameter
    * @return        The children value
    */
   public Object[] getChildren(Object parent)
   {
      /*
       * if (parent == XMLElement.e1) {
       * IAdaptable[] list = new IAdaptable[1];
       * list[0] = XMLElement.e2;
       * return list;
       * }
       */
      return null;
   }


   /*
    * @see IStructuredContentProvider#getElements(Object)
    */
   /**
    * Gets the elements attribute of the XMLChildProvider object
    *
    * @param arg0  Description of the Parameter
    * @return      The elements value
    */
   public Object[] getElements(Object arg0)
   {
      return getChildren(arg0);
   }


   /*
    * @see ITreeContentProvider#getParent(Object)
    */
   /**
    * Gets the parent attribute of the XMLChildProvider object
    *
    * @param child  Description of the Parameter
    * @return       The parent value
    */
   public Object getParent(Object child)
   {
      /*
       * if (child == XMLElement.e2) {
       * return XMLElement.e1;
       * }
       */
      return null;
   }


   /*
    * @see ITreeContentProvider#hasChildren(Object)
    */
   /**
    * Description of the Method
    *
    * @param parent  Description of the Parameter
    * @return        Description of the Return Value
    */
   public boolean hasChildren(Object parent)
   {
      return getChildren(parent) != null;
   }


   /*
    * @see IContentProvider#inputChanged(Viewer, Object, Object)
    */
   /**
    * Description of the Method
    *
    * @param arg0  Description of the Parameter
    * @param arg1  Description of the Parameter
    * @param arg2  Description of the Parameter
    */
   public void inputChanged(Viewer arg0, Object arg1, Object arg2)
   {
   }

}
