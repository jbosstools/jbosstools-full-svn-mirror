 /*******************************************************************************
  * Copyright (c) 2007 Red Hat, Inc.
  * Distributed under license by Red Hat, Inc. All rights reserved.
  * This program is made available under the terms of the
  * Eclipse Public License v1.0 which accompanies this distribution,
  * and is available at http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributors:
  *     Red Hat, Inc. - initial API and implementation
  ******************************************************************************/
package org.jboss.tools.seam.internal.core.el;

import java.util.ArrayList;
import java.util.List;

import org.jboss.tools.seam.core.SeamCorePlugin;

/**
 * EL string parser.
 * Creates list of tokens for the operands and operators 
 * @author Alexey Kazakov
 */
public class SeamELTokenizer {
	private static final int STATE_INITIAL = 0;
	private static final int STATE_OPERAND = 1;
	private static final int STATE_OPERATOR = 2;
	private static final int STATE_RESERVED_WORD = 3;
	private static final int STATE_SEPARATOR = 4;

	private String expression;
	private List<ELToken> fTokens;
	private int index;

	private int fState;
	private static final String OPERATOR_SYMBOLS = "!=&(){}[]:+-*%?',|/%<>";
	private static final String RESERVED_WORDS = " null empty div and or not mod eq ne lt gt le ge true false instanceof ";

	/**
	 * Constructs SeamELTokenizer object.
	 * Constructs SeamELTokenizer object
	 * Parse an expression.
	 * For example: expression is '#{var1.pr != var2.pr}'
	 *              then tokens are {"var1.pr"," ", "!=", " ", "var2",}
	 * @param expression
	 */
	public SeamELTokenizer(String expression) {
		this.expression = expression;
		index = 0;
		fTokens = new ArrayList<ELToken>();
		parse();
	}

	/**
	 * Returns list of tokens for the expression parsed
	 * 
	 * @return
	 */
	public List<ELToken> getTokens() {
		return fTokens;
	}

	/*
	 * Performs parsing of expression
	 */
	private void parse() {
		ELToken token;
		fState = STATE_INITIAL;
		while ((token = getNextToken()) != ELToken.EOF) {

			if (token.getType() == ELToken.EL_OPERAND_TOKEN ||
					token.getType() == ELToken.EL_OPERATOR_TOKEN ||
					token.getType() == ELToken.EL_RESERVED_WORD_TOKEN ||
					token.getType() == ELToken.EL_SEPARATOR_TOKEN) {

				fTokens.add(token);
			}
		}
	}

	/*
	 * Calculates and returns next token for expression
	 * 
	 * @return
	 */
	private ELToken getNextToken() {
		switch (fState) {
			case STATE_INITIAL: { // Just started
				int ch = readNextChar();
				if (ch == -1) {
					return ELToken.EOF;
				}
				releaseChar();
				if (Character.isJavaIdentifierPart((char)ch)) {
					return readOperandOrReservedWordToken();
				}
				if (OPERATOR_SYMBOLS.indexOf(ch)>-1) {
					return readOperatorToken();
				}
				if (ch == ' ') {
					return readSeparatorToken();
				}
				return ELToken.EOF;
			}
			case STATE_RESERVED_WORD: // Reserved word is read - expecting a separator or operator
			case STATE_OPERAND: { // Operand is read - expecting a separator or operator 
				int ch = readNextChar();
				if (ch == -1) {
					return ELToken.EOF;
				}
				releaseChar();
				if (OPERATOR_SYMBOLS.indexOf(ch)>-1) {
					return readOperatorToken();
				}
				if (ch == ' ') {
					return readSeparatorToken();
				}
				return ELToken.EOF;
			}
			case STATE_OPERATOR: { // Operator is read - expecting a separator or operand
				int ch = readNextChar();
				if (ch == -1) {
					return ELToken.EOF;
				}
				releaseChar();
				if (Character.isJavaIdentifierPart((char)ch)) {
					return readOperandOrReservedWordToken();
				}
				if (ch == ' ') {
					return readSeparatorToken();
				}
				return ELToken.EOF;
			}
			case STATE_SEPARATOR: { // Separator is read - expecting a operand or operator
				int ch = readNextChar();
				if (ch == -1) {
					return ELToken.EOF;
				}
				releaseChar();
				if (Character.isJavaIdentifierPart((char)ch)) {
					return readOperandOrReservedWordToken();
				}
				if (OPERATOR_SYMBOLS.indexOf(ch)>-1) {
					return readOperatorToken();
				}
				releaseChar();
				return ELToken.EOF;
			}
		}
		return ELToken.EOF;
	}

