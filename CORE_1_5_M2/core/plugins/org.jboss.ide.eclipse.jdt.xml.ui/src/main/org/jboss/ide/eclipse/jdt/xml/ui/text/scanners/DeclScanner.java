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
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WordRule;
import org.jboss.ide.eclipse.jdt.xml.ui.editors.IXMLSyntaxConstants;
import org.jboss.ide.eclipse.jdt.xml.ui.text.rules.EntityRule;
import org.jboss.ide.eclipse.jdt.xml.ui.text.rules.NmtokenDetector;

/*
 * This file contains materials derived from the
 * Solareclipse project. License can be found at :
 * http://solareclipse.sourceforge.net/legal/cpl-v10.html
 */
/**
 * Color scanner for XML declaration.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class DeclScanner extends BufferedRuleBasedScanner
{

   /**
    * @param tokens  Description of the Parameter
    */
   public DeclScanner(Map tokens)
   {
      IToken decl = (Token) tokens.get(IXMLSyntaxConstants.XML_DECL);

      this.setDefaultReturnToken(decl);

      IToken markup = (Token) tokens.get(IXMLSyntaxConstants.XML_ATT_NAME);

      WordRule rule = new WordRule(new NmtokenDetector(), markup);
      rule.addWord("ATTLIST", decl);//$NON-NLS-1$
      rule.addWord("CDATA", decl);//$NON-NLS-1$
      rule.addWord("DOCTYPE", decl);//$NON-NLS-1$
      rule.addWord("ELEMENT", decl);//$NON-NLS-1$
      rule.addWord("EMPTY", decl);//$NON-NLS-1$
      rule.addWord("ENTITY", decl);//$NON-NLS-1$
      rule.addWord("FIXED", decl);//$NON-NLS-1$
      rule.addWord("ID", decl);//$NON-NLS-1$
      rule.addWord("IDREF", decl);//$NON-NLS-1$
      rule.addWord("IDREFS", decl);//$NON-NLS-1$
      rule.addWord("IMPLIED", decl);//$NON-NLS-1$
      rule.addWord("PCDATA", decl);//$NON-NLS-1$
      rule.addWord("PUBLIC", decl);//$NON-NLS-1$
      rule.addWord("REQUIRED", decl);//$NON-NLS-1$
      rule.addWord("SYSTEM", decl);//$NON-NLS-1$

      IToken string = (Token) tokens.get(IXMLSyntaxConstants.XML_ATT_VALUE);
      IToken entity = (Token) tokens.get(IXMLSyntaxConstants.XML_ENTITY);

      IRule[] rules = {
         rule,
         new MultiLineRule("\"", "\"", string), //$NON-NLS-1$ //$NON-NLS-2$
      new MultiLineRule("'", "'", string), //$NON-NLS-1$ //$NON-NLS-2$
      new EntityRule('%', entity),
         };

      this.setRules(rules);
   }

}
