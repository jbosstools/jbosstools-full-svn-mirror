/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.xdoclet.assist.model;

import java.util.EventObject;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   17 mai 2003
 */
public class TemplateEvent extends EventObject
{
   /** Description of the Field */
   protected TemplateElement templateElement;
   /** Description of the Field */
   protected TemplateList templateList;

   /** Description of the Field */
   protected TemplateTree templateTree;


   /**
    * Constructor for TemplateTreeEvent.
    *
    * @param source
    * @param templateList     Description of the Parameter
    * @param templateTree     Description of the Parameter
    * @param templateElement  Description of the Parameter
    */
   public TemplateEvent(
         Object source,
         TemplateList templateList,
         TemplateTree templateTree,
         TemplateElement templateElement)
   {
      super(source);
      this.templateTree = templateTree;
      this.templateList = templateList;
      this.templateElement = templateElement;
   }


   /**
    * Returns the templateElement.
    *
    * @return   TemplateElement
    */
   public TemplateElement getTemplateElement()
   {
      return templateElement;
   }


   /**
    * Returns the templateList.
    *
    * @return   TemplateList
    */
   public TemplateList getTemplateList()
   {
      return templateList;
   }


   /**
    * Returns the templateTree.
    *
    * @return   TemplateTree
    */
   public TemplateTree getTemplateTree()
   {
      return templateTree;
   }

}
