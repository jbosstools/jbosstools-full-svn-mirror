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
package org.jboss.tools.jst.web.kb.el;

import org.jboss.tools.common.el.core.ELReference;
import org.jboss.tools.jst.web.kb.validation.IValidator;

/**
 * @author Alexey Kazakov
 */
public class KbELReference extends ELReference {

	/* (non-Javadoc)
	 * @see org.jboss.tools.common.el.core.ELReference#getMarkerGroupId()
	 */
	@Override
	protected String getMarkerGroupId() {
		return IValidator.MARKED_RESOURCE_MESSAGE_GROUP;
	}
}