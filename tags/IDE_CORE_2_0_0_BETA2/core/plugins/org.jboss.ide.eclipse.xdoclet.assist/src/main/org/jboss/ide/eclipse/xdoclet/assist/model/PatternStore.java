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

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.Perl5Compiler;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   17 mai 2003
 */
public class PatternStore
{
   private static HashMap compiledPatterns = new HashMap();

   /**
    * Gets the pattern attribute of the PatternStore class
    *
    * @param pattern  Description of the Parameter
    * @return         The pattern value
    */
   public static Pattern getPattern(String pattern)
   {
      if (compiledPatterns.get(pattern) == null)
      {
         try
         {
            compiledPatterns.put(pattern, (new Perl5Compiler()).compile(pattern));
         }
         catch (MalformedPatternException e)
         {
            return null;
         }
      }
      return (Pattern) compiledPatterns.get(pattern);
   }
}
