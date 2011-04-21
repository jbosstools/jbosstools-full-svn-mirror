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
package org.jboss.ide.eclipse.xdoclet.run.model;

import java.util.Iterator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.jboss.ide.eclipse.core.util.IXMLSerializable;

/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 * @created   17 mars 2003
 * @todo      Javadoc to complete
 */
public class XDocletConfiguration extends XDocletData
{
   /**Constructor for the XDocletConfiguration object */
   public XDocletConfiguration()
   {
   }

   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public XDocletData cloneData()
   {
      Iterator iterator;
      XDocletConfiguration config = new XDocletConfiguration();

      config.setName(this.getName());
      config.setUsed(this.isUsed());

      iterator = this.getNodes().iterator();
      while (iterator.hasNext())
      {
         XDocletElement elmt = (XDocletElement) iterator.next();
         config.addNode(elmt.cloneData());
      }

      return config;
   }

   /**
    * Gets the id attribute of the XDocletConfiguration object
    *
    * @return   The id value
    * @see      xdoclet.ide.eclipse.configuration.model.XDocletData#getId()
    */
   public String getId()
   {
      return this.getName();
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
    * @param node       Description of the Parameter
    * @param recursive  Description of the Parameter
    * @see              xdoclet.ide.eclipse.configuration.model.IXMLSerializable#readFromXml(org.w3c.dom.Node, boolean)
    */
   public void readFromXml(Node node, boolean recursive)
   {
      Element element = (Element) node;
      this.setName(element.getAttribute("name"));//$NON-NLS-1$
      this.setUsed(new Boolean(element.getAttribute("used")).booleanValue());//$NON-NLS-1$

      if (recursive)
      {
         NodeList children = node.getChildNodes();
         for (int i = 0; i < children.getLength(); i++)
         {
            Node child = children.item(i);
            if (child.getNodeName().equals("task")//$NON-NLS-1$
            )
            {

               XDocletTask task = new XDocletTask();
               task.readFromXml(child);
               this.addNode(task);
            }
         }
      }
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
      Element element = doc.createElement("configuration");//$NON-NLS-1$
      node.appendChild(element);

      element.setAttribute("name", this.getName());//$NON-NLS-1$
      element.setAttribute("used", "" + this.isUsed());//$NON-NLS-1$ //$NON-NLS-2$

      Iterator iterator = this.getNodes().iterator();
      while (iterator.hasNext())
      {
         IXMLSerializable serializable = (IXMLSerializable) iterator.next();
         serializable.writeToXml(doc, element);
      }
   }
}
