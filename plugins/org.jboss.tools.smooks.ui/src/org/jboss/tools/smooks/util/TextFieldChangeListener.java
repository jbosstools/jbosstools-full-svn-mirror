/*******************************************************************************
 * Copyright (c) 2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.util;

import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.widgets.Text;

/**
 * Text Field change listener.
 * <p/>
 * Forwards a field value change event if the {@link #fieldChanged} method
 * implementation if the carriage return is pressed, or the field looses
 * focus.
 * 
 * @author <a href="mailto:tom.fennelly@gmail.com">tom.fennelly@gmail.com</a>
 */
public abstract class TextFieldChangeListener implements KeyListener, FocusListener {
	
	private Text textField;
	private String lastValue;

	public TextFieldChangeListener(Text textField) {
		this.textField = textField;
		textField.addKeyListener(this);
		textField.addFocusListener(this);
		lastValue = textField.getText();
	}

	public void focusGained(FocusEvent event) {
		lastValue = textField.getText();
	}

	public void focusLost(FocusEvent event) {
		String currentValue = textField.getText();
		if(!lastValue.equals(currentValue)) {
			lastValue = currentValue;
			fieldChanged(currentValue);					
		}
	}

	public void keyPressed(KeyEvent event) {
		String currentValue = textField.getText();
		if(event.character == '\r' && !lastValue.equals(currentValue)) {
			lastValue = currentValue;
			fieldChanged(currentValue);					
		}
	}

	public void keyReleased(KeyEvent event) {		
	}
	
	public abstract void fieldChanged(String newValue);
}
