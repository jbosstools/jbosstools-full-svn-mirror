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

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.Perl5Compiler;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   17 mai 2003
 */
public class Variable implements Serializable
{
   private Pattern compiledPattern;

   private Perl5Compiler compiler = new Perl5Compiler();

   private String pattern;

   private String systemVariable;

   private String variable;

   /**
    *Constructor for the Variable object
    *
    * @param variable        Description of the Parameter
    * @param pattern         Description of the Parameter
    * @param systemVariable  Description of the Parameter
    */
   public Variable(String variable, String pattern, String systemVariable)
   {
      setPattern(pattern);
      setSystemVariable(systemVariable);
      setVariable(variable);
   }

   /**
    * Gets the compiledPattern attribute of the Variable object
    *
    * @return   The compiledPattern value
    */
   public Pattern getCompiledPattern()
   {
      return compiledPattern;
   }

   /**
    * Returns the pattern.
    *
    * @return   Pattern
    */
   public String getPattern()
   {
      return pattern;
   }

   /**
    * Returns the systemVariable.
    *
    * @return   String
    */
   public String getSystemVariable()
   {
      return systemVariable;
   }

   /**
    * Returns the variable.
    *
    * @return   String
    */
   public String getVariable()
   {
      return variable;
   }

   /**
    * Sets the pattern.
    *
    * @param pattern  The pattern to set
    */
   public void setPattern(String pattern)
   {
      if (pattern == null)
      {
         throw new IllegalArgumentException();
      }
      try
      {
         compiledPattern = compiler.compile(pattern);
      }
      catch (MalformedPatternException e)
      {
         throw new IllegalArgumentException();
      }
      this.pattern = pattern;
   }

   /**
    * Sets the systemVariable.
    *
    * @param systemVariable  The systemVariable to set
    */
   public void setSystemVariable(String systemVariable)
   {
      if (systemVariable == null)
      {
         throw new IllegalArgumentException();
      }
      this.systemVariable = systemVariable;
   }

   /**
    * Sets the variable.
    *
    * @param variable  The variable to set
    */
   public void setVariable(String variable)
   {
      if (variable == null)
      {
         throw new IllegalArgumentException();
      }
      this.variable = variable;
   }

}
