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
package org.jboss.ide.eclipse.xdoclet.assist.model.conditions;

import java.util.Iterator;

import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.JavaModelException;

/**
 * Evaluates to true if all the conditions contained evaluate to true. Evaluates
 * to true if there are no nested conditions
 *
 * @author         Aslak Hellesøy
 * @version        $Revision$
 * @created        14. januar 2002
 * @todo-javadoc   Write javadocs
 */
public class And extends Condition
{

   /** Constructor for And. */
   public And()
   {
      super();
      setMaximumNumberOfChildren(Condition.UNLIMITED_NUMBER_OF_CHILDREN);
   }

   /**
    * Description of the Method
    *
    * @param member                  Description of the Parameter
    * @return                        Description of the Return Value
    * @exception JavaModelException  Description of the Exception
    */
   public boolean evalInternal(IMember member) throws JavaModelException
   {
      Iterator iterator = getChildConditions().iterator();
      while (iterator.hasNext())
      {
         Condition condition = (Condition) iterator.next();
         if (!condition.eval(member))
         {
            return false;
         }
      }
      return true;
   }

   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public String toString()
   {
      return getClass().getName();
   }
}
