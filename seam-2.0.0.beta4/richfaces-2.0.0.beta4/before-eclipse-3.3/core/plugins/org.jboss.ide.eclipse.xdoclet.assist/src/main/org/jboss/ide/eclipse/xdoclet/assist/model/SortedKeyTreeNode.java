/*
 * JBoss, a division of Red Hat
 * Copyright 2006, Red Hat Middleware, LLC, and individual contributors as indicated
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
package org.jboss.ide.eclipse.xdoclet.assist.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   17 mai 2003
 */
public class SortedKeyTreeNode implements Comparable, Serializable
{
   /** Description of the Field */
   protected SortedKeyTree tree;

   private HashMap additionalAttributes = new HashMap();

   private TreeMap children = new TreeMap();

   private String name;

   private HashMap objects = new HashMap();

   private SortedKeyTreeNode parentNode;

   /**
    *Constructor for the SortedKeyTreeNode object
    *
    * @param name        Description of the Parameter
    * @param parentNode  Description of the Parameter
    * @param tree        Description of the Parameter
    */
   protected SortedKeyTreeNode(String name, SortedKeyTreeNode parentNode, SortedKeyTree tree)
   {
      this.name = name;
      this.parentNode = parentNode;
      this.tree = tree;
   }

   /**
    * Adds a feature to the AdditionalAttribute attribute of the SortedKeyTreeNode object
    *
    * @param name   The feature to be added to the AdditionalAttribute attribute
    * @param value  The feature to be added to the AdditionalAttribute attribute
    */
   public void addAdditionalAttribute(String name, String value)
   {
      additionalAttributes.put(name, value);
   }

   /**
    * Adds a feature to the Child attribute of the SortedKeyTreeNode object
    *
    * @param name  The feature to be added to the Child attribute
    * @return      Description of the Return Value
    */
   public SortedKeyTreeNode addChild(String name)
   {
      if (name == null)
      {
         throw new IllegalArgumentException("Can't add null to: " + getAbsoluteKey());//$NON-NLS-1$
      }
      if (children.containsKey(name))
      {
         throw new NodeExistsException("element " + getAbsoluteKey() + " " + name + " exists already: ");//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
      }
      SortedKeyTreeNode child = new SortedKeyTreeNode(name, this, tree);
      this.children.put(child.getName(), child);
      return child;
   }

   /**
    * @param o  Description of the Parameter
    * @return   Description of the Return Value
    * @see      java.lang.Comparable#compareTo(Object)
    */
   public int compareTo(Object o)
   {
      return getAbsoluteKey().compareTo(((SortedKeyTreeNode) o).getAbsoluteKey());
   }

   /**
    * @param obj  Description of the Parameter
    * @return     Description of the Return Value
    * @see        java.lang.Object#equals(Object)
    */
   public boolean equals(Object obj)
   {
      if (obj instanceof SortedKeyTreeNode)
      {
         return getAbsoluteKey().equals(((SortedKeyTreeNode) obj).getAbsoluteKey());
      }
      return false;
   }

   /**
    * Returns the additionalAttributes.
    *
    * @return   HashMap
    */
   public Map getAdditionalAttributes()
   {
      return additionalAttributes;
   }

   /**
    * Returns the children.
    *
    * @param name  Description of the Parameter
    * @return      TreeMap
    */
   public SortedKeyTreeNode getChild(String name)
   {
      return (SortedKeyTreeNode) children.get(name);
   }

   /**
    * Gets the childObject attribute of the SortedKeyTreeNode object
    *
    * @param name  Description of the Parameter
    * @param key   Description of the Parameter
    * @return      The childObject value
    */
   public Object getChildObject(String name, String key)
   {
      SortedKeyTreeNode child = getChild(name);
      if (child != null)
      {
         return child.getObject(key);
      }
      return null;
   }

   /**
    * Returns the children.
    *
    * @return   TreeMap
    */
   public SortedKeyTreeNode[] getChildren()
   {
      return (SortedKeyTreeNode[]) children.values().toArray(new SortedKeyTreeNode[children.values().size()]);
   }

   /**
    * Gets the childrenCount attribute of the SortedKeyTreeNode object
    *
    * @return   The childrenCount value
    */
   public int getChildrenCount()
   {
      return getChildren().length;
   }

   /**
    * Gets the childrenObjects attribute of the SortedKeyTreeNode object
    *
    * @param key  Description of the Parameter
    * @return     The childrenObjects value
    */
   public List getChildrenObjects(String key)
   {
      ArrayList list = new ArrayList();
      for (int i = 0; i < getChildren().length; i++)
      {
         if (getChildren()[i].getObject(key) != null)
         {
            list.add(getChildren()[i].getObject(key));
         }
      }
      return list;
   }

   /**
    * Returns the name.
    *
    * @return   String
    */
   public String getName()
   {
      return name;
   }

   /**
    * Returns the object.
    *
    * @param key  Description of the Parameter
    * @return     Object
    */
   public Object getObject(String key)
   {
      return objects.get(key);
   }

   /**
    * Gets the parentCount attribute of the SortedKeyTreeNode object
    *
    * @return   The parentCount value
    */
   public int getParentCount()
   {
      return getPath().length - 1;
   }

   /**
    * Returns the parentNode.
    *
    * @return   DocletNode
    */
   public SortedKeyTreeNode getParentNode()
   {
      if (parentNode == null)
      {
         return null;
      }
      // if root is parent return null
      if (parentNode.parentNode == null)
      {
         return null;
      }
      return parentNode;
   }

   /**
    * Gets the path attribute of the SortedKeyTreeNode object
    *
    * @return   The path value
    */
   public String[] getPath()
   {
      ArrayList list = new ArrayList();
      SortedKeyTreeNode node = this;
      list.add(this);
      while ((node = node.getParentNode()) != null)
      {
         list.add(node);
      }
      String[] path = new String[list.size()];
      for (int i = 0; i < path.length; i++)
      {
         path[i] = ((SortedKeyTreeNode) list.get(path.length - i - 1)).getName();
      }
      return path;
   }

   /**
    * Returns the tree.
    *
    * @return   SortedKeyTree
    */
   public SortedKeyTree getTree()
   {
      return tree;
   }

   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public boolean hasChildren()
   {
      return children.size() > 0;
   }

   /**
    * @return   Description of the Return Value
    * @see      java.lang.Object#hashCode()
    */
   public int hashCode()
   {
      return getAbsoluteKey().hashCode();
   }

   /** Method remove. */
   public void remove()
   {
      parentNode.children.remove(name);
   }

   /**
    * Description of the Method
    *
    * @param key  Description of the Parameter
    */
   public void removeObject(String key)
   {
      this.objects.remove(key);
   }

   /**
    * Sets the object.
    *
    * @param object  The object to set
    * @param key     The new object value
    */
   public void setObject(String key, Object object)
   {
      if (key == null)
      {
         throw new IllegalArgumentException();
      }
      this.objects.put(key, object);
   }

   /**
    * @return   Description of the Return Value
    * @see      java.lang.Object#toString()
    */
   public String toString()
   {
      return name;
   }

   /**
    * Gets the absoluteKey attribute of the SortedKeyTreeNode object
    *
    * @return   The absoluteKey value
    */
   protected String getAbsoluteKey()
   {
      String absoluteKey = "";//$NON-NLS-1$
      String[] path = getPath();
      for (int i = 0; i < path.length; i++)
      {
         absoluteKey += "/" + path[i];//$NON-NLS-1$
      }
      return absoluteKey;
   }

}
