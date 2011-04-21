/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.ui.text.rules;

import org.eclipse.jface.text.rules.IWhitespaceDetector;

/**
 * XML white-space detector.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class WhitespaceDetector implements IWhitespaceDetector
{

   /**
    * @param ch  Description of the Parameter
    * @return    The whitespace value
    * @see       IWhitespaceDetector#isWhitespace(char)
    */
   public boolean isWhitespace(char ch)
   {
      return Character.isWhitespace(ch);
   }

}
