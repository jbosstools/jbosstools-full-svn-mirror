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
package org.jboss.tools.internal.deltacloud.client.utils.test;

import static org.junit.Assert.assertEquals;

import org.jboss.tools.deltacloud.client.utils.UrlBuilder;
import org.junit.Test;

public class UrlBuilderTest {

	@Test
	public void buildsHost() {
		String host = "jboss.org";
		assertEquals(host, new UrlBuilder(host).toString());
	}

	@Test
	public void buildsHostWithPort() {
		assertEquals(
				"jboss.org:8080",
				new UrlBuilder("jboss.org")
						.port(8080)
						.toString());
	}

	@Test
	public void buildsWithPath() {
		assertEquals(
				"jboss.org:8080/tools",
				new UrlBuilder("jboss.org")
						.port(8080)
						.path("tools")
						.toString());
	}

	@Test
	public void buildsWith2Paths() {
		assertEquals(
				"jboss.org:8080/tools/usage",
				new UrlBuilder("jboss.org")
						.port(8080)
						.path("tools")
						.path("usage")
						.toString());
	}

	@Test
	public void buildsWithParameters() {
		assertEquals(
				"jboss.org:8080/tools/usage?parameter=dummy",
				new UrlBuilder("jboss.org")
						.port(8080)
						.path("tools")
						.path("usage")
						.parameter("parameter", "dummy")
						.toString());
	}
}
