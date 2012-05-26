/*******************************************************************************
 * Copyright (c) 2009 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.vpe.editor;

import org.eclipse.jface.dialogs.IMessageProvider;

/**
 * Simple implementation of {@link IMessageProvider}.
 * 
 * @author yradtsevich
 *
 */
public class Message implements IMessageProvider {
	private String message;
	private int messageType;
	
	public Message(String text, int type) {
		this.message = text;
		this.messageType = type;
	}
	public String getMessage() {
		return message;
	}
	public int getMessageType() {
		return messageType;
	}
}
