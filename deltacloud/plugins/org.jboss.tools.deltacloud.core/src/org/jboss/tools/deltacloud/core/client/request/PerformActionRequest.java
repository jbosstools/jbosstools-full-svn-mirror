/*******************************************************************************
 * Copyright (c) 2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.deltacloud.core.client.request;

import org.jboss.tools.deltacloud.core.client.Action;
import org.jboss.tools.deltacloud.core.client.HttpMethod;
import org.jboss.tools.deltacloud.core.client.utils.UrlBuilder;

/**
 * Performs an action on a resource of the deltacloud server. The typical
 * actions are
 * <ul>
 * <li>START</li>
 * <li>STOP</li>
 * <li>DESTROY</li>
 * <li>REBOOT</li>
 * </ul>
 * 
 * @author André Dietisheim
 * 
 * @see Action
 */
public class PerformActionRequest extends AbstractDeltaCloudRequest {

	public PerformActionRequest(String url, HttpMethod httpMethod) {
		super(url, httpMethod);
	}

	@Override
	protected String doCreateUrl(UrlBuilder urlBuilder) {
		return urlBuilder.toString();
	}

	@Override
	protected UrlBuilder createUrlBuilder(String baseURL) {
		return new UrlBuilder(baseURL);
	}

}
