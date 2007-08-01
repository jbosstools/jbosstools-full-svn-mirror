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
package org.jboss.ide.eclipse.xdoclet.assist.completion;

import java.util.ArrayList;

import org.apache.oro.text.regex.Perl5Matcher;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.JavaModelException;
import org.jboss.ide.eclipse.xdoclet.assist.model.DocletTree;
import org.jboss.ide.eclipse.xdoclet.assist.model.PatternStore;
import org.jboss.ide.eclipse.xdoclet.assist.model.conditions.IsClass;
import org.jboss.ide.eclipse.xdoclet.assist.model.conditions.IsMethod;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   15 mai 2003
 * @todo      Javadoc to complete
 */
public class DocletParser
{
   /** Description of the Field */
   protected IsClass isClass = new IsClass();

   /** Description of the Field */
   protected IsMethod isMethod = new IsMethod();

   /** Description of the Field */
   protected Perl5Matcher matcher = new Perl5Matcher();

   private final static String CLASS_LEVEL = ":class";//$NON-NLS-1$

   private final static String CLASS_METHOD_LEVEL = ":class:method";//$NON-NLS-1$

   private final static String COMMAND_PATTERN = "^[\\w-]*";//$NON-NLS-1$

   private final static String FIRST_WORD_LEFT_OF_EQUALS_PATTERN = "^\\s*[*]?\\s*([\\w-]+)\\s*$";//$NON-NLS-1$

   private final static String INCOMPLETE_NAMESPACE_OR_TEMPLATE_PATTERN = "^[\\w-]*$";//$NON-NLS-1$

   private final static String METHOD_LEVEL = ":method";//$NON-NLS-1$

   private final static String NAME_SPACE_PATTERN = "^([\\w-]+)(:|\\.)";//$NON-NLS-1$

   // if there is a valid namespace-command and no equal-sign nothing but one * is allowed
   private final static String NOTHING_BUT_ONE_STAR_PATTERN = "^\\s*[*]?\\s*$";//$NON-NLS-1$

   // private final static String TEMPLATE_PATTERN = "^_([\\w-]+)";//$NON-NLS-1$
   // If the word after the last equals exists, it's an value. After that
   // one '*' is allowed. If the word doesn't exists no '*' is allowed the next
   // valid element is an value, and between attribute and value no '*' is allowed.
   private final static String WORDS_AFTER_EQUALS_PATTERN = "^[\\t\\f\\ ]*(\"(.*)\")\\s*[*]?\\s*$|^[\\t\\f\\ ]*$";//$NON-NLS-1$

   private final static String WORDS_LEFT_OF_EQUALS_PATTERN = "^\\s*\"(.*)\"\\s*[*]?\\s*([\\w-]*)\\s*$";//$NON-NLS-1$

   private final static String WORD_OF_CURSOR_CHECK_PATTERN = "^\"?[\\w-.]*$";//$NON-NLS-1$

   // Finds the last word in the section (it might be of length 0).
   // This last word is what is adjacent to the cursor and has to be completed
   // The last word has to be separated from special character.
   // Examples: viewtype=rem ==> rem  *view ==> view
   private final static String WORD_OF_CURSOR_PATTERN = "[^\\s=*]*$";//$NON-NLS-1$

