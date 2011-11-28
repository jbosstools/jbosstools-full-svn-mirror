/*******************************************************************************
 * Copyright (c) 2007-2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.browsersim.util;

import java.io.InputStream;

import org.jboss.tools.vpe.browsersim.ui.BrowserSim;

/**
 * @author Yahor Radtsevich (yradtsevich)
 */
public class ResourcesUtil {

	public static InputStream getResourceAsStream(String name) {
		return BrowserSim.class.getResourceAsStream(name);
	}
}
