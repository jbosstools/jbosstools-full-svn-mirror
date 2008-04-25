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
package org.jboss.ide.eclipse.xdoclet.assist.ui;

import java.util.ArrayList;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.jboss.ide.eclipse.xdoclet.assist.model.ITemplateListListener;
import org.jboss.ide.eclipse.xdoclet.assist.model.ITemplateTreeListener;
import org.jboss.ide.eclipse.xdoclet.assist.model.TemplateElement;
import org.jboss.ide.eclipse.xdoclet.assist.model.TemplateEvent;
import org.jboss.ide.eclipse.xdoclet.assist.model.TemplateList;
import org.jboss.ide.eclipse.xdoclet.assist.model.TemplateTree;

/**
 * @author    Hans Dockter
 * @version   $Revision: 1420 $
 * @created   17 mai 2003
 */
public class TemplateListContentProvider implements ITreeContentProvider, ITemplateListListener, ITemplateTreeListener
{

   /** Description of the Field */
   protected TemplateList templateList;

   private TreeViewer fViewer;

   /**
    * @param event  Description of the Parameter
    * @see          org.jboss.ide.eclipse.xdoclet.model.ITemplateListListener#changed(java.util.EventObject)
    */
   public void changed(TemplateEvent event)
   {
      fViewer.refresh(false);
   }

   /**
    * @see   org.eclipse.jface.viewers.IContentProvider#dispose()
    */
   public void dispose()
   {
   }

   /**
    * @param event  Description of the Parameter
    * @see          org.jboss.ide.eclipse.xdoclet.model.ITemplateTreeListener#elementAdded(org.jboss.ide.eclipse.xdoclet.model.TemplateTreeEvent)
    */
   public void elementAdded(TemplateEvent event)
   {
   }

   /**
    * @param event  Description of the Parameter
    * @see          org.jboss.ide.eclipse.xdoclet.model.ITemplateTreeListener#elementRemoved(org.jboss.ide.eclipse.xdoclet.model.TemplateTreeEvent)
    */
   public void elementRemoved(TemplateEvent event)
   {
   }

   // protected HashMap templatesForContexTrees = new HashMap();

   /**
    * @param parentElement  Description of the Parameter
    * @return               The children value
    * @see                  org.eclipse.jface.viewers.ITreeContentProvider#getChildren(Object)
    */
   public Object[] getChildren(Object parentElement)
   {
      if (parentElement instanceof TemplateTree)
      {
         ArrayList objects = new ArrayList();
         TemplateTree tree = (TemplateTree) parentElement;
         // objects.add(tree.getContextTree());
         for (int i = 0; i < tree.getChildrenElements().length; i++)
         {
            objects.add(tree.getChildrenElements()[i]);
         }
         return objects.toArray();
      }
      else if (parentElement instanceof TemplateElement)
      {
         return ((TemplateElement) parentElement).getChildrenElements();
      }
      //		else if (parentElement instanceof ContextTree) {
      //			ContextTree contextTree = (ContextTree) parentElement;
      //			return contextTree.getElements();
      //		} else if (!(parentElement instanceof ContextElement)) {
      //			System.out.println("WHAT " + parentElement.getClass().getName());
      //			throw new IllegalArgumentException();
      //		}
      // ContextElement
      return null;
   }

   /**
    * @param inputElement  Description of the Parameter
    * @return              The elements value
    * @see                 org.eclipse.jface.viewers.IStructuredContentProvider#getElements(Object)
    */
   public Object[] getElements(Object inputElement)
   {
      return templateList.getTemplateTrees();
   }

   /**
    * @param element  Description of the Parameter
    * @return         The parent value
    * @see            org.eclipse.jface.viewers.ITreeContentProvider#getParent(Object)
    */
   public Object getParent(Object element)
   {
      if (element instanceof TemplateTree)
      {
         return null;
      }
      else if (element instanceof TemplateElement)
      {
         TemplateElement templateElement = ((TemplateElement) element);
         if (templateElement.getParent() == null)
         {
            return templateElement.getTree();
         }
         return templateElement.getParent();
         //		else if (element instanceof ContextTree) {
         //			ContextTree contextTree = (ContextTree) element;
         //			return templatesForContexTrees.get(contextTree);
         //		} else if (element instanceof ContextElement) {
         //			ContextElement contextElement = (ContextElement) element;
         //			return contextElement.getContextTree();
      }
      else
      {
         throw new IllegalArgumentException();
      }
   }

   /**
    * @param element  Description of the Parameter
    * @return         Description of the Return Value
    * @see            org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(Object)
    */
   public boolean hasChildren(Object element)
   {
      if (element instanceof TemplateTree)
      {
         return true;
      }
      else if (element instanceof TemplateElement)
      {
         return ((TemplateElement) element).hasChildrenElements();
      }
      //		else if (element instanceof ContextTree) {
      //			ContextTree contextTree = (ContextTree) element;
      //			return contextTree.getElements().length > 0;
      //		}

      return false;
   }

   /**
    * @param viewer    Description of the Parameter
    * @param oldInput  Description of the Parameter
    * @param newInput  Description of the Parameter
    * @see             org.eclipse.jface.viewers.IContentProvider#inputChanged(Viewer, Object, Object)
    */
   public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
   {
      fViewer = (TreeViewer) viewer;
      if (oldInput != null)
      {
         ((TemplateList) oldInput).removeTemplateListListener(this);
         ((TemplateList) oldInput).removeTemplateTreeListener(this);
      }
      if (newInput != null)
      {
         templateList = (TemplateList) newInput;
         ((TemplateList) newInput).addTemplateListListener(this);
         ((TemplateList) newInput).addTemplateTreeListener(this);
         //			for (int i = 0; i < templateList.getTemplateTrees().length; i++) {
         //				templatesForContexTrees.put(
         //					templateList.getTemplateTrees()[i].getContextTree(),
         //					templateList.getTemplateTrees()[i]);
         //			}
      }
   }

   /**
    * @param event  Description of the Parameter
    * @see          org.jboss.ide.eclipse.xdoclet.model.ITemplateTreeListener#treeChanged(org.jboss.ide.eclipse.xdoclet.model.TemplateTreeEvent)
    */
   public void treeChanged(TemplateEvent event)
   {
      fViewer.refresh(event.getTemplateTree());
   }

   /**
    * Returns the templateList.
    *
    * @return   TemplateList
    */
   protected TemplateList getTemplateList()
   {
      return templateList;
   }

}
