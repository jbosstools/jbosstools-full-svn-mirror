/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
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
   public XDocletConfiguration() { }


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
