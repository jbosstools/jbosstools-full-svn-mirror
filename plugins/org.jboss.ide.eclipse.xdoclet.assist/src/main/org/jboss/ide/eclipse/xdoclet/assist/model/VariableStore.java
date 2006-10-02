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

import java.util.HashMap;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   17 mai 2003
 */
public class VariableStore
{
   private HashMap variables = new HashMap();

   /**
    * Adds a feature to the Variable attribute of the VariableStore object
    *
    * @param variable  The feature to be added to the Variable attribute
    */
   public void addVariable(Variable variable)
   {
      if (variable == null)
      {
         throw new IllegalArgumentException();
      }
      variables.put(variable.getVariable(), variable);
   }

   /**
    * Gets the size attribute of the VariableStore object
    *
    * @return   The size value
    */
   public int getSize()
   {
      return variables.size();
   }

   /**
    * Gets the variable attribute of the VariableStore object
    *
    * @param name  Description of the Parameter
    * @return      The variable value
    */
   public Variable getVariable(String name)
   {
      return (Variable) variables.get(name);
   }

   /**
    * Description of the Method
    *
    * @param variable  Description of the Parameter
    */
   public void removeVariable(Variable variable)
   {
      if (variable == null)
      {
         throw new IllegalArgumentException();
      }
      variables.remove(variable);
   }
}
