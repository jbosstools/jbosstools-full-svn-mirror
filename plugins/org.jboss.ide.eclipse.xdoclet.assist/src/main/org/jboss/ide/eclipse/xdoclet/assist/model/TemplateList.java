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

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.jface.util.ListenerList;
import org.jboss.ide.eclipse.xdoclet.assist.XDocletAssistMessages;
import org.jboss.ide.eclipse.xdoclet.assist.model.conditions.ConditionTree;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   17 mai 2003
 */
public class TemplateList implements Serializable
{
   /** Description of the Field */
   protected DocletTree docletTree;

   /** Description of the Field */
   protected ListenerList listenerList = new ListenerList();

   /** Description of the Field */
   protected HashMap templates = new HashMap();

   /** Description of the Field */
   protected ListenerList treeListener = new ListenerList();

   /** Description of the Field */
   public final static String NEW_NAME = XDocletAssistMessages.getString("TemplateList.new_template_1");//$NON-NLS-1$

   /**
    * Constructor for TemplateList.
    *
    * @param docletTree  Description of the Parameter
    */
   public TemplateList(DocletTree docletTree)
   {
      super();
      if (docletTree == null)
      {
         throw new IllegalArgumentException();
      }
      this.docletTree = docletTree;
   }

   /**
    * @param name           The feature to be added to the Template attribute
    * @param conditionTree  The feature to be added to the Template attribute
    * @return               Description of the Return Value
    * @see                  org.jboss.ide.eclipse.xdoclet.ITemplateList#addTemplate(String, ITemplateTree)
    */
   public TemplateTree addTemplate(String name, ConditionTree conditionTree)
   {
      if (name == null || docletTree == null || templates.keySet().contains(name))
      {
         throw new IllegalArgumentException();
      }
      boolean notUnique = true;
      long id = 0;
      while (notUnique)
      {
         id = System.currentTimeMillis();
         notUnique = false;
         for (Iterator iter = templates.values().iterator(); iter.hasNext();)
         {
            if (((TemplateTree) iter.next()).id == id)
            {
               notUnique = true;
            }
         }
      }
      TemplateTree templateTree = new TemplateTree(name, docletTree, id);
      templateTree.setConditionTree(conditionTree);
      templates.put(name, templateTree);
      for (int i = 0; i < treeListener.getListeners().length; i++)
      {
         templateTree.addTemplateTreeListener((ITemplateTreeListener) treeListener.getListeners()[i]);
      }
      fireChange(templateTree);
      return templateTree;
   }

   /**
    * @param listener  The feature to be added to the TemplateListListener attribute
    */
   public void addTemplateListListener(ITemplateListListener listener)
   {
      listenerList.add(listener);
   }

   /**
    * Adds a feature to the TemplateTreeListener attribute of the TemplateList object
    *
    * @param listener  The feature to be added to the TemplateTreeListener attribute
    */
   public void addTemplateTreeListener(ITemplateTreeListener listener)
   {
      treeListener.add(listener);
      for (Iterator iter = templates.keySet().iterator(); iter.hasNext();)
      {
         String key = (String) iter.next();
         ((TemplateTree) templates.get(key)).addTemplateTreeListener(listener);
      }
   }

   /**
    * @param template  Description of the Parameter
    * @see             org.jboss.ide.eclipse.xdoclet.ITemplateList#deleteTemplate(String)
    */
   public void deleteTemplate(String template)
   {
      TemplateTree templateTree = getTemplateTree(template);
      if (templateTree != null)
      {
         for (int i = 0; i < treeListener.getListeners().length; i++)
         {
            templateTree.removeTemplateTreeListener((ITemplateTreeListener) treeListener.getListeners()[i]);
         }
      }
      templates.remove(template);
      fireChange(templateTree);
   }

   /**
    * Gets the docletTree attribute of the TemplateList object
    *
    * @return   The docletTree value
    */
   public DocletTree getDocletTree()
   {
      return docletTree;
   }

   /**
    * Gets the newName attribute of the TemplateList object
    *
    * @return   The newName value
    */
   public String getNewName()
   {
      if (!templates.keySet().contains(NEW_NAME))
      {
         return NEW_NAME;
      }
      for (int i = 1;; i++)
      {
         String newName = NEW_NAME + " (" + i + ")";//$NON-NLS-1$ //$NON-NLS-2$
         if (!templates.keySet().contains(newName))
         {
            return newName;
         }
      }
   }

   /**
    * Gets the templateListListener attribute of the TemplateList object
    *
    * @return   The templateListListener value
    */
   public ITemplateListListener[] getTemplateListListener()
   {
      return (ITemplateListListener[]) Arrays.asList(listenerList.getListeners()).toArray(
            new ITemplateListListener[listenerList.size()]);
   }

   /**
    * @param name  Description of the Parameter
    * @return      The templateTree value
    * @see         org.jboss.ide.eclipse.xdoclet.ITemplateList#getTemplateTree(String)
    */
   public TemplateTree getTemplateTree(String name)
   {
      return (TemplateTree) templates.get(name);
   }

   /**
    * @return   The templateTrees value
    * @see      org.jboss.ide.eclipse.xdoclet.ITemplateList#getTemplates()
    */
   public TemplateTree[] getTemplateTrees()
   {
      return (TemplateTree[]) templates.values().toArray(new TemplateTree[templates.values().size()]);
   }

   /**
    * Gets the renameTreeOk attribute of the TemplateList object
    *
    * @param oldName  Description of the Parameter
    * @param newName  Description of the Parameter
    * @return         The renameTreeOk value
    */
   public boolean isRenameTreeOk(String oldName, String newName)
   {
      if (!oldName.equals(newName) && templates.keySet().contains(newName))
      {
         return false;
      }
      return true;
   }

   /**
    * @param name  Description of the Parameter
    * @return      The template value
    * @see         org.jboss.ide.eclipse.xdoclet.ITemplateList#isTemplate(String)
    */
   public boolean isTemplate(String name)
   {
      return templates.get(name) == null ? false : true;
   }

   /**
    * @param listener  Description of the Parameter
    */
   public void removeTemplateListListener(ITemplateListListener listener)
   {
      listenerList.remove(listener);
   }

   /**
    * Description of the Method
    *
    * @param listener  Description of the Parameter
    */
   public void removeTemplateTreeListener(ITemplateTreeListener listener)
   {
      treeListener.remove(listener);
      for (Iterator iter = templates.keySet().iterator(); iter.hasNext();)
      {
         String key = (String) iter.next();
         ((TemplateTree) templates.get(key)).removeTemplateTreeListener(listener);
      }
   }

   /**
    * Description of the Method
    *
    * @param oldName  Description of the Parameter
    * @param newName  Description of the Parameter
    */
   public void renameTree(String oldName, String newName)
   {
      if (!isRenameTreeOk(oldName, newName))
      {
         throw new IllegalArgumentException();
      }
      TemplateTree tree = getTemplateTree(oldName);
      tree.setName(newName);
      templates.remove(oldName);
      templates.put(newName, tree);
   }

   /**
    * Description of the Method
    *
    * @param tree  Description of the Parameter
    */
   private void fireChange(TemplateTree tree)
   {
      Object[] listeners = listenerList.getListeners();
      if (listeners != null)
      {
         TemplateEvent event = new TemplateEvent(this, this, tree, null);
         for (int i = 0; i < listeners.length; i++)
         {
            ITemplateListListener listener = (ITemplateListListener) listeners[i];
            listener.changed(event);
         }
      }
   }
}
