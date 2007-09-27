/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.j2ee.jsp.ui.editors.text.scanners;

import java.util.Map;

import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;

/*
 * This file contains materials derived from the
 * Solareclipse project. License can be found at :
 * http://solareclipse.sourceforge.net/legal/cpl-v10.html
 */
/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class SingleTokenScanner extends RuleBasedScanner
{
   /**
    * Creates a single token scanner.
    *
    * @param tokens    Description of the Parameter
    * @param property  Description of the Parameter
    */
   public SingleTokenScanner(Map tokens, String property)
   {
      setDefaultReturnToken((Token) tokens.get(property));
   }
}
