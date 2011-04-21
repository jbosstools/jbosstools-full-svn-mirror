/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
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
