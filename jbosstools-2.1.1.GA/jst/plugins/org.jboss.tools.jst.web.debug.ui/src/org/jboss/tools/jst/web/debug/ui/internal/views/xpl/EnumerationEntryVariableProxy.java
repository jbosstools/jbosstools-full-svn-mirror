/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc.
 *     Red Hat, Inc. 
 *******************************************************************************/
package org.jboss.tools.jst.web.debug.ui.internal.views.xpl;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.core.model.IVariable;
import org.jboss.tools.jst.web.debug.ui.xpl.WebDebugUIPlugin;

/**
 * @author Jeremy
 */
public class EnumerationEntryVariableProxy extends VariableProxy {
	/**
	 * @param origin
	 */
	EnumerationEntryVariableProxy(StackFrameWrapper frameWrapper, IVariable origin) {
		super(frameWrapper, origin);
	}
	/**
	 * @param origin
	 */
	EnumerationEntryVariableProxy(StackFrameWrapper frameWrapper, IVariable origin, String alias) {
		super(frameWrapper, origin, alias);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.model.IVariable#getValue()
	 */
	public IValue getValue() throws DebugException {

		try {
			IValue value = getOriginValue();
			fValue = (value == null ?  null : ValueProxyFactory.createValueProxy(fStackFrameWrapper, value, EnumerationEntryValueProxy.class));
//			((HeadersEntryValueProxy)fValue).fVariables = null;
		} catch (Exception e) {
        	WebDebugUIPlugin.getPluginLog().logError(e);
		}
		fHasValueChanged = false;
		return fValue;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.model.IVariable#getReferenceTypeName()
	 */
	public String getReferenceTypeName() throws DebugException {
		return "";
	}
	
}
