/*
 * JBoss, Home of Professional Open Source
 * Copyright 2006, JBoss Inc., and individual contributors as indicated
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
package org.jboss.ide.eclipse.freemarker.editor.rules;

import java.util.Stack;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.SingleLineRule;

/**
 * @author <a href="mailto:joe@binamics.com">Joe Hudson</a>
 */
public class InterpolationRule extends SingleLineRule {

    public InterpolationRule(char startChar, IToken token) {
        super(startChar + "{", "}", token);
    }

	protected boolean endSequenceDetected(ICharacterScanner scanner) {
		int c;
		char[][] delimiters= scanner.getLegalLineDelimiters();
		boolean previousWasEscapeCharacter = false;	
		Stack keyStack = new Stack();
		int charsRead = 0;
		while ((c= scanner.read()) != ICharacterScanner.EOF) {
			charsRead ++;
			char cCheck = (char) c;
			if (c == '{') {
				if (keyStack.size() == 0) {
					break;
				}
			}
			else if (c == '\"') {
				if (keyStack.size() > 0 && keyStack.peek().equals("\"")) {
					keyStack.pop();
				}
				else {
					keyStack.push("\"");
				}
			}
			else if (c == '(') {
				if (keyStack.size() > 0 && keyStack.peek().equals("\"")) {
					// string... don't add to stack
				}
				else {
					keyStack.push("(");
				}
			}
			else if (c == ')') {
				if (keyStack.size() > 0 && keyStack.peek().equals("\"")) {
					// string... don't add to stack
				}
				else if (keyStack.size() > 0 && keyStack.peek().equals("(")) {
					keyStack.pop();
				}
			}
			else if (c == fEscapeCharacter) {
				// Skip the escaped character.
				scanner.read();
				charsRead ++;
			}
			else if (c == '}') {
				if (keyStack.size() == 0) {
					return true;
				}
			}
			else if (c == '\n') {
				break;
			}
			previousWasEscapeCharacter = (c == fEscapeCharacter);
		}
		if (fBreaksOnEOF) return true;
		for (int i=0; i<charsRead; i++)
			scanner.unread();
		return false;
	}
}