/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.xdoclet.assist.model;

import java.io.Serializable;

import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.JavaModelException;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   17 mai 2003
 */
public class DocletTree implements Serializable
{

   /** Description of the Field */
   protected SortedKeyTree keyTree;
   /** Description of the Field */
   protected DocletElement rootDoclet;

   // Depth of the value-attribute in regard to the tree-structure (index starts with 0)
   private static int VALUE_DEPTH = 3;


   /** Constructor for DocletDataTree. */
   public DocletTree()
   {
      keyTree = new SortedKeyTree();
      rootDoclet = new DocletElement(keyTree.getRoot(), this);
   }

   // pass-through root
   /**
    * Adds a feature to the Child attribute of the DocletTree object
    *
    * @param name  The feature to be added to the Child attribute
    * @return      Description of the Return Value
    */
   public DocletElement addChild(String name)
   {
      return rootDoclet.addChild(name);
   }


   /**
    * Gets the child attribute of the DocletTree object
    *
    * @param name  Description of the Parameter
    * @return      The child value
    */
   public DocletElement getChild(String name)
   {
      return rootDoclet.getChild(name);
   }


   /**
    * Gets the child attribute of the DocletTree object
    *
    * @param name                    Description of the Parameter
    * @param member                  Description of the Parameter
    * @return                        The child value
    * @exception JavaModelException  Description of the Exception
    */
   public DocletElement getChild(String name, IMember member)
          throws JavaModelException
   {
      return rootDoclet.getChild(name, member);
   }


   /**
    * Gets the childrenCount attribute of the DocletTree object
    *
    * @return   The childrenCount value
    */
   public int getChildrenCount()
   {
      return rootDoclet.getChildrenCount();
   }


   /**
    * Gets the childrenElements attribute of the DocletTree object
    *
    * @param member                  Description of the Parameter
    * @return                        The childrenElements value
    * @exception JavaModelException  Description of the Exception
    */
   public DocletElement[] getChildrenElements(IMember member)
          throws JavaModelException
   {
      return rootDoclet.getChildrenElements(member);
   }


   /**
    * Gets the childrenElements attribute of the DocletTree object
    *
    * @return   The childrenElements value
    */
   public DocletElement[] getChildrenElements()
   {
      return rootDoclet.getChildrenElements();
   }


   /**
    * @param path
    * @param indexCount
    * @return            DocletElement the searched element of null if it does not exists
    */
   public DocletElement getNode(String[] path, int indexCount)
   {
      if (path == null)
      {
         throw new IllegalArgumentException();
      }
      SortedKeyTreeNode node = keyTree.getNode(path, indexCount);
      if (node == null)
      {
         return null;
      }
      return (DocletElement) node.getObject(getKey());
   }


   /**
    * @param path
    * @return      DocletElement the searched element of null if it does not exists
    */
   public DocletElement getNode(String[] path)
   {
      if (path == null)
      {
         throw new IllegalArgumentException();
      }
      return getNode(path, path.length - 1);
   }


   /**
    * Description of the Method
    *
    * @param member                  Description of the Parameter
    * @return                        Description of the Return Value
    * @exception JavaModelException  Description of the Exception
    */
   public boolean hasChildren(IMember member) throws JavaModelException
   {
      return rootDoclet.hasChildren(member);
   }


   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public boolean hasChildren()
   {
      return rootDoclet.hasChildren();
   }


   /**
    * Returns whether a path points to an existing element or not.
    *
    * @param path           The path elements that have to be chechked
    * @param checkNoValues  Description of the Parameter
    * @return               boolean returns whether the element exists or not
    */
   public boolean isNode(String[] path, boolean checkNoValues)
   {
      DocletElement doclet;

      // If the path points to value which is a nondiscrete go in the loop only to the attribute
      // because the value does not exists in the xml-data but is valid anyway.
      if (path.length - 1 == VALUE_DEPTH && checkNoValues)
      {
         doclet = getNode(path, path.length - 2);
         if (doclet == null)
         {
            return false;
         }
         if (doclet.getNode()
               .getAdditionalAttributes()
               .get(IDocletConstants.ATTR_DISCRETE_VALUE_RANGE)
               != null)
         {
            if (doclet.getNode().getChild(path[VALUE_DEPTH]) == null)
            {
               return false;
            }
         }
         return true;
      }
      doclet = getNode(path);

      // if node does not exists
      if (doclet == null)
      {
         return false;
      }
      return true;
   }


   /**
    * Gets the key attribute of the DocletTree object
    *
    * @return   The key value
    */
   protected String getKey()
   {
      return DocletElement.class.getName();
   }


   /**
    * Gets the root attribute of the DocletTree object
    *
    * @return   The root value
    */
   protected DocletElement getRoot()
   {
      return rootDoclet;
   }
}
