/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.xml.ui.text.scanners;

import java.util.Map;

import org.eclipse.jface.text.rules.BufferedRuleBasedScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WordRule;
import org.jboss.ide.eclipse.jdt.xml.ui.editors.IXMLSyntaxConstants;
import org.jboss.ide.eclipse.jdt.xml.ui.text.rules.NameDetector;
import org.jboss.ide.eclipse.jdt.xml.ui.text.rules.XMLTagRule;

/*
 * This file contains materials derived from the
 * Solareclipse project. License can be found at :
 * http://solareclipse.sourceforge.net/legal/cpl-v10.html
 */
/**
 * @version   $Revision$
 */
public class XMLTagScanner extends BufferedRuleBasedScanner
{
   /**
    * Creates a color token scanner.
    *
    * @param tokens  Description of the Parameter
    */
   public XMLTagScanner(Map tokens)
   {
      setDefaultReturnToken((Token)
         tokens.get(IXMLSyntaxConstants.XML_DEFAULT));

      IToken tag = (Token) tokens.get(IXMLSyntaxConstants.XML_TAG);
      IToken attribute = (Token) tokens.get(IXMLSyntaxConstants.XML_ATT_NAME);

      IRule[] rules = {
         new XMLTagRule(tag),
         new WordRule(new NameDetector(), attribute),
         };

      setRules(rules);
   }
}
