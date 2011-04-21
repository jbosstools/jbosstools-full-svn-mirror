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
import org.jboss.ide.eclipse.jdt.xml.ui.editors.IXMLSyntaxConstants;
import org.jboss.ide.eclipse.jdt.xml.ui.text.rules.EntityRule;

/*
 * This file contains materials derived from the
 * Solareclipse project. License can be found at :
 * http://solareclipse.sourceforge.net/legal/cpl-v10.html
 */
/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class TextScanner extends BufferedRuleBasedScanner
{
   /**
    * Creates a color scanner for XML text or attribute value.
    *
    * @param tokens           Description of the Parameter
    * @param startEntity      Description of the Parameter
    * @param defaultProperty  Description of the Parameter
    */
   public TextScanner(Map tokens, char startEntity, String defaultProperty)
   {
      setDefaultReturnToken((Token) tokens.get(defaultProperty));

      IToken entity = (Token) tokens.get(IXMLSyntaxConstants.XML_ENTITY);

      IRule[] rules = {
         new EntityRule(startEntity, entity)
         };

      setRules(rules);
   }
}
