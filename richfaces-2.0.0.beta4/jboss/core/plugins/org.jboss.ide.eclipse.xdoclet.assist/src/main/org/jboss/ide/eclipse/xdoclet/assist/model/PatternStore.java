/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
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
