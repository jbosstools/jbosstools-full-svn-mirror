/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.bpel.model.reordering;

import java.util.List;

import org.eclipse.wst.wsdl.ExtensibleElement;

public interface IExtensibilityElementListHandler {

	void orderList(ExtensibleElement parent, List tempList);
		
}
