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
package org.jboss.ide.eclipse.packaging.core.model;

import java.util.ArrayList;
import java.util.Collection;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import org.jboss.ide.eclipse.core.util.IXMLSerializable;

/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public abstract class PackagingData implements IXMLSerializable, Cloneable
{
   private ArrayList nodes = new ArrayList();

   private PackagingData parent;

   private boolean used = true;

   /**Constructor for the XDocletNode object */
   public PackagingData()
   {
      super();
   }

   /**
    * Adds a feature to the Task attribute of the XDocletConfiguration object
    *
    * @param node  The feature to be added to the Node attribute
    */
   public void addNode(PackagingData node)
   {
      node.setParent(this);
      this.nodes.add(node);
   }

   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    * @see      java.lang.Object#clone()
    */
   public Object clone()
   {
      throw new UnsupportedOperationException("You should never see this");//$NON-NLS-1$
   }

   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public abstract PackagingData cloneData();

   /**
    * Gets the nodes attribute of the XDocletNode object
    *
    * @return   The nodes value
    */
   public Collection getNodes()
   {
      return this.nodes;
   }

   /**
    * @return   XDocletNode
    */
   public PackagingData getParent()
   {
      return this.parent;
   }

   /**
    * Gets the empty attribute of the XDocletNode object
    *
    * @return   The empty value
    */
   public boolean isEmpty()
   {
      return this.nodes.isEmpty();
   }

   /**
    * @return   boolean
    */
   public boolean isUsed()
   {
      return this.used;
   }

   /**
    * Description of the Method
    *
    * @param node       Description of the Parameter
    * @param recursive  Description of the Parameter
    * @see              xdoclet.ide.eclipse.configuration.model.IXMLSerializable#readFromXml(org.w3c.dom.Node, boolean)
    */
   public void readFromXml(Node node, boolean recursive)
   {
      Element element = (Element) node;
      this.setUsed(new Boolean(element.getAttribute("used")).booleanValue());//$NON-NLS-1$
   }

   /**
    * Description of the Method
    *
    * @param node  Description of the Parameter
    */
   public void readFromXml(Node node)
   {
      this.readFromXml(node, true);
   }

   /**
    * Description of the Method
    *
    * @param node  Description of the Parameter
    */
   public void removeNode(PackagingData node)
   {
      this.nodes.remove(node);
   }

   /**
    * Sets the parent.
    *
    * @param parent  The parent to set
    */
   public void setParent(PackagingData parent)
   {
      this.parent = parent;
   }

   /**
    * Sets the used.
    *
    * @param used  The used to set
    */
   public void setUsed(boolean used)
   {
      this.used = used;
   }

   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public String toString()
   {
      return "Packaging Data (Parent = " + this.getParent().toString() + ")";//$NON-NLS-1$ //$NON-NLS-2$
   }
}
