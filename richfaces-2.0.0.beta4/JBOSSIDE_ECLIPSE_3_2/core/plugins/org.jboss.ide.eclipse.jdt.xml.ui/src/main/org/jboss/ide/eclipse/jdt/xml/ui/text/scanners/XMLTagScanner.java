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
      setDefaultReturnToken((Token) tokens.get(IXMLSyntaxConstants.XML_DEFAULT));

      IToken tag = (Token) tokens.get(IXMLSyntaxConstants.XML_TAG);
      IToken attribute = (Token) tokens.get(IXMLSyntaxConstants.XML_ATT_NAME);

      IRule[] rules =
      {new XMLTagRule(tag), new WordRule(new NameDetector(), attribute),};

      setRules(rules);
   }
}
