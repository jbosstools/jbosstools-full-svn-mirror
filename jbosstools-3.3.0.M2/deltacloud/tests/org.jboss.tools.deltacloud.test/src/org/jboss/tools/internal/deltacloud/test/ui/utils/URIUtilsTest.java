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
package org.jboss.tools.internal.deltacloud.test.ui.utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jboss.tools.internal.deltacloud.ui.utils.URIUtils;
import org.junit.Test;

/**
 * @author André Dietisheim
 *
 */
public class URIUtilsTest {

	@Test
	public void uriWithPortDoesNotStartWithScheme() {
		assertFalse(URIUtils.startsWithScheme("localhost:3001"));
	}
	
	@Test
	public void uriWithoutPortDoesNotStartWithScheme() {
		assertFalse(URIUtils.startsWithScheme("localhost"));
	}

	@Test
	public void uriWithPortStartsWithScheme() {
		assertTrue(URIUtils.startsWithScheme("http://localhost:3001"));
	}

	@Test
	public void uriWithoutPortStartsWithScheme() {
		assertTrue(URIUtils.startsWithScheme("http://localhost"));
	}
}
