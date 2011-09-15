/******************************************************************************* 
 * Copyright (c) 2007 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.jboss.ide.eclipse.as.openshift.core.internal.response;

import org.jboss.dmr.ModelNode;

/**
 * @author André Dietisheim
 */
public class ApplicationStatusResponseUnmarshaller extends AbstractOpenshiftJsonResponseUnmarshaller<String> {

	@Override
	protected String createFromResultNode(ModelNode node) {
		return node.asString();
	}
}
