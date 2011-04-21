/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
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

import java.util.Iterator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.eclipse.core.resources.IProject;
import org.jboss.ide.eclipse.core.util.IXMLSerializable;

/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class PackagingArchive extends PackagingData
{
   private String destination = "";//$NON-NLS-1$

   private boolean exploded = false;//$NON-NLS-1$

   private String name = "";//$NON-NLS-1$

   private IProject project;

   /**Constructor for the XDocletConfiguration object */
   public PackagingArchive()
   {
      super();
   }

   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public Object clone()
   {
      Iterator iterator;
      PackagingArchive config = new PackagingArchive();

      config.setName(this.getName());
      config.setDestination(this.getDestination());
      config.setUsed(this.isUsed());
      config.setExploded(this.isExploded());

      iterator = this.getNodes().iterator();
      while (iterator.hasNext())
      {
         PackagingData data = (PackagingData) iterator.next();
         config.addNode((PackagingData) data.clone());
      }

      return config;
   }

   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public PackagingData cloneData()
   {
      return (PackagingData) this.clone();
   }

   /**
    * Gets the destination attribute of the PackagingConfiguration object
    *
    * @return   The destination value
    */
   public String getDestination()
   {
      return this.destination;
   }

   /**
    * @return   String
    */
   public String getName()
   {
      return this.name;
   }

   /**
    * @return   IProject
    */
   public IProject getProject()
   {
      return project;
   }

   /**
    * @return   boolean
    */
   public boolean isExploded()
   {
      return exploded;
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
      super.readFromXml(node, recursive);
      this.setName(element.getAttribute("name"));//$NON-NLS-1$
      this.setDestination(element.getAttribute("destination"));//$NON-NLS-1$
      this.setExploded(new Boolean(element.getAttribute("exploded")).booleanValue());//$NON-NLS-1$

      if (recursive)
      {
         NodeList children = node.getChildNodes();
         for (int i = 0; i < children.getLength(); i++)
         {
            Node child = children.item(i);
            String nodeName = child.getNodeName();

            if ("file".equals(nodeName)//$NON-NLS-1$
            )
            {
               PackagingFile file = new PackagingFile();
               file.readFromXml(child);
               this.addNode(file);
            }
            if ("folder".equals(nodeName)//$NON-NLS-1$
            )
            {
               PackagingFolder folder = new PackagingFolder();
               folder.readFromXml(child);
               this.addNode(folder);
            }
         }
      }
   }

   /**
    * Sets the destination attribute of the PackagingConfiguration object
    *
    * @param destination  The new destination value
    */
   public void setDestination(String destination)
   {
      this.destination = destination;
   }

   /**
    * Sets the exploded.
    *
    * @param exploded  The exploded to set
    */
   public void setExploded(boolean exploded)
   {
      this.exploded = exploded;
   }

   /**
    * Sets the name.
    *
    * @param name  The name to set
    */
   public void setName(String name)
   {
      this.name = name;
   }

   /**
    * Sets the project.
    *
    * @param project  The project to set
    */
   public void setProject(IProject project)
   {
      this.project = project;
   }

   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public String toString()
   {
      StringBuffer buffer = new StringBuffer();

      buffer.append(this.getName());

      if (!"".equals(getDestination())//$NON-NLS-1$
      )
      {
         buffer.append(" -> ");//$NON-NLS-1$
         buffer.append(this.getDestination());
      }

      return buffer.toString();
   }

   /**
    * Description of the Method
    *
    * @param doc   Description of the Parameter
    * @param node  Description of the Parameter
    * @see         xdoclet.ide.eclipse.configuration.model.XDocletData#writeToXml(org.w3c.dom.Document, org.w3c.dom.Node)
    */
   public void writeToXml(Document doc, Node node)
   {
      Element element = doc.createElement("archive");//$NON-NLS-1$
      node.appendChild(element);

      element.setAttribute("name", this.getName());//$NON-NLS-1$
      element.setAttribute("destination", this.getDestination());//$NON-NLS-1$
      element.setAttribute("used", "" + this.isUsed());//$NON-NLS-1$ //$NON-NLS-2$
      element.setAttribute("exploded", "" + this.isExploded());//$NON-NLS-1$ //$NON-NLS-2$

      Iterator iterator = this.getNodes().iterator();
      while (iterator.hasNext())
      {
         IXMLSerializable serializable = (IXMLSerializable) iterator.next();
         serializable.writeToXml(doc, element);
      }
   }
}
