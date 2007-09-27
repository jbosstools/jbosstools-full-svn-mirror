package org.hibernate.eclipse.console.editors;

import org.eclipse.jface.text.rules.IWhitespaceDetector;

public class HQLWhitespaceDetector implements IWhitespaceDetector {

	public boolean isWhitespace(char c) {
		return (c == ' ' || c == '\t' || c == '\n' || c == '\r');
	}
}
