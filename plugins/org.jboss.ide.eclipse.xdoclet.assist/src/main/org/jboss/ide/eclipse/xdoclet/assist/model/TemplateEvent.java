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
   public TemplateEvent(Object source, TemplateList templateList, TemplateTree templateTree,
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
