/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
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

         loop :
         while (true)
         {
            switch (ch)
            {
               case ICharacterScanner.EOF:
               case 0x09:
               case 0x0A:
               case 0x0D:
               case 0x20:
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
