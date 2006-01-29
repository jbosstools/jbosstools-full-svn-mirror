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
package org.jboss.ide.eclipse.jdt.xml.ui.text.rules;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

/*
 * This file contains materials derived from the
 * Solareclipse project. License can be found at :
 * http://solareclipse.sourceforge.net/legal/cpl-v10.html
 */
/**
 * Rule detecting XML or DTD entities.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class EntityRule implements IRule
{

   private char start;

   private IToken token;

   private static NameDetector detector = new NameDetector();

   /**
    *Constructor for the EntityRule object
    *
    * @param start  Description of the Parameter
    * @param token  Description of the Parameter
    */
   public EntityRule(char start, IToken token)
   {
      this.start = start;
      this.token = token;
   }

   /**
    * Description of the Method
    *
    * @param scanner  Description of the Parameter
    * @return         Description of the Return Value
    */
   public IToken evaluate(ICharacterScanner scanner)
   {
      int ch = scanner.read();

      if (ch == start)
      {
         ch = scanner.read();

         if (ch == ICharacterScanner.EOF)
         {
            scanner.unread();
            return token;
         }

         if (ch == ';')
         {
            return token;
         }

         if (!detector.isWordStart((char) ch))
         {
            scanner.unread();
            return token;
         }

         while (true)
         {
            ch = scanner.read();

            if (ch == ICharacterScanner.EOF)
            {
               scanner.unread();
               return token;
            }

            if (ch == ';')
            {
               return token;
            }

            if (!detector.isWordPart((char) ch))
            {
               scanner.unread();
               return token;
            }
         }
      }

      scanner.unread();

      return Token.UNDEFINED;
   }
}
