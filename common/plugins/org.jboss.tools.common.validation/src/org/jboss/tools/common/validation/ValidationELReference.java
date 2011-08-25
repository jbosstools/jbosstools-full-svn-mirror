/******************************************************************************* 
 * Copyright (c) 2009 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.common.validation;

import org.jboss.tools.common.el.core.ELReference;

/**
 * @author Alexey Kazakov
 */
public class ValidationELReference extends ELReference {

	public ValidationELReference() {
		super(IValidator.MARKED_RESOURCE_MESSAGE_GROUP);
	}
}