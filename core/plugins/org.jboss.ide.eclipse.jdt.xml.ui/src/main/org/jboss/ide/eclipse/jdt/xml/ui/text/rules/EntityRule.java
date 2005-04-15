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
