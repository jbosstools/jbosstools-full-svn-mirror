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
import java.util.List;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   17 mai 2003
 */
public class TemplateElement implements Comparable, Serializable
{

   /** Description of the Field */
   protected DocletElement doclet;

   /** Description of the Field */
   protected SortedKeyTreeNode node;

   /** Description of the Field */
   protected TemplateTree tree;

   /**
    *Constructor for the TemplateElement object
    *
    * @param doclet  Description of the Parameter
    * @param tree    Description of the Parameter
    */
   public TemplateElement(DocletElement doclet, TemplateTree tree)
   {
      super();
      if (doclet == null || tree == null)
      {
         throw new IllegalArgumentException();
      }
      this.doclet = doclet;
      this.node = doclet.getNode();
      this.node.setObject(tree.getKey(), this);
      this.tree = tree;
   }

   /**
    * @param arg0  Description of the Parameter
    * @return      Description of the Return Value
    * @see         java.lang.Comparable#compareTo(java.lang.Object)
    */
   public int compareTo(Object arg0)
   {
      return node.compareTo(((TemplateElement) arg0).getNode());
   }

   /**
    * DocletElements are equals if there nodes are equals. The latter is determined by the key. This makes sense
    * as there must be no two DocletElements with the same key.
    *
    * @param obj  Description of the Parameter
    * @return     Description of the Return Value
    * @see        java.lang.Object#equals(Object)
    */
   public boolean equals(Object obj)
   {
      if (!(obj instanceof TemplateElement))
      {
         return false;
      }
      TemplateElement element = (TemplateElement) obj;
      if (this.node.equals(element.node) && getTree().getId() == element.getTree().getId())
      {
         return true;
      }
      return false;
   }

   /**
    * Gets the child attribute of the TemplateElement object
    *
    * @param name  Description of the Parameter
    * @return      The child value
    */
   public TemplateElement getChild(String name)
   {
      return (TemplateElement) node.getChildObject(name, getTree().getKey());
   }

   /**
    * Gets the childrenCount attribute of the TemplateElement object
    *
    * @return   The childrenCount value
    */
   public int getChildrenCount()
   {
      return getChildrenElements().length;
   }

   /**
    * Gets the childrenElements attribute of the TemplateElement object
    *
    * @return   The childrenElements value
    */
   public TemplateElement[] getChildrenElements()
   {
      List list = node.getChildrenObjects(tree.getKey());
      return (TemplateElement[]) list.toArray(new TemplateElement[list.size()]);
   }

   /**
    * Returns the doclet.
    *
    * @return   Doclet
    */
   public DocletElement getDocletElement()
   {
      return doclet;
   }

   /**
    * Gets the name attribute of the TemplateElement object
    *
    * @return   The name value
    */
   public String getName()
   {
      return node.getName();
   }

   /**
    * Returns the node.
    *
    * @return   SortedKeyTreeNode
    */
   public SortedKeyTreeNode getNode()
   {
      return node;
   }

   /**
    * Gets the parent attribute of the TemplateElement object
    *
    * @return   The parent value
    */
   public TemplateElement getParent()
   {
      if (node.getParentNode() != null)
      {
         return (TemplateElement) node.getParentNode().getObject(tree.getKey());
      }
      return null;
   }

   /**
    * Gets the tree attribute of the TemplateElement object
    *
    * @return   The tree value
    */
   public TemplateTree getTree()
   {
      return tree;
   }

   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public boolean hasChildrenElements()
   {
      return getChildrenElements().length > 0 ? true : false;
   }

   /**
    * See equals
    *
    * @return   Description of the Return Value
    * @see      java.lang.Object#hashCode()
    */
   public int hashCode()
   {
      return (node.getAbsoluteKey() + getTree().getKey()).hashCode();
   }

   /**
    * @return   Description of the Return Value
    * @see      java.lang.Object#toString()
    */
   public String toString()
   {
      return getName();
   }

}
