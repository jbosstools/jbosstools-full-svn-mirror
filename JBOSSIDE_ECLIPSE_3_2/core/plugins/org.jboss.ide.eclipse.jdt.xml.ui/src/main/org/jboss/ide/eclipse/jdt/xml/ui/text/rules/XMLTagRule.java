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
 * Rule detecting XML tag brackets and name.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class XMLTagRule implements IRule
{
   private IToken token;

   /**
    *Constructor for the XMLTagRule object
    *
    * @param token  Description of the Parameter
    */
   public XMLTagRule(IToken token)
   {
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

      if (ch == '>')
      {
         return token;
      }

      if (ch == '/')
      {
         ch = scanner.read();
         if (ch == '>')
         {
            return token;
         }

         scanner.unread();
         scanner.unread();
         return Token.UNDEFINED;
      }

      if (ch == '<')
      {
         ch = scanner.read();

         if (ch == '/')
         {
            ch = scanner.read();
         }

         loop : while (true)
         {
            switch (ch)
            {
               case ICharacterScanner.EOF :
               case 0x09 :
               case 0x0A :
               case 0x0D :
               case 0x20 :
                  break loop;
            }

            ch = scanner.read();
         }

         scanner.unread();
         return token;
      }

      scanner.unread();
      return Token.UNDEFINED;
   }
}
