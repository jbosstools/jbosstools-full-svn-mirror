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
package org.jboss.ide.eclipse.xdoclet.assist.model.conditions;

import java.io.Serializable;
import java.util.Arrays;
import java.util.EventObject;

import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.util.ListenerList;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   17 mai 2003
 */
public class ConditionTree implements Serializable
{

   /** Description of the Field */
   protected transient ListenerList listenerList = new ListenerList();

   Condition root;

   /** Description of the Field */
   protected final static int CHILD_ADDED = 0;

   /** Description of the Field */
   protected final static int REMOVED = 1;

   /**
    * Constructor for ConditionTree.
    *
    * @param root  Description of the Parameter
    */
   public ConditionTree(Condition root)
   {
      super();
      setRoot(root);
   }

   /**
    * Adds a feature to the ConditionListener attribute of the ConditionTree object
    *
    * @param listener  The feature to be added to the ConditionListener attribute
    */
   public void addConditionListener(IConditionTreeListener listener)
   {
      listenerList.add(listener);
   }

   /**
    * Method eval.
    *
    * @param member                  Description of the Parameter
    * @return                        String
    * @exception JavaModelException  Description of the Exception
    */
   public boolean eval(IMember member) throws JavaModelException
   {
      if (root != null)
      {
         return root.eval(member);
      }
      return true;
   }

   /**
    * Gets the conditionListeners attribute of the ConditionTree object
    *
    * @return   The conditionListeners value
    */
   public IConditionTreeListener[] getConditionListeners()
   {
      return (IConditionTreeListener[]) Arrays.asList(listenerList.getListeners()).toArray(
            new IConditionTreeListener[listenerList.size()]);
   }

   /**
    * Returns the root.
    *
    * @return   Condition
    */
   public Condition getRoot()
   {
      return root;
   }

   /**
    * Description of the Method
    *
    * @param listener  Description of the Parameter
    */
   public void removeConditionListener(IConditionTreeListener listener)
   {
      listenerList.remove(listener);
   }

   /**
    * Sets the root.
    *
    * @param root  The root to set
    */
   public void setRoot(Condition root)
   {
      this.root = root;
      if (root != null)
      {
         this.root.tree = this;
      }
   }

   /**
    * Description of the Method
    *
    * @param type  Description of the Parameter
    */
   protected void fireChange(int type)
   {
      Object[] listeners = listenerList.getListeners();
      if (listeners != null)
      {
         EventObject event = new EventObject(this);
         for (int i = 0; i < listeners.length; i++)
         {
            IConditionTreeListener listener = (IConditionTreeListener) listeners[i];
            switch (type)
            {
               case CHILD_ADDED :
                  listener.childAdded(event);
                  break;
               case REMOVED :
                  listener.removed(event);
                  break;
               default :
                  break;
            }
         }
      }
   }

}