	/*
	 * Returns the CharSequence object
	 *  
	 * @param start
	 * @param length
	 * @return
	 */
	private CharSequence getCharSequence(int start, int length) {
		String text = ""; //$NON-NLS-1$
		try {
			text = expression.substring(start, start + length);
		} catch (StringIndexOutOfBoundsException e) {
			SeamCorePlugin.getDefault().logError(e);
			text = ""; // For sure //$NON-NLS-1$
		}
		return text.subSequence(0, text.length());
	}

	/*
	 * Reads and returns the operator token from the expression
	 * @return
	 */
	private ELToken readOperatorToken() {
		fState = STATE_OPERATOR;
		int startOfToken = index;
		int ch;
		while((ch = readNextChar()) != -1) {
			if (OPERATOR_SYMBOLS.indexOf(ch)==-1) {
				break;
			}
		}
		releaseChar();
		int length = index - startOfToken;
		return (length > 0 ? new ELToken(startOfToken, length, getCharSequence(startOfToken, length), ELToken.EL_OPERATOR_TOKEN) : ELToken.EOF);
	}

	/* 
	 * Reads and returns the separator token from the expression
	 * @return
	 */
	private ELToken readSeparatorToken() {
		fState = STATE_SEPARATOR;
		int startOfToken = index;
		int ch;
		while((ch = readNextChar()) != -1) {
			if (ch!=' ') {
				break;
			}
		}
		releaseChar();
		int length = index - startOfToken;
		return (length > 0 ? new ELToken(startOfToken, length, getCharSequence(startOfToken, length), ELToken.EL_SEPARATOR_TOKEN) : ELToken.EOF);
	}

	/*
	 * Reads and returns the operand token from the expression
	 * @return
	 */
	private ELToken readOperandOrReservedWordToken() {
		fState = STATE_OPERAND;
		int startOfToken = index;
		int ch;
		while((ch = readNextChar()) != -1) {
			if (!Character.isJavaIdentifierPart(ch) && ch!='.') {
				break;
			}
		}
		releaseChar();
		int length = index - startOfToken;
		boolean reservedWord = isResorvedWord(startOfToken, length);
		int tokenType = ELToken.EL_OPERAND_TOKEN;
		if(reservedWord) {
			tokenType = ELToken.EL_RESERVED_WORD_TOKEN;
			fState = STATE_RESERVED_WORD;
		}

		return (length > 0 ? new ELToken(startOfToken, length, getCharSequence(startOfToken, length), tokenType) : ELToken.EOF);
	}

	private boolean isResorvedWord(String word) {
		return RESERVED_WORDS.indexOf(" " + word.trim() + " ")>-1;
	}

	private boolean isResorvedWord(int beginIndex, int length) {
		String word = expression.substring(beginIndex, beginIndex + length);
		return isResorvedWord(word);
	}

	/* Reads the next character
	 * @return
	 */
	private int readNextChar() {
		int c = -1;
		try {
			if (index < expression.length()) {
				c = expression.charAt(index);
			}
		} catch (StringIndexOutOfBoundsException e) {
			SeamCorePlugin.getPluginLog().logError(e);
		}
		index++;
		return c;
	}

	/* 
	 * returns the character to the document
	 */
	private void releaseChar() {
		if (index > 0) {
			index--;
		}
	}
}