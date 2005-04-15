/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.xdoclet.run.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 * @created   18 mars 2003
 * @todo      Javadoc to complete
 */
public class XDocletAttribute extends XDocletElement
{
   /** Description of the Field */
   private String value = null;


   /**Constructor for the XDocletAttribute object */
   public XDocletAttribute() { }


   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public Object clone()
   {
      XDocletAttribute attribute = new XDocletAttribute();

      attribute.setName(this.getName());
      attribute.setValue(this.getValue());
      attribute.setUsed(this.isUsed());

      return attribute;
   }


   /**
    * @return   String
    */
   public String getValue()
   {
      return this.value;
   }


   /**
    * Description of the Method
    *
    * @param node       Description of the Parameter
    * @param recursive  Description of the Parameter
    */
   public void readFromXml(Node node, boolean recursive)
   {
      Element element = (Element) node;
      this.setName(element.getAttribute("name"));//$NON-NLS-1$
      this.setValue(element.getAttribute("value"));//$NON-NLS-1$
      this.setUsed(new Boolean(element.getAttribute("used")).booleanValue());//$NON-NLS-1$
   }


   /**
    * Sets the value.
    *
    * @param value  The value to set
    */
   public void setValue(String value)
   {
      this.value = value;
   }


   /**
    * Description of the Method
    *
    * @param doc   Description of the Parameter
    * @param node  Description of the Parameter
    * @see         xdoclet.ide.eclipse.configuration.model.XDocletElement#writeToXml(org.w3c.dom.Document, org.w3c.dom.Node)
    */
   public void writeToXml(Document doc, Node node)
   {
      Element element = doc.createElement("attribute");//$NON-NLS-1$
      node.appendChild(element);

      element.setAttribute("name", this.getName());//$NON-NLS-1$
      element.setAttribute("value", this.getValue());//$NON-NLS-1$
      element.setAttribute("used", "" + this.isUsed());//$NON-NLS-1$ //$NON-NLS-2$
   }
}
