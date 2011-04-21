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
package org.jboss.ide.eclipse.xdoclet.assist.model;

import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.jboss.ide.eclipse.xdoclet.assist.model.conditions.ConditionTree;
import org.jdom.CDATA;
import org.jdom.Document;
import org.jdom.Element;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   17 mai 2003
 */
public class TemplateListXMLManager
{
   /** Description of the Field */
   protected TemplateList list;

   /** Description of the Field */
   public static String LEAVE_PATH_SEPARATOR = ".";//$NON-NLS-1$

   /** Constructor for TemplateListPersistent. */
   public TemplateListXMLManager()
   {
      super();
   }

   /**
    * Returns the list.
    *
    * @return   TemplateList
    */
   public TemplateList getList()
   {
      return list;
   }

   /**
    * Sets the list.
    *
    * @param list  The list to set
    */
   public void setList(TemplateList list)
   {
      this.list = list;
   }

   /**
    * Sets the templateListFromDocument attribute of the TemplateListXMLManager object
    *
    * @param document    The new templateListFromDocument value
    * @param docletTree  The new templateListFromDocument value
    * @return            Description of the Return Value
    */
   public TemplateList setTemplateListFromDocument(Document document, DocletTree docletTree)
   {
      TemplateList templateList = new TemplateList(docletTree);

      Element rootElement = document.getRootElement();
      List templateElements = rootElement.getChildren(IDocletConstants.TEMPLATE_TAG);

      DocletElement templateDocletElement;
      TemplateTree templateTree;
      ConditionTree conditionTree;

      for (Iterator templateIterator = templateElements.iterator(); templateIterator.hasNext();)
      {
         Element templateElement = (Element) templateIterator.next();
         conditionTree = new ConditionTree(null);
         templateTree = templateList.addTemplate(templateElement.getAttributeValue(IDocletConstants.NAME_ATTRIBUTE),
               conditionTree);
         templateTree.setHelptext(templateElement.getChild(IDocletConstants.USAGE_DESCRIPTION_TAG).getTextTrim());
         //
         String conditionDescription = "";//$NON-NLS-1$
         if (templateElement.getChild(IDocletConstants.CONDITION_DESCRIPTION_TAG) != null)
         {
            conditionDescription = templateElement.getChild(IDocletConstants.CONDITION_DESCRIPTION_TAG).getTextTrim();
         }
         templateTree.setConditionDescription(conditionDescription);
         //
         List leaveElements = templateElement.getChildren(IDocletConstants.LEAVE_TAG);
         for (Iterator leaveElementsIterator = leaveElements.iterator(); leaveElementsIterator.hasNext();)
         {
            Element leaveElement = (Element) leaveElementsIterator.next();
            StringTokenizer pathTokenizer = new StringTokenizer(leaveElement
                  .getAttributeValue(IDocletConstants.NAME_ATTRIBUTE), ".");//$NON-NLS-1$
            String[] path = new String[pathTokenizer.countTokens()];
            for (int i = 0; pathTokenizer.hasMoreTokens(); i++)
            {
               path[i] = pathTokenizer.nextToken();
            }
            if ((templateDocletElement = docletTree.getNode(path)) != null)
            {
               templateTree.addElement(templateDocletElement);
            }
         }
      }
      setList(templateList);
      return templateList;
   }

   /**
    * The document must match the structure of the templates dtd
    *
    * @param document
    */
   public void writeToDocument(Document document)
   {
      Element templatesElement = document.getRootElement();
      templatesElement.removeChildren();
      TemplateTree tree;
      Element template;
      Element usageDescription;
      //Element condition;
      Element conditionDescription;
      Element leave;
      String leaveName;
      for (int i = 0; i < list.getTemplateTrees().length; i++)
      {
         tree = list.getTemplateTrees()[i];
         template = new Element(IDocletConstants.TEMPLATE_TAG);
         template.setAttribute(IDocletConstants.NAME_ATTRIBUTE, tree.getName());
         templatesElement.addContent(template);
         usageDescription = new Element(IDocletConstants.USAGE_DESCRIPTION_TAG);
         usageDescription.addContent(new CDATA(tree.getHelptext()));
         template.addContent(usageDescription);
         conditionDescription = new Element(IDocletConstants.CONDITION_DESCRIPTION_TAG);
         conditionDescription.addContent(new CDATA(tree.getConditionDescription()));
         template.addContent(conditionDescription);
         //
         DocletElement[] leaveDocletElements = tree.getAllDocletElements();
         for (int j = 0; j < leaveDocletElements.length; j++)
         {
            if (tree.getTemplateElement(leaveDocletElements[j]).getChildrenCount() == 0)
            {
               leave = new Element(IDocletConstants.LEAVE_TAG);
               leaveName = "";//$NON-NLS-1$
               for (int k = 0; k < leaveDocletElements[j].getNode().getPath().length; k++)
               {
                  if (k != 0)
                  {
                     leaveName += "." + leaveDocletElements[j].getNode().getPath()[k];//$NON-NLS-1$
                  }
                  else
                  {
                     leaveName += leaveDocletElements[j].getNode().getPath()[k];
                  }
               }
               leave.setAttribute(IDocletConstants.NAME_ATTRIBUTE, leaveName);
               template.addContent(leave);
            }
         }
      }
   }

}
