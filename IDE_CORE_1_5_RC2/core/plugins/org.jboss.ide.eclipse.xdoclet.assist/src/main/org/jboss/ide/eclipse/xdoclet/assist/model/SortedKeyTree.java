/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.xdoclet.assist.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   17 mai 2003
 */
public class SortedKeyTree implements Serializable
{
   /** Description of the Field */
   protected SortedKeyTreeNode root;
   /** Description of the Field */
   protected final static String ROOT_NAME = "root";//$NON-NLS-1$


   /** Constructor for SortedKeyTree. */
   public SortedKeyTree()
   {
      super();
      root = new SortedKeyTreeNode(ROOT_NAME, null, this);
   }


   /**
    * Adds a feature to the Child attribute of the SortedKeyTree object
    *
    * @param name  The feature to be added to the Child attribute
    * @return      Description of the Return Value
    */
   public SortedKeyTreeNode addChild(String name)
   {
      return root.addChild(name);
   }


   /**
    * Gets the child attribute of the SortedKeyTree object
    *
    * @param key  Description of the Parameter
    * @return     The child value
    */
   public SortedKeyTreeNode getChild(String key)
   {
      return root.getChild(key);
   }

   // Pass-through root

   /**
    * Gets the children attribute of the SortedKeyTree object
    *
    * @return   The children value
    */
   public SortedKeyTreeNode[] getChildren()
   {
      return root.getChildren();
   }


   /**
    * Gets the childrenCount attribute of the SortedKeyTree object
    *
    * @return   The childrenCount value
    */
   public int getChildrenCount()
   {
      return root.getChildrenCount();
   }


   /**
    * Gets the childrenObjects attribute of the SortedKeyTree object
    *
    * @param key  Description of the Parameter
    * @return     The childrenObjects value
    */
   public List getChildrenObjects(String key)
   {
      return root.getChildrenObjects(key);
   }


   /**
    * Gets the node attribute of the SortedKeyTree object
    *
    * @param path        Description of the Parameter
    * @param indexCount  Description of the Parameter
    * @return            The node value
    */
   public SortedKeyTreeNode getNode(String[] path, int indexCount)
   {
      if (path == null || indexCount < 0 || (path.length - 1) < indexCount)
      {
         throw new IllegalArgumentException();
      }
      SortedKeyTreeNode node = root;
      for (int i = 0; i <= indexCount; i++)
      {
         node = node.getChild(path[i]);
         if (node == null)
         {
            return null;
         }
      }
      return node;
   }


   /**
    * Gets the node attribute of the SortedKeyTree object
    *
    * @param path  Description of the Parameter
    * @return      The node value
    */
   public SortedKeyTreeNode getNode(String[] path)
   {
      if (path == null)
      {
         throw new IllegalArgumentException();
      }
      return getNode(path, path.length - 1);
   }


   /**
    * Gets the object attribute of the SortedKeyTree object
    *
    * @param key  Description of the Parameter
    * @return     The object value
    */
   public Object getObject(String key)
   {
      return root.getObject(key);
   }


   /**
    * Gets the root attribute of the SortedKeyTree object
    *
    * @return   The root value
    */
   public SortedKeyTreeNode getRoot()
   {
      return root;
   }


   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public boolean hasChildren()
   {
      return root.hasChildren();
   }


   /**
    * Returns whether a path points to an existing element or not.
    *
    * @param path  The path elements that have to be chechked
    * @return      boolean returns whether the element exists or not
    */
   public boolean isNode(String[] path)
   {
      SortedKeyTreeNode node = getNode(path);
      if (node == null)
      {
         return false;
      }
      return true;
   }


   /**
    * Description of the Method
    *
    * @param key  Description of the Parameter
    */
   public void removeObject(String key)
   {
      root.removeObject(key);
   }


   /**
    * Sets the object attribute of the SortedKeyTree object
    *
    * @param key     The new object value
    * @param object  The new object value
    */
   public void setObject(String key, Object object)
   {
      root.setObject(key, object);
   }
}
