/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.xdoclet.run.model;

import java.util.ArrayList;
import java.util.Collection;

import org.jboss.ide.eclipse.core.util.IXMLSerializable;

/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 * @created   20 mars 2003
 * @todo      Javadoc to complete
 */
public abstract class XDocletData implements IXMLSerializable, Cloneable
{
   /** Description of the Field */
   private String id = null;
   /** Description of the Field */
   private String name = null;
   /** Description of the Field */
   private ArrayList nodes = new ArrayList();
   /** Description of the Field */
   private XDocletData parent;
   /** Description of the Field */
   private boolean used = true;



   /**Constructor for the XDocletNode object */
   public XDocletData()
   {
      super();
   }


   /**
    * Adds a feature to the Task attribute of the XDocletConfiguration object
    *
    * @param node  The feature to be added to the Node attribute
    */
   public void addNode(XDocletData node)
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
   public abstract XDocletData cloneData();


   /**
    * Gets the id attribute of the XDocletElement object
    *
    * @return   The id value
    * @see      xdoclet.ide.eclipse.configuration.model.XDocletData#getId()
    */
   public String getId()
   {
      if (this.id == null)
      {
         XDocletData current = this;
         this.id = current.getName();
         while (current.getParent() != null)
         {
            current = current.getParent();
            this.id = current.getName() + "/" + this.id;//$NON-NLS-1$
         }
      }
      return this.id;
   }


   /**
    * @return   String
    */
   public String getName()
   {
      return this.name;
   }


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
   public XDocletData getParent()
   {
      return this.parent;
   }


   /**
    * Gets the rootParent attribute of the XDocletData object
    *
    * @return   The rootParent value
    */
   public XDocletData getRootParent()
   {
      XDocletData current = this;
      while (current.getParent() != null)
      {
         current = current.getParent();
      }
      return current;
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
    * @param node  Description of the Parameter
    */
   public void removeNode(XDocletData node)
   {
      this.nodes.remove(node);
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
    * Sets the parent.
    *
    * @param parent  The parent to set
    */
   public void setParent(XDocletData parent)
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
      return this.name;
   }
}
