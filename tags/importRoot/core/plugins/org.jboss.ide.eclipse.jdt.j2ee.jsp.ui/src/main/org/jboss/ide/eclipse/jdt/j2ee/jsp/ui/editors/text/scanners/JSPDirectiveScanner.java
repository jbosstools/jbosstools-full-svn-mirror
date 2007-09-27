/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.j2ee.jsp.ui.editors.text.scanners;

import java.util.Map;

import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WordRule;
import org.jboss.ide.eclipse.jdt.j2ee.jsp.ui.editors.IJSPSyntaxConstants;
import org.jboss.ide.eclipse.jdt.xml.ui.text.rules.NmtokenDetector;

/*
 * This file contains materials derived from the
 * Solareclipse project. License can be found at :
 * http://solareclipse.sourceforge.net/legal/cpl-v10.html
 */
/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class JSPDirectiveScanner extends RuleBasedScanner
{
   /**
    * Creates a color scanner for JSP directive.
    *
    * @param tokens  Description of the Parameter
    */
   public JSPDirectiveScanner(Map tokens)
   {
      IToken decl = (Token) tokens.get(IJSPSyntaxConstants.JSP_DIRECTIVE);

      setDefaultReturnToken(decl);

      IToken markup = (Token) tokens.get(IJSPSyntaxConstants.JSP_ATT_NAME);

      WordRule rule = new WordRule(new NmtokenDetector(), markup);
      rule.addWord("alias", decl);//$NON-NLS-1$
      rule.addWord("attribute", decl);//$NON-NLS-1$
      rule.addWord("autoFlush", decl);//$NON-NLS-1$
      rule.addWord("body-content", decl);//$NON-NLS-1$
      rule.addWord("buffer", decl);//$NON-NLS-1$
      rule.addWord("charset", decl);//$NON-NLS-1$
      rule.addWord("contentType", decl);//$NON-NLS-1$
      rule.addWord("declare", decl);//$NON-NLS-1$
      rule.addWord("description", decl);//$NON-NLS-1$
      rule.addWord("display-name", decl);//$NON-NLS-1$
      rule.addWord("dynamic-attributes", decl);//$NON-NLS-1$
      rule.addWord("errorPage", decl);//$NON-NLS-1$
      rule.addWord("example", decl);//$NON-NLS-1$
      rule.addWord("extends", decl);//$NON-NLS-1$
      rule.addWord("file", decl);//$NON-NLS-1$
      rule.addWord("fragment", decl);//$NON-NLS-1$
      rule.addWord("import", decl);//$NON-NLS-1$
      rule.addWord("include", decl);//$NON-NLS-1$
      rule.addWord("info", decl);//$NON-NLS-1$
      rule.addWord("isELIgnored", decl);//$NON-NLS-1$
      rule.addWord("isErrorPage", decl);//$NON-NLS-1$
      rule.addWord("isThreadSafe", decl);//$NON-NLS-1$
      rule.addWord("language", decl);//$NON-NLS-1$
      rule.addWord("large-icon", decl);//$NON-NLS-1$
      rule.addWord("name", decl);//$NON-NLS-1$
      rule.addWord("name-from-attribute", decl);//$NON-NLS-1$
      rule.addWord("name-given", decl);//$NON-NLS-1$
      rule.addWord("page", decl);//$NON-NLS-1$
      rule.addWord("pageEncoding", decl);//$NON-NLS-1$
      rule.addWord("prefix", decl);//$NON-NLS-1$
      rule.addWord("required", decl);//$NON-NLS-1$
      rule.addWord("rtexprvalue", decl);//$NON-NLS-1$
      rule.addWord("scope", decl);//$NON-NLS-1$
      rule.addWord("session", decl);//$NON-NLS-1$
      rule.addWord("small-icon", decl);//$NON-NLS-1$
      rule.addWord("tag", decl);//$NON-NLS-1$
      rule.addWord("taglib", decl);//$NON-NLS-1$
      rule.addWord("type", decl);//$NON-NLS-1$
      rule.addWord("uri", decl);//$NON-NLS-1$
      rule.addWord("variable", decl);//$NON-NLS-1$
      rule.addWord("variable-class", decl);//$NON-NLS-1$

      IToken string = (Token) tokens.get(IJSPSyntaxConstants.JSP_ATT_VALUE);

      IRule[] rules = {
         rule,
         new MultiLineRule("\"", "\"", string), //$NON-NLS-1$ //$NON-NLS-2$
      new MultiLineRule("'", "'", string),//$NON-NLS-1$ //$NON-NLS-2$
      };

      setRules(rules);
   }
}