   /**
    * Gets the proposals attribute of the DocletParser object
    *
    * @param docletTree              Description of the Parameter
    * @param section                 Description of the Parameter
    * @param member                  Description of the Parameter
    * @return                        The proposals value
    * @exception JavaModelException  Description of the Exception
    */
   public DocletStructure getProposals(DocletTree docletTree, String section, IMember member) throws JavaModelException
   {
      //		logger.debug("Section: " + section);
      DocletStructure docletStructure = new DocletStructure();

      int pos;
      // Search for last occurence of @. If not existent then return
      if ((pos = section.lastIndexOf('@')) == -1)
      {
         return null;
      }

      // Remove @
      section = section.substring(pos + 1);
      System.out.println("Trimmed section:" + section + ":");//$NON-NLS-1$ //$NON-NLS-2$

      /*
       * Get and check namespace and command. If namespace not existent or invalid return null.
       * If command invalid return null, if command is not existent return tag.
       */
      // Syntax-check for namespace
      if (!matcher.contains(section, PatternStore.getPattern(NAME_SPACE_PATTERN)))
      {
         //			System.out.println("No complete namespace: " + section);
         if (matcher.matches(section, PatternStore.getPattern(INCOMPLETE_NAMESPACE_OR_TEMPLATE_PATTERN)))
         {
            //				System.out.println("Incomplete namespace or template: " + section);
            docletStructure.wordLeftOfCursor = section;
            return docletStructure;
         }
         return null;
      }
      // It's a complete namespace, now check for the value-range
      if (!docletTree.isNode(new String[]
      {matcher.getMatch().group(1)}, true))
      {
         //			System.out.println("Invalid namespace");
         return null;
      }
      docletStructure.namespace = matcher.getMatch().group(1);
      //		System.out.println("Valid namespace:" + docletStructure.namespace + ":");
      // remove namespace + : from section
      section = section.substring(docletStructure.namespace.length() + 1);
      //		System.out.println("Trimmed section:" + section + ":");

      // Get the word that is adjacent left to the cursor (that might be an empty string)
      matcher.contains(section, PatternStore.getPattern(WORD_OF_CURSOR_PATTERN));
      // find is always true

      // If there is word of cursor check the syntax, if invalid return null
      if ((docletStructure.wordLeftOfCursor = matcher.getMatch().group(0)).length() == 0)
      {
         docletStructure.wordLeftOfCursor = null;
      }
      else
      {
         if (!matcher.matches(docletStructure.wordLeftOfCursor, PatternStore.getPattern(WORD_OF_CURSOR_CHECK_PATTERN)))
         {
            //				System.out.println(
            //					"Invalid word of Cursor#" + docletStructure.wordLeftOfCursor + "#");
            return null;
         }
      }
      //		System.out.println(
      //			"Word of Cursor:" + docletStructure.wordLeftOfCursor + ":");
      // remove word of cursor from section
      section = section.substring(0, section.length()
            - ((docletStructure.wordLeftOfCursor == null) ? 0 : docletStructure.wordLeftOfCursor.length()));
      //		System.out.println("Trimmed section:" + section + ":");

      // command-check
      if (section.length() == 0)
      {
         return docletStructure;
      }
      matcher.contains(section, PatternStore.getPattern(COMMAND_PATTERN));
      String command = matcher.getMatch().group(0);
      boolean found = false;
      if (command.length() != 0)
      {
         if (isClass.eval(member))
         {
            if (docletTree.isNode(new String[]
            {docletStructure.namespace, command + CLASS_LEVEL}, true))
            {
               command += CLASS_LEVEL;
               found = true;
            }
         }
         else if (isMethod.eval(member))
         {
            if (docletTree.isNode(new String[]
            {docletStructure.namespace, command + METHOD_LEVEL}, true))
            {
               command += METHOD_LEVEL;
               found = true;
            }
         }
         if (!found)
         {
            if (docletTree.isNode(new String[]
            {docletStructure.namespace, command + CLASS_METHOD_LEVEL}, true))
            {
               command += CLASS_METHOD_LEVEL;
               found = true;
            }
            if (!found)
            {
               System.out.println("Invalid commandname");//$NON-NLS-1$
               return null;
            }
         }
      }
      docletStructure.command = command;
      System.out.println("Struc: " + docletStructure);//$NON-NLS-1$
      //		System.out.println("Valid command:" + docletStructure.command + ":");

      // Remove command from section
      section = section.substring(matcher.getMatch().group(0).length());
      //		System.out.println("Trimmed Section:" + section + ":");

      // Look for =
      ArrayList equalsPosList = new ArrayList();
      int equalPos = -1;
      while ((equalPos = section.indexOf('=', equalPos + 1)) != -1)
      {
         // System.out.println(equalPos);
         equalsPosList.add(new Integer(equalPos));
      }

      //		System.out.println("Number of equals found: " + equalsPosList.size());

      if (equalsPosList.size() == 0)
      {
         if (!matcher.matches(section, PatternStore.getPattern(NOTHING_BUT_ONE_STAR_PATTERN)))
         {
            //				System.out.println("No content allowed: " + section + ":");
            return null;
         }
         return docletStructure;
      }

      // Check attributes-value pairs
      // Check if there is one and only one word before the first equal char
      if (!matcher.matches(section.substring(0, ((Integer) equalsPosList.get(0)).intValue()), PatternStore
            .getPattern(FIRST_WORD_LEFT_OF_EQUALS_PATTERN)))
      {
         //			System.out.println("First word was invalid");
         return null;
      }
      //		System.out.println("First word:" + matcher.getMatch().group(1) + ":");

      docletStructure.attributes.add(new Property(matcher.getMatch().group(1), null));

      // Check if before the equal-chars after the first one there is a proper pair
      // example: @ejb:bean name = "x" view-type = "zzz" (here the proper pair is: "x" viewtype
      if (equalsPosList.size() > 1)
      {
         for (int i = 1; i < equalsPosList.size(); i++)
         {
            String matchString = section.substring(((Integer) equalsPosList.get(i - 1)).intValue() + 1,
                  ((Integer) equalsPosList.get(i)).intValue());
            if (!matcher.matches(matchString, PatternStore.getPattern(WORDS_LEFT_OF_EQUALS_PATTERN)))
            {
               //					System.out.println("Pair was invalid");
               return null;
            }
            //				System.out.println(
            //					"Pair: "
            //						+ matcher.getMatch().group(1)
            //						+ " "
            //						+ matcher.getMatch().group(2));
            // Set value of last porperty
            ((Property) docletStructure.attributes.get(docletStructure.attributes.size() - 1)).setValue(matcher
                  .getMatch().group(1));
            // Set new Property
            docletStructure.attributes.add(new Property(matcher.getMatch().group(2), null));
         }
      }// numberOfEqualCharacters > 1

      // Check what is right of the last equal-charcter. Only a value is allowed

      String matchString = section.substring(((Integer) equalsPosList.get(equalsPosList.size() - 1)).intValue() + 1,
            section.length());
      //		System.out.println("Checked section:" + matchString + ":");
      if (matchString.length() > 0
            && !matcher.matches(matchString, PatternStore.getPattern(WORDS_AFTER_EQUALS_PATTERN)))
      {
         //			System.out.println("Word after last equal was invalid");
         return null;
      }

      // if there is an value
      if (matchString.length() > 0 && matcher.getMatch().group(1) != null)
      {
         // Set value of last property
         ((Property) docletStructure.attributes.get(docletStructure.attributes.size() - 1)).setValue(matcher.getMatch()
               .group(2));
         //			System.out.println(
         //				"Word after last Equal:#" + matcher.getMatch().group(2) + "#");
         docletStructure.lastElementIsAttribute = false;
      }
      else
      {
         docletStructure.lastElementIsAttribute = true;
      }

      // Validate attributes and values
      Property p;
      for (int i = 0; i < docletStructure.attributes.size(); i++)
      {
         p = (Property) docletStructure.attributes.get(i);
         if (!docletTree.isNode(new String[]
         {docletStructure.namespace, docletStructure.command, p.getName()}, true))
         {
            //				System.out.println("Invalid attribute: " + p.getName());
            return null;
         }
         if (p.getValue() != null && !docletTree.isNode(new String[]
         {docletStructure.namespace, docletStructure.command, p.getName(), p.getValue()}, true))
         {
            //				System.out.println("Invalid value: " + p.getValue());
            return null;
         }
      }
      return docletStructure;
   }
}
