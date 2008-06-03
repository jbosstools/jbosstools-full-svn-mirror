/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.vpe.editor.toolbar.format.handler;

import java.util.HashMap;

import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.template.textformating.FormatData;
import org.jboss.tools.vpe.editor.toolbar.format.FormatControllerManager;

/**
 * @author Igels
 */
public class HandlerFactory {

	private FormatControllerManager manager;
	private HashMap<String,IFormatHandler> handlers = new HashMap<String,IFormatHandler>();

	/**
	 * Constructor 
	 */
	public HandlerFactory(FormatControllerManager manager) {
		this.manager = manager;
	}

	/**
	 * Create format handler for selected node.
	 * @param formatData
	 * @return IFormatHandler if formatData contains information aboute handler class.
	 * And <b>null</b> if not.
	 */
	public IFormatHandler createHandler(FormatData formatData) {
		String handlerClassName = formatData.getHandler();
		if(handlerClassName!=null && handlerClassName.trim().length()>0) {
			Object object = handlers.get(handlerClassName);
			if(object!=null) {
				return (IFormatHandler)object;
			}
			try {
				Class handlerClass = Class.forName(handlerClassName);
				object = handlerClass.newInstance();
				if(object instanceof IFormatHandler) {
					IFormatHandler handler = (IFormatHandler)object;
					if(handler instanceof FormatHandler) {
						FormatHandler formatHandler = (FormatHandler)handler;
						formatHandler.setManager(manager);
					}
					handlers.put(handlerClassName, handler);
					return handler;
				} else {
					VpePlugin.getPluginLog().logError("Wrong format handler. Class - " + handlerClassName + ". Handler must be instance of org.jboss.tools.vpe.editor.toolbar.format.handler.IFormatHandler",  //$NON-NLS-1$//$NON-NLS-2$
							new Exception("Handler must be instance of org.jboss.tools.vpe.editor.toolbar.format.handler.IFormatHandler")); //$NON-NLS-1$
				}
			} catch (Exception e) {
				VpePlugin.getPluginLog().logError("Can't create format handler. Class: " + handlerClassName, e); //$NON-NLS-1$
			}
		}
		return null;
	}

	/**
	 * Create IAddNodeHandler handler for selected node.
	 * @param formatData
	 * @return IAddNodeHandler if formatData contains information aboute handler class.
	 */
	public IAddNodeHandler createAddNodeHandler(FormatData formatData) {
		// TODO
		return null;
	}
	
	/**
	 * Get count of handlers
	 * 
	 * @return count of handlers 
	 */
	public int getCountHandlers() {
	    return handlers.size();
	}
}