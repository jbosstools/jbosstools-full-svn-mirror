/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
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
