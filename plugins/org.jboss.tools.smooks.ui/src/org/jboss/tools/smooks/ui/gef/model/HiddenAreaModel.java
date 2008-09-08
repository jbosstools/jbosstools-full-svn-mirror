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
package org.jboss.tools.smooks.ui.gef.model;

import java.util.Collections;
import java.util.List;

/**
 * @author Dart Peng
 *
 */
public class HiddenAreaModel extends AbstractStructuredDataModel {

	@Override
	public List getChildren() {
		return Collections.EMPTY_LIST;
	}

}
