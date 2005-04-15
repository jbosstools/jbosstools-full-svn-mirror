/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.xdoclet.run.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.jboss.ide.eclipse.core.util.IXMLSerializable;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 * @created   17 mars 2003
 * @todo      Javadoc to complete
 */
public class XDocletElement extends XDocletData implements IXMLSerializable
{
   /** Description of the Field */
   private ArrayList attributes = new ArrayList();


   /**Constructor for the XDocletElement object */
   public XDocletElement() { }


   /**
    * Adds a feature to the Attribute attribute of the XDocletElement object
    *
    * @param attribute  The feature to be added to the Attribute attribute
    */
   public void addAttribute(XDocletAttribute attribute)
   {
      this.attributes.add(attribute);
   }


   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    * @see      java.lang.Object#clone()
    */
   public Object clone()
   {
      Iterator iterator;
      XDocletElement element = new XDocletElement();

      element.setName(this.getName());
      element.setUsed(this.isUsed());

      iterator = this.getAttributes().iterator();
      while (iterator.hasNext())
      {
         XDocletAttribute attribute = (XDocletAttribute) iterator.next();
         element.addAttribute((XDocletAttribute) attribute.clone());
      }

      return element;
   }


   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public XDocletData cloneData()
   {
      Iterator iterator;
      XDocletElement element = (XDocletElement) this.clone();

      iterator = this.getNodes().iterator();
      while (iterator.hasNext())
      {
         XDocletElement elmt = (XDocletElement) iterator.next();
         element.addNode(elmt.cloneData());
      }

      return element;
   }


   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public Collection getAttributes()
   {
      return this.attributes;
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

      NodeList children = node.getChildNodes();
      for (int i = 0; i < children.getLength(); i++)
      {
         Node child = children.item(i);
         if (child.getNodeName().equals("attribute")//$NON-NLS-1$
         )
         {

            XDocletAttribute attribute = new XDocletAttribute();
            attribute.readFromXml(child);
            this.addAttribute(attribute);
         }

         if (recursive)
         {
            if (child.getNodeName().equals("element")//$NON-NLS-1$
            )
            {

               XDocletElement elmt = new XDocletElement();
               elmt.readFromXml(child);
               this.addNode(elmt);
            }
         }
      }
   }


   /**
    * Description of the Method
    *
    * @param attribute  Description of the Parameter
    */
   public void removeAttribute(XDocletAttribute attribute)
   {
      this.attributes.remove(attribute);
   }


   /**
    * Description of the Method
    *
    * @param doc   Description of the Parameter
    * @param node  Description of the Parameter
    * @see         xdoclet.ide.eclipse.configuration.model.IXMLSerializable#writeToXml(org.w3c.dom.Document, org.w3c.dom.Node)
    */
   public void writeToXml(Document doc, Node node)
   {
      Element element = doc.createElement("element");//$NON-NLS-1$
      node.appendChild(element);

      element.setAttribute("name", this.getName());//$NON-NLS-1$
      element.setAttribute("used", "" + this.isUsed());//$NON-NLS-1$ //$NON-NLS-2$

      Iterator iterator = this.getAttributes().iterator();
      while (iterator.hasNext())
      {
         IXMLSerializable serializable = (IXMLSerializable) iterator.next();
         serializable.writeToXml(doc, element);
      }

      iterator = this.getNodes().iterator();
      while (iterator.hasNext())
      {
         IXMLSerializable serializable = (IXMLSerializable) iterator.next();
         serializable.writeToXml(doc, element);
      }
   }
}
